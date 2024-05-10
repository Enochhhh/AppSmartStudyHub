package com.focusedapp.smartstudyhub.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.focusedapp.smartstudyhub.model.HistoryDaily;
import com.focusedapp.smartstudyhub.model.User;

@Repository
public interface HistoryDailyDAO extends JpaRepository<HistoryDaily, Integer> {
	List<HistoryDaily> findByUser(User user, Pageable pageable);
}
