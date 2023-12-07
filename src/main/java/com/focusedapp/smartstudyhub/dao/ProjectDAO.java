package com.focusedapp.smartstudyhub.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.focusedapp.smartstudyhub.model.Project;

@Repository
public interface ProjectDAO extends JpaRepository<Project, Integer> {

	Optional<Project> findByIdAndStatus(Integer id, String status);
	
	List<Project> findByUserIdAndStatus(Integer userId, String status);
	
	List<Project> findByUserIdAndFolderIdAndStatus(Integer userId, Integer folderId, String status);
	
	@Query(value = "SELECT * FROM project p WHERE p.user_id = :userId "
			+ "AND (p.folder_id IS NULL OR p.folder_id = :folderId) AND p.status = 'ACTIVE'", nativeQuery = true)
	List<Project> findProjectsForUpdatingFolder(Integer userId, Integer folderId);
	
	List<Project> findByFolderIdAndStatus(Integer folderId, String status);
	
	@Query(value = "SELECT * FROM project p WHERE p.user_id = :userId "
			+ "AND (p.status = 'ACTIVE' OR p.status = 'COMPLETED')", nativeQuery = true)
	List<Project> getProjectsActiveAndCompletedByUserId(Integer userId);
	
}
