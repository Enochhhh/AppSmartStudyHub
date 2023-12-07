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
import com.focusedapp.smartstudyhub.service.AuthenticationService;
import com.focusedapp.smartstudyhub.service.UserService;
import com.focusedapp.smartstudyhub.util.enumerate.StatusCode;


@RestController
@RequestMapping("/mobile/v1/auth")
@CrossOrigin(origins ="*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE, RequestMethod.PUT})
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
		if (request == null) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);
		}
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
		
		if (request == null) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);
		}
		
		AuthenticationDTO data = authenticationService.authenticate(request);

		if (data == null) {
			result.getMeta().setStatusCode(StatusCode.LOGIN_FAILURE.getCode());
			result.getMeta().setMessage(StatusCode.LOGIN_FAILURE.getMessage());
			result.getMeta().setDetails("Username and Password incorrect");
			return createResponseEntity(result, HttpStatus.FORBIDDEN);
		} else {
			result.setData(data);
			result.getMeta().setStatusCode(StatusCode.LOGIN_SUCCESS.getCode());
			result.getMeta().setMessage(StatusCode.LOGIN_SUCCESS.getMessage());
		}

		return createResponseEntity(result);
	}

	/**
	 * Resend OTP Code
	 * 
	 * @param userId
	 * @return
	 */
	@PostMapping("/resendotp")
	public ResponseEntity<Result<AuthenticationDTO>> resendOtpCode(@RequestBody AuthenticationDTO request) {
		Result<AuthenticationDTO> result = new Result<>();
		if (request == null || StringUtils.isBlank(request.getEmail())) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("Please provide the email to resend OTP code");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);
		}
		
		AuthenticationDTO data = authenticationService.resendOtpCode(request.getEmail());

		if (data == null) {
			result.getMeta().setStatusCode(StatusCode.RESEND_OTP_FAILURE.getCode());
			result.getMeta().setMessage(StatusCode.RESEND_OTP_FAILURE.getMessage());
			result.getMeta().setDetails("Email Invalid or not exist");
			return createResponseEntity(result, HttpStatus.NOT_FOUND);
		} else {
			result.setData(data);
			result.getMeta().setStatusCode(StatusCode.RESEND_OTP_SUCCESS.getCode());
			result.getMeta().setMessage(StatusCode.RESEND_OTP_SUCCESS.getMessage());
		}
		return createResponseEntity(result);
	}
	
	@PostMapping("/forgot-password")
	public ResponseEntity<Result<Object>> changePassword(@RequestBody AuthenticationDTO authenticationDTO) {

		Result<Object> result = new Result<Object>();
		if (authenticationDTO == null 
				|| authenticationDTO.getPassword() == null
				|| authenticationDTO.getEmail() == null) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("Please provide the new password and email to change password");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);
		}

		userService.changePassword(authenticationDTO);

		result.getMeta().setStatusCode(StatusCode.CHANGE_PASSWORD_SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.CHANGE_PASSWORD_SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	@PostMapping("/check-email-exist")
	public ResponseEntity<Result<Boolean>> checkEmailExist(@RequestBody AuthenticationDTO authenticationDTO) {
		
		Result<Boolean> result = new Result<>();
		HttpStatus httpStatus;
		if (authenticationDTO == null || authenticationDTO.getEmail() == null) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("Please provide the Email to check if exist");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);
		}
		
		Boolean isExist = userService.existsByEmailAndProviderLocal(authenticationDTO.getEmail());
		result.setData(isExist);
		if (isExist) {
			result.getMeta().setStatusCode(StatusCode.DATA_EXISTED.getCode());
			result.getMeta().setMessage(StatusCode.DATA_EXISTED.getMessage());
			result.getMeta().setDetails("Email existed");
			httpStatus = HttpStatus.OK;
			
		} else {
			result.getMeta().setStatusCode(StatusCode.DATA_NOT_FOUND.getCode());
			result.getMeta().setMessage(StatusCode.DATA_NOT_FOUND.getMessage());
			result.getMeta().setDetails("Email not existed");
			httpStatus = HttpStatus.NOT_FOUND;
		}
		return createResponseEntity(result, httpStatus);
	}
	
	/**
	 * Authenticate to recover account, it don't use Spring Security
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping("/authenticate-to-recover")
	public ResponseEntity<Result<AuthenticationDTO>> authenticateToRecoverAccount(@RequestBody AuthenticationDTO request) {
		
		Result<AuthenticationDTO> result = new Result<>();
		
		if (request == null) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);
		}
		
		AuthenticationDTO data = authenticationService.authenticateToRecoverAccount(request);

		if (data == null) {
			result.getMeta().setStatusCode(StatusCode.LOGIN_FAILURE.getCode());
			result.getMeta().setMessage(StatusCode.LOGIN_FAILURE.getMessage());
			result.getMeta().setDetails("Username and Password incorrect");
			return createResponseEntity(result, HttpStatus.FORBIDDEN);
		} else {
			result.setData(data);
			result.getMeta().setStatusCode(StatusCode.LOGIN_SUCCESS.getCode());
			result.getMeta().setMessage(StatusCode.LOGIN_SUCCESS.getMessage());
		}

		return createResponseEntity(result);
	}
	
}
