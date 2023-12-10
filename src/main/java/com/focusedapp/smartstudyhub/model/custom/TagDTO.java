package com.focusedapp.smartstudyhub.model.custom;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.focusedapp.smartstudyhub.model.Tag;
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
	private List<WorkDTO> works;
	
	public TagDTO(Tag tag) {
		this.id = tag.getId();
		this.userId = tag.getUser().getId();
		this.tagName = tag.getTagName();
		this.colorCode = tag.getColorCode();
		this.createdDate = tag.getCreatedDate().getTime();
		this.status = tag.getStatus();
		this.works = new ArrayList<>();
		
		if (!CollectionUtils.isEmpty(tag.getWorks())) {
			this.works = tag.getWorks().stream()
				.map(w -> new WorkDTO(w))
				.collect(Collectors.toList());
		}
	}
	
}
