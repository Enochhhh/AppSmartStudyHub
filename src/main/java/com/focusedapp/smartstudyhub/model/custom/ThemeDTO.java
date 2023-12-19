package com.focusedapp.smartstudyhub.model.custom;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.focusedapp.smartstudyhub.model.Theme;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class ThemeDTO implements Serializable {
	 
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	private Integer userId;
	
	private String nameTheme;
	
	private String url;
	
	private String statusTheme;
	
	private Long createdDate;
	
	private String status;
	
	public ThemeDTO(Theme theme) {
		this.id = theme.getId();
		if (theme.getUser() != null) {
			this.userId = theme.getUser().getId();
		}
		this.nameTheme = theme.getNameTheme();
		this.url = theme.getUrl();
		this.statusTheme = theme.getStatusTheme();
		this.createdDate = theme.getCreatedDate().getTime();
		this.status = theme.getStatus();
	}

}
