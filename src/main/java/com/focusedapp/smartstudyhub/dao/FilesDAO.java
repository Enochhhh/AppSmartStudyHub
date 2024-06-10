package com.focusedapp.smartstudyhub.dao;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
	
	List<Files> findByUserIdNotNullAndCreatedAtBetween(Date startDate, Date endDate, Pageable pageable);
	
	List<Files> findByUserIdNotNullAndType(String type, Pageable pageable);
	
	List<Files> findByUserIdNotNullAndTypeAndCreatedAtBetween(String type, Date startDate, Date enDate, Pageable pageable);
	
	List<Files> findByUserIdNotNull(Pageable pageable);
	
	List<Files> findByUserIdAndType(Integer userId, String type, Pageable pageable);
	
	List<Files> findByUserIdAndTypeAndCreatedAtBetween(Integer userId, String type, Date startDate, Date enDate, Pageable pageable);

	List<Files> findByUserId(Integer userId, Pageable pageable);
	
	List<Files> findByUserIdAndCreatedAtBetween(Integer userId, Date startDate, Date enDate, Pageable pageable);
	
	List<Files> findByUserNullAndType(String type, Pageable pageable);
	
	List<Files> findByUserNullAndTypeAndCreatedAtBetween(String type, Date startDate, Date enDate, Pageable pageable);
	
	List<Files> findByUserNull(Pageable pageable);
	
	List<Files> findByUserNullAndCreatedAtBetween(Date startDate, Date endDate, Pageable pageable);
	
}
