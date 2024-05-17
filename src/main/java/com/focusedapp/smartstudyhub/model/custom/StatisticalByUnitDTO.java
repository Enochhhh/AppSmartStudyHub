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
public class StatisticalByUnitDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer unitId;
	private String unitName;
	private String unitColor;
	private Integer totalTimeFocusInUnit;
	
	public StatisticalByUnitDTO(Integer unitId, String unitName, Integer totalTimeFocusInUnit) {
		this.unitId = unitId;
		this.unitName = unitName;
		this.totalTimeFocusInUnit = totalTimeFocusInUnit;
	}
	
}
