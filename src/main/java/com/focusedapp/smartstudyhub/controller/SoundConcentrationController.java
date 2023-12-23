package com.focusedapp.smartstudyhub.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.focusedapp.smartstudyhub.model.User;
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
	
	/**
	 * Get Sound Concentration of Premium User
	 * 
	 * @return
	 */
	@GetMapping("/premium/soundconcentration/get")
	public ResponseEntity<Result<List<SoundConcentrationDTO>>> getSoundConcentrationOfPremiumUser() {

		Result<List<SoundConcentrationDTO>> result = new Result<>();	

		User user = getAuthenticatedUser();
		List<SoundConcentrationDTO> listSoundConcentration = soundConcentrationService.getSoundConcentrationOfPremiumUser(user);
		
		result.setData(listSoundConcentration);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Update Sound Concentration of Premium User
	 * 
	 * @param soundConcentrationRequest
	 * @return
	 * @throws IOException
	 */
	@PutMapping("/premium/soundconcentration/update")
	public ResponseEntity<Result<SoundConcentrationDTO>> updateSoundConcentrationOfPremiumUser(
			@RequestBody SoundConcentrationDTO soundConcentrationRequest) throws IOException {

		Result<SoundConcentrationDTO> result = new Result<>();	

		SoundConcentrationDTO soundConcentrationUpdated = soundConcentrationService.updateSoundConcentrationOfPremiumUser(soundConcentrationRequest);
		
		result.setData(soundConcentrationUpdated);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Insert Sound Concentration of Premium User
	 * 
	 * @return
	 */
	@PostMapping("/premium/soundconcentration/insert")
	public ResponseEntity<Result<SoundConcentrationDTO>> insertSoundConcentrationOfPremiumUser(@RequestBody SoundConcentrationDTO soundConcentrationData) {

		Result<SoundConcentrationDTO> result = new Result<>();	

		User user = getAuthenticatedUser();
		SoundConcentrationDTO soundConcentrationInserted = soundConcentrationService.insertSoundConcentrationOfPremiumUser(
				soundConcentrationData, user);
		
		result.setData(soundConcentrationInserted);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Mark Deleted Sound Concentration of Premium User
	 * 
	 * @return
	 */
	@DeleteMapping("/premium/soundconcentration/mark-deleted/{soundConcentrationId}")
	public ResponseEntity<Result<SoundConcentrationDTO>> markDeletedSoundConcentrationOfPremiumUser(@PathVariable Integer soundConcentrationId) {

		Result<SoundConcentrationDTO> result = new Result<>();	

		SoundConcentrationDTO soundConcentrationDeleted = soundConcentrationService.markDeletedSoundConcentrationOfPremiumUser(
				soundConcentrationId);
		
		result.setData(soundConcentrationDeleted);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Recover Sound Concentration of Premium User
	 * 
	 * @return
	 */
	@PutMapping("/premium/soundconcentration/recover/{soundConcentrationId}")
	public ResponseEntity<Result<SoundConcentrationDTO>> recoverSoundConcentrationOfPremiumUser(@PathVariable Integer soundConcentrationId) {

		Result<SoundConcentrationDTO> result = new Result<>();	

		SoundConcentrationDTO soundConcentrationRecovered = soundConcentrationService.recoverSoundConcentrationOfPremiumUser(soundConcentrationId);
		
		result.setData(soundConcentrationRecovered);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Delete Sound Concentration of Premium user
	 * 
	 * @param soundConcentrationId
	 * @return
	 * @throws IOException
	 */
	@Transactional(propagation=Propagation.REQUIRED, noRollbackFor=Exception.class)
	@DeleteMapping("/premium/soundconcentration/delete/{soundConcentrationId}")
	public ResponseEntity<Result<SoundConcentrationDTO>> deleteSoundConcentrationOfPremiumUser(@PathVariable Integer soundConcentrationId) 
			throws IOException{

		Result<SoundConcentrationDTO> result = new Result<>();	

		SoundConcentrationDTO soundConcentrationDeleted = soundConcentrationService.deleteSoundConcentrationOfPremiumUser(soundConcentrationId);
		
		result.setData(soundConcentrationDeleted);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Get Sound Concentration Deleted of Premium User
	 * 
	 * @return
	 */
	@GetMapping("/premium/soundconcentration/get-deleted")
	public ResponseEntity<Result<List<SoundConcentrationDTO>>> getSoundConcentrationDeletedOfPremiumUser() {

		Result<List<SoundConcentrationDTO>> result = new Result<>();	

		User user = getAuthenticatedUser();
		List<SoundConcentrationDTO> listSoundConcentration = soundConcentrationService.getSoundConcentrationDeletedOfPremiumUser(user);
		
		result.setData(listSoundConcentration);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
}
