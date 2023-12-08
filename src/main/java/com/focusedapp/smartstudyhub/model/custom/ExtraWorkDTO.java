package com.focusedapp.smartstudyhub.model.custom;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.focusedapp.smartstudyhub.model.ExtraWork;
import com.focusedapp.smartstudyhub.model.Work;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(value = Include.NON_NULL)
public class ExtraWorkDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Integer workId;
	private String extraWorkName;
	private String status;
	private Long startTime;
	private Long endTime;
	private Integer numberOfPomodoros;
	private Integer timePassed;
	private Long createdDate;
	
	public ExtraWorkDTO(ExtraWork extraWork) {
		this.id = extraWork.getId();
		Work work = extraWork.getWork();
		if (work != null) {
			this.workId = extraWork.getWork().getId();
		}
		this.extraWorkName = extraWork.getExtraWorkName();
		this.status = extraWork.getStatus();
		
		Date startTimeDate = extraWork.getStartTime();
		if (startTimeDate != null) {
			this.startTime = startTimeDate.getTime();
		}
		Date endTimeDate = extraWork.getEndTime();
		if (endTimeDate != null) {
			this.endTime = endTimeDate.getTime();
		}
		
		this.numberOfPomodoros = extraWork.getNumberOfPomodoros();
		this.timePassed = extraWork.getTimePassed();
		this.createdDate = extraWork.getCreatedDate().getTime();
	}
	
}
