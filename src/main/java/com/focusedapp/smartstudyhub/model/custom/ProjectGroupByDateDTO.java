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
public class ProjectGroupByDateDTO {

	private Long date;
	private List<ProjectDTO> projects;
	private Integer timeFocus;
	private Integer totalWorkCompleted;
	private Integer totalProjectCompleted;
	
	public ProjectGroupByDateDTO (Long date, List<ProjectDTO> projects) {
		this.date = date;
		this.projects = projects;
		this.timeFocus = 0;
		this.totalWorkCompleted = 0;
		this.totalProjectCompleted = projects.size();
		if (!CollectionUtils.isEmpty(projects)) {
			projects.stream().forEach(p -> {
				List<WorkDTO> works = p.getListWorkCompleted();
				this.totalWorkCompleted += works.size();
				works.stream().forEach(w -> {
					this.timeFocus += w.getTimePassed();
				});
			});
		}
	}
}
