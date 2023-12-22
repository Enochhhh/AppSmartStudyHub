package com.focusedapp.smartstudyhub.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.focusedapp.smartstudyhub.controller.BaseController;
import com.focusedapp.smartstudyhub.model.custom.Result;
import com.focusedapp.smartstudyhub.model.custom.UserAdminCreatedDTO;
import com.focusedapp.smartstudyhub.service.UserService;
import com.focusedapp.smartstudyhub.util.enumerate.StatusCode;

@RestController
@RequestMapping("/mobile/v1/admin/user")
@CrossOrigin(origins ="*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE, RequestMethod.PUT})
public class AdminUserController extends BaseController {

	@Autowired
	private UserService userService;
	
	/**
	 * Create User By Admin
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping("/create")
	public ResponseEntity<Result<UserAdminCreatedDTO>> createUser(@RequestBody UserAdminCreatedDTO request) {
		Result<UserAdminCreatedDTO> result = new Result<>();
		if (request == null) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);
		}
		UserAdminCreatedDTO data = userService.createUser(request);

		if (data == null) {
			result.getMeta().setStatusCode(StatusCode.CREATE_USER_FAILURE.getCode());
			result.getMeta().setMessage(StatusCode.CREATE_USER_FAILURE.getMessage());
		} else {
			result.setData(data);
			result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
			result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		}

		return createResponseEntity(result);
	}
	
	@PutMapping("/update")
	public ResponseEntity<Result<UserAdminCreatedDTO>> updateUser(@RequestBody UserAdminCreatedDTO request) {
		Result<UserAdminCreatedDTO> result = new Result<>();
		if (request == null) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);
		}
		UserAdminCreatedDTO data = userService.updateUser(request);
	
		result.setData(data);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());

		return createResponseEntity(result);
	}
	
}
