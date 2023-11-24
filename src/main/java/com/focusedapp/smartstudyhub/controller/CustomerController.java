package com.focusedapp.smartstudyhub.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.focusedapp.smartstudyhub.model.custom.AuthenticationDTO;
import com.focusedapp.smartstudyhub.model.custom.Result;
import com.focusedapp.smartstudyhub.service.UserService;
import com.focusedapp.smartstudyhub.util.enumerate.StatusCode;

@RestController
@RequestMapping("/mobile/v1/user/customer")
@CrossOrigin(origins ="*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE, RequestMethod.PUT})
public class CustomerController extends BaseController {
	
	@Autowired
	UserService userService;
	
	/**
	 * Send OTP to Gmail to change password
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping("/otp-change-pass")
	public ResponseEntity<Result<AuthenticationDTO>> sendOtpToChangePassword(@RequestBody AuthenticationDTO request) {
		Result<AuthenticationDTO> result = new Result<>();
		if (request == null || StringUtils.isBlank(request.getEmail())) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("Please provide the email to send OTP code");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);
		}
		
		AuthenticationDTO data = userService.resendOtpCodeToUserLocal(request.getEmail());

		if (data == null) {
			result.getMeta().setStatusCode(StatusCode.RESEND_OTP_FAILURE.getCode());
			result.getMeta().setMessage(StatusCode.RESEND_OTP_FAILURE.getMessage());
			result.getMeta().setDetails("Email Invalid. Please make sure to enter the correct Email you registered with");
			return createResponseEntity(result, HttpStatus.NOT_FOUND);
		} else {
			result.setData(data);
			result.getMeta().setStatusCode(StatusCode.RESEND_OTP_SUCCESS.getCode());
			result.getMeta().setMessage(StatusCode.RESEND_OTP_SUCCESS.getMessage());
		}
		return createResponseEntity(result);
	}

	/**
	 * Change account's password of User
	 * 
	 * @param authenticationDTO
	 * @return
	 */
	@PostMapping("/change-password")
	public ResponseEntity<Result<Object>> changePassword(@RequestBody AuthenticationDTO authenticationDTO) {

		Result<Object> result = new Result<Object>();
		if (authenticationDTO == null || authenticationDTO.getPassword() == null) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("Please provide the new password to change password");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);
		}

		userService.changePassword(authenticationDTO);

		result.getMeta().setStatusCode(StatusCode.CHANGE_PASSWORD_SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.CHANGE_PASSWORD_SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
}
