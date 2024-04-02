package com.focusedapp.smartstudyhub.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.focusedapp.smartstudyhub.dao.ExtraWorkDAO;
import com.focusedapp.smartstudyhub.dao.PomodoroDAO;
import com.focusedapp.smartstudyhub.dao.WorkDAO;
import com.focusedapp.smartstudyhub.exception.NotFoundValueException;
import com.focusedapp.smartstudyhub.model.ExtraWork;
import com.focusedapp.smartstudyhub.model.Pomodoro;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.Work;
import com.focusedapp.smartstudyhub.model.custom.PomodoroDTO;
import com.focusedapp.smartstudyhub.model.custom.PomorodoGroupByDateDTO;
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
			if (work.getStartTime() == null && (pomodoroRequest.getIsEndPomo() == null || pomodoroRequest.getIsEndPomo() == false)) {
				isStarting = true;
			}
		}
		
		Optional<ExtraWork> extraWorkOptional = extraWorkDAO.findById(pomodoroRequest.getExtraWorkId() == null ? -1 : pomodoroRequest.getExtraWorkId());
		ExtraWork extraWork = null;
		if (!extraWorkOptional.isEmpty()) {
			extraWork = extraWorkOptional.get();
			namePomo = extraWork.getExtraWorkName();
			if (extraWork.getStartTime() == null && (pomodoroRequest.getIsEndPomo() == null || pomodoroRequest.getIsEndPomo() == false)) {
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
				.numberPomoDoneOfWork(0)
				.createdDate(new Date())
				.build();
		
		if (work != null && pomodoroRequest.getIsEndPomo() != null && pomodoroRequest.getIsEndPomo()) {
			pomodoro.setNumberPomoDoneOfWork(work.getNumberOfPomodorosDone() == null ? 0 : work.getNumberOfPomodorosDone());
		} else if (extraWork != null && pomodoroRequest.getIsEndPomo() != null && pomodoroRequest.getIsEndPomo()) {
			pomodoro.setNumberPomoDoneOfWork(extraWork.getNumberOfPomodoros() == null ? 0 : extraWork.getNumberOfPomodoros());
		}
		pomodoro = pomodoroDAO.save(pomodoro);
		
		return new PomodoroDTO(pomodoro);
				
	}
	
	/**
	 * Get Pomodoros
	 * 
	 * @param userId
	 * @return
	 */
	public List<PomorodoGroupByDateDTO> getPomodoros(Integer userId) {
		
		List<Pomodoro> pomorosDb = pomodoroDAO.findByUserId(userId);
		
		Map<Long, List<PomodoroDTO>> mapPomodoro = pomorosDb.stream()	
				.map(pomo -> new PomodoroDTO(pomo))				
				.collect(Collectors.groupingBy(p -> { 
						Date date = new Date(p.getEndTime());
						// Get date at time 00:00:00
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(date);
						calendar.set(Calendar.HOUR_OF_DAY, 0);
						calendar.set(Calendar.MINUTE, 0);
				        calendar.set(Calendar.SECOND, 0);
				        calendar.set(Calendar.MILLISECOND, 0);
				        date = calendar.getTime();
						return date.getTime();
					}, Collectors.toList()));
		mapPomodoro.values().forEach(list -> list.sort(Comparator.comparing(PomodoroDTO::getEndTime)));
		List<PomorodoGroupByDateDTO> pomodoros = new ArrayList<>();
		for (Map.Entry<Long, List<PomodoroDTO>> entry : mapPomodoro.entrySet()) {
			pomodoros.add(new PomorodoGroupByDateDTO(entry.getKey(), entry.getValue()));
		}
		return pomodoros;
	}
	
	/**
	 * Delete Pomodoro
	 * 
	 * @param pomodoroId
	 * @return
	 */
	public PomodoroDTO deletePomodoro(Integer pomodoroId) {
		Pomodoro pomodoro = pomodoroDAO.findById(pomodoroId)
				.orElseThrow(() -> new NotFoundValueException("Not Found Pomodoro to delete!", "PomodoroService -> deletePomodoro"));
		
		pomodoroDAO.delete(pomodoro);
		return new PomodoroDTO(pomodoro);
	}
	
	/**
	 * Calculate Total Time Focus Previous Month
	 * 
	 * @param userId
	 * @param date
	 * @return
	 */
	public Integer calculateTotalTimeFocusPreviousMonth(User user, Date date) {
		return pomodoroDAO.calculateTotalTimeFocusPreviousMonth(user, date);
	}
	
	/**
	 * Delete All Pomodoros of User
	 * 
	 * @param user
	 */
	public void deleteAllPomodorosOfUser(User user) {
		pomodoroDAO.deleteByUser(user);
	}
}
