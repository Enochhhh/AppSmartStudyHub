package com.focusedapp.smartstudyhub.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.focusedapp.smartstudyhub.model.OtpCode;

public interface OtpCodeDAO extends JpaRepository<OtpCode, Integer> {
	Optional<OtpCode> findByEmail(String email);
}
