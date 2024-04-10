package com.focusedapp.smartstudyhub.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.focusedapp.smartstudyhub.model.custom.AllResponseTypeDTO;
import com.focusedapp.smartstudyhub.model.custom.Result;
import com.focusedapp.smartstudyhub.model.custom.WorkDTO;
import com.focusedapp.smartstudyhub.model.custom.WorkResponseDTO;
import com.focusedapp.smartstudyhub.model.custom.WorkSortedResponseDTO;
import com.focusedapp.smartstudyhub.service.WorkService;
import com.focusedapp.smartstudyhub.util.enumerate.StatusCode;

@RestController
@RequestMapping("/mobile/v1/user/guest/work")
@CrossOrigin(origins ="*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE, RequestMethod.PUT})
public class WorkController extends BaseController {

	@Autowired
	WorkService workService;
	
	/**
	 * Create Work
	 * 
	 * @param workDTO
	 * @return
	 */
	@PostMapping("/create")
	public ResponseEntity<Result<WorkDTO>> createWork(@RequestBody WorkDTO workDTO) {
		Result<WorkDTO> result = new Result<>();
		
		if (workDTO == null) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("Data Invalid!");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);							
		}
		
		WorkDTO workCreated = workService.createWork(workDTO);
		
		result.setData(workCreated);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Update Work
	 * 
	 * @param workDTO
	 * @return
	 */
	@PutMapping("/update")
	public ResponseEntity<Result<WorkDTO>> updateWork(@RequestBody WorkDTO workDTO) {
		Result<WorkDTO> result = new Result<>();
		
		if (workDTO == null || workDTO.getId() == null || workDTO.getId() < 1) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("Data Invalid!");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);							
		}
		
		WorkDTO workUpdated = workService.updateWork(workDTO);
		
		result.setData(workUpdated);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Delete Work
	 * 
	 * @param workId
	 * @return
	 */
	@DeleteMapping("/delete/{workId}")
	public ResponseEntity<Result<WorkDTO>> markDeletedWork(@PathVariable Integer workId) {
		Result<WorkDTO> result = new Result<>();
		
		if (workId == null || workId < 1) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("Data Invalid!");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);							
		}
		
		WorkDTO workDeleted = workService.markDeletedWork(workId);
		
		if (workDeleted == null) {
			result.getMeta().setStatusCode(StatusCode.MARK_DELETED_WORK_FAILURE.getCode());
			result.getMeta().setMessage(StatusCode.MARK_DELETED_WORK_FAILURE.getMessage());
			return createResponseEntity(result, HttpStatus.FORBIDDEN);
		}
		
		result.setData(workDeleted);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Delete Completely Work
	 * 
	 * @param workId
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED, noRollbackFor=Exception.class)
	@DeleteMapping("/delete-completely/{workId}")
	public ResponseEntity<Result<WorkDTO>> deletedWork(@PathVariable Integer workId) {
		Result<WorkDTO> result = new Result<>();
		
		if (workId == null || workId < 1) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("Data Invalid!");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);							
		}
		
		WorkDTO workDeleted = workService.deleteById(workId);
		
		result.setData(workDeleted);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Get Works In Project
	 * 
	 * @param projectId
	 * @return
	 */
	@GetMapping("/get-in-project")
	public ResponseEntity<Result<List<WorkDTO>>> getWorksByProjectAndStatus(@RequestParam(required = false) Integer projectId, @RequestParam String status
			, @RequestParam Integer userId) {
		Result<List<WorkDTO>> result = new Result<>();
		
		List<WorkDTO> works = workService.getWorksByProjectAndStatus(projectId, userId, status);
		
		result.setData(works);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Get Detail Work
	 * 
	 * @param projectId
	 * @return
	 */
	@GetMapping("/get-detail")
	public ResponseEntity<Result<WorkDTO>> getDetailWork(@RequestParam Integer workId) {
		Result<WorkDTO> result = new Result<>();
		
		if (workId == null || workId < 1) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("Data Invalid!");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);							
		}
		
		WorkDTO work = workService.getDetailWork(workId);
		
		result.setData(work);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Mark Completed Work
	 * 
	 * @param workId
	 * @return
	 */
	@PutMapping("/mark-completed")
	public ResponseEntity<Result<WorkDTO>> markCompleted(@RequestParam Integer workId) {
		
		Result<WorkDTO> result = new Result<>();
		
		if (workId == null || workId < 1) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("WorkId Invalid!");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);							
		}
		
		WorkDTO work = workService.markCompleted(workId);
		
		if (work == null) {
			result.getMeta().setStatusCode(StatusCode.MARK_COMPLETED_WORK_FAILURE.getCode());
			result.getMeta().setMessage(StatusCode.MARK_COMPLETED_WORK_FAILURE.getMessage());
			return createResponseEntity(result, HttpStatus.FORBIDDEN);
		}
		
		result.setData(work);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Recover Work
	 * 
	 * @param extraWorkId
	 * @return
	 */
	@PutMapping("/recover/{workId}")
	public ResponseEntity<Result<WorkDTO>> recover(@PathVariable Integer workId) {
		Result<WorkDTO> result = new Result<>();
		
		if (workId == null || workId < 1) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("Work Id Invalid!");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);
		}
		
		WorkDTO workRecovered = workService.recover(workId);
		
		if (workRecovered == null) {
			result.getMeta().setStatusCode(StatusCode.RECOVER_WORK_FAILURE.getCode());
			result.getMeta().setMessage(StatusCode.RECOVER_WORK_FAILURE.getMessage());
			return createResponseEntity(result, HttpStatus.FORBIDDEN);
		}
		
		result.setData(workRecovered);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Find Work by name
	 * 
	 * @param projectId
	 * @return
	 */
	@PostMapping("/search-by-name")
	public ResponseEntity<Result<List<WorkDTO>>> findByName(@RequestBody AllResponseTypeDTO req) {
		Result<List<WorkDTO>> result = new Result<>();
		
		if (req == null || req.getStringType() == null) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("Data Invalid!");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);							
		}
		
		List<WorkDTO> works = workService.searchByName(req.getStringType(), req.getIntegerType());
		
		result.setData(works);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Get Works by type
	 * 
	 * @param type
	 * @param userId
	 * @return
	 */
	@GetMapping("/get-by-type")
	public ResponseEntity<Result<WorkResponseDTO>> getWorkByType(@RequestParam String type, @RequestParam Integer userId) {
		Result<WorkResponseDTO> result = new Result<>();
		
		if (StringUtils.isEmpty(type)) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("Data Invalid!");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);							
		}
		
		WorkResponseDTO works = workService.getWorkByType(type, userId);
		
		result.setData(works);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Get Works by Priority
	 * 
	 * @param priority
	 * @param userId
	 * @return
	 */
	@GetMapping("/get-by-priority")
	public ResponseEntity<Result<WorkResponseDTO>> getByPriority(@RequestParam String priority, @RequestParam Integer userId) {
		Result<WorkResponseDTO> result = new Result<>();
		
		if (StringUtils.isEmpty(priority)) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("Data Invalid!");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);							
		}
		
		WorkResponseDTO works = workService.getByPriority(priority, userId);
		
		result.setData(works);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
		
	}
	
	/**
	 * Get Work Completed Controller
	 * 
	 * @param userId
	 * @return
	 */
	@GetMapping("/get-work-completed")
	public ResponseEntity<Result<List<WorkDTO>>> getWorkCompletedOfUser(@RequestParam Integer userId) {
		
		Result<List<WorkDTO>> result = new Result<>();
		
		if (userId == null || userId < 1) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("User Id Invalid!");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);
		}
		
		List<WorkDTO> workCompleted = workService.getWorkCompletedOfUser(userId);
		
		result.setData(workCompleted);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	@PostMapping("/sort-work")
	public ResponseEntity<Result<List<WorkSortedResponseDTO> >> sortWork(@RequestParam String type, @RequestBody List<WorkDTO> works) {
		
		Result<List<WorkSortedResponseDTO> > result = new Result<>();
		
		if (StringUtils.isBlank(type)) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("Type Invalid!");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);
		}
		
		List<WorkSortedResponseDTO>  worksSorted = workService.sortWork(type, works);
		
		result.setData(worksSorted);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Get Work Completed Controller
	 * 
	 * @param userId
	 * @return
	 */
	@GetMapping("/get-work-deleted")
	public ResponseEntity<Result<List<WorkDTO>>> getWorkDeletedOfUser(@RequestParam Integer userId) {
		
		Result<List<WorkDTO>> result = new Result<>();
		
		if (userId == null || userId < 1) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("User Id Invalid!");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);
		}
		
		List<WorkDTO> workDeleted = workService.getWorkDeletedOfUser(userId);
		
		result.setData(workDeleted);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
}
