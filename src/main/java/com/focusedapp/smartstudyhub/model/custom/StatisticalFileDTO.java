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
	private Integer totalFiles;
}
