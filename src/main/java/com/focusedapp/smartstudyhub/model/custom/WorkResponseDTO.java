package com.focusedapp.smartstudyhub.model.custom;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatus;
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
public class WorkResponseDTO {

	List<WorkDTO> listWorkActive;
	List<WorkDTO> listWorkCompleted;
	private Integer totalTimeWork;
	private Integer totalWorkActive;
	private Integer totalWorkCompleted;
	private Integer totalTimePassed;
	
	public WorkResponseDTO(List<WorkDTO> works) {
		this.listWorkActive = new ArrayList<>();
		this.listWorkCompleted = new ArrayList<>();
		this.totalTimeWork = 0;
		this.totalWorkActive = 0;
		this.totalWorkCompleted = 0;
		this.totalTimePassed = 0;
		
		if (!CollectionUtils.isEmpty(works)) {
			works.stream().forEach(w -> {				
				
				this.totalTimePassed += w.getTimePassed();
				if (w.getStatus().equals(EnumStatus.ACTIVE.getValue())) {
					Integer numberPomosDone = w.getNumberOfPomodorosDone() == null ? 0 : w.getNumberOfPomodorosDone();
					Integer numberPomo = w.getNumberOfPomodoros() - numberPomosDone;
					Integer time = (numberPomo < 0 ? 0 : numberPomo) * w.getTimeOfPomodoro();
					
					this.listWorkActive.add(w);
					this.totalTimeWork += time;
				} else if (w.getStatus().equals(EnumStatus.COMPLETED.getValue())) {
					this.listWorkCompleted.add(w);
				};
			});
			this.totalWorkActive = this.listWorkActive.size();
			this.totalWorkCompleted = this.listWorkCompleted.size();
		}
	}
}
