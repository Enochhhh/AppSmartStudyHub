package com.focusedapp.smartstudyhub.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.focusedapp.smartstudyhub.model.EventSchedule;
import com.focusedapp.smartstudyhub.model.User;

@Repository
public interface EventScheduleDAO extends JpaRepository<EventSchedule, Integer> {
	
	@Query(value = "select * from event_schedule e "
			+ "where e.user_id = :userId and (e.end_time > :startDate or e.start_time <= :endDate)", nativeQuery = true)
	List<EventSchedule> findByEndTimeGreaterThanOrStartTimeLessThanEqualAndUserId(@Param("startDate") Date startDate
			, @Param("endDate") Date endDate, @Param("userId") Integer userId);
	
	void deleteByUser(User user);
}
