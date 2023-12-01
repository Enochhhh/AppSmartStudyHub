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
import com.focusedapp.smartstudyhub.model.custom.ProjectDTO;
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
	
	public List<ProjectDTO> getProjectsForUpdatingFolder(Integer userId, Integer folderId) {

		List<Project> projects = projectDAO.findProjectsForUpdatingFolder(userId, folderId);
		List<ProjectDTO> projectsDto = projects.stream()
				.map(p -> new ProjectDTO(p))
				.collect(Collectors.toList());
		return projectsDto;
	}
	
}
