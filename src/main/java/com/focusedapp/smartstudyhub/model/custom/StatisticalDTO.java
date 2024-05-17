package com.focusedapp.smartstudyhub.model.custom;

import java.io.Serializable;
import java.util.List;

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
public class StatisticalDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long date;
	private Long startDate;
	private Long endDate;
	private List<StatisticalByUnitDTO> listDataStatisticalByUnit;
	private Integer totalTimeFocus;
	
	public StatisticalDTO(Long date, Integer totalTimeFocus) {
		this.date = date;
		this.totalTimeFocus = totalTimeFocus;
	}
	
	public StatisticalDTO(Long startDate, Long endDate, Integer totalTimeFocus) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.totalTimeFocus = totalTimeFocus;
	}
	
	public StatisticalDTO(List<StatisticalByUnitDTO> listDataStatisticalByUnit, Integer totalTimeFocus) {
		this.listDataStatisticalByUnit = listDataStatisticalByUnit;
		this.totalTimeFocus = totalTimeFocus;
	}
	
}
