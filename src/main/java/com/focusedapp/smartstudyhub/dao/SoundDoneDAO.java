package com.focusedapp.smartstudyhub.dao;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.focusedapp.smartstudyhub.model.SoundDone;
import com.focusedapp.smartstudyhub.model.User;

@Repository
public interface SoundDoneDAO extends JpaRepository<SoundDone, Integer> {

	public List<SoundDone> findByUserIdIsNullAndStatus(String status);
	
	@Query(value = "SELECT * FROM sound_done s WHERE (s.user_id = :userId or s.user_id IS NULL)"
			+ " AND s.status = :status", nativeQuery = true)
	List<SoundDone> findByUserIdOrUserIdIsNullAndStatus(@Param("userId") Integer userId, @Param("status") String status);
	
	Optional<SoundDone> findByIdAndStatus(Integer soundDoneId, String status);
	
	List<SoundDone> findByUserIdAndStatus(Integer userId, String status);
	
	void deleteByUser(User user);
	
	SoundDone findByUrl(String url);
	
	List<SoundDone> findByUserNullAndStatusSound(String statusSound, Pageable pageable);
	
	List<SoundDone> findByUserNullAndStatusSoundAndCreatedDateBetween(String statusSound, Date startDate, 
			Date endDate, Pageable pageable);
	
	List<SoundDone> findByUserNull(Pageable pageable);
	
	List<SoundDone> findByUserNullAndCreatedDateBetween(Date startDate, Date endDate, Pageable pageable);
	
	List<SoundDone> findByUserNull();
	
}
