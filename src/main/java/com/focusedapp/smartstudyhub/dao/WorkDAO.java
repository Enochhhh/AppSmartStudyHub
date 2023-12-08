package com.focusedapp.smartstudyhub.dao;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.focusedapp.smartstudyhub.model.Work;

@Repository
public interface WorkDAO extends JpaRepository<Work, Integer> {
	
	Optional<Work> findByIdAndStatus(Integer workId, String status);
	
	List<Work> findByProjectIdAndStatus(Integer projectid, String status);
	
	List<Work> findByProjectIdAndUserIdAndStatus(Integer projectid, Integer userId, String status);
}
