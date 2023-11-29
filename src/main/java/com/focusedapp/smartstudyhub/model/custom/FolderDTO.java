package com.focusedapp.smartstudyhub.model.custom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.focusedapp.smartstudyhub.model.Folder;
import com.focusedapp.smartstudyhub.model.Project;
import com.focusedapp.smartstudyhub.model.User;
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
public class FolderDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer userId;
	private String folderName;
	private String colorCode;
	private String iconUrl;
	private String status;
	private Integer totalTimeWork;
	private Integer totalWork;
	private List<ProjectDTO> listProject;

	public FolderDTO(Folder folder) {
		this.id = folder.getId();
		User user = folder.getUser();
		this.userId = user == null ? null : user.getId();
		this.folderName = folder.getFolderName();
		this.colorCode = folder.getColorCode();
		this.iconUrl = folder.getIconUrl();
		this.status = folder.getStatus();
		this.totalTimeWork = 0;
		this.totalWork = 0;
		this.listProject = new ArrayList<>();

		List<Project> projects = folder.getProjects();
		if (!CollectionUtils.isEmpty(projects)) {
			projects.stream().forEach(p -> {
				this.listProject.add(new ProjectDTO(p));
				List<Work> works = p.getWorks();
				this.totalWork += works.size();
				works.stream().forEach(w -> {
					Integer time = w.getNumberOfPomodoros() * w.getTimeOfPomodoro();
					this.totalTimeWork += time;
				});
			});
		}
	}

}
