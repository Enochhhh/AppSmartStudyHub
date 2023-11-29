package com.focusedapp.smartstudyhub.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.focusedapp.smartstudyhub.model.Project;

@Repository
public interface ProjectDAO extends JpaRepository<Project, Integer> {

	Optional<Project> findByIdAndStatus(Integer id, String status);
}
