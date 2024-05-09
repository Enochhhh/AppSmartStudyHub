package com.focusedapp.smartstudyhub.model.custom;

import java.io.Serializable;
import java.util.ArrayList;
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
public class TimeLineEventDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long keyDate;
	private List<EventScheduleDTO> events;
	private List<WorkDTO> works;
	private List<EventScheduleDTO> eventsAllDay;
	private List<WorkDTO> worksDueDate;
	private Integer totalWorksDueDate;
	private Integer totalData;
	
	public TimeLineEventDTO(Long keyDate) {
		this.keyDate = keyDate;
		this.events = new ArrayList<>();
		this.works = new ArrayList<>();
		this.eventsAllDay = new ArrayList<>();
		this.worksDueDate = new ArrayList<>();
		this.totalWorksDueDate = 0;
		this.totalData = 0;
	}

}
