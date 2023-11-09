package com.focusedapp.smartstudyhub.controller.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.focusedapp.smartstudyhub.model.custom.Result;
import com.focusedapp.smartstudyhub.model.custom.UserDTO;
import com.focusedapp.smartstudyhub.service.UserService;
import com.focusedapp.smartstudyhub.util.enumerate.StatusCode;



@RestController
@RequestMapping("/mobile/v1/user")
public class UserController {
	
	@Autowired
	UserService userService;
	
	
	/**
	 * 
	 * Create Guest User
	 * 
	 * @param userDTO
	 * @return
	 */
	@GetMapping("/guest/create")
	public ResponseEntity<Result<UserDTO>> createGuestUser() {
		
		Result<UserDTO> result = new Result<UserDTO>();
		
		UserDTO userResponse = userService.createGuestUser();
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		result.setData(userResponse);
		
		return ResponseEntity.ok(result);
		
	}
}
