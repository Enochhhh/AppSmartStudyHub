package com.focusedapp.smartstudyhub.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.focusedapp.smartstudyhub.model.SoundDone;
import com.focusedapp.smartstudyhub.model.User;

@Repository
public interface SoundDoneDAO extends JpaRepository<SoundDone, Integer> {

	public List<SoundDone> findByUserIdIsNullAndStatus(String status);
	
	List<SoundDone> findByUserIdOrUserIdIsNullAndStatus(Integer userId, String status);
	
	Optional<SoundDone> findByIdAndStatus(Integer soundDoneId, String status);
	
	List<SoundDone> findByUserIdAndStatus(Integer userId, String status);
	
	void deleteByUser(User user);
	
}
