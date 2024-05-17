package com.focusedapp.smartstudyhub.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.focusedapp.smartstudyhub.model.Pomodoro;
import com.focusedapp.smartstudyhub.model.Tag;
import com.focusedapp.smartstudyhub.model.custom.StatisticalByUnitDTO;
import com.focusedapp.smartstudyhub.model.custom.StatisticalDTO;
import com.focusedapp.smartstudyhub.model.custom.TimeLineStatisticalDTO;
import com.focusedapp.smartstudyhub.util.DateUtils;
import com.nimbusds.oauth2.sdk.util.CollectionUtils;

@Service
public class StatisticalService {

	@Autowired PomodoroService pomodoroService;
	
	/**
	 * Statistical Time Focus
	 * 
	 * @param startDateMili
	 * @param endDateMili
	 * @param userId
	 * @param type
	 * @return
	 */
	public TimeLineStatisticalDTO statisticalTimeFocus(Long startDateMili, Long endDateMili, Integer userId, String type) {
		Date startDate = new Date(startDateMili);
		Date endDate = new Date(endDateMili + 60 * 1000);
		
		List<Pomodoro> pomodoros = pomodoroService
				.findByUserIdAndEndTimeGreaterThanEqualAndEndTimeLessThanAndIsEndPomoFalse(userId, startDate, endDate);
		if (CollectionUtils.isEmpty(pomodoros)) {
			return null;
		}
		
		List<StatisticalDTO> listDataStatistical = new ArrayList<>();
		if (type.equals("WEEK")) {
			do {
				Date tempStartDate = startDate;
				Date tempEndDate = DateUtils.addWeeksForDate(tempStartDate, 1);
				Integer totalTimeFocus = pomodoros.stream()
						.filter(p -> p.getEndTime().getTime() >= tempStartDate.getTime() 
						&& p.getEndTime().getTime() < tempEndDate.getTime())
						.collect(Collectors.summingInt(Pomodoro::getTimeOfPomodoro));
				if (totalTimeFocus != 0) {
					listDataStatistical.add(new StatisticalDTO(tempStartDate.getTime(), tempEndDate.getTime() - 60 * 1000, totalTimeFocus));
				}	
				startDate = tempEndDate;
			} while (startDate.getTime() < endDate.getTime());
		} else if (type.equals("DAY")) {
			Map<Long, List<Pomodoro>> mapPomodoro = pomodoros.stream()
					.collect(Collectors.groupingBy(p -> {
						return DateUtils.setTimeOfDateToMidnight(p.getEndTime().getTime()).getTime();
					}, Collectors.toList()));
			mapPomodoro = mapPomodoro.entrySet().stream()
					.sorted(Map.Entry.<Long, List<Pomodoro>>comparingByKey())
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
			for (Map.Entry<Long, List<Pomodoro>> entry : mapPomodoro.entrySet()) {
				Integer totalTimeFocus = entry.getValue().stream()
						.collect(Collectors.summingInt(Pomodoro::getTimeOfPomodoro));
				listDataStatistical.add(new StatisticalDTO(entry.getKey(), totalTimeFocus));
			}
		} else if (type.equals("MONTH")) {
			do {
				Date tempStartDate = startDate;
				Date tempEndDate = DateUtils.addMonthsForDate(tempStartDate, 1);
				Integer totalTimeFocus = pomodoros.stream()
						.filter(p -> p.getEndTime().getTime() >= tempStartDate.getTime() 
						&& p.getEndTime().getTime() < tempEndDate.getTime())
						.collect(Collectors.summingInt(Pomodoro::getTimeOfPomodoro));
				if (totalTimeFocus != 0) {
					listDataStatistical.add(new StatisticalDTO(tempStartDate.getTime(), tempEndDate.getTime() - 60 * 1000, totalTimeFocus));
				}	
				startDate = tempEndDate;
			} while (startDate.getTime() < endDate.getTime());
		}
		

		Integer sumTimeFocus = 0;
		Integer maxTimeFocus = 0;
		for (StatisticalDTO statisticalDTO : listDataStatistical) {
			sumTimeFocus += statisticalDTO.getTotalTimeFocus();
			if (statisticalDTO.getTotalTimeFocus() > maxTimeFocus) {
				maxTimeFocus = statisticalDTO.getTotalTimeFocus();
			}
		}
		Double averageTimeFocus = sumTimeFocus.doubleValue() / listDataStatistical.size();
		return new TimeLineStatisticalDTO(listDataStatistical, listDataStatistical.size(), 
				maxTimeFocus, averageTimeFocus, sumTimeFocus);
	}
	
	/**
	 * Statistical Time Focus by work
	 * 
	 * @param startDateMili
	 * @param endDateMili
	 * @param userId
	 * @param type
	 * @param unit
	 * @return
	 */
	public StatisticalDTO statisticalTimeFocusByWork(Long startDateMili, Long endDateMili, Integer userId, String unit) {
		Date startDate = new Date(startDateMili);
		Date endDate = new Date(endDateMili + 60 * 1000);
		
		List<Pomodoro> pomodoros = pomodoroService
				.findByUserIdAndEndTimeGreaterThanEqualAndEndTimeLessThanAndIsEndPomoFalse(userId, startDate, endDate);
		if (CollectionUtils.isEmpty(pomodoros)) {
			return null;
		}
		
		Map<String, Integer> mapTotalTimeFocusByUnit = new HashMap<>();
		Integer totalTimeFocus = 0;
		List<StatisticalByUnitDTO> listDataResponse = new ArrayList<>();
		if (unit.equals("WORK")) {
			pomodoros.stream().forEach(p -> {
				String key = "";
				if (p.getWork() == null && p.getExtraWork() == null) {
					key = "-1, No Assigned Job Yet";
				} else if (p.getWork() == null) {
					key = p.getExtraWork().getWork().getId().toString().concat(", ")
							.concat(p.getExtraWork().getWork().getWorkName());
				} else {
					key = p.getWork().getId().toString().concat(", ")
							.concat(p.getWork().getWorkName());
				}
				Integer totalTimeFocusInUnit = mapTotalTimeFocusByUnit.get(key) == null ? 0 : mapTotalTimeFocusByUnit.get(key);
				mapTotalTimeFocusByUnit.put(key, totalTimeFocusInUnit + p.getTimeOfPomodoro());
			});
						
			for (Map.Entry<String, Integer> entry : mapTotalTimeFocusByUnit.entrySet()) {
				String[] keyArr = entry.getKey().split(", ");
				listDataResponse.add(new StatisticalByUnitDTO(Integer.valueOf(keyArr[0]), keyArr[1], entry.getValue()));
				totalTimeFocus += entry.getValue();
			} 
		} else if (unit.equals("PROJECT")) {
			pomodoros.stream().forEach(p -> {
				String key = "";
				if (p.getWork() == null && p.getExtraWork() == null) {
					key = "-1, No Projects, None";
				} else if (p.getWork() == null) {
					if (p.getExtraWork().getWork().getProject() == null) {
						key = "0, Tasks, None";
					} else {
						key = p.getExtraWork().getWork().getProject().getId().toString().concat(", ")
								.concat(p.getExtraWork().getWork().getProject().getProjectName()).concat(", ")
								.concat(p.getExtraWork().getWork().getProject().getColorCode());
					}					
				} else {
					if (p.getWork().getProject() == null) {
						key = "0, Tasks, None";
					} else {
						key = p.getWork().getProject().getId().toString().concat(", ")
								.concat(p.getWork().getProject().getProjectName()).concat(", ")
								.concat(p.getWork().getProject().getColorCode());
					}
				}
				Integer totalTimeFocusInUnit = mapTotalTimeFocusByUnit.get(key) == null ? 0 : mapTotalTimeFocusByUnit.get(key);
				mapTotalTimeFocusByUnit.put(key, totalTimeFocusInUnit + p.getTimeOfPomodoro());
			});
						
			for (Map.Entry<String, Integer> entry : mapTotalTimeFocusByUnit.entrySet()) {
				String[] keyArr = entry.getKey().split(", ");
				listDataResponse.add(new StatisticalByUnitDTO(Integer.valueOf(keyArr[0]), keyArr[1], keyArr[2], entry.getValue()));
				totalTimeFocus += entry.getValue();
			} 
		} else if (unit.equals("TAG")) {
			pomodoros.stream().forEach(p -> {
				if (p.getExtraWork() != null) {
					List<Tag> tags = p.getExtraWork().getWork().getTags();
					if (!CollectionUtils.isEmpty(tags)) {
						tags.stream().forEach(t -> {
							String key = t.getId().toString().concat(", ")
									.concat(t.getTagName()).concat(", ")
									.concat(t.getColorCode());
							Integer totalTimeFocusInUnit = mapTotalTimeFocusByUnit.get(key) == null ? 0 
									: mapTotalTimeFocusByUnit.get(key);
							mapTotalTimeFocusByUnit.put(key, totalTimeFocusInUnit + p.getTimeOfPomodoro());
						});
					} 					
				} else if (p.getWork() != null) {
					List<Tag> tags = p.getWork().getTags();
					if (!CollectionUtils.isEmpty(tags)) {
						tags.stream().forEach(t -> {
							String key = t.getId().toString().concat(", ")
									.concat(t.getTagName()).concat(", ")
									.concat(t.getColorCode());
							Integer totalTimeFocusInUnit = mapTotalTimeFocusByUnit.get(key) == null ? 0 
									: mapTotalTimeFocusByUnit.get(key);
							mapTotalTimeFocusByUnit.put(key, totalTimeFocusInUnit + p.getTimeOfPomodoro());
						});
					} 			
				}
			});
						
			for (Map.Entry<String, Integer> entry : mapTotalTimeFocusByUnit.entrySet()) {
				String[] keyArr = entry.getKey().split(", ");
				listDataResponse.add(new StatisticalByUnitDTO(Integer.valueOf(keyArr[0]), keyArr[1], keyArr[2], entry.getValue()));
				totalTimeFocus += entry.getValue();
			} 
		}
		
		listDataResponse = listDataResponse.stream()
			.sorted(Comparator.comparing(StatisticalByUnitDTO::getTotalTimeFocusInUnit).reversed())
			.collect(Collectors.toList());
		
		return new StatisticalDTO(listDataResponse, totalTimeFocus);
	}
}
