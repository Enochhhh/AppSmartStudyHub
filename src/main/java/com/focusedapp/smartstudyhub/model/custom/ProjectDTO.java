package com.focusedapp.smartstudyhub.model.custom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.springframework.util.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.focusedapp.smartstudyhub.model.Project;
import com.focusedapp.smartstudyhub.model.Work;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatus;
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
	private List<WorkDTO> listWorkActive;
	private Integer totalTimeWork;
	private Integer totalWorkActive;
	private Integer totalWorkCompleted;
	private Integer totalTimePassed;
	private List<WorkDTO> listWorkCompleted;
	private Long createdDate;
	private List<WorkDTO> listWorkDeleted;
	
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
		this.totalWorkActive = 0;
		this.totalWorkCompleted = 0;
		this.totalTimePassed = 0;
		this.listWorkActive = new ArrayList<>();
		this.listWorkCompleted = new ArrayList<>();
		this.listWorkDeleted = new ArrayList<>();
		
		List<Work> works = project.getWorks();
		
		if (!CollectionUtils.isEmpty(works)) {
			works.stream().forEach(w -> {				
				Integer time = w.getNumberOfPomodoros() * w.getTimeOfPomodoro();
				this.totalTimeWork += time;
				this.totalTimePassed += w.getTimePassed();
				if (w.getStatus().equals(EnumStatus.ACTIVE.getValue())) {
					this.listWorkActive.add(new WorkDTO(w));
				} else if (w.getStatus().equals(EnumStatus.COMPLETED.getValue())) {
					this.listWorkCompleted.add(new WorkDTO(w));
				} else {
					this.listWorkDeleted.add(new WorkDTO(w));
				};
			});
			this.totalWorkActive = this.listWorkActive.size();
			this.totalWorkCompleted = this.listWorkCompleted.size();
		}
		this.createdDate = project.getCreatedDate().getTime();
	}
}
