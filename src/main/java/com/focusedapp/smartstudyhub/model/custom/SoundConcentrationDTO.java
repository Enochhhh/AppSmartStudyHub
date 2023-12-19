package com.focusedapp.smartstudyhub.model.custom;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.focusedapp.smartstudyhub.model.SoundConcentration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class SoundConcentrationDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Integer id;
	
	private Integer userId;
	
	private String nameSound;
	
	private String url;
	
	private String statusSound;
	
	private Long createdDate;
	
	private String status;
	
	public SoundConcentrationDTO(SoundConcentration soundConcentration) {
		this.id = soundConcentration.getId();
		if (soundConcentration.getUser() != null) {
			this.userId = soundConcentration.getUser().getId();
		}
		this.nameSound = soundConcentration.getNameSound();
		this.url = soundConcentration.getUrl();
		this.statusSound = soundConcentration.getStatusSound();
		this.createdDate = soundConcentration.getCreatedDate().getTime();
		this.status = soundConcentration.getStatus();
	}
	
}
