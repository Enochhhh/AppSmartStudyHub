package com.focusedapp.smartstudyhub.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.focusedapp.smartstudyhub.model.Folder;

@Repository
public interface FolderDAO extends JpaRepository<Folder, Integer> {

	@Query(value = "SELECT * FROM folder f " + 
			"WHERE f.user_id = :userId " + 
				"AND f.status = :status ", nativeQuery = true)
	List<Folder> findByUserIdAndStatus(@Param("userId") Integer userId, @Param("status") String status);
	
	Optional<Folder> findByIdAndStatus(Integer id, String status);
	
	List<Folder> findByFolderNameContainingAndUserIdAndStatus(String keySearch, Integer userId, String status);
	
}
