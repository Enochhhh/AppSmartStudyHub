package com.focusedapp.smartstudyhub.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.focusedapp.smartstudyhub.exception.ISException;
import com.focusedapp.smartstudyhub.model.Files;
import com.focusedapp.smartstudyhub.model.SoundConcentration;
import com.focusedapp.smartstudyhub.model.SoundDone;
import com.focusedapp.smartstudyhub.model.Theme;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.custom.UserDTO;
import com.focusedapp.smartstudyhub.util.constant.ConstantUrl;
import com.focusedapp.smartstudyhub.util.enumerate.EnumTypeFile;

@Service
public class ThreadService {
	
	@Autowired UserService userService;
	@Autowired ThemeService themeService;
	@Autowired SoundConcentrationService soundConcentrationService;
	@Autowired SoundDoneService soundDoneService;
	@Autowired FilesService filesService;
	@Autowired HistoryDailyService historyDailyService;
	@Autowired FolderService folderService;
	@Autowired EventScheduleService eventScheduleService;
	@Autowired ProjectService projectService;
	@Autowired WorkService workService;
	@Autowired PomodoroService pomodoroService;
	@Autowired CloudinaryService cloudinaryService;

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
	public void deleteAllFilesOfUserByType(User user, String type) {
		Thread thread = new Thread() {
			public void run() {
				try {
					filesService.deleteAllFilesByTypeOfUser(user, type);
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
	public void deleteAllHistoryDailiesOfUser(User user) {
		Thread thread = new Thread() {
			public void run() {
				historyDailyService.deleteByUser(user);
			}
		};
		thread.start();
	}
	
	/**
	 * Thread delete all history folders of User
	 * 
	 * @param user
	 */
	public void deleteAllFoldersOfUser(Integer userId) {
		Thread thread = new Thread() {
			public void run() {
				User user = userService.findById(userId);
				folderService.deleteAllFolderOfUser(user);
			}
		};
		thread.start();
	}
	
	/**
	 * Thread delete all events of User
	 * 
	 * @param user
	 */
	public void deleteAllEventsOfUser(User user) {
		Thread thread = new Thread() {
			public void run() {
				eventScheduleService.deleteAllEventsOfUser(user);
			}
		};
		thread.start();
	}
	
	/**
	 * Thread delete all projects of User
	 * 
	 * @param user
	 */
	public void deleteAllProjectsOfUser(Integer userId) {
		Thread thread = new Thread() {
			public void run() {
				User user = userService.findById(userId);
				projectService.deleteAllProjectOfUser(user);
			}
		};
		thread.start();
	}
	
	/**
	 * Thread delete all works of User
	 * 
	 * @param user
	 */
	public void deleteAllWorksOfUser(Integer userId) {
		Thread thread = new Thread() {
			public void run() {
				User user = userService.findById(userId);
				workService.deleteAllWorksOfUser(user);
			}
		};
		thread.start();
	}
	
	/**
	 * Thread delete all pomodoros of User
	 * 
	 * @param user
	 */
	public void deleteAllPomodorosOfUser(Integer userId) {
		Thread thread = new Thread() {
			public void run() {
				User user = userService.findById(userId);
				pomodoroService.deleteAllPomodorosOfUser(user);
			}
		};
		thread.start();
	}
	
	/**
	 * Thread delete file of User by Admin with type
	 * 
	 * @param file
	 */
	public void deleteThemeOfUserByAdminWithFileType(Files file, String type) throws IOException {
		if (type.equals(EnumTypeFile.THEME.getValue())) {
			Theme theme = themeService.findByUrl(file.getSecureUrl());
			themeService.delete(theme);
		} else if (type.equals(EnumTypeFile.SOUNDDONE.getValue())) {
			SoundDone soundDone = soundDoneService.findByUrl(file.getSecureUrl());
			soundDoneService.delete(soundDone);
		} else if (type.equals(EnumTypeFile.SOUNDCONCENTRATION.getValue())) {
			SoundConcentration soundConcentration = soundConcentrationService.findByUrl(file.getSecureUrl());
			soundConcentrationService.delete(soundConcentration);
		} else if (type.equals(EnumTypeFile.USER.getValue())) {
			User user = file.getUser();
			if (user != null && file.getSecureUrl().equals(user.getImageUrl())) {
				user.setImageUrl(ConstantUrl.DEFAULT_IMAGE);
				file.setUser(user);
				file = filesService.persistent(file);
			}
		} else if (type.equals(EnumTypeFile.REPORT.getValue())) {
			
		} else if (type.equals(EnumTypeFile.COVERIMAGE.getValue())) {
			User user = file.getUser();
			if (user != null && user.getCoverImage() != null && file.getSecureUrl().equals(user.getCoverImage())) {
				user.setCoverImage(null);
				file.setUser(user);
				file = filesService.persistent(file);
			}
		}
		cloudinaryService.deleteFileInCloudinary(file.getPublicId());
		filesService.delete(file);
	}
	
}
