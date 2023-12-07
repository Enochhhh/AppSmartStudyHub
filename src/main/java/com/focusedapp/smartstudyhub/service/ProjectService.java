package com.focusedapp.smartstudyhub.service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.focusedapp.smartstudyhub.dao.FolderDAO;
import com.focusedapp.smartstudyhub.dao.ProjectDAO;
import com.focusedapp.smartstudyhub.exception.NotFoundValueException;
import com.focusedapp.smartstudyhub.model.Folder;
import com.focusedapp.smartstudyhub.model.Project;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.custom.ProjectDTO;
import com.focusedapp.smartstudyhub.util.enumerate.EnumRole;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatus;

@Service
public class ProjectService {

	@Autowired
	ProjectDAO projectDAO;
	@Autowired
	FolderService folderService;
	@Autowired
	UserService userService;
	@Autowired 
	FolderDAO folderDAO;
	
	/**
	 * Find project by id
	 * 
	 * @param id
	 * @return
	 */
	public Project findByIdAndStatus(Integer id, String status) {
		Project project = projectDAO.findByIdAndStatus(id, EnumStatus.ACTIVE.getValue())
				.orElseThrow(() -> new NotFoundValueException("Not found project by Id!", "ProjectService -> findByIdAndStatus"));
		return project;
	}
	
	/**
	 * Create Project
	 * 
	 * @param dataCreate
	 * @return
	 */
	public ProjectDTO createProject(ProjectDTO dataCreate) {

		Optional<Folder> folderOpt = folderDAO.findByIdAndStatus(dataCreate.getFolderId(), EnumStatus.ACTIVE.getValue());
		Folder folder = null;
		if (!folderOpt.isEmpty()) {
			folder = folderOpt.get();
		}
		
		Project project = Project.builder()
				.user(userService.findByIdAndStatus(dataCreate.getUserId(), EnumStatus.ACTIVE.getValue()))
				.folder(folder)
				.projectName(dataCreate.getProjectName())
				.colorCode(dataCreate.getColorCode())
				.iconUrl(dataCreate.getIconUrl())
				.status(EnumStatus.ACTIVE.getValue()).build();
		project = projectDAO.save(project);

		return new ProjectDTO(project);
	}
	
	/**
	 * Get Projects of user by status
	 * 
	 * @param userId
	 * @return
	 */
	public List<ProjectDTO> getProjectsOfUserByStatus(Integer userId, String status) {

		List<Project> projects = projectDAO.findByUserIdAndStatus(userId, status);
		List<ProjectDTO> projectsDto = projects.stream().map(p -> new ProjectDTO(p)).collect(Collectors.toList());
		return projectsDto;
	}
	
	/**
	 * Get Projects of user by folder and status
	 * 
	 * @param userId
	 * @param folderId
	 * @param status
	 * @return
	 */
	public List<ProjectDTO> getProjectsOfUserByFolderAndStatus(Integer userId, Integer folderId, String status) {

		List<Project> projects = projectDAO.findByUserIdAndFolderIdAndStatus(userId, folderId, status);
		List<ProjectDTO> projectsDto = projects.stream()
				.map(p -> new ProjectDTO(p))
				.collect(Collectors.toList());
		return projectsDto;
	}
	
	/**
	 * Get Project for updating Folder
	 * 
	 * @param userId
	 * @param folderId
	 * @return
	 */
	public List<ProjectDTO> getProjectsForUpdatingFolder(Integer userId, Integer folderId) {

		List<Project> projects = projectDAO.findProjectsForUpdatingFolder(userId, folderId);
		List<ProjectDTO> projectsDto = projects.stream()
				.map(p -> new ProjectDTO(p))
				.collect(Collectors.toList());
		return projectsDto;
	}
	
	/**
	 * Update Project Information
	 * 
	 * @param projectRequest
	 * @return
	 */
	public Project updateProject(ProjectDTO projectRequest) {
		Project projectDb = projectDAO.findById(projectRequest.getId())
				.orElseThrow(() -> new NotFoundValueException("Not Fould the Project to update!", "ProjectService -> updateProject"));
		Folder folder = folderService.findByIdAndStatus(projectRequest.getFolderId(), EnumStatus.ACTIVE.getValue());
		
		projectDb.setProjectName(projectRequest.getProjectName());
		projectDb.setColorCode(projectRequest.getColorCode());
		projectDb.setIconUrl(projectRequest.getIconUrl());
		projectDb.setFolder(folder);
		projectDb.setStatus(projectRequest.getStatus() == null ? EnumStatus.ACTIVE.getValue() : projectRequest.getStatus());
		
		projectDb = projectDAO.save(projectDb);
		return projectDb;		
	}
	
	/**
	 * Delete Project
	 * 
	 * @param projectRequest
	 * @return
	 */
	public Project deleteProject(Integer projectId) {
		Project projectDb = projectDAO.findById(projectId)
				.orElseThrow(() -> new NotFoundValueException("Not Fould the Project to delete!", "ProjectService -> deleteProject"));
		
		projectDb.setStatus(EnumStatus.DELETED.getValue());
		
		projectDb = projectDAO.save(projectDb);
		return projectDb;		
	}
	
	/**
	 * Get Projects for adding folder
	 * 
	 * @return
	 */
	public List<ProjectDTO> getProjectsForAddingFolder(Integer userId) {
		
		List<Project> projects = projectDAO.findByUserIdAndFolderIdAndStatus(userId, null, EnumStatus.ACTIVE.getValue());
		
		return projects.stream()
				.map(proj -> new ProjectDTO(proj))
				.collect(Collectors.toList());
	}
	
	/**
	 * Check Maxium number of projects
	 * 
	 * @param userId
	 * @return
	 */
	public Boolean checkMaximumProject(Integer userId) {
		
		User user = userService.findByIdAndStatus(userId, EnumStatus.ACTIVE.getValue());
		if (user.getRole().equals(EnumRole.PREMIUM.getValue())) {
			return false;
		}
		
		List<Project> projects = projectDAO.findByUserIdAndStatus(userId, EnumStatus.ACTIVE.getValue());
		Integer size = projects.size();
		
		if (size >= 7) {
			return true;
		}
		return false;
		
	}
	
	/**
	 * Get Projects Active and Completed Of User
	 * 
	 * @param userId
	 * @return
	 */
	public List<Project> getProjectsActiveAndCompletedOfUser(Integer userId) {
		
		List<Project> projects = projectDAO.getProjectsActiveAndCompletedByUserId(userId);
		
		return projects;
	}

}
