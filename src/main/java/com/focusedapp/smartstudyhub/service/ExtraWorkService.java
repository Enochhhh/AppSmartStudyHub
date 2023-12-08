package com.focusedapp.smartstudyhub.service;


import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.focusedapp.smartstudyhub.dao.ExtraWorkDAO;
import com.focusedapp.smartstudyhub.exception.NotFoundValueException;
import com.focusedapp.smartstudyhub.model.ExtraWork;
import com.focusedapp.smartstudyhub.model.Work;
import com.focusedapp.smartstudyhub.model.custom.ExtraWorkDTO;
import com.focusedapp.smartstudyhub.model.custom.PomodoroDTO;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatus;

@Service
public class ExtraWorkService {

	@Autowired
	ExtraWorkDAO extraWorkDAO;
	
	@Autowired
	WorkService workService;
	
	@Autowired
	PomodoroService pomodoroService;
	
	/**
	 * Create Extra Work
	 * 
	 * @param extraWorkDTO
	 * @return
	 */
	public ExtraWorkDTO createExtraWork(ExtraWorkDTO extraWorkDTO) {
		
		Work work = workService.findByIdAndStatus(extraWorkDTO.getWorkId(), EnumStatus.ACTIVE.getValue());
		
		ExtraWork extraWork = ExtraWork.builder()
				.work(work)
				.extraWorkName(extraWorkDTO.getExtraWorkName())
				.status(EnumStatus.ACTIVE.getValue())
				.createdDate(new Date())
				.build();
		
		extraWork = extraWorkDAO.save(extraWork);
		
		return new ExtraWorkDTO(extraWork);
	}
	
	/**
	 * Find By Id And Status
	 * 
	 * @param extraWorkId
	 * @param status
	 * @return
	 */
	public ExtraWork findByIdAndStatus(Integer extraWorkId, String status) {
		return extraWorkDAO.findByIdAndStatus(extraWorkId, status)
				.orElseThrow(() -> new NotFoundValueException("ExtraWorkService", "findByIdAndStatus"));
	}
	
	/**
	 * Update Extra Work
	 * 
	 * @param extraWorkDTO
	 * @return
	 */
	public ExtraWorkDTO updateExtraWork(ExtraWorkDTO extraWorkDTO) {
		
		// Work work = workService.findByIdAndStatus(extraWorkDTO.getWorkId(), EnumStatus.ACTIVE.getValue());
		ExtraWork extraWork = findByIdAndStatus(extraWorkDTO.getId(), EnumStatus.ACTIVE.getValue());
		
		extraWork.setExtraWorkName(extraWorkDTO.getExtraWorkName());
		extraWork.setStatus(extraWorkDTO.getStatus() == null ? EnumStatus.ACTIVE.getValue() : extraWorkDTO.getStatus());
		
		extraWork = extraWorkDAO.save(extraWork);
		
		return new ExtraWorkDTO(extraWork);
	}
	
	/**
	 * Mark Complete Extra Work
	 * 
	 * @param workId
	 * @return
	 */
	public ExtraWorkDTO markCompleted(Integer extraWorkId) {

		ExtraWork extraWorkDb = findByIdAndStatus(extraWorkId, EnumStatus.ACTIVE.getValue());
				
		extraWorkDb.setStatus(EnumStatus.COMPLETED.getValue());
		
		PomodoroDTO pomodoroRequest = PomodoroDTO.builder()
				.userId(extraWorkDb.getWork().getUser().getId())
				.extraWorkId(extraWorkDb.getId())
				.endTime(new Date().getTime())
				.isEndPomo(true)
				.build();
		extraWorkDb = extraWorkDAO.save(extraWorkDb);
		pomodoroService.createPomodoro(pomodoroRequest);

		return new ExtraWorkDTO(extraWorkDb);
	}
	
}
