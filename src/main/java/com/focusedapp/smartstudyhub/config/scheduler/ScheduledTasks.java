package com.focusedapp.smartstudyhub.config.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.focusedapp.smartstudyhub.service.UserService;

@Component
public class ScheduledTasks {
	
	@Autowired UserService userService;
	
	@Scheduled(cron = "0 0 0 * * ?", zone = "Asia/Ho_Chi_Minh")
	public void taskRunOnMidnightEveryday() {
		userService.resetDataOfUserDaily();
		userService.resetDueDatePremium();
	}
	
	@Scheduled(cron = "0 0 0 ? * Mon", zone = "Asia/Ho_Chi_Minh")
	public void taskRunOnWeekly() {
		userService.resetDataOfUserDaily();
		userService.resetDataOfUserWeekly();
	}
}
