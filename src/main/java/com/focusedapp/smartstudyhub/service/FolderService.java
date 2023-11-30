package com.focusedapp.smartstudyhub.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.focusedapp.smartstudyhub.dao.FolderDAO;
import com.focusedapp.smartstudyhub.exception.NotFoundValueException;
import com.focusedapp.smartstudyhub.model.Folder;
import com.focusedapp.smartstudyhub.model.Project;
import com.focusedapp.smartstudyhub.model.custom.FolderDTO;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatus;

@Service
public class FolderService {

	@Autowired
	UserService userService;
	@Autowired
	FolderDAO folderDAO;
	@Autowired
	ProjectService projectService;

	/**
	 * Create new folder
	 * 
	 * @param dataCreate
	 * @return
	 */
	public FolderDTO createFolder(FolderDTO dataCreate) {

		List<Project> projects = dataCreate.getListProject().stream()
				.map(p -> projectService.findByIdAndStatus(p.getId(), EnumStatus.ACTIVE.getValue()))
				.collect(Collectors.toList());

		Folder folder = Folder.builder()
				.user(userService.findByIdAndStatus(dataCreate.getUserId(), EnumStatus.ACTIVE.getValue()))
				.folderName(dataCreate.getFolderName()).colorCode(dataCreate.getColorCode())
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
		List<Integer> projectIds = dataCreate.getListProject().stream()
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
}
