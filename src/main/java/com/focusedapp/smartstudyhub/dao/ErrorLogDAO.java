package com.focusedapp.smartstudyhub.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.focusedapp.smartstudyhub.model.ErrorLog;

@Repository
public interface ErrorLogDAO extends JpaRepository<ErrorLog, Integer> {

}
