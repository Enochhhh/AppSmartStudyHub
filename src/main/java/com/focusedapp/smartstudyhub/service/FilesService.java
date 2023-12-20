package com.focusedapp.smartstudyhub.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.focusedapp.smartstudyhub.dao.FilesDAO;
import com.focusedapp.smartstudyhub.exception.NotFoundValueException;
import com.focusedapp.smartstudyhub.model.Files;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.custom.FilesDTO;
import com.focusedapp.smartstudyhub.util.constant.ConstantUrl;

@Service
public class FilesService {

	@Autowired
	FilesDAO filesDAO;
	@Autowired
	CloudinaryService cloudinaryService;
	@Autowired
	UserService userService;
	
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
		
		cloudinaryService.deleteFileInCloudinary(files.getPublicId());
		return new FilesDTO(files);
	}
	
}
