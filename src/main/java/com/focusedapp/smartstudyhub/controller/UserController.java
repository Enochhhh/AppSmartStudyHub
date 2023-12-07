package com.focusedapp.smartstudyhub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.custom.AuthenticationDTO;
import com.focusedapp.smartstudyhub.model.custom.Result;
import com.focusedapp.smartstudyhub.model.custom.UserDTO;
import com.focusedapp.smartstudyhub.service.UserService;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatus;import com.focusedapp.smartstudyhub.util.enumerate.EnumStatusWork;
import com.focusedapp.smartstudyhub.util.enumerate.StatusCode;

@RestController
@RequestMapping("/mobile/v1/user/guest")
@CrossOrigin(origins ="*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE, RequestMethod.PUT})
public class UserController extends BaseController {

	@Autowired
	UserService userService;
	
	/**
	 * 
	 * Create Guest User
	 * 
	 * @param userDTO
	 * @return
	 */
	@GetMapping("/create")
	public ResponseEntity<Result<UserDTO>> createGuestUser() {

		Result<UserDTO> result = new Result<UserDTO>();

		UserDTO userResponse = userService.createGuestUser();
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		result.setData(userResponse);

		return createResponseEntity(result);

	}
	
	/**
	 * Recover user's account if it was deleted
	 * 
	 * @return
	 */
	@PutMapping("/recover-account")
	public ResponseEntity<Result<AuthenticationDTO>> recoverAccount(@RequestBody AuthenticationDTO request) {
		
		Result<AuthenticationDTO> result = new Result<>();
				
		AuthenticationDTO userAfterUpdate = userService.changeStatus(request.getId(), EnumStatus.ACTIVE.getValue());
		if (userAfterUpdate == null) {
			result.getMeta().setStatusCode(StatusCode.FAIL.getCode());
			result.getMeta().setMessage(StatusCode.FAIL.getMessage());
			result.getMeta().setDetails("Update Status User Failed");
			return createResponseEntity(result, HttpStatus.METHOD_NOT_ALLOWED);
		}
		result.setData(userAfterUpdate);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	@DeleteMapping("/delete/{userId}")
	public ResponseEntity<Result<UserDTO>> deleteUserGuest(@PathVariable Integer userId) {
		Result<UserDTO> result = new Result<>();
		
		User userDeleted = userService.deleteById(userId);				
		
		if (userDeleted == null) {
			result.getMeta().setStatusCode(StatusCode.AUTHENTICATION_REQUIRE.getCode());
			result.getMeta().setMessage(StatusCode.AUTHENTICATION_REQUIRE.getMessage());
			return createResponseEntity(result, HttpStatus.UNAUTHORIZED);
		}
		
		result.setData(new UserDTO(userDeleted));
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}

}
