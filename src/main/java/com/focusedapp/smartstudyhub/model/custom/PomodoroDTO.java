package com.focusedapp.smartstudyhub.model.custom;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.focusedapp.smartstudyhub.model.Pomodoro;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class PomodoroDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Integer userId;
	private Integer workId;
	private Integer extraWorkId;
	private Integer timeOfPomodoro;
	private Long startTime;
	private Long endTime;
	private Boolean isStartPomo;
	private Boolean isEndPomo;
	private String mode;
	private Integer numberPomoDoneOfWork;
	
	public PomodoroDTO(Pomodoro pomodoro) {
		this.id = pomodoro.getId();
		
		if (pomodoro.getUser() != null) {
			this.userId = pomodoro.getUser().getId();
		}
		
		if (pomodoro.getWork() != null) {
			this.workId = pomodoro.getWork().getId();
		}
		
		if (pomodoro.getExtraWork() != null) {
			this.extraWorkId = pomodoro.getExtraWork().getId();
		}
		
		this.timeOfPomodoro = pomodoro.getTimeOfPomodoro();
		
		if (pomodoro.getStartTime() != null) {
			this.startTime = pomodoro.getStartTime().getTime();
		}
		
		if (pomodoro.getEndTime() != null) {
			this.endTime = pomodoro.getEndTime().getTime();
		}
		this.isStartPomo = pomodoro.getIsStartPomo();
		this.isEndPomo = pomodoro.getIsEndPomo();
		this.mode = pomodoro.getMode();
		this.numberPomoDoneOfWork = pomodoro.getNumberPomoDoneOfWork();
	}

}
