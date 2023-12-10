package com.focusedapp.smartstudyhub.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.focusedapp.smartstudyhub.dao.FolderDAO;
import com.focusedapp.smartstudyhub.exception.NotFoundValueException;
import com.focusedapp.smartstudyhub.model.ExtraWork;
import com.focusedapp.smartstudyhub.model.Folder;
import com.focusedapp.smartstudyhub.model.Project;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.Work;
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
			projects = dataCreate.getListProjectActive().stream()
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
		folder.get().setStatus(EnumStatus.DELETED.getValue());
		
		List<Project> projects = folder.get().getProjects();
		if (projects != null) {
			projects.stream().forEach(pro -> {				
				List<Work> works = pro.getWorks();
				if (works != null) {
					works.stream().forEach(w -> {
						List<ExtraWork> extraWorks = w.getExtraWorks();
						if (extraWorks != null) {
							extraWorks.stream()
								.forEach(e -> e.setStatus(EnumStatus.DELETED.getValue()));
						}
						w.setStatus(EnumStatus.DELETED.getValue());
					}); 
				}
				pro.setStatus(EnumStatus.DELETED.getValue());
			});
		}
		folder.get().setProjects(projects);
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
	 * Mark Completed Folder
	 * 
	 * @param folderId
	 * @return
	 */
	public FolderDTO markCompleted(Integer folderId) {
		
		Optional<Folder> folderOpt = folderDAO.findByIdAndStatus(folderId, EnumStatus.ACTIVE.getValue());
		if (folderOpt.isEmpty()) {
			return null;
		}
		Folder folder = folderOpt.get();
		List<Project> projects = folder.getProjects();
		
		if (projects != null) {
			projects.stream()
				.forEach(p -> {
					if (p.getStatus().equals(EnumStatus.ACTIVE.getValue())) {
						List<Work> works = p.getWorks();
						if (works != null) {
							works.stream()
								.forEach(w -> {
									if (w.getStatus().equals(EnumStatus.ACTIVE.getValue())) {
										List<ExtraWork> extraWorks = w.getExtraWorks();
										if (extraWorks != null) {
											extraWorks.stream()
												.forEach(ew -> {
													if (ew.getStatus().equals(EnumStatus.ACTIVE.getValue())) {
														extraWorkService.markCompleted(ew.getId());
													}
												});
										}
										workService.markCompleted(w.getId());
									}
								});
						}
						p.setStatus(EnumStatus.COMPLETED.getValue());
					}
				});
		}
		
		folder.setStatus(EnumStatus.COMPLETED.getValue());
		
		folder = folderDAO.save(folder);
		
		return new FolderDTO(folder);
	}
	
}
