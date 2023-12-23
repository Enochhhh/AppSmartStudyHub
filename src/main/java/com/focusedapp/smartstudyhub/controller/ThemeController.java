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
import com.focusedapp.smartstudyhub.model.custom.ThemeDTO;
import com.focusedapp.smartstudyhub.service.ThemeService;
import com.focusedapp.smartstudyhub.util.enumerate.StatusCode;

@RestController
@RequestMapping("/mobile/v1/user")
@CrossOrigin(origins = "*", methods = { RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE,
		RequestMethod.PUT })
public class ThemeController extends BaseController {
	
	@Autowired 
	ThemeService themeService;
	
	/**
	 * Get Theme of Guest
	 * 
	 * @return
	 */
	@GetMapping("/guest/theme/get")
	public ResponseEntity<Result<List<ThemeDTO>>> getThemeOfGuest() {

		Result<List<ThemeDTO>> result = new Result<>();	

		List<ThemeDTO> listTheme = themeService.getThemeOfGuest();
		
		result.setData(listTheme);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Get Theme of Premium User
	 * 
	 * @return
	 */
	@GetMapping("/premium/theme/get")
	public ResponseEntity<Result<List<ThemeDTO>>> getThemeOfPremiumUser() {

		Result<List<ThemeDTO>> result = new Result<>();	

		User user = getAuthenticatedUser();
		List<ThemeDTO> listTheme = themeService.getThemeOfPremiumUser(user);
		
		result.setData(listTheme);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Update Theme of Premium User
	 * 
	 * @return
	 */
	@PutMapping("/premium/theme/update")
	public ResponseEntity<Result<ThemeDTO>> getThemeOfPremiumUser(@RequestBody ThemeDTO themeRequest) 
			throws IOException {

		Result<ThemeDTO> result = new Result<>();	

		ThemeDTO themeUpdated = themeService.updateThemeOfPremiumUser(themeRequest);
		
		result.setData(themeUpdated);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Mark Deleted Theme of Premium User
	 * 
	 * @return
	 */
	@DeleteMapping("/premium/theme/mark-deleted/{themeId}")
	public ResponseEntity<Result<ThemeDTO>> markDeletedThemeOfPremiumUser(@PathVariable Integer themeId) {

		Result<ThemeDTO> result = new Result<>();	

		ThemeDTO themeDeleted = themeService.markDeletedThemeOfPremiumUser(themeId);
		
		result.setData(themeDeleted);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Insert Theme of Premium User
	 * 
	 * @return
	 */
	@PostMapping("/premium/theme/insert")
	public ResponseEntity<Result<ThemeDTO>> insertThemeOfPremiumUser(@RequestBody ThemeDTO themeData) {

		Result<ThemeDTO> result = new Result<>();	

		User user = getAuthenticatedUser();
		ThemeDTO themeInserted = themeService.insertThemeOfPremiumUser(themeData, user);
		
		result.setData(themeInserted);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Delete Theme of Premium user
	 * 
	 * @param themeId
	 * @return
	 * @throws IOException
	 */
	@Transactional(propagation=Propagation.REQUIRED, noRollbackFor=Exception.class)
	@DeleteMapping("/premium/theme/delete/{themeId}")
	public ResponseEntity<Result<ThemeDTO>> deleteThemeOfPremiumUser(@PathVariable Integer themeId) 
			throws IOException{

		Result<ThemeDTO> result = new Result<>();	

		ThemeDTO themeDeleted = themeService.deleteThemeOfPremiumUser(themeId);
		
		result.setData(themeDeleted);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Recover Theme of Premium User
	 * 
	 * @return
	 */
	@PutMapping("/premium/theme/recover/{themeId}")
	public ResponseEntity<Result<ThemeDTO>> recoverThemeOfPremiumUser(@PathVariable Integer themeId) {

		Result<ThemeDTO> result = new Result<>();	

		ThemeDTO themeDeleted = themeService.recoverThemeOfPremiumUser(themeId);
		
		result.setData(themeDeleted);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Get Theme Deleted of Premium User
	 * 
	 * @return
	 */
	@GetMapping("/premium/theme/get-deleted")
	public ResponseEntity<Result<List<ThemeDTO>>> getThemeDeletedOfPremiumUser() {

		Result<List<ThemeDTO>> result = new Result<>();	

		User user = getAuthenticatedUser();
		List<ThemeDTO> listTheme = themeService.getThemeDeletedOfPremiumUser(user);
		
		result.setData(listTheme);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}

}
