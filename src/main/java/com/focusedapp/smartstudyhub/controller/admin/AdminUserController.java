package com.focusedapp.smartstudyhub.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.focusedapp.smartstudyhub.controller.BaseController;
import com.focusedapp.smartstudyhub.model.User;
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
	
	/**
	 * Update User
	 * 
	 * @param request
	 * @return
	 */
	@PutMapping("/update")
	public ResponseEntity<Result<UserAdminCreatedDTO>> updateUser(@RequestBody UserAdminCreatedDTO request) {
		Result<UserAdminCreatedDTO> result = new Result<>();
		User admin = getAuthenticatedUser();
		if (request == null || admin.getId().equals(request.getId())) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("Data Invalid!");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);
		}
		UserAdminCreatedDTO data = userService.updateUser(request);
	
		result.setData(data);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());

		return createResponseEntity(result);
	}
	
	/**
	 * Mark Status
	 * 
	 * @param request
	 * @return
	 */
	@PutMapping("/markstatus")
	public ResponseEntity<Result<UserAdminCreatedDTO>> markStatus(@RequestBody UserAdminCreatedDTO request) {
		Result<UserAdminCreatedDTO> result = new Result<>();
		User admin = getAuthenticatedUser();
		if (request == null || admin.getId().equals(request.getId())) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("Data Invalid!");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);
		}
		UserAdminCreatedDTO data = userService.markStatus(request);
	
		result.setData(data);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());

		return createResponseEntity(result);
	}
	
	/**
	 * Get All Users
	 * 
	 * @return
	 */
	@GetMapping("/getall")
	public ResponseEntity<Result<List<UserAdminCreatedDTO>>> getAll(Pageable pageable) {
		Result<List<UserAdminCreatedDTO>> result = new Result<>();
		
		List<UserAdminCreatedDTO> data = userService.getAll(pageable);
	
		result.setData(data);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());

		return createResponseEntity(result);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED, noRollbackFor=Exception.class)
	@PutMapping("/delete/{id}")
	public ResponseEntity<Result<UserAdminCreatedDTO>> delete(@PathVariable Integer id) {
		Result<UserAdminCreatedDTO> result = new Result<>();
		User admin = getAuthenticatedUser();
		if (id == null || id < 1 || admin.getId().equals(id)) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("Data Invalid!");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);
		}
		// UserAdminCreatedDTO data = userService.delete(id);
	
		// result.setData(data);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());

		return createResponseEntity(result);
	}
	
}
