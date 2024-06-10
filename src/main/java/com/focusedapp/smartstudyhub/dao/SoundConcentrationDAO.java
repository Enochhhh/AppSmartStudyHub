package com.focusedapp.smartstudyhub.dao;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.focusedapp.smartstudyhub.model.SoundConcentration;
import com.focusedapp.smartstudyhub.model.User;


@Repository
public interface SoundConcentrationDAO extends JpaRepository<SoundConcentration, Integer> {

	List<SoundConcentration> findByUserIdIsNullAndStatus(String status);
	
	@Query(value = "SELECT * FROM sound_concentration s WHERE (s.user_id = :userId or s.user_id IS NULL)"
			+ " AND s.status = :status", nativeQuery = true)
	List<SoundConcentration> findByUserIdOrUserIdIsNullAndStatus(@Param("userId") Integer userId, @Param("status") String status);
	
	Optional<SoundConcentration> findByIdAndStatus(Integer soundConcentrationId, String status);
	
	List<SoundConcentration> findByUserIdAndStatus(Integer userId, String status);
	
	void deleteByUser(User user);
	
	SoundConcentration findByUrl(String url);
	
	List<SoundConcentration> findByUserNullAndStatusSound(String statusSound, Pageable pageable);
	
	List<SoundConcentration> findByUserNull(Pageable pageable);
	
	List<SoundConcentration> findByUserNullAndCreatedDateBetween(Date startDate, Date endDate, Pageable pageable);
	
	List<SoundConcentration> findByUserNull();
}
