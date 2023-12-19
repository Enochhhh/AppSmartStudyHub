package com.focusedapp.smartstudyhub.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.focusedapp.smartstudyhub.model.SoundConcentration;

@Repository
public interface SoundConcentrationDAO extends JpaRepository<SoundConcentration, Integer> {

	public List<SoundConcentration> findByUserIdIsNullAndStatus(String status);
}
