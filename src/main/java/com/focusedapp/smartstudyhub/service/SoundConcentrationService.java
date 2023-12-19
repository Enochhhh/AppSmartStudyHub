package com.focusedapp.smartstudyhub.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.focusedapp.smartstudyhub.dao.SoundConcentrationDAO;
import com.focusedapp.smartstudyhub.model.SoundConcentration;
import com.focusedapp.smartstudyhub.model.custom.SoundConcentrationDTO;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatus;

@Service
public class SoundConcentrationService {

	@Autowired
	SoundConcentrationDAO soundConcentrationDAO;
	
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
}
