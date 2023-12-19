package com.focusedapp.smartstudyhub.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.focusedapp.smartstudyhub.dao.FilesDAO;
import com.focusedapp.smartstudyhub.model.Files;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.custom.FilesDTO;

@Service
public class FilesService {

	@Autowired
	FilesDAO filesDAO;
	
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
}
