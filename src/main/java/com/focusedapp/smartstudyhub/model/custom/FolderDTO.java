package com.focusedapp.smartstudyhub.model.custom;

import java.beans.JavaBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.focusedapp.smartstudyhub.model.Folder;
import com.focusedapp.smartstudyhub.model.Project;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.Work;
import com.focusedapp.smartstudyhub.service.ProjectService;
import com.focusedapp.smartstudyhub.service.WorkService;
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
@JavaBean
public class FolderDTO implements Serializable {
	
	@Autowired
	ProjectService projectService;
	@Autowired
	WorkService workService;

	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer userId;
	private String folderName;
	private String colorCode;
	private String iconUrl;
	private String status;
	private Integer totalTimeWork;
	private Integer totalWorkActive;
	private Integer totalWorkCompleted;
	private Integer totalProjectActive;
	private Integer totalProjectCompleted;
	private Integer totalTimePassed;
	private List<ProjectDTO> listProjectActive;
	private List<ProjectDTO> listProjectCompleted;
	private Long createdDate;

	public FolderDTO(Folder folder) {
		this.id = folder.getId();
		User user = folder.getUser();
		this.userId = user == null ? null : user.getId();
		this.folderName = folder.getFolderName();
		this.colorCode = folder.getColorCode();
		this.iconUrl = folder.getIconUrl();
		this.status = folder.getStatus();
		this.totalTimeWork = 0;
		this.totalWorkActive = 0;
		this.totalWorkCompleted = 0;
		this.totalProjectActive = 0;
		this.totalProjectCompleted = 0;
		this.totalTimePassed = 0;
		this.listProjectActive = new ArrayList<>();
		this.listProjectCompleted = new ArrayList<>();

		List<Project> projectsAllStatus = folder.getProjects();
		List<Project> projects = new ArrayList<>();
		if (!CollectionUtils.isEmpty(projectsAllStatus)) {
			projects = projectsAllStatus.stream()
					.filter(p -> p.getStatus().equals(EnumStatus.ACTIVE.getValue()) 
							||  p.getStatus().equals(EnumStatus.COMPLETED.getValue()))
					.collect(Collectors.toList());
		}
		if (!CollectionUtils.isEmpty(projects)) {
			projects.stream().forEach(p -> {
				if (p.getStatus().equals(EnumStatus.ACTIVE.getValue())) {
					this.listProjectActive.add(new ProjectDTO(p));
				}else {
					this.listProjectCompleted.add(new ProjectDTO(p));
				}
				List<Work> worksAllStatus = p.getWorks();
				List<Work> works = new ArrayList<>();
				if (!CollectionUtils.isEmpty(worksAllStatus)) {
					works = worksAllStatus.stream()
							.filter(w -> w.getStatus().equals(EnumStatus.ACTIVE.getValue()) 
									||  w.getStatus().equals(EnumStatus.COMPLETED.getValue()))
							.collect(Collectors.toList());
				}
				if (!CollectionUtils.isEmpty(works)) {
					works.stream().forEach(w -> {
						Integer time = w.getNumberOfPomodoros() * w.getTimeOfPomodoro();
						this.totalTimeWork += time;
						this.totalTimePassed += w.getTimePassed();
						if (w.getStatus().equals(EnumStatus.ACTIVE.getValue())) {
							this.totalWorkActive++;
						} else {
							this.totalWorkCompleted++;
						}
					});
				}
			});
			this.totalProjectActive = this.listProjectActive.size();
			this.totalProjectCompleted = this.listProjectCompleted.size();
			this.createdDate = folder.getCreatedDate().getTime();
		}
	}

}
