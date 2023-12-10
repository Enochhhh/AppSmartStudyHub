package com.focusedapp.smartstudyhub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.custom.AllResponseTypeDTO;
import com.focusedapp.smartstudyhub.model.custom.AuthenticationDTO;
import com.focusedapp.smartstudyhub.model.custom.Result;
import com.focusedapp.smartstudyhub.model.custom.UserDTO;
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
	public ResponseEntity<Result<AuthenticationDTO>> sendOtpToChangePassword() {
		Result<AuthenticationDTO> result = new Result<>();		
		
		User user = getAuthenticatedUser();
		AuthenticationDTO data = userService.resendOtpCodeToUserLocal(user.getEmail());

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
	
	@DeleteMapping("/delete")
	public ResponseEntity<Result<UserDTO>> deleteUserHaveAccount() {
		Result<UserDTO> result = new Result<>();
		User user = getAuthenticatedUser();
		
		User userDeleted = userService.markDeletedByUserId(user.getId());				
		
		try {
			result.setData(new UserDTO(userDeleted));
		} catch (Exception e) {
			throw e;
		}
		
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Check Password Correct
	 * 
	 * @param authenRequest
	 * @return
	 */
	@PostMapping("/check-password-correct")
	public ResponseEntity<Result<AllResponseTypeDTO>> checkPasswordCorrect(@RequestBody AuthenticationDTO authenRequest) {
		Result<AllResponseTypeDTO> result = new Result<>();
		User user = getAuthenticatedUser();
		
		Boolean isValid = userService.checkPasswordCorrect(authenRequest, user);				
		
		AllResponseTypeDTO data = AllResponseTypeDTO.builder()
				.booleanType(isValid)
				.stringType(isValid ? "Password Is Corect!" : "Password Is Incorrect!")
				.build();
		result.setData(data);		
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Change Email User Controller
	 * 
	 * @param authenRequest
	 * @return
	 */
	@PostMapping("/change-email")
	public ResponseEntity<Result<UserDTO>> changeEmailUser(@RequestBody AuthenticationDTO authenRequest) {
		Result<UserDTO> result = new Result<>();
		User user = getAuthenticatedUser();
		
		UserDTO userNew = userService.changeEmailUser(authenRequest, user);	
		
		if (userNew == null) {
			result.getMeta().setStatusCode(StatusCode.CHANGE_EMAIL_NOT_LOCAL.getCode());
			result.getMeta().setMessage(StatusCode.CHANGE_EMAIL_NOT_LOCAL.getMessage());
			return createResponseEntity(result, HttpStatus.FORBIDDEN);
		}
		result.setData(userNew);		
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Get Info of User
	 * 
	 * @return
	 */
	@GetMapping("/get-info")
	public ResponseEntity<Result<UserDTO>> getInformationUserCustomer() {
		
		Result<UserDTO> result = new Result<>();
			
		User user = getAuthenticatedUser();
		
		result.setData(new UserDTO(user));		
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Update Information User
	 * 
	 * @param userInfo
	 * @return
	 */
	@PutMapping("/update-info")
	public ResponseEntity<Result<UserDTO>> updateInformationUserCustomer(@RequestBody UserDTO userInfo) {
		
		Result<UserDTO> result = new Result<>();
		
		if (userInfo == null) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);
		}
		
		User user = getAuthenticatedUser();
		
		UserDTO userNew = userService.updateInformationUserCustomer(userInfo, user);	
		
		if (userNew == null) {
			result.getMeta().setStatusCode(StatusCode.UPDATE_INFO_FAILURE.getCode());
			result.getMeta().setMessage(StatusCode.UPDATE_INFO_FAILURE.getMessage());
			return createResponseEntity(result, HttpStatus.FORBIDDEN);
		}
		result.setData(userNew);		
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
}
