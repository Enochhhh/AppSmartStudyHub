package com.focusedapp.smartstudyhub.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.custom.AllResponseTypeDTO;
import com.focusedapp.smartstudyhub.model.custom.AuthenticationDTO;
import com.focusedapp.smartstudyhub.model.custom.RankUserFocusDTO;
import com.focusedapp.smartstudyhub.model.custom.Result;
import com.focusedapp.smartstudyhub.model.custom.UserDTO;
import com.focusedapp.smartstudyhub.service.UserService;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatus;
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
	
	/**
	 * Delete User Guest
	 * 
	 * @param userId
	 * @return
	 */
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
	
	/**
	 * Rank By Time Focus All Time
	 * 
	 * @return
	 */
	@GetMapping("/rank-by-focus-all-time")
	public ResponseEntity<Result<RankUserFocusDTO>> rankByTimeFocusAllTime(@RequestParam Integer userId, 
			@RequestParam Integer page, @RequestParam Integer size) {

		Result<RankUserFocusDTO> result = new Result<>();

		RankUserFocusDTO rankResponse = userService.rankByTimeFocusAllTime(userId, page, size);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		result.setData(rankResponse);

		return createResponseEntity(result);

	}
	
	/**
	 * Rank By Time Focus Previous Month
	 * 
	 * @return
	 */
	@GetMapping("/rank-by-focus-previous-month")
	public ResponseEntity<Result<RankUserFocusDTO>> rankByTimeFocusPreviousMonth(@RequestParam Integer userId, Pageable pageable) {

		Result<RankUserFocusDTO> result = new Result<>();

		RankUserFocusDTO rankResponse = userService.rankByTimeFocusPreviousMonth(userId, pageable);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		result.setData(rankResponse);

		return createResponseEntity(result);

	}
	
	/**
	 * Update time last use of Guest
	 * 
	 * @param guestId
	 * @return
	 */
	@PutMapping("/update-time-last-use")
	public ResponseEntity<Result<AllResponseTypeDTO>> updateTimeLastUseOfGuest(@RequestParam Integer guestId) {
		Result<AllResponseTypeDTO> result = new Result<>();
		
		Boolean isUpdated = userService.updateTimeLastUseOfGuest(guestId);
		AllResponseTypeDTO data = new AllResponseTypeDTO();
		if (isUpdated) {
			data.setStringType("Updated Success!");
			result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
			result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		} else {
			data.setStringType("Updated Failure! Don't have permission to update this account.");
			result.getMeta().setStatusCode(StatusCode.UNAUTHORIZED.getCode());
			result.getMeta().setMessage(StatusCode.UNAUTHORIZED.getMessage());
		}
		data.setBooleanType(isUpdated);
		result.setData(data);
		
		return createResponseEntity(result);
	}
	
	/**
	 * Get Data of User
	 * 
	 * @return
	 */
	@GetMapping("/get-data/{id}")
	public ResponseEntity<Result<UserDTO>> getDataOfUser(@PathVariable Integer id) {
		Result<UserDTO> result = new Result<>();
		if (id == null || id < 1) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("Data Invalid!");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);
		}
			
		UserDTO user = userService.getById(id);		
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		result.setData(user);
		return createResponseEntity(result);
	}
	
	/**
	 * Get Info User Guest
	 * 
	 * @return
	 */
	@GetMapping("/get-info/{id}")
	public ResponseEntity<Result<UserDTO>> getInfoUserGuest(@PathVariable Integer id) {
		Result<UserDTO> result = new Result<>();
		if (id == null || id < 1) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("Data Invalid!");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);
		}
			
		UserDTO user = userService.getByIdAndStatus(id, EnumStatus.ACTIVE.getValue());		
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		result.setData(user);
		return createResponseEntity(result);
	}

}
