package com.focusedapp.smartstudyhub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.focusedapp.smartstudyhub.model.custom.AuthenticationDTO;
import com.focusedapp.smartstudyhub.model.custom.Result;
import com.focusedapp.smartstudyhub.model.custom.UserDTO;
import com.focusedapp.smartstudyhub.service.AuthenticationService;
import com.focusedapp.smartstudyhub.service.UserService;
import com.focusedapp.smartstudyhub.util.enumerate.StatusCode;

@RestController
@RequestMapping("/mobile/v1/auth")
public class AuthenticationController extends BaseController {

	@Autowired
	AuthenticationService authenticationService;
	@Autowired
	UserService userService;

	/**
	 * Controller Register User
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping("/register")
	public ResponseEntity<Result<AuthenticationDTO>> register(@RequestBody AuthenticationDTO request) {
		Result<AuthenticationDTO> result = new Result<>();
		AuthenticationDTO data = authenticationService.register(request);

		if (data == null) {
			result.getMeta().setStatusCode(StatusCode.REGISTER_FAILURE.getCode());
			result.getMeta().setMessage(StatusCode.REGISTER_FAILURE.getMessage());
		} else {
			result.setData(data);
			result.getMeta().setStatusCode(StatusCode.REGISTER_SUCCESS.getCode());
			result.getMeta().setMessage(StatusCode.REGISTER_SUCCESS.getMessage());
		}

		return createResponseEntity(result);
	}

	/**
	 * Controller Login User
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping("/authenticate")
	public ResponseEntity<Result<AuthenticationDTO>> authenticate(@RequestBody AuthenticationDTO request) {
		Result<AuthenticationDTO> result = new Result<>();
		AuthenticationDTO data = authenticationService.authenticate(request);

		if (data == null) {
			result.getMeta().setStatusCode(StatusCode.LOGIN_FAILURE.getCode());
			result.getMeta().setMessage(StatusCode.LOGIN_FAILURE.getMessage());
			result.getMeta().setDetails("Username and Password incorrect");
		} else {
			result.setData(data);
			result.getMeta().setStatusCode(StatusCode.LOGIN_SUCCESS.getCode());
			result.getMeta().setMessage(StatusCode.LOGIN_SUCCESS.getMessage());
		}

		return createResponseEntity(result);
	}

	@GetMapping("/resendotp")
	public ResponseEntity<Result<AuthenticationDTO>> resendOtpCode(@RequestParam Integer userId) {
		Result<AuthenticationDTO> result = new Result<>();
		AuthenticationDTO data = authenticationService.resendOtpCode(userId);

		if (data == null) {
			result.getMeta().setStatusCode(StatusCode.RESEND_OTP_FAILURE.getCode());
			result.getMeta().setMessage(StatusCode.RESEND_OTP_FAILURE.getMessage());
			result.getMeta().setDetails("Email Invalid or not exist");
		} else {
			result.setData(data);
			result.getMeta().setStatusCode(StatusCode.RESEND_OTP_SUCCESS.getCode());
			result.getMeta().setMessage(StatusCode.RESEND_OTP_SUCCESS.getMessage());
		}
		return createResponseEntity(result);
	}

	@DeleteMapping("/deleteuser/{userId}")
	public ResponseEntity<Result<UserDTO>> deleteUserRegistered(@PathVariable Integer userId) {
		Result<UserDTO> result = new Result<>();
		UserDTO data = authenticationService.deleteUserRegistered(userId);

		result.setData(data);
		result.getMeta().setStatusCode(StatusCode.DELETE_USER_REGISTERED_SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.DELETE_USER_REGISTERED_SUCCESS.getMessage());

		return createResponseEntity(result);
	}
}
