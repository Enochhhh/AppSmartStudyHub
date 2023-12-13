package com.focusedapp.smartstudyhub.model.custom;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.focusedapp.smartstudyhub.model.Tag;
import com.focusedapp.smartstudyhub.model.Work;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatus;
import com.nimbusds.oauth2.sdk.util.CollectionUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(value = Include.NON_NULL)
public class TagDTO {

	private Integer id;
	private Integer userId;
	private String tagName;
	private String colorCode;
	private Long createdDate;
	private String status;
	private List<WorkDTO> listWorkActive;
	private Integer totalTimeWork;
	private Integer totalWorkActive;
	private Integer totalWorkCompleted;
	private Integer totalTimePassed;
	private List<WorkDTO> listWorkCompleted;
	private List<WorkDTO> listWorkDeleted;
	
	public TagDTO(Tag tag) {
		this.id = tag.getId();
		this.userId = tag.getUser().getId();
		this.tagName = tag.getTagName();
		this.colorCode = tag.getColorCode();
		this.createdDate = tag.getCreatedDate().getTime();
		this.status = tag.getStatus();
		this.listWorkActive = new ArrayList<>();
		this.totalTimeWork = 0;
		this.totalWorkActive = 0;
		this.totalWorkCompleted = 0;
		this.totalTimePassed = 0;
		this.listWorkCompleted = new ArrayList<>();
		this.listWorkDeleted = new ArrayList<>();
		
		List<Work> works = tag.getWorks();	
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
	}
}
