package com.focusedapp.smartstudyhub.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.focusedapp.smartstudyhub.model.Work;

@Repository
public interface WorkDAO extends JpaRepository<Work, Integer> {

}
