package com.focusedapp.smartstudyhub.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.focusedapp.smartstudyhub.model.SoundConcentration;

@Repository
public interface SoundConcentrationDAO extends JpaRepository<SoundConcentration, Integer> {

	List<SoundConcentration> findByUserIdIsNullAndStatus(String status);
	
	List<SoundConcentration> findByUserIdOrUserIdIsNullAndStatus(Integer userId, String status);
	
	Optional<SoundConcentration> findByIdAndStatus(Integer soundConcentrationId, String status);
}
