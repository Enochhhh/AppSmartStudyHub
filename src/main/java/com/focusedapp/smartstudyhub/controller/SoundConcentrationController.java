package com.focusedapp.smartstudyhub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.focusedapp.smartstudyhub.model.custom.Result;
import com.focusedapp.smartstudyhub.model.custom.SoundConcentrationDTO;
import com.focusedapp.smartstudyhub.service.SoundConcentrationService;
import com.focusedapp.smartstudyhub.util.enumerate.StatusCode;

@RestController
@RequestMapping("/mobile/v1/user")
@CrossOrigin(origins = "*", methods = { RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE,
		RequestMethod.PUT })
public class SoundConcentrationController extends BaseController {

	@Autowired
	SoundConcentrationService soundConcentrationService;
	
	/**
	 * Get Sound Concentration of Guest
	 * 
	 * @return
	 */
	@GetMapping("/guest/soundconcentration/get")
	public ResponseEntity<Result<List<SoundConcentrationDTO>>> getSoundConcentrationOfGuest() {

		Result<List<SoundConcentrationDTO>> result = new Result<>();	

		List<SoundConcentrationDTO> listSoundConcentration = soundConcentrationService.getSoundConcentrationOfGuest();
		
		result.setData(listSoundConcentration);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
}
