package com.focusedapp.smartstudyhub.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.focusedapp.smartstudyhub.exception.ISException;
import com.focusedapp.smartstudyhub.model.HistoryDaily;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.custom.UserDTO;

@Service
public class ThreadService {
	
	@Autowired UserService userService;
	@Autowired ThemeService themeService;
	@Autowired SoundConcentrationService soundConcentrationService;
	@Autowired SoundDoneService soundDoneService;
	@Autowired FilesService filesService;
	@Autowired HistoryDailyService historyDailyService;

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
	
	/**
	 * Thread delete all history dailies of User
	 * 
	 * @param user
	 */
	public void deleteAllHistoryDailiesOfUser(List<HistoryDaily> historyDailies) {
		Thread thread = new Thread() {
			public void run() {
				historyDailyService.deleteAll(historyDailies);
			}
		};
		thread.start();
	}
}
