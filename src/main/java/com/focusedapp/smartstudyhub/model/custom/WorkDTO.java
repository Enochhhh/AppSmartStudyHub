package com.focusedapp.smartstudyhub.model.custom;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.focusedapp.smartstudyhub.model.Project;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.Work;
import com.focusedapp.smartstudyhub.util.MethodUtils;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatusWork;

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
	private Integer tagId;
	private Long dueDate;
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
	private String statusWork;
	
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
		// this.tagId = tag.getId();
		this.workName = work.getWorkName();
		this.priority = work.getPriority();
		this.numberOfPomodoros = work.getNumberOfPomodoros();
		this.timeOfPomodoro = work.getTimeOfPomodoro();
		this.timePassed = work.getTimePassed();
		
		if (work.getStartTime() != null) {
			this.startTime = work.getStartTime().getTime();
		}
		
		if (work.getEndTime() != null) {
			this.endTime = work.getEndTime().getTime();
		}
		
		this.isReminder = work.getIsRemindered();
		this.isRepeated = work.getIsRepeated();
		this.note = work.getNote();
		User assignee = work.getAssignee();
		if (assignee != null) {
			this.assigneeId = assignee.getId();
		}
		this.mode = work.getMode();
		this.status = work.getStatus();
		
		// Handle set status work
		if (work.getDueDate() == null) {
			this.statusWork = EnumStatusWork.SOMEDAY.getValue();
		} else {
			this.dueDate = work.getDueDate().getTime();
			Long distanceOfTwoDate = MethodUtils.distanceBetweenTwoDate(new Date(), work.getDueDate(), TimeUnit.DAYS);
			SimpleDateFormat dateFormat = new SimpleDateFormat("F");
			Long dayOfWeek = Long.valueOf(dateFormat.format(work.getDueDate()));
			if (distanceOfTwoDate < 0) {
				this.statusWork = EnumStatusWork.OUTOFDATE.getValue();
			} else if (distanceOfTwoDate == 0) {
				this.statusWork = EnumStatusWork.TODAY.getValue();
			} else if (distanceOfTwoDate == 1) {
				this.statusWork = EnumStatusWork.TOMORROW.getValue();
			}else if(dayOfWeek + distanceOfTwoDate <= 8) {
				this.statusWork = EnumStatusWork.THISWEEK.getValue();
			} else if (distanceOfTwoDate <= 7) {
				this.statusWork = EnumStatusWork.NEXT7DAY.getValue();
			} else {
				this.statusWork = EnumStatusWork.PARTICULARDAY.getValue();
			}
		}
	}

}
