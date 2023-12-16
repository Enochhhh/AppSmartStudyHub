package com.focusedapp.smartstudyhub.model.custom;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.nimbusds.oauth2.sdk.util.CollectionUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class PomorodoGroupByDateDTO {
	
	private Long date;
	private List<PomodoroDTO> pomodoros;
	private Integer timeFocus;
	private Integer totalWorkCompleted;
	
	public PomorodoGroupByDateDTO(Long date, List<PomodoroDTO> pomodoros) {
		this.date = date;
		this.pomodoros = pomodoros;
		this.timeFocus = 0;
		this.totalWorkCompleted = 0;
		if (!CollectionUtils.isEmpty(pomodoros)) {
			for (PomodoroDTO pomodoro : pomodoros) {
				if (pomodoro.getIsEndPomo()) {
					totalWorkCompleted++;
				} else {
					timeFocus += pomodoro.getTimeOfPomodoro();
				}
			}
		}
	}
}
