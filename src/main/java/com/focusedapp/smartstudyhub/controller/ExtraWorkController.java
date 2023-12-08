package com.focusedapp.smartstudyhub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.focusedapp.smartstudyhub.model.custom.ExtraWorkDTO;
import com.focusedapp.smartstudyhub.model.custom.Result;
import com.focusedapp.smartstudyhub.service.ExtraWorkService;
import com.focusedapp.smartstudyhub.util.enumerate.StatusCode;

@RestController
@RequestMapping("/mobile/v1/user/guest/extrawork")
@CrossOrigin(origins ="*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE, RequestMethod.PUT})
public class ExtraWorkController extends BaseController {

	@Autowired
	ExtraWorkService extraWorkService;
	
	/**
	 * Create Extra Work
	 * 
	 * @param extraWorkDTO
	 * @return
	 */
	@PostMapping("/create")
	public ResponseEntity<Result<ExtraWorkDTO>> createExtraWork(@RequestBody ExtraWorkDTO extraWorkDTO) {
		Result<ExtraWorkDTO> result = new Result<ExtraWorkDTO>();
		
		if (extraWorkDTO == null || extraWorkDTO.getWorkId() == null || extraWorkDTO.getWorkId() < 1) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("Data Invalid!");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);							
		}
		
		ExtraWorkDTO extraWorkCreated = extraWorkService.createExtraWork(extraWorkDTO);
		
		result.setData(extraWorkCreated);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Update Extra Work Controller
	 * 
	 * @param extraWorkDTO
	 * @return
	 */
	@PutMapping("/update")
	public ResponseEntity<Result<ExtraWorkDTO>> updateExtraWork(@RequestBody ExtraWorkDTO extraWorkDTO) {
		Result<ExtraWorkDTO> result = new Result<ExtraWorkDTO>();
		
		if (extraWorkDTO == null || extraWorkDTO.getId() == null || extraWorkDTO.getId() < 1) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("Data Invalid!");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);							
		}
		
		ExtraWorkDTO extraWorkUpdated = extraWorkService.updateExtraWork(extraWorkDTO);
		
		result.setData(extraWorkUpdated);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Mark Completed Extra Work
	 * 
	 * @param extraWorkId
	 * @return
	 */
	@PutMapping("/mark-completed")
	public ResponseEntity<Result<ExtraWorkDTO>> markCompleted(@RequestParam Integer extraWorkId) {
		
		Result<ExtraWorkDTO> result = new Result<>();
		
		if (extraWorkId == null || extraWorkId < 1) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("ExtraWorkId Invalid!");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);							
		}
		
		ExtraWorkDTO extraWork = extraWorkService.markCompleted(extraWorkId);
		
		result.setData(extraWork);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
}
