package com.focusedapp.smartstudyhub.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.focusedapp.smartstudyhub.model.Report;

@Repository
public interface ReportDAO extends JpaRepository<Report, Integer> {

	List<Report> findByUserIdOrderByCreatedDateDesc(Integer userId, Pageable pageable);
	
	@Query(value = "select * from report r order by r.status_report desc, r.created_date desc", nativeQuery = true)
	List<Report> findAllReports(Pageable pageable);
}
