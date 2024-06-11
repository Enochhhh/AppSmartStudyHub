package com.focusedapp.smartstudyhub.model.custom;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class StatisticalFileDTO implements Serializable {

private static final long serialVersionUID = 1L;
	
	private Long firstDateOfMonthOrDateInMonth;
	private Integer totalThemes;
	private Integer totalSoundDones;
	private Integer totalSoundConcentrations;
	private Integer totalAvatars;
	private Integer totalCoverImages;
	private Integer totalFilesUploadedForReport;
	private Integer totalFiles;
	
	public StatisticalFileDTO(Long firstDateOfMonthOrDateInMonth, Integer totalThemes, Integer totalSoundDones, 
			Integer totalSoundConcentrations, Integer totalAvatars, Integer totalCoverImages, Integer totalFilesUploadedForReport) {
		this.firstDateOfMonthOrDateInMonth = firstDateOfMonthOrDateInMonth;
		this.totalThemes = totalThemes;
		this.totalSoundDones = totalSoundDones;
		this.totalSoundConcentrations = totalSoundConcentrations;
		this.totalAvatars = totalAvatars;
		this.totalCoverImages = totalCoverImages;
		this.totalFilesUploadedForReport = totalFilesUploadedForReport;
		this.totalFiles = totalThemes + totalSoundDones + totalSoundConcentrations + totalAvatars + totalCoverImages + totalFilesUploadedForReport;
	}
}
