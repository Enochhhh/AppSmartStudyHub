package com.focusedapp.smartstudyhub.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.focusedapp.smartstudyhub.model.Pomodoro;
import com.focusedapp.smartstudyhub.model.User;


@Repository
public interface PomodoroDAO extends JpaRepository<Pomodoro, Integer> {

	List<Pomodoro> findByUserId(Integer userId);
	
	@Query("select sum(pomo.timeOfPomodoro) from Pomodoro pomo WHERE pomo.user = :user and pomo.createdDate >= :date "
			+ "and pomo.isEndPomo = false group by pomo.user")
	Integer calculateTotalTimeFocusPreviousMonth(@Param("user") User user, @Param("date") Date date);
	
	void deleteByUser(User user);
	
	List<Pomodoro> findByIdIn(List<Integer> ids);
	
	List<Pomodoro> findByUserIdAndCreatedDateGreaterThanEqualAndCreatedDateLessThan(Integer userId, Date startDate, Date endDate);
}
