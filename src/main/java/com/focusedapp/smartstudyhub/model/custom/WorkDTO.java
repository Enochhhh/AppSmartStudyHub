package com.focusedapp.smartstudyhub.model.custom;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.focusedapp.smartstudyhub.model.Project;
import com.focusedapp.smartstudyhub.model.Tag;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.Work;
import com.focusedapp.smartstudyhub.util.MethodUtils;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatus;
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
	private String projectName;
	private Long dueDate;
	private String workName;
	private String priority;
	private Integer numberOfPomodoros;
	private Integer numberOfPomodorosDone;
	private Integer timeOfPomodoro;
	private Integer timePassed;
	private Long startTime;
	private Long endTime;
	private Boolean isRemindered;
	private Boolean isRepeated;
	private String note;
	private Integer assigneeId;
	private Long timeWillStart;
	private Long timeWillAnnounce;
	private String status;
	private String statusWork;
	private Long createdDate;
	private List<ExtraWorkDTO> extraWorks;
	private List<TagDTO> tags;
	
	public WorkDTO(Work work) {
		this.id = work.getId();
		User user = work.getUser();
		if (user != null) {
			this.userId = user.getId();
		}
		Project project = work.getProject();
		if (project != null) {
			this.projectId = project.getId();
			this.projectName = project.getProjectName();
		}
		this.workName = work.getWorkName();
		this.priority = work.getPriority();
		this.numberOfPomodoros = work.getNumberOfPomodoros() == null ? 0 : work.getNumberOfPomodoros();
		this.timeOfPomodoro = work.getTimeOfPomodoro();
		this.timePassed = work.getTimePassed() == null ? 0 : work.getTimePassed();				
		
		if (work.getStartTime() != null) {
			this.startTime = work.getStartTime().getTime();
		}
		
		if (work.getEndTime() != null) {
			this.endTime = work.getEndTime().getTime();
		}
		
		if (work.getTimeWillStart() != null) {
			this.timeWillStart = work.getTimeWillStart().getTime();
		}
		
		if (work.getTimeWillAnnounce() != null) {
			this.timeWillAnnounce = work.getTimeWillAnnounce().getTime();
		}
		
		this.isRemindered = work.getIsRemindered();
		this.isRepeated = work.getIsRepeated();
		this.note = work.getNote();
		User assignee = work.getAssignee();
		if (assignee != null) {
			this.assigneeId = assignee.getId();
		}
		this.status = work.getStatus();
		
		// Handle set status work
		if (work.getDueDate() == null) {
			this.statusWork = EnumStatusWork.SOMEDAY.getValue();
		} else {
			this.dueDate = work.getDueDate().getTime();
			Date nowDate = new Date();
			Long distanceOfTwoDate = MethodUtils.distanceBetweenTwoDate(nowDate, work.getDueDate(), TimeUnit.DAYS);		
			
			// Convert to LocalDate Timezone VietNam to get DayOfWeek Exactly
			LocalDateTime dateTimeZone = MethodUtils.convertoToLocalDateTime(nowDate);
			// DayOfWeek in Local Date represent 1 = Monday, 2 = Tuesday
			int dayOfWeek = dateTimeZone.getDayOfWeek().getValue() + 1;
			if (distanceOfTwoDate < 0) {
				this.statusWork = EnumStatusWork.OUTOFDATE.getValue();
			} else if (distanceOfTwoDate == 0) {				
				if (new Date().getTime() > work.getDueDate().getTime()) {
					this.statusWork = EnumStatusWork.OUTOFDATE.getValue();
				} else {
					this.statusWork = EnumStatusWork.TODAY.getValue();
				}
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
		this.numberOfPomodorosDone = work.getNumberOfPomodorosDone();
		this.createdDate = work.getCreatedDate().getTime();
		if (work.getExtraWorks() != null) {
			this.extraWorks = work.getExtraWorks().stream()
					.filter(ew -> !ew.getStatus().equals(EnumStatus.DELETED.getValue()))				
					.map(ew -> new ExtraWorkDTO(ew))
					.collect(Collectors.toList());
		}
		
		List<Tag> tagListEntity = work.getTags();
		if (tagListEntity != null) {
			this.tags = tagListEntity.stream()
							.map(tag -> {
								TagDTO tagNew = new TagDTO();
								tagNew.setId(tag.getId());
								tagNew.setUserId(tag.getUser().getId());
								tagNew.setTagName(tag.getTagName());
								tagNew.setColorCode(tag.getColorCode());
								tagNew.setCreatedDate(tag.getCreatedDate().getTime());
								tagNew.setStatus(tag.getStatus());
								
								return tagNew;
							})
							.collect(Collectors.toList());
		}
	}

}
