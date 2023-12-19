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
import com.focusedapp.smartstudyhub.model.custom.SoundDoneDTO;
import com.focusedapp.smartstudyhub.service.SoundDoneService;
import com.focusedapp.smartstudyhub.util.enumerate.StatusCode;

@RestController
@RequestMapping("/mobile/v1/user")
@CrossOrigin(origins = "*", methods = { RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE,
		RequestMethod.PUT })
public class SoundDoneController extends BaseController {

	@Autowired
	SoundDoneService soundDoneService;
	
	/**
	 * Get Sound Done of Guest
	 * 
	 * @return
	 */
	@GetMapping("/guest/sounddone/get")
	public ResponseEntity<Result<List<SoundDoneDTO>>> getSoundDoneOfGuest() {

		Result<List<SoundDoneDTO>> result = new Result<>();	

		List<SoundDoneDTO> listSoundDone = soundDoneService.getSoundDoneOfGuest();
		
		result.setData(listSoundDone);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
}
