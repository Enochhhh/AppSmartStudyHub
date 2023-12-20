package com.focusedapp.smartstudyhub.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
	
	/**
	 * Get Sound Done of Premium User
	 * 
	 * @return
	 */
	@GetMapping("/premium/sounddone/get")
	public ResponseEntity<Result<List<SoundDoneDTO>>> getSoundDoneOfPremiumUser() {

		Result<List<SoundDoneDTO>> result = new Result<>();	

		User user = getAuthenticatedUser();
		List<SoundDoneDTO> listSoundDone = soundDoneService.getSoundDoneOfPremiumUser(user);
		
		result.setData(listSoundDone);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Insert Sound Done of Premium User
	 * 
	 * @param soundDoneData
	 * @return
	 */
	@PostMapping("/premium/sounddone/insert")
	public ResponseEntity<Result<SoundDoneDTO>> insertSoundDoneOfPremiumUser(@RequestBody SoundDoneDTO soundDoneData) {

		Result<SoundDoneDTO> result = new Result<>();	

		User user = getAuthenticatedUser();
		SoundDoneDTO soundConcentrationInserted = soundDoneService.insertSoundDoneOfPremiumUser(
				soundDoneData, user);
		
		result.setData(soundConcentrationInserted);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Update Sound Done of Premium User
	 * 
	 * @return
	 */
	@PutMapping("/premium/sounddone/update")
	public ResponseEntity<Result<SoundDoneDTO>> updateSoundDoneOfPremiumUser(@RequestBody SoundDoneDTO soundDoneRequest) 
			throws IOException {

		Result<SoundDoneDTO> result = new Result<>();	

		SoundDoneDTO soundDoneUpdated = soundDoneService.updateSoundDoneOfPremiumUser(soundDoneRequest);
		
		result.setData(soundDoneUpdated);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Mark Deleted Sound Concentration of Premium User
	 * 
	 * @return
	 */
	@DeleteMapping("/premium/sounddone/mark-deleted/{soundDoneId}")
	public ResponseEntity<Result<SoundDoneDTO>> markDeletedSoundDoneOfPremiumUser(@PathVariable Integer soundDoneId) {

		Result<SoundDoneDTO> result = new Result<>();	

		SoundDoneDTO soundDoneDeleted = soundDoneService.markDeletedSoundDoneOfPremiumUser(
				soundDoneId);
		
		result.setData(soundDoneDeleted);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Recover Sound Done of Premium User
	 * 
	 * @return
	 */
	@PutMapping("/premium/sounddone/recover/{soundDoneId}")
	public ResponseEntity<Result<SoundDoneDTO>> recoverSoundDoneOfPremiumUser(@PathVariable Integer soundDoneId) {

		Result<SoundDoneDTO> result = new Result<>();	

		SoundDoneDTO soundDoneRecovered = soundDoneService.recoverSoundDoneOfPremiumUser(soundDoneId);
		
		result.setData(soundDoneRecovered);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Delete Sound Done of Premium user
	 * 
	 * @param soundDoneId
	 * @return
	 * @throws IOException
	 */
	@DeleteMapping("/premium/sounddone/delete/{soundDoneId}")
	public ResponseEntity<Result<SoundDoneDTO>> deleteSoundDoneOfPremiumUser(@PathVariable Integer soundDoneId) 
			throws IOException{

		Result<SoundDoneDTO> result = new Result<>();	

		SoundDoneDTO soundConcentrationDeleted = soundDoneService.deleteSoundDoneOfPremiumUser(soundDoneId);
		
		result.setData(soundConcentrationDeleted);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
}
