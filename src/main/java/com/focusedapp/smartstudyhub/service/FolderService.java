package com.focusedapp.smartstudyhub.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.focusedapp.smartstudyhub.dao.FolderDAO;
import com.focusedapp.smartstudyhub.exception.NotFoundValueException;
import com.focusedapp.smartstudyhub.model.Folder;
import com.focusedapp.smartstudyhub.model.Project;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.custom.FolderDTO;
import com.focusedapp.smartstudyhub.model.custom.ProjectDTO;
import com.focusedapp.smartstudyhub.util.enumerate.EnumRole;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatus;

@Service
public class FolderService {

	@Autowired
	UserService userService;
	@Autowired
	FolderDAO folderDAO;
	@Autowired
	ProjectService projectService;
	@Autowired
	WorkService workService;
	@Autowired
	ExtraWorkService extraWorkService;

	/**
	 * Create new folder
	 * 
	 * @param dataCreate
	 * @return
	 */
	public FolderDTO createFolder(FolderDTO dataCreate) {

		List<ProjectDTO> projectsRquest = dataCreate.getListProjectActive();
		List<Project> projects = new ArrayList<>();
		if (!CollectionUtils.isEmpty(projectsRquest)) {			
			projects = projectsRquest.stream()
							.map(p -> projectService.findByIdAndStatus(p.getId(), EnumStatus.ACTIVE.getValue()))
							.collect(Collectors.toList());
		}

		Folder folder = Folder.builder()
				.user(userService.findByIdAndStatus(dataCreate.getUserId(), EnumStatus.ACTIVE.getValue()))
				.folderName(dataCreate.getFolderName()).colorCode(dataCreate.getColorCode())
				.createdDate(new Date())
				.iconUrl(dataCreate.getIconUrl()).status(EnumStatus.ACTIVE.getValue()).build();
		folder = folderDAO.save(folder);
		for (Project project : projects) {
			project.setFolder(folder);
		}

		folder.setProjects(projects);
		folder = folderDAO.save(folder);

		return new FolderDTO(folder);
	}

	/**
	 * Get all folders of user
	 * 
	 * @param userId
	 * @return
	 */
	public List<FolderDTO> getAllFolderOfUser(Integer userId) {

		List<Folder> folders = folderDAO.findByUserIdAndStatus(userId, EnumStatus.ACTIVE.getValue());
		List<FolderDTO> folderDTOs = folders.stream().map(f -> new FolderDTO(f)).collect(Collectors.toList());
		return folderDTOs;
	}

	/**
	 * Get all folders deleted of user
	 * 
	 * @param userId
	 * @return
	 */
	public List<FolderDTO> getAllFolderDeletedOfUser(Integer userId) {

		List<Folder> folders = folderDAO.findByUserIdAndStatus(userId, EnumStatus.DELETED.getValue());
		List<FolderDTO> folderDTOs = folders.stream().map(f -> new FolderDTO(f)).collect(Collectors.toList());
		return folderDTOs;
	}

	/**
	 * Update Folder
	 * 
	 * @param dataCreate
	 * @return
	 */
	public FolderDTO updateFolder(FolderDTO dataCreate) {
		
		Folder folderDb = folderDAO.findById(dataCreate.getId())
				.orElseThrow(() -> new NotFoundValueException("Not Found Folder to update!", "FolderService -> updateFolder"));
		List<Integer> projectIds = dataCreate.getListProjectActive().stream()
				.map(p -> p.getId())
				.collect(Collectors.toList());

		List<Project> projectsDb = folderDb.getProjects();
		List<Project> projectDeleted = new ArrayList<>();
		for (Project project : projectsDb) {
			if (!projectIds.contains(project.getId())) {
				project.setFolder(null);				
				projectDeleted.add(project);
			} else {
				projectIds.remove(project.getId());
			}
		}
		
		folderDb.setFolderName(dataCreate.getFolderName());
		folderDb.setColorCode(dataCreate.getColorCode());
		folderDb.setIconUrl(dataCreate.getIconUrl());
		
		// The project unhandle - it means project unexist in db before
		for (Integer projId : projectIds) {
			Project project = projectService.findByIdAndStatus(projId, EnumStatus.ACTIVE.getValue());
			project.setFolder(folderDb);
			projectsDb.add(project);
		}
		folderDb.setProjects(projectsDb);
		folderDb = folderDAO.save(folderDb);
		
		for (Project project : projectDeleted) {
			projectsDb.remove(project);
		}
		folderDb.setProjects(projectsDb);

		return new FolderDTO(folderDb);
	}
	
	/**
	 * Delete Folder
	 * 
	 * @param dataCreate
	 * @return
	 */
	public FolderDTO deleteFolder(Integer folderId) {
		Optional<Folder> folder = folderDAO.findById(folderId);
		if (folder.isEmpty()) {
			return null;
		}
				
		List<Project> projects = folder.get().getProjects();
		if (projects != null) {
			projects.stream().forEach(pro -> {				
				projectService.deleteProject(pro.getId());		
			});
		}	
		folder.get().setStatus(EnumStatus.DELETED.getValue());	
				
		return new FolderDTO(folderDAO.save(folder.get()));
	}
	
	/**
	 * Find folder by id and status
	 * 
	 * @param id
	 * @param status
	 * @return
	 */
	public Folder findByIdAndStatus(Integer id, String status) {
		Folder folder = folderDAO.findByIdAndStatus(id, status)
				.orElseThrow(() -> new NotFoundValueException("Not Found Folder by id!", "FolderService -> findByIdAndStatus"));
		return folder;
	}
	
	/**
	 * Check Maximum number of Projects
	 * 
	 * @param userId
	 * @return
	 */
	public Boolean checkMaximumFolder(Integer userId) {
		
		User user = userService.findByIdAndStatus(userId, EnumStatus.ACTIVE.getValue());
		if (user.getRole().equals(EnumRole.PREMIUM.getValue())) {
			return false;
		}
		
		List<Folder> folders = folderDAO.findByUserIdAndStatus(userId, EnumStatus.ACTIVE.getValue());
		Integer size = folders.size();
		
		if (size >= 3) {
			return true;
		}
		return false;
		
	}
	
	/**
	 * Delete Completely Folder
	 * 
	 * @param folderId
	 * @return
	 */
	public FolderDTO deleteCompletelyFolder(Integer folderId) {
		
		Optional<Folder> folder = folderDAO.findById(folderId);
		if (folder.isEmpty()) {
			return null;
		}
		
		folderDAO.delete(folder.get());
		
		return new FolderDTO(folder.get());
	}
	
	/**
	 * Recover Folder
	 * 
	 * @param folderId
	 * @return
	 */
	public FolderDTO recover(Integer folderId) {
		
		Optional<Folder> folderOpt = folderDAO.findById(folderId);
		if (folderOpt.isEmpty()) {
			return null;
		}
		Folder folder = folderOpt.get();
		
		List<Project> projects = folder.getProjects() == null ? new ArrayList<>() : folder.getProjects();
		folder.setStatus(EnumStatus.ACTIVE.getValue());
		projects.stream()
			.forEach(p -> {
				if (p.getStatus().equals(EnumStatus.DELETED.getValue())) {
					projectService.recover(p.getId());
				}
			});		
		folder = folderDAO.save(folder);
		
		return new FolderDTO(folder);
	}
	
	/**
	 * Find Folder by Id
	 * 
	 * @param id
	 * @return
	 */
	public Folder findById(Integer id) {
		return folderDAO.findById(id)
					.orElseThrow(() -> new NotFoundValueException("Not found Folder by Id!", "FolderService -> findById"));
	}
	
	/**
	 * Searh Folder By Name
	 * 
	 * @param keySearch
	 * @return
	 */
	public List<FolderDTO> searchByName(String keySearch, Integer userId) {
		List<Folder> folders = new ArrayList<>();
		if (StringUtils.isEmpty(keySearch)) {
			folders = folderDAO.findByUserIdAndStatus(userId, EnumStatus.ACTIVE.getValue());
		} else {
			folders = folderDAO.findByFolderNameContainingAndUserIdAndStatus(keySearch, userId, EnumStatus.ACTIVE.getValue());
		}
		return folders.stream()
					.map(f -> new FolderDTO(f))
					.collect(Collectors.toList());
	}
	
	/**
	 * Delete All Folders of User
	 * 
	 * @param user
	 */
	public void deleteAllFolderOfUser(User user) {
		folderDAO.deleteByUser(user);
	}
	
}