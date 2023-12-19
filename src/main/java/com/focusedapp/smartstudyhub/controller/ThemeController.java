package com.focusedapp.smartstudyhub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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

}
