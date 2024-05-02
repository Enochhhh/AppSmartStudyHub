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
public class WorkGroupByDateDTO {

	private Long date;
	private List<WorkDTO> works;
	private Integer timeFocus;
	private Integer totalWorkCompleted;
	
	public WorkGroupByDateDTO (Long date, List<WorkDTO> works) {
		this.date = date;
		this.works = works;
		this.timeFocus = 0;
		this.totalWorkCompleted = works.size();
		if (!CollectionUtils.isEmpty(works)) {
			works.stream().forEach(w -> {
				this.timeFocus += w.getTimePassed();
			});
		}
	}
}
