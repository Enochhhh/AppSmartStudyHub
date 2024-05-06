package com.focusedapp.smartstudyhub.config.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.focusedapp.smartstudyhub.service.UserService;

@Component
public class ScheduledTasks {
	
	@Autowired UserService userService;
	
	/**
	 * It will auto run at midnight every day
	 * 
	 */
	// @Scheduled(cron = "@daily", zone = "Asia/Ho_Chi_Minh")
	//@Scheduled(cron = "0 0 0 * * ?", zone = "Asia/Ho_Chi_Minh")
	@Scheduled(cron = "0 52 14 * * ?", zone = "Asia/Ho_Chi_Minh")
	public void resetDueDatePremium() {
		userService.resetDueDatePremium();
	}
}
