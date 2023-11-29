package com.focusedapp.smartstudyhub.model.custom;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.focusedapp.smartstudyhub.model.Work;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class WorkDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private UserDTO user;
	private ProjectDTO projectDTO;
	private String statusWork;
	private Date dueDate;
	private String workName;
	private String priority;
	private Integer numberOfPomodoros;
	private Integer timeOfPomodoro;
	private Date startTime;
	private Date endTime;
	private Boolean isReminder;
	private Boolean isRepeated;
	private String note;
	private UserDTO assignee;
	private String status;
	
	public WorkDTO(Work work) {
		this.id = work.getId();
		this.user = new UserDTO(work.getUser());
		//this.projectDTO = new ProjectDTO(work.getProject());
		this.statusWork = work.getStatusWork();
		this.dueDate = work.getDueDate();
		this.workName = work.getWorkName();
		this.priority = work.getPriority();
		this.numberOfPomodoros = work.getNumberOfPomodoros();
		this.timeOfPomodoro = work.getTimeOfPomodoro();
		this.startTime = work.getStartTime();
		this.endTime = work.getEndTime();
		this.isReminder = work.getIsRemindered();
		this.isRepeated = work.getIsRepeated();
		this.note = work.getNote();
		this.assignee = new UserDTO(work.getAssignee());
		this.status = work.getStatus();
	}

}
