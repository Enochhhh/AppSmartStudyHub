package com.focusedapp.smartstudyhub.controller.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.focusedapp.smartstudyhub.controller.BaseController;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.custom.AllResponseTypeDTO;
import com.focusedapp.smartstudyhub.model.custom.DeviceDTO;
import com.focusedapp.smartstudyhub.model.custom.Result;
import com.focusedapp.smartstudyhub.service.DeviceService;
import com.focusedapp.smartstudyhub.util.enumerate.StatusCode;
import com.nimbusds.oauth2.sdk.util.CollectionUtils;

@RestController
@RequestMapping("/mobile/v1/user/customer/device")
@CrossOrigin(origins ="*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE, RequestMethod.PUT})
public class DeviceController extends BaseController {
	
	@Autowired DeviceService deviceService;
	
	/**
	 * Create or update device
	 * 
	 * @param dataCreateUpdate
	 * @return
	 */
	@PutMapping("/create-update")
	ResponseEntity<Result<DeviceDTO>> createUpdateDevice(@RequestBody DeviceDTO dataCreateUpdate) {
		Result<DeviceDTO> result = new Result<>();		
		
		User user = getAuthenticatedUser();
		DeviceDTO data = deviceService.createUpdateDevice(dataCreateUpdate, user);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		if (data == null) {
			result.getMeta().setStatusCode(StatusCode.CREATE_OR_UPDATE_DEVICE_FAILURE.getCode());
			result.getMeta().setMessage(StatusCode.CREATE_OR_UPDATE_DEVICE_FAILURE.getMessage());
		}
		result.setData(data);
		return createResponseEntity(result);
	}
	
	/**
	 * Refresh Registration Token Of Device
	 * 
	 * @param deviceDTO
	 * @return
	 */
	@PutMapping("/refresh-registration-token")
	ResponseEntity<Result<DeviceDTO>> refreshRegistrationTokenOfDevice(@RequestBody DeviceDTO deviceDTO) {
		Result<DeviceDTO> result = new Result<>();		
		
		DeviceDTO data = deviceService.refreshRegistrationTokenOfDevice(deviceDTO);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		if (data == null) {
			result.getMeta().setStatusCode(StatusCode.REFRESH_REGISTRATION_TOKEN_DEVICE_FAILURE.getCode());
			result.getMeta().setMessage(StatusCode.REFRESH_REGISTRATION_TOKEN_DEVICE_FAILURE.getMessage());
			result.getMeta().setDetails("Not Found Any Device To Refresh Registration Token!");
		}
		result.setData(data);
		return createResponseEntity(result);
	}
	
	/**
	 * Logout devices selected
	 * 
	 * @param deviceDTO
	 * @return
	 */
	@PutMapping("/logout")
	ResponseEntity<Result<List<DeviceDTO>>> logOutDeviceSelected(@RequestBody List<DeviceDTO> devicesSelected) {
		Result<List<DeviceDTO>> result = new Result<>();		
		if (CollectionUtils.isEmpty(devicesSelected)) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("Data Invalid!");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);							
		}
		
		User user = getAuthenticatedUser();
		List<DeviceDTO> data = deviceService.logOutDevicesSelected(devicesSelected, user);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		if (data == null) {
			result.getMeta().setStatusCode(StatusCode.LOGOUT_DEVICES_FAILURE.getCode());
			result.getMeta().setMessage(StatusCode.LOGOUT_DEVICES_FAILURE.getMessage());
			result.getMeta().setDetails("Not Found Any Device To Logout!");
		}
		result.setData(data);
		return createResponseEntity(result);
	}
	
	/**
	 * Delete devices selected
	 * 
	 * @param deviceDTO
	 * @return
	 */
	@DeleteMapping("/delete")
	ResponseEntity<Result<AllResponseTypeDTO>> deleteDeviceSelected(@RequestBody List<DeviceDTO> devicesSelected) {
		Result<AllResponseTypeDTO> result = new Result<>();		
		if (CollectionUtils.isEmpty(devicesSelected)) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("Data Invalid!");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);							
		}
		
		User user = getAuthenticatedUser();
		Boolean isDeleted = deviceService.deleteDevicesSelected(devicesSelected, user);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		AllResponseTypeDTO data = new AllResponseTypeDTO();
		data.setBooleanType(isDeleted);
		data.setStringType("Deleted Devices Seletected Success!");
		if (!isDeleted) {
			result.getMeta().setStatusCode(StatusCode.DELETE_DEVICES_FAILURE.getCode());
			result.getMeta().setMessage(StatusCode.DELETE_DEVICES_FAILURE.getMessage());
			data.setStringType("Not Found Any Device To Delete!");
		}
		result.setData(data);
		return createResponseEntity(result);
	}
	
	/**
	 * Delete devices selected
	 * 
	 * @param deviceDTO
	 * @return
	 */
	@GetMapping("/get")
	ResponseEntity<Result<List<DeviceDTO>>> getDevicesOfUser() {
		Result<List<DeviceDTO>> result = new Result<>();		
		
		User user = getAuthenticatedUser();
		List<DeviceDTO> data = deviceService.getDevicesOfUser(user);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		if (CollectionUtils.isEmpty(data)) {
			result.getMeta().setStatusCode(StatusCode.GET_DEVICES_FAILURE.getCode());
			result.getMeta().setMessage(StatusCode.GET_DEVICES_FAILURE.getMessage());
			result.getMeta().setDetails("Not found any devices of User!");
		}
		result.setData(data);
		return createResponseEntity(result);
	}
}
