package com.focusedapp.smartstudyhub.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.focusedapp.smartstudyhub.dao.ThemeDAO;
import com.focusedapp.smartstudyhub.model.Theme;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.custom.ThemeDTO;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatus;

@Service
public class ThemeService {

	@Autowired
	ThemeDAO themeDAO;
	
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
	 * Get Theme of Premium User
	 * 
	 * @param user
	 * @return
	 */
	public List<ThemeDTO> getThemeOfPremiumUser(User user) {
		
		List<Theme> themes = themeDAO.findByUserIdAndStatus(user.getId(), EnumStatus.ACTIVE.getValue());
		
		return themes.stream()
				.map(t -> new ThemeDTO(t))
				.collect(Collectors.toList());
	}

}
