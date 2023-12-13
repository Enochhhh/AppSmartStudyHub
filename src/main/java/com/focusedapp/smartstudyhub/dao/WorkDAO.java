package com.focusedapp.smartstudyhub.dao;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.focusedapp.smartstudyhub.model.Work;

@Repository
public interface WorkDAO extends JpaRepository<Work, Integer> {
	
	Optional<Work> findByIdAndStatus(Integer workId, String status);
	
	List<Work> findByProjectIdAndStatus(Integer projectid, String status);
	
	List<Work> findByProjectIdAndUserIdAndStatus(Integer projectid, Integer userId, String status);
	
	List<Work> findByUserIdAndStatus(Integer userId, String status);
	
	List<Work> findByWorkNameContainingAndUserIdAndStatus(String keySearch, Integer userId, String status);
	
	@Query(value = "select * from works w where w.user_id = :userId and (w.status = :statusFirst "
			+ " or w.status = :statusSecond)", nativeQuery = true)
	List<Work> findByUserIdAndOneOfTwoStatus(@Param("userId") Integer userId, @Param("statusFirst") String statusFirst, 
			@Param("statusSecond") String statusSecond);
}
