package com.focusedapp.smartstudyhub.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.focusedapp.smartstudyhub.dao.SoundConcentrationDAO;
import com.focusedapp.smartstudyhub.exception.NoRightToPerformException;
import com.focusedapp.smartstudyhub.exception.NotFoundValueException;
import com.focusedapp.smartstudyhub.model.SoundConcentration;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.custom.SoundConcentrationDTO;
import com.focusedapp.smartstudyhub.util.constant.ConstantUrl;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatus;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatusCustomContent;
import com.focusedapp.smartstudyhub.util.enumerate.EnumTypeFile;

@Service
public class SoundConcentrationService {

	@Autowired
	private SoundConcentrationDAO soundConcentrationDAO;
	
	@Autowired
	private CloudinaryService cloudinaryService;
	
	@Autowired
	private FilesService filesService;
	
	/**
	 * Get Sound Concentration of Guest
	 * 
	 * @return
	 */
	public List<SoundConcentrationDTO> getSoundConcentrationOfGuest() {
		
		List<SoundConcentration> sounds = soundConcentrationDAO.findByUserIdIsNullAndStatus(EnumStatus.ACTIVE.getValue());
		
		return sounds.stream()
				.map(s -> new SoundConcentrationDTO(s))
				.collect(Collectors.toList());
	}
	
	/**
	 * Get Sound Concentration of Premium User
	 * 
	 * @param user
	 * @return
	 */
	public List<SoundConcentrationDTO> getSoundConcentrationOfPremiumUser(User user) {
		
		List<SoundConcentration> sounds = soundConcentrationDAO.findByUserIdOrUserIdIsNullAndStatus(user.getId(), EnumStatus.ACTIVE.getValue());
		
		return sounds.stream()
				.map(s -> new SoundConcentrationDTO(s))
				.collect(Collectors.toList());
	}
	
	/**
	 * Update Sound Concentration 
	 * 
	 * @param soundConcentrationData
	 * @return
	 */
	public SoundConcentrationDTO updateSoundConcentrationOfPremiumUser(SoundConcentrationDTO soundConcentrationData) 
			throws IOException {
		
		SoundConcentration soundConcentration = soundConcentrationDAO.findByIdAndStatus(soundConcentrationData.getId(), EnumStatus.ACTIVE.getValue())
				.orElseThrow(() -> new NotFoundValueException("Not Found the Sound Concentration to update!", 
						"SoundConcentrationService -> updateSoundConcentrationOfPremiumUser"));
		if (soundConcentration.getUser() == null || !soundConcentration.getStatusSound().equals(EnumStatusCustomContent.OWNED.getValue())) {
			throw new NoRightToPerformException("No right to perform exception!", "SoundConcentrationService -> updateSoundConcentrationOfPremiumUser");
		}
		soundConcentration.setNameSound(soundConcentrationData.getNameSound());
		if (!soundConcentrationData.getUrl().equals(soundConcentration.getUrl())) {
			String publicId = null;
			if (StringUtils.isNotBlank(soundConcentration.getUrl())) {
				Integer startingIndex = soundConcentration.getUrl().indexOf(ConstantUrl.URL_FOLDER);
				publicId = soundConcentration.getUrl().substring(startingIndex + 1).split("\\.")[0];
			}
			
			if (publicId != null) {
				cloudinaryService.deleteFileInCloudinary(publicId);
			}
			soundConcentration.setUrl(soundConcentrationData.getUrl());
		}
			
		soundConcentration = soundConcentrationDAO.save(soundConcentration);
		
		return new SoundConcentrationDTO(soundConcentration);
	}
	
	/**
	 * Insert Sound Concentration of Premium User
	 * 
	 * @param soundConcentrationData
	 * @param user
	 * @return
	 */
	public SoundConcentrationDTO insertSoundConcentrationOfPremiumUser(SoundConcentrationDTO soundConcentrationData, User user) {
		
		SoundConcentration soundConcentration = SoundConcentration.builder()
				.user(user)
				.nameSound(soundConcentrationData.getNameSound())
				.url(soundConcentrationData.getUrl())
				.statusSound(EnumStatusCustomContent.OWNED.getValue())
				.status(EnumStatus.ACTIVE.getValue())
				.createdDate(new Date())
				.build();
		
		soundConcentration = soundConcentrationDAO.save(soundConcentration);
		
		return new SoundConcentrationDTO(soundConcentration);
	}
	
	/**
	 * Mark Deleted Sound Concentration of Premium User 
	 * 
	 * @param soundConcentrationId
	 * @return
	 */
	public SoundConcentrationDTO markDeletedSoundConcentrationOfPremiumUser(Integer soundConcentrationId) {
		
		SoundConcentration soundConcentration = soundConcentrationDAO.findById(soundConcentrationId)
				.orElseThrow(() -> new NotFoundValueException("Not Found the Sound Concentration to delete!", 
						"SoundConcentrationService -> markDeletedSoundConcentrationOfPremiumUser"));
		if (soundConcentration.getUser() == null || !soundConcentration.getStatusSound().equals(EnumStatusCustomContent.OWNED.getValue())) {
			throw new NoRightToPerformException("No right to perform exception!", "SoundConcentrationService -> markDeletedSoundConcentrationOfPremiumUser");
		}
		soundConcentration.setStatus(EnumStatus.DELETED.getValue());
		
		soundConcentration = soundConcentrationDAO.save(soundConcentration);
		
		return new SoundConcentrationDTO(soundConcentration);
	}
	
	/**
	 * Recover Sound Concentration 
	 * 
	 * @param themeData
	 * @return
	 */
	public SoundConcentrationDTO recoverSoundConcentrationOfPremiumUser(Integer soundConcentrationId) {
		
		SoundConcentration soundConcentration = soundConcentrationDAO.findById(soundConcentrationId)
				.orElseThrow(() -> new NotFoundValueException("Not Found the Sound Concentration to recover!", 
						"SoundConcentrationService -> recoverSoundConcentrationOfPremiumUser"));
		if (soundConcentration.getUser() == null || !soundConcentration.getStatusSound().equals(EnumStatusCustomContent.OWNED.getValue())) {
			throw new NoRightToPerformException("No right to perform exception!", 
					"SoundConcentrationService -> recoverSoundConcentrationOfPremiumUser");
		}
		soundConcentration.setStatus(EnumStatus.ACTIVE.getValue());
		
		soundConcentration = soundConcentrationDAO.save(soundConcentration);
		
		return new SoundConcentrationDTO(soundConcentration);
	}
	
	/**
	 * Delete Sound Concentration 
	 * 
	 * @param soundConcentrationId
	 * @return
	 */
	public SoundConcentrationDTO deleteSoundConcentrationAndFileUploaded(Integer soundConcentrationId) throws IOException {
		
		SoundConcentration soundConcentration = soundConcentrationDAO.findById(soundConcentrationId)
				.orElseThrow(() -> new NotFoundValueException("Not Found the SoundConcentration to delete!", 
						"SoundConcentrationService -> deleteSoundConcentrationAndFileUploaded"));
		if (soundConcentration.getUser() == null || !soundConcentration.getStatusSound().equals(EnumStatusCustomContent.OWNED.getValue())) {
			throw new NoRightToPerformException("No right to perform exception!", 
					"SoundConcentrationService -> deleteSoundConcentrationAndFileUploaded");
		}
		
		String publicId = null;
		if (StringUtils.isNotBlank(soundConcentration.getUrl())) {
			Integer startingIndex = soundConcentration.getUrl().indexOf(ConstantUrl.URL_FOLDER);
			publicId = soundConcentration.getUrl().substring(startingIndex + 1).split("\\.")[0];
		}
		
		if (publicId != null) {
			cloudinaryService.deleteFileInCloudinary(publicId);
		}

		soundConcentrationDAO.delete(soundConcentration);
		
		return new SoundConcentrationDTO(soundConcentration);
	}
	
	/**
	 * Get Sound Concentration Deleted of Premium User
	 * 
	 * @param user
	 * @return
	 */
	public List<SoundConcentrationDTO> getSoundConcentrationDeletedOfPremiumUser(User user) {
		
		List<SoundConcentration> sounds = soundConcentrationDAO.findByUserIdAndStatus(user.getId(), EnumStatus.DELETED.getValue());
		
		return sounds.stream()
				.map(s -> new SoundConcentrationDTO(s))
				.collect(Collectors.toList());
	}
	
	/**
	 * Delete all sounds concentration of User
	 * 
	 * @param user
	 * @throws IOException
	 */
	@Transactional(propagation=Propagation.REQUIRED, noRollbackFor=Exception.class)
	public void deleteAllSoundsConcentrationOfUser(User user) throws IOException {
		filesService.deleteAllFilesByTypeOfUser(user, EnumTypeFile.SOUNDCONCENTRATION.getValue());
		soundConcentrationDAO.deleteByUser(user);
	}
	
	public SoundConcentration findByUrl(String url) {
		return soundConcentrationDAO.findByUrl(url);
	}
	
	@Transactional(propagation=Propagation.REQUIRED, noRollbackFor=Exception.class)
	public void delete(SoundConcentration soundConcentration) {
		soundConcentrationDAO.delete(soundConcentration);
	}
	
	public SoundConcentration persistent(SoundConcentration soundConcentration) {
		return soundConcentrationDAO.save(soundConcentration);
	}
	
	public SoundConcentration findById(Integer id) {
		return soundConcentrationDAO.findById(id)
				.orElseThrow(() -> new NotFoundValueException("Not Found Sound Concentration!", 
						"SoundConcentrationService -> findById"));
	}
	
	public List<SoundConcentration> findByUserNullAndStatusSound(String statusSound, Pageable pageable) {
		return soundConcentrationDAO.findByUserNullAndStatusSound(statusSound, pageable);
	}
	
	public List<SoundConcentration> findByUserNullAndStatusSoundAndCreatedDateBetween(String statusSound, Date startDate, 
			Date endDate, Pageable pageable) {
		return soundConcentrationDAO.findByUserNullAndStatusSound(statusSound, pageable);
	}
	
	public List<SoundConcentration> findByUserNull(Pageable pageable) {
		return soundConcentrationDAO.findByUserNull(pageable);
	}
	
	public List<SoundConcentration> findByUserNull() {
		return soundConcentrationDAO.findByUserNull();
	}
	
	public List<SoundConcentration> findByUserNullAndCreatedDateBetween(Date startDate, Date endDate, Pageable pageable) {
		return soundConcentrationDAO.findByUserNullAndCreatedDateBetween(startDate, endDate, pageable);
	}
	
	/**
	 * Delete Sound Concentration 
	 * 
	 * @param soundConcentrationId
	 * @return
	 */
	public SoundConcentrationDTO adminDeleteSoundConcentrationAndFileUploaded(Integer soundConcentrationId) throws IOException {
		
		SoundConcentration soundConcentration = soundConcentrationDAO.findById(soundConcentrationId)
				.orElseThrow(() -> new NotFoundValueException("Not Found the SoundConcentration to delete!", 
						"SoundConcentrationService -> deleteSoundConcentrationAndFileUploaded"));
		
		String publicId = null;
		if (StringUtils.isNotBlank(soundConcentration.getUrl())) {
			Integer startingIndex = soundConcentration.getUrl().indexOf(ConstantUrl.URL_FOLDER);
			publicId = soundConcentration.getUrl().substring(startingIndex + 1).split("\\.")[0];
		}
		
		if (publicId != null) {
			cloudinaryService.deleteFileInCloudinary(publicId);
		}

		soundConcentrationDAO.delete(soundConcentration);
		
		return new SoundConcentrationDTO(soundConcentration);
	}
}
