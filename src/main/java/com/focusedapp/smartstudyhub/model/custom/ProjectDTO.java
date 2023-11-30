package com.focusedapp.smartstudyhub.model.custom;

import java.io.Serializable;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.focusedapp.smartstudyhub.model.Project;
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
public class ProjectDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Integer userId;
	private String projectName;
	private String colorCode;
	private String iconUrl;
	private String status;
	private Integer folderId;
	private List<WorkDTO> listWorkDto;
	private Integer totalTimeWork;
	private Integer totalWork;
	
	public ProjectDTO(Project project) {
		this.id = project.getId();
		this.userId = project.getUser().getId();	
		if (project.getFolder() != null) {
			this.folderId = project.getFolder().getId();
		}		
		this.projectName = project.getProjectName();
		this.colorCode = project.getColorCode();
		this.iconUrl = project.getIconUrl();
		this.status = project.getStatus();
		this.totalTimeWork = 0;
		this.totalWork = 0;
		
		List<Work> works = project.getWorks();
		if (!CollectionUtils.isEmpty(works)) {
			totalWork = works.size();
			works.stream().forEach(w -> {				
				Integer time = w.getNumberOfPomodoros() * w.getTimeOfPomodoro();
				this.totalTimeWork += time;
			});
		}
	}
}
