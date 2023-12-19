package com.focusedapp.smartstudyhub.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.focusedapp.smartstudyhub.dao.SoundDoneDAO;
import com.focusedapp.smartstudyhub.model.SoundDone;
import com.focusedapp.smartstudyhub.model.custom.SoundDoneDTO;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatus;

@Service
public class SoundDoneService {

	@Autowired
	SoundDoneDAO soundDoneDAO;
	
	/**
	 * Get Sound Concentration of Guest
	 * 
	 * @return
	 */
	public List<SoundDoneDTO> getSoundDoneOfGuest() {
		
		List<SoundDone> sounds = soundDoneDAO.findByUserIdIsNullAndStatus(EnumStatus.ACTIVE.getValue());
		
		return sounds.stream()
				.map(s -> new SoundDoneDTO(s))
				.collect(Collectors.toList());
	}
	
}
