package com.focusedapp.smartstudyhub.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.focusedapp.smartstudyhub.model.User;

@Repository
public interface UserDAO extends JpaRepository<User, Integer> {

	Optional<User> findByEmailAndStatus(String email, String status);
	Boolean existsByEmail(String email);
}
