package com.focusedapp.smartstudyhub.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.focusedapp.smartstudyhub.model.Pomodoro;

@Repository
public interface PomodoroDAO extends JpaRepository<Pomodoro, Integer> {

}
