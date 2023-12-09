package com.focusedapp.smartstudyhub.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.focusedapp.smartstudyhub.dao.ExtraWorkDAO;
import com.focusedapp.smartstudyhub.dao.PomodoroDAO;
import com.focusedapp.smartstudyhub.dao.WorkDAO;
import com.focusedapp.smartstudyhub.model.ExtraWork;
import com.focusedapp.smartstudyhub.model.Pomodoro;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.Work;
import com.focusedapp.smartstudyhub.model.custom.PomodoroDTO;
import com.focusedapp.smartstudyhub.util.enumerate.EnumModePomo;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatus;

@Service
public class PomodoroService {
	
	@Autowired
	PomodoroDAO pomodoroDAO;
	@Autowired
	WorkDAO workDAO;
	@Autowired
	ExtraWorkDAO extraWorkDAO;
	@Autowired
	UserService userService;
	
	/**
	 * Create Pomodoro
	 * 
	 * @param pomodoroRequest
	 * @param isStarting
	 * @return
	 */
	public PomodoroDTO createPomodoro(PomodoroDTO pomodoroRequest) {
		
		User user = userService.findByIdAndStatus(pomodoroRequest.getUserId(), EnumStatus.ACTIVE.getValue());
			
		Optional<Work> workOptional = workDAO.findById(pomodoroRequest.getWorkId() == null ? -1 : pomodoroRequest.getWorkId());
		
		String namePomo = "No assigned job yet";
		Boolean isStarting = false;
		Work work = null;
		if (!workOptional.isEmpty()) {
			work = workOptional.get();
			namePomo = work.getWorkName();
			if (work.getStartTime() == null) {
				isStarting = true;
			}
		}
		
		Optional<ExtraWork> extraWorkOptional = extraWorkDAO.findById(pomodoroRequest.getExtraWorkId() == null ? -1 : pomodoroRequest.getExtraWorkId());
		ExtraWork extraWork = null;
		if (!extraWorkOptional.isEmpty()) {
			extraWork = extraWorkOptional.get();
			namePomo = extraWork.getExtraWorkName();
			if (extraWork.getStartTime() == null) {
				isStarting = true;
			}
		}
		
		Pomodoro pomodoro = Pomodoro.builder()
				.user(user)
				.work(work)
				.extraWork(extraWork)
				.timeOfPomodoro(pomodoroRequest.getTimeOfPomodoro())
				.startTime(pomodoroRequest.getStartTime() == null ? null : new Date(pomodoroRequest.getStartTime()))
				.endTime(pomodoroRequest.getEndTime() == null ? null : new Date(pomodoroRequest.getEndTime()))
				.isStartPomo(isStarting)
				.isEndPomo(pomodoroRequest.getIsEndPomo() == null ? false : pomodoroRequest.getIsEndPomo())
				.pomodoroName(namePomo)
				.mode(work == null && extraWork == null ? EnumModePomo.NONSPECIFIED.getValue() : EnumModePomo.SPECIFIED.getValue())
				.createdDate(new Date())
				.build();
		
		if (work != null && pomodoroRequest.getIsEndPomo() != null && pomodoroRequest.getIsEndPomo()) {
			pomodoro.setNumberPomoDoneOfWork(work.getNumberOfPomodorosDone());
		} else if (extraWork != null && pomodoroRequest.getIsEndPomo() != null && pomodoroRequest.getIsEndPomo()) {
			pomodoro.setNumberPomoDoneOfWork(extraWork.getNumberOfPomodoros());
		}
		pomodoro = pomodoroDAO.save(pomodoro);
		
		return new PomodoroDTO(pomodoro);
				
	}
}
