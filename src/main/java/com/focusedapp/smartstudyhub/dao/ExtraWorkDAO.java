package com.focusedapp.smartstudyhub.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.focusedapp.smartstudyhub.model.ExtraWork;

@Repository
public interface ExtraWorkDAO extends JpaRepository<ExtraWork, Integer> {

	Optional<ExtraWork> findByIdAndStatus(Integer id, String status);
}
