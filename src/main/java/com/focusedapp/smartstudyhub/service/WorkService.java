package com.focusedapp.smartstudyhub.service;



import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.focusedapp.smartstudyhub.dao.ProjectDAO;
import com.focusedapp.smartstudyhub.dao.WorkDAO;
import com.focusedapp.smartstudyhub.exception.NotFoundValueException;
import com.focusedapp.smartstudyhub.model.Project;
import com.focusedapp.smartstudyhub.model.Work;
import com.focusedapp.smartstudyhub.model.custom.PomodoroDTO;
import com.focusedapp.smartstudyhub.model.custom.WorkDTO;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatus;

@Service
public class WorkService {
	
	@Autowired
	WorkDAO workDAO;
	
	@Autowired
	ProjectDAO projectDAO;
	
	@Autowired
	UserService userService;
	
	@Autowired
	PomodoroService pomodoroService;
	
	/**
	 * Find Work By Id and Status
	 * 
	 * @param workId
	 * @param status
	 * @return
	 */
	public Work findByIdAndStatus(Integer workId, String status) {
		return workDAO.findByIdAndStatus(workId, EnumStatus.ACTIVE.getValue())
				.orElseThrow(() -> new NotFoundValueException("Not Found The Work By id and status", "WorkService -> findByIdAndStatus"));
	}
	
	/**
	 * Create Work
	 * 
	 * @param dataCreate
	 * @return
	 */
	public WorkDTO createWork(WorkDTO dataCreate) {

		Optional<Project> projectOtp = projectDAO.findByIdAndStatus(dataCreate.getProjectId(), EnumStatus.ACTIVE.getValue());
		Project project = null;
		if (!projectOtp.isEmpty()) {
			project = projectOtp.get();
		}
		
		Work work = Work.builder()
				.user(userService.findByIdAndStatus(dataCreate.getUserId(), EnumStatus.ACTIVE.getValue()))
				.project(project)
				.workName(dataCreate.getWorkName())
				.dueDate(dataCreate.getDueDate() == null ? null : new Date(dataCreate.getDueDate()))
				.priority(dataCreate.getPriority())
				.numberOfPomodoros(dataCreate.getNumberOfPomodoros())
				.timeOfPomodoro(dataCreate.getTimeOfPomodoro())
				.isRemindered(Boolean.FALSE)
				.isRepeated(Boolean.FALSE)
				.createdDate(new Date())
				.status(EnumStatus.ACTIVE.getValue())
				.assignee(dataCreate.getAssigneeId() == null ? null : 
					userService.findByIdAndStatus(dataCreate.getAssigneeId(), EnumStatus.ACTIVE.getValue()))
				.build();
				
		work = workDAO.save(work);

		return new WorkDTO(work);
	}
	
	/**
	 * Update Work
	 * 
	 * @param dataUpdate
	 * @return
	 */
	public WorkDTO updateWork(WorkDTO dataUpdate) {

		Work workDb = findByIdAndStatus(dataUpdate.getId(), EnumStatus.ACTIVE.getValue());
		Optional<Project> projectOtp = projectDAO.findByIdAndStatus(dataUpdate.getProjectId(), EnumStatus.ACTIVE.getValue());
		Project project = null;
		if (!projectOtp.isEmpty()) {
			project = projectOtp.get();
		}
		
		workDb.setProject(project);
		workDb.setWorkName(dataUpdate.getWorkName());
		workDb.setDueDate(dataUpdate.getDueDate() == null ? null : new Date(dataUpdate.getDueDate()));
		workDb.setPriority(dataUpdate.getPriority());
		workDb.setNumberOfPomodoros(dataUpdate.getNumberOfPomodoros());
		workDb.setTimeOfPomodoro(dataUpdate.getTimeOfPomodoro());
		workDb.setIsRemindered(dataUpdate.getIsRemindered());
		workDb.setIsRepeated(dataUpdate.getIsRepeated());
		workDb.setNote(dataUpdate.getNote());
		workDb.setTimePassed(dataUpdate.getTimePassed());		
		workDb.setAssignee(dataUpdate.getAssigneeId() == null ? null : 
					userService.findByIdAndStatus(dataUpdate.getAssigneeId(), EnumStatus.ACTIVE.getValue()));
		workDb.setNumberOfPomodorosDone(dataUpdate.getNumberOfPomodorosDone());
		if (dataUpdate.getStartTime() != null) {
			workDb.setStartTime(new Date(dataUpdate.getStartTime()));
		}
		if (dataUpdate.getEndTime() != null) {
			workDb.setEndTime(new Date(dataUpdate.getEndTime()));
		}		
		workDb.setStatus(dataUpdate.getStatus() == null ? EnumStatus.ACTIVE.getValue() : dataUpdate.getStatus());
				
		workDb = workDAO.save(workDb);

		return new WorkDTO(workDb);
	}
	
	/**
	 * Mark Deleted Work
	 * 
	 * @param workId
	 * @return
	 */
	public WorkDTO markDeletedWork(Integer workId) {

		Work workDb = findByIdAndStatus(workId, EnumStatus.ACTIVE.getValue());
		workDb.setStatus(EnumStatus.DELETED.getValue());
				
		workDb = workDAO.save(workDb);

		return new WorkDTO(workDb);
	}
	
	/**
	 * Delete Completely Work By Id
	 * 
	 * @param workId
	 * @return
	 */
	public WorkDTO deleteById(Integer workId) {
		
		Work workDb = findByIdAndStatus(workId, EnumStatus.ACTIVE.getValue());
		workDAO.delete(workDb);
		
		return new WorkDTO(workDb);
	}
	
	/**
	 * Get works by Project And Status
	 * 
	 * @param projectId
	 * @param status
	 * @return
	 */
	public List<WorkDTO> getWorksByProjectAndStatus(Integer projectId, Integer userId, String status) {
		
		List<Work> works = workDAO.findByProjectIdAndUserIdAndStatus(projectId, userId, status);
		
		return works.stream()
				.map(w -> new WorkDTO(w))
				.collect(Collectors.toList());
	}
	
	/**
	 * Get Detail Work
	 * 
	 * @param workId
	 * @return
	 */
	public WorkDTO getDetailWork(Integer workId) {
		Work work = findByIdAndStatus(workId, EnumStatus.ACTIVE.getValue());
		
		return new WorkDTO(work);
	}
	
	/**
	 * Update Work
	 * 
	 * @param dataUpdate
	 * @return
	 */
	public WorkDTO markCompleted(Integer workId) {

		Work workDb = findByIdAndStatus(workId, EnumStatus.ACTIVE.getValue());
				
		workDb.setStatus(EnumStatus.COMPLETED.getValue());
		
		PomodoroDTO pomodoroRequest = PomodoroDTO.builder()
				.userId(workDb.getUser().getId())
				.workId(workDb.getId())
				.endTime(new Date().getTime())
				.isEndPomo(true)
				.build();
		workDb = workDAO.save(workDb);
		pomodoroService.createPomodoro(pomodoroRequest);

		return new WorkDTO(workDb);
	}

}
