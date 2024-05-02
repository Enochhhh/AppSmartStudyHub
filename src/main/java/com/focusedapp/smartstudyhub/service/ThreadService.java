package com.focusedapp.smartstudyhub.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.focusedapp.smartstudyhub.exception.ISException;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.custom.UserDTO;

@Service
public class ThreadService {
	
	@Autowired UserService userService;
	@Autowired ThemeService themeService;
	@Autowired SoundConcentrationService soundConcentrationService;
	@Autowired SoundDoneService soundDoneService;
	@Autowired FilesService filesService;

	/**
	 * Thread send new account info to Email User
	 * 
	 * @param request
	 */
	public void sendNewAccountToEmailUser(UserDTO request) {
		Thread thread = new Thread() {
			public void run() {
				userService.sendNewAccountToEmailUser(request);
			}
		};
		thread.start();
	}
	
	/**
	 * Thread delete all themes of User
	 * 
	 * @param user
	 */
	public void deleteAllThemesOfUser(User user) {
		Thread thread = new Thread() {
			public void run() {
				try {
					themeService.deleteAllThemesOfUser(user);
				} catch (IOException e) {
					throw new ISException(e);
				}
			}
		};
		thread.start();
	}
	
	/**
	 * Thread delete all sound concentration of User
	 * 
	 * @param user
	 */
	public void deleteAllSoundsConcentrationOfUser(User user) {
		Thread thread = new Thread() {
			public void run() {
				try {
					soundConcentrationService.deleteAllSoundsConcentrationOfUser(user);
				} catch (IOException e) {
					throw new ISException(e);
				}
			}
		};
		thread.start();
	}
	
	/**
	 * Thread delete all sound done of User
	 * 
	 * @param user
	 */
	public void deleteAllSoundsDoneOfUser(User user) {
		Thread thread = new Thread() {
			public void run() {
				try {
					soundDoneService.deleteAllSoundsDoneOfUser(user);
				} catch (IOException e) {
					throw new ISException(e);
				}
			}
		};
		thread.start();
	}
	
	/**
	 * Thread delete all files of User
	 * 
	 * @param user
	 */
	public void deleteAllFilesUser(User user) {
		Thread thread = new Thread() {
			public void run() {
				try {
					filesService.deleteAllFilesUser(user);
				} catch (IOException e) {
					throw new ISException(e);
				}
			}
		};
		thread.start();
	}
	
	/**
	 * Thread delete all files of User
	 * 
	 * @param user
	 */
	public void clearInfo(User user) {
		Thread thread = new Thread() {
			public void run() {
				userService.clearInfo(user);
			}
		};
		thread.start();
	}
}
