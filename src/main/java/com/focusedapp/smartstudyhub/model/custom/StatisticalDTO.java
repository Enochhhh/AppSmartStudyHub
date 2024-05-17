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
	private Integer totalValue;
	
	public StatisticalDTO(Long date, Integer totalValue) {
		this.date = date;
		this.totalValue = totalValue;
	}
	
	public StatisticalDTO(Long startDate, Long endDate, Integer totalValue) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.totalValue = totalValue;
	}
	
	public StatisticalDTO(List<StatisticalByUnitDTO> listDataStatisticalByUnit, Integer totalValue) {
		this.listDataStatisticalByUnit = listDataStatisticalByUnit;
		this.totalValue = totalValue;
	}
	
}
