package com.focusedapp.smartstudyhub.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.focusedapp.smartstudyhub.model.Theme;

@Repository
public interface ThemeDAO extends JpaRepository<Theme, Integer> {

	List<Theme> findByUserIdIsNullAndStatus(String status);
	
	List<Theme> findByUserIdOrUserIdIsNullAndStatus(Integer userId, String status);
	
	List<Theme> findByUserIdAndStatus(Integer userId, String status);
	
	Optional<Theme> findByIdAndStatus(Integer themeId, String status);
}
