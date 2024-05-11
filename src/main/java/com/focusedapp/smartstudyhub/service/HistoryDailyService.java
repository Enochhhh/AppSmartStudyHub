package com.focusedapp.smartstudyhub.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.focusedapp.smartstudyhub.dao.HistoryDailyDAO;
import com.focusedapp.smartstudyhub.model.HistoryDaily;
import com.focusedapp.smartstudyhub.model.Pomodoro;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.Work;
import com.focusedapp.smartstudyhub.model.custom.HistoryDailyDTO;
import com.focusedapp.smartstudyhub.model.custom.PomodoroDTO;
import com.focusedapp.smartstudyhub.model.custom.WorkDTO;
import com.focusedapp.smartstudyhub.util.DateUtils;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatus;
import com.nimbusds.oauth2.sdk.util.CollectionUtils;

@Service
public class HistoryDailyService {

	@Autowired HistoryDailyDAO historyDailyDAO;
	@Autowired UserService userService;
	@Autowired WorkService workService;
	@Autowired PomodoroService pomodoroService;
	
	public void createHistoryDaily() {
		Date nowDate = new Date();	
		Date endOfDate = DateUtils.setTimeOfDateToMidnight(nowDate.getTime());
		Date startDate = DateUtils.addDaysForDate(endOfDate, -1);
		List<User> users = userService.findUsersToResetDataDaily();
		List<HistoryDaily> historyList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(users)) {
			users.stream().forEach(u -> {
				HistoryDaily historyDaily = new HistoryDaily();
				historyDaily.setUser(u);
				historyDaily.setDates(startDate);
				historyDaily.setTotalPomodorosDone(u.getTotalPomodorosToday());
				historyDaily.setTotalWorksDone(u.getTotalWorksToday());
				historyDaily.setTotalTimeFocus(u.getTotalTimeFocus());
				
				List<Work> worksCompletedToday = workService
						.findByUserIdAndStatusAndDateMarkCompletedGreaterThanEqualAndDateMarkCompletedLessThan(u.getId(), 
						EnumStatus.COMPLETED.getValue(), startDate, endOfDate);
				if (!CollectionUtils.isEmpty(worksCompletedToday)) {
					String workIds = worksCompletedToday.stream()
							.map(w -> w.getId().toString()).collect(Collectors.joining(", "));
					historyDaily.setWorkIds(workIds);
				}
				
				List<Pomodoro> pomodorosCompletedToday = pomodoroService
						.findByUserIdAndCreatedDateGreaterThanEqualAndCreatedDateLessThan(u.getId(), startDate, endOfDate);
				if (!CollectionUtils.isEmpty(pomodorosCompletedToday)) {
					String pomodoroIds = pomodorosCompletedToday.stream()
							.map(w -> w.getId().toString()).collect(Collectors.joining(", "));
					historyDaily.setPomodoroIds(pomodoroIds);
				}
				historyList.add(historyDaily);
			});
			historyDailyDAO.saveAll(historyList);
		}
	}
	
	/**
	 * Get List history daily of User
	 * 
	 * @param userId
	 * @param page
	 * @param size
	 * @return
	 */
	public List<HistoryDailyDTO> getListHistoryDailyOfUser(Integer userId, Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
		User user = userService.findByIdAndStatus(userId, EnumStatus.ACTIVE.getValue());
		List<HistoryDaily> historyDailies = historyDailyDAO.findByUser(user, pageable);
		if (!CollectionUtils.isEmpty(historyDailies)) {
			return historyDailies.stream()
					.map(h -> {
						List<WorkDTO> works = new ArrayList<>();
						if (StringUtils.isNotBlank(h.getWorkIds())) {
							List<Integer> workIdsListInteger = Arrays.asList(h.getWorkIds().split(", ")).stream()
									.map(w -> Integer.valueOf(w)).collect(Collectors.toList());
							works = workService.getByIdInAndStatusForHistoryActivity(workIdsListInteger, EnumStatus.COMPLETED.getValue());
						}
						List<PomodoroDTO> pomodoros = new ArrayList<>();
						if (StringUtils.isNotBlank(h.getPomodoroIds())) {
							List<Integer> pomodoroIdsListInteger = Arrays.asList(h.getPomodoroIds().split(", ")).stream()
									.map(w -> Integer.valueOf(w)).collect(Collectors.toList());
							pomodoros = pomodoroService.getByIdInForHistoryActivity(pomodoroIdsListInteger);
						}
						return new HistoryDailyDTO(h, works, pomodoros);
					})
					.collect(Collectors.toList());
		}
		return new ArrayList<>();
	}
}
