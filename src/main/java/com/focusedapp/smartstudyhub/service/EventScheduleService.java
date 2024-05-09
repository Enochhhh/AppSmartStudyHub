package com.focusedapp.smartstudyhub.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.focusedapp.smartstudyhub.dao.EventScheduleDAO;
import com.focusedapp.smartstudyhub.model.EventSchedule;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.Work;
import com.focusedapp.smartstudyhub.model.custom.EventScheduleDTO;
import com.focusedapp.smartstudyhub.model.custom.TimeLineEventDTO;
import com.focusedapp.smartstudyhub.model.custom.WorkDTO;
import com.focusedapp.smartstudyhub.util.MethodUtils;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatus;
import com.focusedapp.smartstudyhub.util.enumerate.EnumZoneId;
import com.nimbusds.oauth2.sdk.util.CollectionUtils;

@Service
public class EventScheduleService {

	@Autowired EventScheduleDAO eventScheduleDAO;
	@Autowired UserService userService;
	@Autowired WorkService workService;
	
	/**
	 * Create Event Service
	 * 
	 * @param eventData
	 * @return
	 */
	public EventScheduleDTO createEvent(EventScheduleDTO eventData) {
		User user = userService.findByIdAndStatus(eventData.getUserId(), EnumStatus.ACTIVE.getValue());
		
		LocalDateTime startTime = MethodUtils.convertoToLocalDateTime(new Date(eventData.getStartTime()));
		LocalDateTime endTime = MethodUtils.convertoToLocalDateTime(new Date(eventData.getEndTime()));
		Long totalDays = MethodUtils.distanceDaysBetweenTwoDate(startTime, endTime, 
				ZoneId.of(EnumZoneId.ASIA_HOCHIMINH.getNameZone())) + 1;
		if (eventData.getEndTime() == MethodUtils.setTimeOfDateToMidnight(eventData.getEndTime()).getTime()) {
			totalDays -= 1;
		}

		EventSchedule eventSchedule = EventSchedule.builder()
				.eventName(eventData.getEventName())
				.user(user)
				.startTime(new Date(eventData.getStartTime()))
				.endTime(new Date(eventData.getEndTime()))
				.isAllDay(eventData.getIsAllDay() == null ? false : eventData.getIsAllDay())
				.totalDays(totalDays.intValue())
				.place(eventData.getPlace())
				.typeRemindered(eventData.getTypeRemindered())
				.dateRemindered(eventData.getDateRemindered() == null ? null : new Date(eventData.getDateRemindered()))
				.colorCode(eventData.getColorCode())
				.descriptions(eventData.getDescriptions())
				.createdDate(new Date())
				.isPresent(eventData.getIsPresent())
				.build();
		eventSchedule = eventScheduleDAO.save(eventSchedule);
		return new EventScheduleDTO(eventSchedule);
	}
	
	/**
	 * Get Detail Event by id Service
	 * 
	 * @param id
	 * @return
	 */
	public EventScheduleDTO getById(Integer id) {
		Optional<EventSchedule> eventSchedule = eventScheduleDAO.findById(id);
		if (eventSchedule.isEmpty()) {
			return null;
		}
		return new EventScheduleDTO(eventSchedule.get());
	}
	
	/**
	 * Update Event
	 * 
	 * @param eventData
	 * @return
	 */
	public EventScheduleDTO update(EventScheduleDTO eventData) {
		Optional<EventSchedule> eventScheduleOpt = eventScheduleDAO.findById(eventData.getId());
		if (eventScheduleOpt.isEmpty()) {
			return null;
		}
		LocalDateTime startTime = MethodUtils.convertoToLocalDateTime(new Date(eventData.getStartTime()));
		LocalDateTime endTime = MethodUtils.convertoToLocalDateTime(new Date(eventData.getEndTime()));
		Long totalDays = MethodUtils.distanceDaysBetweenTwoDate(startTime, endTime, 
				ZoneId.of(EnumZoneId.ASIA_HOCHIMINH.getNameZone())) + 1;
		if (eventData.getEndTime() == MethodUtils.setTimeOfDateToMidnight(eventData.getEndTime()).getTime()) {
			totalDays -= 1;
		}
		
		EventSchedule eventSchedule = eventScheduleOpt.get();
		eventSchedule.setEventName(eventData.getEventName());		
		eventSchedule.setStartTime(new Date(eventData.getStartTime()));
		eventSchedule.setEndTime(new Date(eventData.getEndTime()));
		eventSchedule.setIsAllDay(eventData.getIsAllDay());
		eventSchedule.setTotalDays(totalDays.intValue());
		eventSchedule.setPlace(eventData.getPlace());
		eventSchedule.setTypeRemindered(eventData.getTypeRemindered());
		eventSchedule.setDateRemindered(eventData.getDateRemindered() == null ? null : new Date(eventData.getDateRemindered()));
		eventSchedule.setColorCode(eventData.getColorCode());
		eventSchedule.setDescriptions(eventData.getDescriptions());
		eventSchedule.setIsPresent(eventData.getIsPresent());
		eventSchedule = eventScheduleDAO.save(eventSchedule);
		
		return new EventScheduleDTO(eventSchedule);
	}
	
	/**
	 * Delete Event Service
	 * 
	 * @param id
	 * @return
	 */
	public Boolean delete(Integer id) {
		Optional<EventSchedule> eventOptional = eventScheduleDAO.findById(id);
		if (eventOptional.isEmpty()) {
			return false;
		}
		eventScheduleDAO.delete(eventOptional.get());
		return true;
	}
	
	/**
	 * Get Time Line Event
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<TimeLineEventDTO> getTimeLineEvent(Long startDateMili, Long endDateMili, Integer userId) {
		User user = userService.findByIdAndStatus(userId, EnumStatus.ACTIVE.getValue());
		Date startDate = new Date(startDateMili);
		Date endDate = new Date(endDateMili);
		List<Work> works = workService.findByDueDateBetweenAndStatusNotAndUser(startDate, endDate, 
				EnumStatus.DELETED.getValue(), user);
		List<EventSchedule> eventSchedules = eventScheduleDAO.findByEndTimeGreaterThanOrStartTimeLessThanEqualAndUserId(
				startDate, endDate, userId);
		
		Map<Long, TimeLineEventDTO> mapTimeLine = new LinkedHashMap<>();
		Date tempStartDate = new Date(startDate.getTime());
		Date tempEndDate = new Date(endDate.getTime());
		while (tempStartDate.getTime() <= tempEndDate.getTime()) {
			mapTimeLine.put(tempStartDate.getTime(), new TimeLineEventDTO(tempStartDate.getTime()));
			tempStartDate = MethodUtils.addDaysForDate(tempStartDate, 1);
		}
		if (!CollectionUtils.isEmpty(works)) {
			works.stream().forEach(w -> {
				for (Map.Entry<Long, TimeLineEventDTO> entry : mapTimeLine.entrySet()) {
					Date dateStart = new Date(entry.getKey());
					Date dateEnd = MethodUtils.addDaysForDate(dateStart, 1);
					Long nowDateMili = new Date().getTime();
					if (w.getDueDate().getTime() <= nowDateMili 
							&& w.getStatus().equals(EnumStatus.ACTIVE.getValue())
							&& MethodUtils.distanceDaysBetweenTwoDateNotLocalDateTime(dateStart, new Date(nowDateMili), 
									ZoneId.of(EnumZoneId.ASIA_HOCHIMINH.getNameZone())) == 0) {
						System.out.println(MethodUtils.distanceDaysBetweenTwoDateNotLocalDateTime(dateStart, new Date(nowDateMili), 
								ZoneId.of(EnumZoneId.ASIA_HOCHIMINH.getNameZone())));
						entry.getValue().getWorksDueDate().add(new WorkDTO(w));
					} else if (w.getDueDate().getTime() >= dateStart.getTime() 
							&& w.getDueDate().getTime() < dateEnd.getTime()) {
						entry.getValue().getWorks().add(new WorkDTO(w));
					}
				}
			});
		}
		if (!CollectionUtils.isEmpty(eventSchedules)) {
			eventSchedules.stream().forEach(e -> {
				Date tempStartTimeEvent = MethodUtils.setTimeOfDateToMidnight(e.getStartTime().getTime());
				Date tempEndTimeEvent = new Date(e.getEndTime().getTime());
				for (Map.Entry<Long, TimeLineEventDTO> entry : mapTimeLine.entrySet()) {
					if (entry.getKey() >= tempStartTimeEvent.getTime() && entry.getKey() < tempEndTimeEvent.getTime()) {
						if (e.getIsAllDay() || e.getTotalDays() > 1) {
							EventScheduleDTO eventScheduleDTO = new EventScheduleDTO(e);
							Long dayOutOfTotalDays = MethodUtils.distanceDaysBetweenTwoDateNotLocalDateTime(
									tempStartTimeEvent, new Date(entry.getKey()), 
									ZoneId.of(EnumZoneId.ASIA_HOCHIMINH.getNameZone())) + 1;
							eventScheduleDTO.setNowDateOutOfTotalDays(dayOutOfTotalDays.intValue());
							entry.getValue().getEventsAllDay().add(eventScheduleDTO);
						} else {
							entry.getValue().getEvents().add(new EventScheduleDTO(e));
						}
						
					}
				}
			});
		}
		return mapTimeLine.values().stream()
				.map(t -> {
					t.setTotalWorksDueDate(t.getWorksDueDate().size());
					t.setTotalData(t.getEventsAllDay().size() + t.getTotalWorksDueDate() != 0 ? 1 : 0);
					return t;
				})
				.collect(Collectors.toList());
	}
	
}
