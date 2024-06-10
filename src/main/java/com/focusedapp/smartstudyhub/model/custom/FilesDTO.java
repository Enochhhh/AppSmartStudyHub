package com.focusedapp.smartstudyhub.model.custom;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.focusedapp.smartstudyhub.model.Files;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class FilesDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	private Integer userId;
	
	private String folder;
	
	private String fileName;
	
	private String format;
	
	private String resourceType;
	
	private String secureUrl;
	
	private Long createdAt;
	
	private String publicId;
	
	private String type;
	
	public FilesDTO(Files files) {
		this.id = files.getId();
		if (files.getUser() != null) {
			this.userId = files.getUser().getId();
		}
		this.folder = files.getFolder();
		this.fileName = files.getFileName();
		this.format = files.getFormat();
		this.resourceType = files.getResourceType();
		this.secureUrl = files.getSecureUrl();
		this.createdAt = files.getCreatedAt().getTime();
		this.publicId = files.getPublicId();
		this.type = files.getType();
	}

}
