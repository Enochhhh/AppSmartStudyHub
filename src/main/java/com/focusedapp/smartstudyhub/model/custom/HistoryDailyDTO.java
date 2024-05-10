package com.focusedapp.smartstudyhub.model.custom;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.focusedapp.smartstudyhub.model.HistoryDaily;
import com.focusedapp.smartstudyhub.service.PomodoroService;
import com.focusedapp.smartstudyhub.service.WorkService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class HistoryDailyDTO implements Serializable {
	
	@Autowired WorkService workService;
	@Autowired PomodoroService pomodoroService;
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Integer userId;	
	private Long dates;
	private String workIds;
	private List<WorkDTO> works;
	private String pomodoroIds;
	private List<PomodoroDTO> pomodoros;
	private Integer totalWorksDone;
	private Integer totalPomodorosDone;
	private Integer totalTimeFocus;
	
	public HistoryDailyDTO(HistoryDaily historyDaily, List<WorkDTO> works, List<PomodoroDTO> pomodoros) {
		this.id = historyDaily.getId();
		this.userId = historyDaily.getUser().getId();
		this.dates = historyDaily.getDates().getTime();
		this.works = works;
		this.pomodoros = pomodoros;
		this.totalWorksDone = historyDaily.getTotalWorksDone();
		this.totalPomodorosDone = historyDaily.getTotalPomodorosDone();
		this.totalTimeFocus = historyDaily.getTotalTimeFocus();
	}
}
