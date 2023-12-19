package com.focusedapp.smartstudyhub.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.focusedapp.smartstudyhub.model.Theme;

@Repository
public interface ThemeDAO extends JpaRepository<Theme, Integer> {

	public List<Theme> findByUserIdIsNullAndStatus(String status);
	
	public List<Theme> findByUserIdAndStatus(Integer userId, String status);
}
