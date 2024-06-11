package com.focusedapp.smartstudyhub.dao;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.focusedapp.smartstudyhub.model.Files;

@Repository
public interface FilesDAO extends JpaRepository<Files, Integer> {

	Optional<Files> findTopByOrderByIdDesc();
	
	Optional<Files> findByPublicId(String publicId);
	
	List<Files> findByUserIdAndType(Integer userId, String type);
	
	List<Files> findByUserId(Integer userId);
	
	Page<Files> findByUserIdNotNullAndCreatedAtBetween(Date startDate, Date endDate, Pageable pageable);
	
	Page<Files> findByUserIdNotNullAndType(String type, Pageable pageable);
	
	Page<Files> findByUserIdNotNullAndTypeAndCreatedAtBetween(String type, Date startDate, Date enDate, Pageable pageable);
	
	Page<Files> findByUserIdNotNull(Pageable pageable);
	
	Page<Files> findByUserIdAndType(Integer userId, String type, Pageable pageable);
	
	Page<Files> findByUserIdAndTypeAndCreatedAtBetween(Integer userId, String type, Date startDate, Date enDate, Pageable pageable);

	Page<Files> findByUserId(Integer userId, Pageable pageable);
	
	Page<Files> findByUserIdAndCreatedAtBetween(Integer userId, Date startDate, Date enDate, Pageable pageable);
	
	Page<Files> findByUserNullAndType(String type, Pageable pageable);
	
	Page<Files> findByUserNullAndTypeAndCreatedAtBetween(String type, Date startDate, Date enDate, Pageable pageable);
	
	Page<Files> findByUserNull(Pageable pageable);
	
	Page<Files> findByUserNullAndCreatedAtBetween(Date startDate, Date endDate, Pageable pageable);
	
	List<Files> findByUserIdNotNullAndCreatedAtBetween(Date startDate, Date endDate);
	
	List<Files> findByUserIdAndCreatedAtBetween(Integer userId, Date startDate, Date endDate);
	
}
