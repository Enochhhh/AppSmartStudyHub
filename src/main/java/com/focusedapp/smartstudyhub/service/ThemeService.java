package com.focusedapp.smartstudyhub.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.focusedapp.smartstudyhub.dao.ThemeDAO;
import com.focusedapp.smartstudyhub.exception.NoRightToPerformException;
import com.focusedapp.smartstudyhub.exception.NotFoundValueException;
import com.focusedapp.smartstudyhub.model.Theme;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.custom.ThemeDTO;
import com.focusedapp.smartstudyhub.util.constant.ConstantUrl;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatus;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatusCustomContent;
import com.focusedapp.smartstudyhub.util.enumerate.EnumTypeFile;

@Service
public class ThemeService {

	@Autowired
	ThemeDAO themeDAO;
	
	@Autowired
	CloudinaryService cloudinaryService;
	
	@Autowired
	FilesService filesService;
	
	/**
	 * Get Theme of Guest
	 * 
	 * @return
	 */
	public List<ThemeDTO> getThemeOfGuest() {
		
		List<Theme> themes = themeDAO.findByUserIdIsNullAndStatus(EnumStatus.ACTIVE.getValue());
		
		return themes.stream()
				.map(t -> new ThemeDTO(t))
				.collect(Collectors.toList());
	}
	
	/**
	 * Insert Theme of Premium User
	 * 
	 * @param themeData
	 * @param user
	 * @return
	 */
	public ThemeDTO insertThemeOfPremiumUser(ThemeDTO themeData, User user) {
		
		Theme theme = Theme.builder()
				.user(user)
				.nameTheme(themeData.getNameTheme())
				.url(themeData.getUrl())
				.statusTheme(EnumStatusCustomContent.OWNED.getValue())
				.status(EnumStatus.ACTIVE.getValue())
				.createdDate(new Date())
				.build();
		
		theme = themeDAO.save(theme);
		
		return new ThemeDTO(theme);
	}
	
	/**
	 * Get Theme of Premium User
	 * 
	 * @param user
	 * @return
	 */
	public List<ThemeDTO> getThemeOfPremiumUser(User user) {
		
		List<Theme> themes = themeDAO.findByUserIdOrUserIdIsNullAndStatus(user.getId(), EnumStatus.ACTIVE.getValue());
		
		return themes.stream()
				.map(t -> new ThemeDTO(t))
				.collect(Collectors.toList());
	}
	
	/**
	 * Update Theme 
	 * 
	 * @param themeData
	 * @return
	 */
	public ThemeDTO updateThemeOfPremiumUser(ThemeDTO themeData) 
			throws IOException {
		
		Theme theme = themeDAO.findByIdAndStatus(themeData.getId(), EnumStatus.ACTIVE.getValue())
				.orElseThrow(() -> new NotFoundValueException("Not Found the Theme to update!", "ThemeService -> updateThemeOfPremiumUser"));
		if (theme.getUser() == null || !theme.getStatusTheme().equals(EnumStatusCustomContent.OWNED.getValue())) {
			throw new NoRightToPerformException("No right to perform exception!", "ThemeService -> updateThemeOfPremiumUser");
		}
		theme.setNameTheme(themeData.getNameTheme());
		if (!themeData.getUrl().equals(theme.getUrl())) {
			String publicId = null;
			if (StringUtils.isNotBlank(theme.getUrl())) {
				Integer startingIndex = theme.getUrl().indexOf(ConstantUrl.URL_FOLDER);
				publicId = theme.getUrl().substring(startingIndex + 1).split("\\.")[0];
			}
			
			if (publicId != null) {
				cloudinaryService.deleteFileInCloudinary(publicId);
			}
			theme.setUrl(themeData.getUrl());
		}
		
		theme = themeDAO.save(theme);
		
		return new ThemeDTO(theme);
	}
	
	/**
	 * Mark Deleted Theme 
	 * 
	 * @param themeId
	 * @return
	 */
	public ThemeDTO markDeletedThemeOfPremiumUser(Integer themeId) {
		
		Theme theme = themeDAO.findById(themeId)
				.orElseThrow(() -> new NotFoundValueException("Not Found the Theme to delete!", "ThemeService -> markDeletedThemeOfPremiumUser"));
		if (theme.getUser() == null || !theme.getStatusTheme().equals(EnumStatusCustomContent.OWNED.getValue())) {
			throw new NoRightToPerformException("No right to perform exception!", "ThemeService -> markDeletedThemeOfPremiumUser");
		}
		theme.setStatus(EnumStatus.DELETED.getValue());
		
		theme = themeDAO.save(theme);
		
		return new ThemeDTO(theme);
	}
	
	/**
	 * Delete Theme 
	 * 
	 * @param themeId
	 * @return
	 */
	public ThemeDTO deleteThemeOfPremiumUser(Integer themeId) throws IOException {
		
		Theme theme = themeDAO.findById(themeId)
				.orElseThrow(() -> new NotFoundValueException("Not Found the Theme to delete!", "ThemeService -> deleteThemeOfPremiumUser"));
		if (theme.getUser() == null || !theme.getStatusTheme().equals(EnumStatusCustomContent.OWNED.getValue())) {
			throw new NoRightToPerformException("No right to perform exception!", "ThemeService -> deleteThemeOfPremiumUser");
		}
		
		String publicId = null;
		if (StringUtils.isNotBlank(theme.getUrl())) {
			Integer startingIndex = theme.getUrl().indexOf(ConstantUrl.URL_FOLDER);
			publicId = theme.getUrl().substring(startingIndex + 1).split("\\.")[0];
		}
		
		if (publicId != null) {
			cloudinaryService.deleteFileInCloudinary(publicId);
		}

		themeDAO.delete(theme);
		
		return new ThemeDTO(theme);
	}
	
	/**
	 * Recover Theme 
	 * 
	 * @param themeId
	 * @return
	 */
	public ThemeDTO recoverThemeOfPremiumUser(Integer themeId) {
		
		Theme theme = themeDAO.findById(themeId)
				.orElseThrow(() -> new NotFoundValueException("Not Found the Theme to recover!", "ThemeService -> recoverThemeOfPremiumUser"));
		if (theme.getUser() == null || !theme.getStatusTheme().equals(EnumStatusCustomContent.OWNED.getValue())) {
			throw new NoRightToPerformException("No right to perform exception!", "ThemeService -> recoverThemeOfPremiumUser");
		}
		theme.setStatus(EnumStatus.ACTIVE.getValue());
		
		theme = themeDAO.save(theme);
		
		return new ThemeDTO(theme);
	}
	
	/**
	 * Get Theme Deleted of Premium User
	 * 
	 * @param user
	 * @return
	 */
	public List<ThemeDTO> getThemeDeletedOfPremiumUser(User user) {
		
		List<Theme> themes = themeDAO.findByUserIdAndStatus(user.getId(), EnumStatus.DELETED.getValue());
		
		return themes.stream()
				.map(t -> new ThemeDTO(t))
				.collect(Collectors.toList());
	}
	
	/**
	 * Delete All Themes of User
	 * 
	 * @param user
	 */
	@Transactional(propagation=Propagation.REQUIRED, noRollbackFor=Exception.class)
	public void deleteAllThemesOfUser(User user) throws IOException {
		filesService.deleteAllFilesByTypeOfUser(user, EnumTypeFile.THEME.getValue());
		themeDAO.deleteByUser(user);
	}

}
