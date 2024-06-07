package com.focusedapp.smartstudyhub.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import com.nimbusds.oauth2.sdk.util.CollectionUtils;

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
	
	public Files persistent(Files file) {
		return filesDAO.save(file);
	}
	
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
		if (type.equals(EnumTypeFile.USER.getValue())){
			user.setImageUrl(ConstantUrl.DEFAULT_IMAGE);
		} else if (type.equals(EnumTypeFile.COVERIMAGE.getValue())) {
			user.setCoverImage(null);
		}
		for (Files file : files) {
			deleteCompletelyFilesById(user, file.getId());
		}
		userService.persistent(user);
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
	
	/**
	 * Get files uploaded of User
	 * 
	 * @param userId
	 * @param startDate
	 * @param endDate
	 * @param type
	 * @param page
	 * @param size
	 * @return
	 */
	public List<FilesDTO> getFilesUploadedOfUser(Integer userId, Long startDate, Long endDate, String type, 
			String fieldSort, String sortType, Integer page, Integer size) {
		Sort sort = Sort.by("createdAt").descending();
		if (StringUtils.isNotBlank(fieldSort)) {
			sort = Sort.by(fieldSort);
			if (StringUtils.isNotBlank(sortType) && sortType.equals("DESC")) {
				sort = sort.descending();
			}
		}
		Pageable pageable = PageRequest.of(page, size, sort);
		List<Files> files = new ArrayList<>();
		
		if (userId == null || userId < 0) {
			if (StringUtils.isNotBlank(type)) {
				if (startDate == null && endDate == null) {
					files = filesDAO.findByUserIdNotNullAndType(type, pageable);
				} else {
					files = filesDAO.findByUserIdNotNullAndTypeAndCreatedAtBetween(type, new Date(startDate), new Date(endDate), 
							pageable);
				}
			} else {
				if (startDate == null && endDate == null) {			
					files = filesDAO.findByUserIdNotNull(pageable);
				} else {
					files = filesDAO.findByUserIdNotNullAndCreatedAtBetween(new Date(startDate), new Date(endDate), pageable);
				}
			}
		} else {
			if (StringUtils.isNotBlank(type)) {
				if (startDate == null && endDate == null) {
					files = filesDAO.findByUserIdAndType(userId, type, pageable);
				} else {
					files = filesDAO.findByUserIdAndTypeAndCreatedAtBetween(userId, type, new Date(startDate), 
							new Date(endDate), pageable);
				}
			} else {
				if (startDate == null && endDate == null) {			
					files = filesDAO.findByUserId(userId, pageable);
				} else {
					files = filesDAO.findByUserIdAndCreatedAtBetween(userId, new Date(startDate), new Date(endDate), pageable);
				}
			}
		}
		
		if (CollectionUtils.isEmpty(files)) {
			return new ArrayList<>();
		}
		return files.stream()
				.map(f -> new FilesDTO(f))
				.collect(Collectors.toList());
	}
	
	@Transactional(propagation=Propagation.REQUIRED, noRollbackFor=Exception.class)
	public void delete(Files file) {
		filesDAO.delete(file);
	}
	
	/**
	 * Delete file uploaded of User
	 * 
	 * @param id
	 * @return
	 */
	public Boolean deleteFileUploadedOfUser(Integer id) throws IOException {
		Optional<Files> file = filesDAO.findById(id);
		if (file.isEmpty()) {
			return false;
		}
		Files fileData = file.get();
		threadService.deleteThemeOfUserByAdminWithFileType(fileData, fileData.getType());
		return true;
	}
	
}
