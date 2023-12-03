package com.focusedapp.smartstudyhub.model.custom;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.focusedapp.smartstudyhub.model.Project;
import com.focusedapp.smartstudyhub.model.User;
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
	private Integer userId;
	private ProjectDTO projectDTO;
	private Integer projectId;
	private String statusWork;
	private Date dueDate;
	private String workName;
	private String priority;
	private Integer numberOfPomodoros;
	private Integer timeOfPomodoro;
	private Integer timePassed;
	private Long startTime;
	private Long endTime;
	private Boolean isReminder;
	private Boolean isRepeated;
	private String note;
	private Integer assigneeId;
	private String mode;
	private String status;
	
	public WorkDTO(Work work) {
		this.id = work.getId();
		User user = work.getUser();
		if (user != null) {
			this.userId = user.getId();
		}
		//this.projectDTO = new ProjectDTO(work.getProject());
		Project project = work.getProject();
		if (project != null) {
			this.projectId = project.getId();
		}
		this.dueDate = work.getDueDate();
		this.workName = work.getWorkName();
		this.priority = work.getPriority();
		this.numberOfPomodoros = work.getNumberOfPomodoros();
		this.timeOfPomodoro = work.getTimeOfPomodoro();
		this.timePassed = work.getTimePassed();
		this.startTime = work.getStartTime().getTime();
		this.endTime = work.getEndTime().getTime();
		this.isReminder = work.getIsRemindered();
		this.isRepeated = work.getIsRepeated();
		this.note = work.getNote();
		User assignee = work.getAssignee();
		if (assignee != null) {
			this.assigneeId = assignee.getId();
		}
		this.mode = work.getMode();
		this.status = work.getStatus();
	}

}
