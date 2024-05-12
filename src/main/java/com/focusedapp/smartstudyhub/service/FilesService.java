package com.focusedapp.smartstudyhub.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.focusedapp.smartstudyhub.dao.FilesDAO;
import com.focusedapp.smartstudyhub.exception.NotFoundValueException;
import com.focusedapp.smartstudyhub.model.Files;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.custom.FilesDTO;
import com.focusedapp.smartstudyhub.util.constant.ConstantUrl;
import com.focusedapp.smartstudyhub.util.enumerate.EnumTypeFile;

@Service
public class FilesService {

	@Autowired
	FilesDAO filesDAO;
	@Autowired
	CloudinaryService cloudinaryService;
	@Autowired
	UserService userService;
	@Autowired 
	ThreadService threadService;
	
	/**
	 * Get Files Uploaded of User
	 * 
	 * @param user
	 * @param type
	 * @return
	 */
	public List<FilesDTO> getFilesUploadedOfUser(User user, String type) {
		
		List<Files> files = filesDAO.findByUserIdAndType(user.getId(), type);
		
		return files.stream()
				.map(f -> new FilesDTO(f))
				.collect(Collectors.toList());
	}
	
	/**
	 * Find By User Id And Type
	 * 
	 * @param userId
	 * @param type
	 * @return
	 */
	public List<Files> findByUserIdAndType(Integer userId, String type) {
		return filesDAO.findByUserIdAndType(userId, type);
	}
	
	/**
	 * Delete Completely Files by Id
	 * 
	 * @param user
	 * @param filesId
	 * @return
	 */
	public FilesDTO deleteCompletelyFilesById(User user, Integer filesId) throws IOException {
		
		Files files = filesDAO.findById(filesId)
				.orElseThrow(() -> new NotFoundValueException("FilesService", "deleteCompletelyFilesById"));
		
		if (user.getImageUrl().equals(files.getSecureUrl())) {
			user.setImageUrl(ConstantUrl.DEFAULT_IMAGE);
			userService.persistent(user);
		}
		if (user.getCoverImage() != null && user.getCoverImage().equals(files.getSecureUrl())) {
			user.setCoverImage(null);
			userService.persistent(user);
		}
		
		cloudinaryService.deleteFileInCloudinary(files.getPublicId());
		return new FilesDTO(files);
	}
	
	/**
	 * Delete All Files User
	 * 
	 * @param user
	 * @throws IOException
	 */
	public void deleteAllFilesUser(User user) throws IOException {
		
		List<Files> files = filesDAO.findByUserId(user.getId());
		
		for (Files file : files) {
			deleteCompletelyFilesById(user, file.getId());
		}
	}
	
	/**
	 * Delete All Files User by Type
	 * 
	 * @param user
	 * @param type
	 * @throws IOException
	 */
	@Transactional(propagation=Propagation.REQUIRED, noRollbackFor=Exception.class)
	public void deleteAllFilesByTypeOfUser(User user, String type) throws IOException {
		List<Files> files = filesDAO.findByUserIdAndType(user.getId(), type);
		
		for (Files file : files) {
			deleteCompletelyFilesById(user, file.getId());
		}
	}
	
	/**
	 * Delete All Files of User by type using Thread
	 * 
	 * @param userId
	 */
	public Boolean deleteAllFilesOfUserByTypeUsingThread(User user, String type) {
		threadService.deleteAllFilesOfUserByType(user, type);
		return true;
	}
	
	/**
	 * Delete All Themes or Sounds of User by type using Thread
	 * 
	 * @param userId
	 */
	public Boolean deleteAllThemesOrSoundsOfUserByUsingThread(User user, String type) {
		if (type.equals(EnumTypeFile.THEME.getValue())) {
			threadService.deleteAllThemesOfUser(user);
		} else if (type.equals(EnumTypeFile.SOUNDCONCENTRATION.getValue())) {
			threadService.deleteAllSoundsConcentrationOfUser(user);
		} else {
			threadService.deleteAllSoundsDoneOfUser(user);
		}
		return true;
	}
	
}
