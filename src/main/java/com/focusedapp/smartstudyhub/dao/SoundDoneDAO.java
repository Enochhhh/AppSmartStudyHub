package com.focusedapp.smartstudyhub.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.focusedapp.smartstudyhub.model.SoundDone;

@Repository
public interface SoundDoneDAO extends JpaRepository<SoundDone, Integer> {

	public List<SoundDone> findByUserIdIsNullAndStatus(String status);
}
