package com.focusedapp.smartstudyhub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.focusedapp.smartstudyhub.model.custom.UserDTO;

@Service
public class ThreadService {
	
	@Autowired UserService userService;

	public void sendNewAccountToEmailUser(UserDTO request) {
		Thread thread = new Thread() {
			public void run() {
				userService.sendNewAccountToEmailUser(request);
			}
		};
		thread.start();
	}
}
