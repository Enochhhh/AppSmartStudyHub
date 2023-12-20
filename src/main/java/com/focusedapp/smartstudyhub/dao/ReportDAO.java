package com.focusedapp.smartstudyhub.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.focusedapp.smartstudyhub.model.Report;

@Repository
public interface ReportDAO extends JpaRepository<Report, Integer> {

}
