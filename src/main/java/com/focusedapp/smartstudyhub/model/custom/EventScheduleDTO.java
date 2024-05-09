package com.focusedapp.smartstudyhub.model.custom;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.focusedapp.smartstudyhub.model.EventSchedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class EventScheduleDTO {
	
	private Integer id;
	private String eventName;
	private Integer userId;
	private Long startTime;
	private Long endTime;
	private Boolean isAllDay;
	private Integer totalDays;
	private Integer nowDateOutOfTotalDays;
	private String place;
	private String typeRemindered;
	private Long dateRemindered;
	private String colorCode;
	private String descriptions;
	private Long createdDate;
	private Boolean isPresent;
	
	public EventScheduleDTO(EventSchedule eventSchedule) {
		this.id = eventSchedule.getId();
		this.eventName = eventSchedule.getEventName();
		this.userId = eventSchedule.getUser().getId();
		this.startTime = eventSchedule.getStartTime().getTime();
		this.endTime = eventSchedule.getEndTime().getTime();
		this.isAllDay = eventSchedule.getIsAllDay();
		this.totalDays = eventSchedule.getTotalDays();
		this.place = eventSchedule.getPlace();
		this.typeRemindered = eventSchedule.getTypeRemindered();
		if (eventSchedule.getDateRemindered() != null) {
			this.dateRemindered = eventSchedule.getDateRemindered().getTime();
		}
		this.colorCode = eventSchedule.getColorCode();
		this.descriptions = eventSchedule.getDescriptions();
		this.createdDate = eventSchedule.getCreatedDate().getTime();
		this.isPresent = eventSchedule.getIsPresent();
	}
}
