package com.focusedapp.smartstudyhub.model.custom;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.focusedapp.smartstudyhub.model.Report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class ReportDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Integer id;
	
	private Integer userId;
	
	private String email;
	
	private String phoneNumber;
	
	private String title;
	
	private String content;
	
	private String statusReport;
	
	private Long createdDate;
	
	private String typeReport;
	
	private String urlFile;
	
	private String status;
	
	public ReportDTO(Report report) {
		this.id = report.getId();
		this.userId = report.getUser().getId();
		this.email = report.getEmail();
		this.phoneNumber = report.getPhoneNumber();
		this.title = report.getTitle();
		this.content = report.getContent();
		this.statusReport = report.getStatusReport();
		this.createdDate = report.getCreatedDate().getTime();
		this.typeReport = report.getTypeReport();
		this.urlFile = report.getUrlFile();
		this.status = report.getStatus();
	}
}
