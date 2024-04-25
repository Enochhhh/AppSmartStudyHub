package com.focusedapp.smartstudyhub.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.focusedapp.smartstudyhub.model.custom.UserDTO;
import com.focusedapp.smartstudyhub.service.UserService;
import com.focusedapp.smartstudyhub.util.enumerate.EnumRole;
import com.focusedapp.smartstudyhub.util.enumerate.StatusCode;

import net.minidev.json.JSONObject;

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
	public ResponseEntity<Result<UserDTO>> createUser(@RequestBody UserDTO request) {
		Result<UserDTO> result = new Result<>();
		if (request == null) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);
		}
		UserDTO data = userService.createUser(request);

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
	public ResponseEntity<Result<UserDTO>> updateUser(@RequestBody UserDTO request) {
		Result<UserDTO> result = new Result<>();
		User admin = getAuthenticatedUser();
		if (request == null || admin.getId().equals(request.getId())) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("Data Invalid!");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);
		}
		UserDTO data = userService.updateUser(request);
	
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
	public ResponseEntity<Result<UserDTO>> markStatus(@RequestBody UserDTO request) {
		Result<UserDTO> result = new Result<>();
		User admin = getAuthenticatedUser();
		if (request == null || admin.getId().equals(request.getId())) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("Data Invalid!");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);
		}
		UserDTO data = userService.markStatus(request);
	
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
	public ResponseEntity<Result<List<UserDTO>>> getAll(Pageable pageable) {
		Result<List<UserDTO>> result = new Result<>();
		
		List<UserDTO> data = userService.getAll(pageable);
		Integer totalUsers = userService.countByRoleNot(EnumRole.ADMIN.getValue());
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("totalUsers", totalUsers);
		result.setExtendProp(jsonObject);
	
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
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Result<UserDTO>> delete(@PathVariable Integer id) {
		Result<UserDTO> result = new Result<>();
		User admin = getAuthenticatedUser();
		if (id == null || id < 1 || admin.getId().equals(id)) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("Data Invalid!");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);
		}
		UserDTO data = userService.adminDeleteById(id);
	
		result.setData(data);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());

		return createResponseEntity(result);
	}
	
}
