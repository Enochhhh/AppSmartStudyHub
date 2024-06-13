package com.focusedapp.smartstudyhub.service;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.focusedapp.smartstudyhub.dao.FilesDAO;
import com.focusedapp.smartstudyhub.exception.NotFoundValueException;
import com.focusedapp.smartstudyhub.model.Files;
import com.focusedapp.smartstudyhub.model.SoundConcentration;
import com.focusedapp.smartstudyhub.model.SoundDone;
import com.focusedapp.smartstudyhub.model.Theme;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.custom.AllResponseTypeDTO;
import com.focusedapp.smartstudyhub.model.custom.FilesAdminDTO;
import com.focusedapp.smartstudyhub.model.custom.FilesDTO;
import com.focusedapp.smartstudyhub.model.custom.SoundConcentrationDTO;
import com.focusedapp.smartstudyhub.model.custom.SoundDoneDTO;
import com.focusedapp.smartstudyhub.model.custom.StatisticalFileDTO;
import com.focusedapp.smartstudyhub.model.custom.ThemeDTO;
import com.focusedapp.smartstudyhub.util.constant.ConstantUrl;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatus;
import com.focusedapp.smartstudyhub.util.enumerate.EnumTypeFile;
import com.focusedapp.smartstudyhub.util.enumerate.EnumZoneId;
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
	@Autowired 
	ThemeService themeService;
	@Autowired 
	SoundConcentrationService soundConcentrationService;
	@Autowired
	SoundDoneService soundDoneService;
	
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
	public AllResponseTypeDTO getFilesUploadedOfUser(Integer userId, Long startDate, Long endDate, String type, 
			String fieldSort, String sortType, Integer page, Integer size) {
		AllResponseTypeDTO data = new AllResponseTypeDTO();
		Sort sort = Sort.by("createdAt").descending();
		if (StringUtils.isNotBlank(fieldSort)) {
			sort = Sort.by(fieldSort);
			if (StringUtils.isNotBlank(sortType) && sortType.equals("DESC")) {
				sort = sort.descending();
			}
		}
		Pageable pageable = PageRequest.of(page, size, sort);
		Page<Files> files = null;
		
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
		
		if (CollectionUtils.isEmpty(files.getContent())) {
			data.setFiles( new ArrayList<>());			
			return data;
		}
		
		List<FilesDTO> filesRet = files.stream()
				.map(f -> new FilesDTO(f))
				.collect(Collectors.toList());
		data.setFiles(filesRet);
		data.setIntegerType(files.getTotalPages());
		data.setLongType(files.getTotalElements());
		return data;
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
	
	/**
	 * Get files in Sysmtem
	 * 
	 * @param startDate
	 * @param endDate
	 * @param type
	 * @param page
	 * @param size
	 * @return
	 */
	public AllResponseTypeDTO getFilesInSystem(Long startDate, Long endDate, String type, 
			String fieldSort, String sortType, Integer page, Integer size) {
		AllResponseTypeDTO data = new AllResponseTypeDTO();
		Sort sort = Sort.by("createdAt").descending();
		if (StringUtils.isNotBlank(fieldSort)) {
			sort = Sort.by(fieldSort);
			if (StringUtils.isNotBlank(sortType) && sortType.equals("DESC")) {
				sort = sort.descending();
			}
		}
		Pageable pageable = PageRequest.of(page, size, sort);
		Page<Files> pageFiles = null;
		if (StringUtils.isNotBlank(type)) {
			if (startDate == null && endDate == null) {
				pageFiles = filesDAO.findByUserNullAndType(type, pageable);			
			} else {
				pageFiles = filesDAO.findByUserNullAndTypeAndCreatedAtBetween(type, new Date(startDate), new Date(endDate), 
						pageable);
			}
		} else {
			if (startDate == null && endDate == null) {			
				pageFiles = filesDAO.findByUserNull(pageable);
			} else {
				pageFiles = filesDAO.findByUserNullAndCreatedAtBetween(new Date(startDate), new Date(endDate), pageable);
			}
		}
		
		List<Files> files = pageFiles.getContent();
		if (CollectionUtils.isEmpty(files)) {
			data.setFiles(new ArrayList<>());;
			return data;
		}
		List<FilesDTO> filesRet = files.stream()
				.map(f -> new FilesDTO(f))
				.collect(Collectors.toList());
		
		data.setFiles(filesRet);
		data.setIntegerType(pageFiles.getTotalPages());
		data.setLongType(pageFiles.getTotalElements());
		return data;
	}
	
	public void moveFileToAnotherFolder(FilesAdminDTO filesAdminDTO) {
		String[] arrayUrlSplited = filesAdminDTO.getUrl().split("/");
		if (!filesAdminDTO.getStatus().equals(arrayUrlSplited[9])) {
			String publicId = ConstantUrl.URL_FOLDER.substring(1)
					.concat(filesAdminDTO.getUrl().substring(
							filesAdminDTO.getUrl().indexOf(ConstantUrl.URL_FOLDER) + ConstantUrl.URL_FOLDER.length())
					) 
					.split("\\.")[0];
			String newPublicId = publicId.replaceFirst(arrayUrlSplited[9], filesAdminDTO.getStatus());
			String url = cloudinaryService.moveFileToAnotherFolder(publicId, newPublicId);
			filesAdminDTO.setUrl(url);
		}
	}
	
	/**
	 * Upload file Admin
	 * 
	 * @param file
	 * @param filesAdminDTO
	 * @return
	 * @throws IOException
	 */
	public Object uploadFileAdmin(FilesAdminDTO filesAdminDTO) throws IOException {
		Object object = null;
		if (filesAdminDTO.getType().equals(EnumTypeFile.THEME.getValue())) {
			Theme theme = null;
			if (filesAdminDTO.getId() != null) {
				theme = themeService.findById(filesAdminDTO.getId());
				
				if (StringUtils.isNotBlank(theme.getUrl()) && !theme.getUrl().equals(filesAdminDTO.getUrl())) {
					String publicId = null;
					Integer startingIndex = theme.getUrl().indexOf(ConstantUrl.URL_FOLDER);
					publicId = theme.getUrl().substring(startingIndex + 1).split("\\.")[0];
					
					if (publicId != null) {
						cloudinaryService.deleteFileInCloudinary(publicId);
					}
				}
				moveFileToAnotherFolder(filesAdminDTO);
				theme.setUrl(filesAdminDTO.getUrl());
				theme.setNameTheme(filesAdminDTO.getName());
				theme.setStatusTheme(filesAdminDTO.getStatus());
			} else {
				moveFileToAnotherFolder(filesAdminDTO);
				theme = Theme.builder()
						.nameTheme(filesAdminDTO.getName())
						.url(filesAdminDTO.getUrl())
						.statusTheme(filesAdminDTO.getStatus())
						.createdDate(new Date())
						.status(EnumStatus.ACTIVE.getValue())
						.build();
			}
			object = themeService.persistent(theme);
		} else if (filesAdminDTO.getType().equals(EnumTypeFile.SOUNDCONCENTRATION.getValue())) {
			SoundConcentration soundConcentration = null;
			if (filesAdminDTO.getId() != null) {
				soundConcentration = soundConcentrationService.findById(filesAdminDTO.getId());
				
				if (StringUtils.isNotBlank(soundConcentration.getUrl()) 
						&& !soundConcentration.getUrl().equals(filesAdminDTO.getUrl())) {
					String publicId = null;
					Integer startingIndex = soundConcentration.getUrl().indexOf(ConstantUrl.URL_FOLDER);
					publicId = soundConcentration.getUrl().substring(startingIndex + 1).split("\\.")[0];
					
					if (publicId != null) {
						cloudinaryService.deleteFileInCloudinary(publicId);
					}
				}
				moveFileToAnotherFolder(filesAdminDTO);
				soundConcentration.setUrl(filesAdminDTO.getUrl());
				soundConcentration.setNameSound(filesAdminDTO.getName());
				soundConcentration.setStatusSound(filesAdminDTO.getStatus());
			} else {
				moveFileToAnotherFolder(filesAdminDTO);
				soundConcentration = SoundConcentration.builder()
						.nameSound(filesAdminDTO.getName())
						.url(filesAdminDTO.getUrl())
						.statusSound(filesAdminDTO.getStatus())
						.createdDate(new Date())
						.status(EnumStatus.ACTIVE.getValue())
						.build();
			}
			object = soundConcentrationService.persistent(soundConcentration);
		} else {
			SoundDone soundDone = null;
			if (filesAdminDTO.getId() != null) {
				soundDone = soundDoneService.findById(filesAdminDTO.getId());
				
				if (StringUtils.isNotBlank(soundDone.getUrl()) 
						&& !soundDone.getUrl().equals(filesAdminDTO.getUrl())) {
					String publicId = null;
					Integer startingIndex = soundDone.getUrl().indexOf(ConstantUrl.URL_FOLDER);
					publicId = soundDone.getUrl().substring(startingIndex + 1).split("\\.")[0];
					
					if (publicId != null) {
						cloudinaryService.deleteFileInCloudinary(publicId);
					}
				}
				moveFileToAnotherFolder(filesAdminDTO);
				soundDone.setUrl(filesAdminDTO.getUrl());
				soundDone.setNameSound(filesAdminDTO.getName());
				soundDone.setStatusSound(filesAdminDTO.getStatus());
			} else {
				moveFileToAnotherFolder(filesAdminDTO);
				soundDone = SoundDone.builder()
						.nameSound(filesAdminDTO.getName())
						.url(filesAdminDTO.getUrl())
						.statusSound(filesAdminDTO.getStatus())
						.createdDate(new Date())
						.status(EnumStatus.ACTIVE.getValue())
						.build();
			}
			object = soundDoneService.persistent(soundDone);
		}
		
		return object;
	}
	
	/**
	 * Get File by id
	 * 
	 * @param id
	 * @return
	 */
	public FilesDTO getById(Integer id) {
		Optional<Files> file = filesDAO.findById(id);
		if (file.isEmpty()) {
			return null;
		}
		return new FilesDTO(file.get());
	}
	
	/**
	 * Get themes and sounds in Sysmtem
	 * 
	 * @param startDate
	 * @param endDate
	 * @param type
	 * @param page
	 * @param size
	 * @return
	 */
	public AllResponseTypeDTO getThemesAndSoundsInSystem(String type, String statusFile, Long startDate, Long endDate, 
			String fieldSort, String sortType, Integer page, Integer size) {
		AllResponseTypeDTO data = new AllResponseTypeDTO();
		Sort sort = Sort.by("createdAt").descending();
		if (StringUtils.isNotBlank(fieldSort)) {
			sort = Sort.by(fieldSort);
			if (StringUtils.isNotBlank(sortType) && sortType.equals("DESC")) {
				sort = sort.descending();
			}
		}
		Pageable pageable = PageRequest.of(page, size, sort);
		List<Object> files = new ArrayList<>();
		

		if (type.equals(EnumTypeFile.THEME.getValue())) {
			Page<Theme> themes = null;
			if (StringUtils.isNotBlank(statusFile)) {
				if (startDate == null && endDate == null) {			
					themes = themeService.findByUserNullAndStatusTheme(statusFile, pageable);
				} else {
					themes = themeService.findByUserNullAndStatusThemeAndCreatedDateBetween(statusFile, new Date(startDate), 
							new Date(endDate), pageable);
				}
			} else {
				if (startDate == null && endDate == null) {			
					themes = themeService.findByUserNull(pageable);
				} else {
					themes = themeService.findByUserNullAndCreatedDateBetween(new Date(startDate), new Date(endDate), pageable);
				}
			}
			
			if (CollectionUtils.isNotEmpty(themes.getContent())) {
				files = themes.stream()
						.map(t -> new ThemeDTO(t))
						.collect(Collectors.toList());
				data.setObjects(files);
				data.setIntegerType(themes.getTotalPages());
				data.setLongType(themes.getTotalElements());
			}
		} else if (type.equals(EnumTypeFile.SOUNDCONCENTRATION.getValue())) {
			Page<SoundConcentration> soundConcentrations = null;
			if (StringUtils.isNotBlank(statusFile)) {
				if (startDate == null && endDate == null) {			
					soundConcentrations = soundConcentrationService.findByUserNullAndStatusSound(statusFile, pageable);
				} else {
					soundConcentrations = soundConcentrationService
							.findByUserNullAndStatusSoundAndCreatedDateBetween(statusFile, new Date(startDate), 
									new Date(endDate), pageable);
				}
			} else {
				if (startDate == null && endDate == null) {			
					soundConcentrations = soundConcentrationService.findByUserNull(pageable);
				} else {
					soundConcentrations = soundConcentrationService
							.findByUserNullAndCreatedDateBetween(new Date(startDate), new Date(endDate), pageable);
				}
			}
			
			if (CollectionUtils.isNotEmpty(soundConcentrations.getContent())) {
				files = soundConcentrations.stream()
						.map(s -> new SoundConcentrationDTO(s))
						.collect(Collectors.toList());
				data.setObjects(files);
				data.setIntegerType(soundConcentrations.getTotalPages());
				data.setLongType(soundConcentrations.getTotalElements());
			}
		} else {
			Page<SoundDone> soundDones = null;
			if (StringUtils.isNotBlank(statusFile)) {
				if (startDate == null && endDate == null) {			
					soundDones = soundDoneService.findByUserNullAndStatusSound(statusFile, pageable);
				} else {
					soundDones = soundDoneService
							.findByUserNullAndStatusSoundAndCreatedDateBetween(statusFile, new Date(startDate), 
									new Date(endDate), pageable);
				}
			} else {
				if (startDate == null && endDate == null) {			
					soundDones = soundDoneService.findByUserNull(pageable);
				} else {
					soundDones = soundDoneService.findByUserNullAndCreatedDateBetween(new Date(startDate), new Date(endDate), 
							pageable);
				}
			}
			
			if (CollectionUtils.isNotEmpty(soundDones.getContent())) {
				files = soundDones.stream()
						.map(s -> {
							SoundDoneDTO soundDoneDTO = new SoundDoneDTO(s);
							Object object = soundDoneDTO;
							return object;
						})
						.collect(Collectors.toList());
				data.setObjects(files);
				data.setIntegerType(soundDones.getTotalPages());
				data.setLongType(soundDones.getTotalElements());
			}
		}
		
		return data;
	}
	
	/**
	 * Get Specific File In System
	 * 
	 * @param id
	 * @param fileType
	 * @return
	 */
	public Object getSpecificFileInSystem(Integer id, String fileType) {
		if (fileType.equals(EnumTypeFile.THEME.getValue())) {
			return themeService.findById(id);
		} else if (fileType.equals(EnumTypeFile.SOUNDCONCENTRATION.getValue())) {
			return soundConcentrationService.findById(id);
		} else {
			return soundDoneService.findById(id);
		}
	}
	
	/**
	 * Delete Specific File In System
	 * 
	 * @param id
	 * @param fileType
	 * @return
	 */
	public AllResponseTypeDTO deleteSpecificFileInSystem(Integer id, String fileType) throws IOException {
		AllResponseTypeDTO data = new AllResponseTypeDTO();
		data.setBooleanType(false);
		data.setStringType("Delete Data Failure!");
		if (fileType.equals(EnumTypeFile.THEME.getValue())) {
			themeService.adminDeleteThemeAndFileUploaded(id);
			data.setBooleanType(true);
			data.setStringType("Delete Theme Success!");
			
		} else if (fileType.equals(EnumTypeFile.SOUNDCONCENTRATION.getValue())) {
			soundConcentrationService.adminDeleteSoundConcentrationAndFileUploaded(id);
			data.setBooleanType(true);
			data.setStringType("Delete Sound Concentration Success!");
		} else {
			soundDoneService.adminDeleteSoundDoneAndFileUploaded(id);
			data.setBooleanType(true);
			data.setStringType("Delete Sound Done Success!");
		}
		return data;
	}
	
	/**
	 * Delete Specific File In System
	 * 
	 * @param id
	 * @param fileType
	 * @return
	 */
	public List<Object> searchFileInSystem(String key, String fileType) throws IOException {
		List<Object> data = new ArrayList<>();
		if (fileType.equals(EnumTypeFile.THEME.getValue())) {
			List<Theme> themes = themeService.findByUserNull();
			if (CollectionUtils.isEmpty(themes)) {
				return data;
			}
			data = themes.stream()
					.filter(t -> t.getId().toString().contains(key) 
							|| t.getNameTheme().contains(key))
					.map(t -> new ThemeDTO(t))
					.collect(Collectors.toList());
		} else if (fileType.equals(EnumTypeFile.SOUNDCONCENTRATION.getValue())) {
			List<SoundConcentration> soundConcentrations = soundConcentrationService.findByUserNull();
			if (CollectionUtils.isEmpty(soundConcentrations)) {
				return data;
			}
			data = soundConcentrations.stream()
					.filter(t -> t.getId().toString().contains(key) 
							|| t.getNameSound().contains(key))
					.map(t -> new SoundConcentrationDTO(t))
					.collect(Collectors.toList());
		} else {
			List<SoundDone> soundDones = soundDoneService.findByUserNull();
			if (CollectionUtils.isEmpty(soundDones)) {
				return data;
			}
			data = soundDones.stream()
					.filter(t -> t.getId().toString().contains(key) 
							|| t.getNameSound().contains(key))
					.map(t -> new SoundDoneDTO(t))
					.collect(Collectors.toList());
		}
		return data;
	}
	
	/**
	 * Statistical Files Uploaded of Users
	 * 
	 * @param typeQuery
	 * @param year
	 * @param month
	 * @return
	 */
	public List<StatisticalFileDTO> statisticalFilesUploadedOfAllUsers(Integer userId, String typeQuery, Integer year, 
			Integer month) {
		Map<Long, List<Files>> mapFiles = new LinkedHashMap<>();
		LocalDateTime firstDay = null;
		LocalDateTime lastDay = null;
		if (typeQuery.equals("YEAR")) {
			firstDay = LocalDateTime.of(YearMonth.of(year, 1).atDay(1), LocalTime.MIDNIGHT);
			lastDay = LocalDateTime.of(YearMonth.of(year, 12).atEndOfMonth(), LocalTime.MAX);		
			List<Files> files = new ArrayList<>();
			if (userId == null || userId < 1) {
				files = filesDAO.findByUserIdNotNullAndCreatedAtBetween(
							Date.from(firstDay.atZone(ZoneId.of(EnumZoneId.ASIA_HOCHIMINH.getNameZone())).toInstant()),
							Date.from(lastDay.atZone(ZoneId.of(EnumZoneId.ASIA_HOCHIMINH.getNameZone())).toInstant()));
			} else {
				files = filesDAO.findByUserIdAndCreatedAtBetween(userId,
						Date.from(firstDay.atZone(ZoneId.of(EnumZoneId.ASIA_HOCHIMINH.getNameZone())).toInstant()),
						Date.from(lastDay.atZone(ZoneId.of(EnumZoneId.ASIA_HOCHIMINH.getNameZone())).toInstant()));
			}
					
			if (CollectionUtils.isNotEmpty(files)) {
				mapFiles = files.stream()
						.collect(Collectors.groupingBy(f -> {
							LocalDateTime localDateCreatedAt = f.getCreatedAt().toInstant()
									.atZone(ZoneId.of(EnumZoneId.ASIA_HOCHIMINH.getNameZone()))
									.toLocalDateTime();
							LocalDateTime firstDateInMonth = localDateCreatedAt.with(firstDayOfMonth()).with(LocalTime.MIDNIGHT);
							return Date.from(firstDateInMonth.atZone(
									ZoneId.of(EnumZoneId.ASIA_HOCHIMINH.getNameZone())).toInstant()).getTime();
						}, Collectors.toList()));
			}
		} else {
			firstDay = LocalDateTime.of(YearMonth.of(year, month).atDay(1), LocalTime.MIDNIGHT);
			lastDay = LocalDateTime.of(YearMonth.of(year, month).atEndOfMonth(), LocalTime.MAX);	
			List<Files> files = null;
			if (userId == null || userId < 1) {
				files = filesDAO.findByUserIdNotNullAndCreatedAtBetween(
							Date.from(firstDay.atZone(ZoneId.of(EnumZoneId.ASIA_HOCHIMINH.getNameZone())).toInstant()),
							Date.from(lastDay.atZone(ZoneId.of(EnumZoneId.ASIA_HOCHIMINH.getNameZone())).toInstant()));
			} else {
				files = filesDAO.findByUserIdAndCreatedAtBetween(userId,
						Date.from(firstDay.atZone(ZoneId.of(EnumZoneId.ASIA_HOCHIMINH.getNameZone())).toInstant()),
						Date.from(lastDay.atZone(ZoneId.of(EnumZoneId.ASIA_HOCHIMINH.getNameZone())).toInstant()));
			}
			if (CollectionUtils.isNotEmpty(files)) {
				mapFiles = files.stream()
						.collect(Collectors.groupingBy(f -> {
							LocalDateTime localDateCreatedAt = f.getCreatedAt().toInstant()
									.atZone(ZoneId.of(EnumZoneId.ASIA_HOCHIMINH.getNameZone()))
									.toLocalDateTime();
							Integer dayWillMinus = localDateCreatedAt.getDayOfMonth() % 3 - 1;
							dayWillMinus = dayWillMinus < 0 ? 2 : dayWillMinus;
							localDateCreatedAt = localDateCreatedAt.minusDays(dayWillMinus);
							localDateCreatedAt = localDateCreatedAt.with(LocalTime.MIDNIGHT);
							return Date.from(localDateCreatedAt.atZone(
									ZoneId.of(EnumZoneId.ASIA_HOCHIMINH.getNameZone())).toInstant()).getTime();
						}, Collectors.toList()));
			}
		}
		return mapFiles.entrySet().stream()
				.map(f -> {
					Integer totalThemes = f.getValue().stream()
							.filter(file -> file.getType().equals(EnumTypeFile.THEME.getValue()))
							.collect(Collectors.counting()).intValue();
					Integer totalSoundDones = f.getValue().stream()
							.filter(file -> file.getType().equals(EnumTypeFile.SOUNDDONE.getValue()))
							.collect(Collectors.counting()).intValue();
					Integer totalSoundConcentrations = f.getValue().stream()
							.filter(file -> file.getType().equals(EnumTypeFile.SOUNDCONCENTRATION.getValue()))
							.collect(Collectors.counting()).intValue();
					Integer totalAvatars = f.getValue().stream()
							.filter(file -> file.getType().equals(EnumTypeFile.USER.getValue()))
							.collect(Collectors.counting()).intValue();
					Integer totalCoverImages = f.getValue().stream()
							.filter(file -> file.getType().equals(EnumTypeFile.COVERIMAGE.getValue()))
							.collect(Collectors.counting()).intValue();
					Integer totalReports = f.getValue().stream()
							.filter(file -> file.getType().equals(EnumTypeFile.REPORT.getValue()))
							.collect(Collectors.counting()).intValue();
					return new StatisticalFileDTO(f.getKey(), totalThemes, totalSoundDones, totalSoundConcentrations, 
							totalAvatars, totalCoverImages, totalReports);
				})
				.sorted(Comparator.comparing(StatisticalFileDTO::getFirstDateOfMonthOrDateInMonth))
				.collect(Collectors.toList());
	}
	
}
