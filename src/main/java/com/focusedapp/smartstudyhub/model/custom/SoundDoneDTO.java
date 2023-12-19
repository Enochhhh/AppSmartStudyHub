package com.focusedapp.smartstudyhub.model.custom;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.focusedapp.smartstudyhub.model.SoundDone;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class SoundDoneDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	
	private Integer userId;
	
	private String nameSound;
	
	private String url;
	
	private String statusSound;
	
	private Long createdDate;
	
	private String status;
	
	public SoundDoneDTO(SoundDone soundDone) {
		this.id = soundDone.getId();
		if (soundDone.getUser() != null) {
			this.userId = soundDone.getUser().getId();
		}
		this.nameSound = soundDone.getNameSound();
		this.url = soundDone.getUrl();
		this.statusSound = soundDone.getStatusSound();
		this.createdDate = soundDone.getCreatedDate().getTime();
		this.status = soundDone.getStatus();
	}
}
