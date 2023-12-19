package com.focusedapp.smartstudyhub.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.focusedapp.smartstudyhub.model.Files;

@Repository
public interface FilesDAO extends JpaRepository<Files, Integer> {

	Optional<Files> findTopByOrderByIdDesc();
	
	Optional<Files> findByPublicId(String publicId);
	
	List<Files> findByUserIdAndType(Integer userId, String type);
}
