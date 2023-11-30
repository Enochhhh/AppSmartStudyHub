package com.focusedapp.smartstudyhub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.focusedapp.smartstudyhub.model.custom.FolderDTO;
import com.focusedapp.smartstudyhub.model.custom.Result;
import com.focusedapp.smartstudyhub.service.FolderService;
import com.focusedapp.smartstudyhub.util.enumerate.StatusCode;

@RestController
@RequestMapping("/mobile/v1/user/guest/folder")
@CrossOrigin(origins ="*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE, RequestMethod.PUT})
public class FolderController extends BaseController {
	
	@Autowired
	FolderService folderService;
	
	/**
	 * API Create new folder
	 * 
	 * @param folderDTO
	 * @return
	 */
	@PostMapping("/create")
	public ResponseEntity<Result<FolderDTO>> createFolder(@RequestBody FolderDTO folderDTO) {
		Result<FolderDTO> result = new Result<>();
		
		FolderDTO folderCreated = folderService.createFolder(folderDTO);
		
		result.setData(folderCreated);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Get folder of user
	 * 
	 * @param userId
	 * @return
	 */
	@GetMapping("/get-by-user")
	public ResponseEntity<Result<List<FolderDTO>>> getAllFolderOfUser(@RequestParam Integer userId) {
		Result<List<FolderDTO>> result = new Result<>();
		
		if (userId == null || userId < 1) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("userId Invalid!");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);
		}
		
		List<FolderDTO> folderCreated = folderService.getAllFolderOfUser(userId);
		
		result.setData(folderCreated);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Get deleted folder of user
	 * 
	 * @param userId
	 * @return
	 */
	@GetMapping("/get-deleted-by-user")
	public ResponseEntity<Result<List<FolderDTO>>> getAllFolderDeletedOfUser(@RequestParam Integer userId) {
		Result<List<FolderDTO>> result = new Result<>();
		
		if (userId == null || userId < 1) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("userId Invalid!");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);
		}
		
		List<FolderDTO> folderDeleted = folderService.getAllFolderDeletedOfUser(userId);
		
		result.setData(folderDeleted);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Update Folder of user
	 * 
	 * @param folderDTO
	 * @return
	 */
	@PutMapping("/update")
	public ResponseEntity<Result<FolderDTO>> updateFolder(@RequestBody FolderDTO folderDTO) {
		Result<FolderDTO> result = new Result<>();
		
		FolderDTO folderCreated = folderService.updateFolder(folderDTO);
		
		result.setData(folderCreated);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Delete Folder of user
	 * 
	 * @param folderDTO
	 * @return
	 */
	@DeleteMapping("/delete/{folderId}")
	public ResponseEntity<Result<FolderDTO>> deleteFolder(@PathVariable Integer folderId) {
		Result<FolderDTO> result = new Result<>();
		
		FolderDTO folderCreated = folderService.deleteFolder(folderId);
		
		if (folderCreated == null) {
			result.getMeta().setStatusCode(StatusCode.FAIL.getCode());
			result.getMeta().setMessage(StatusCode.FAIL.getMessage());
			return createResponseEntity(result);
		}
		
		result.setData(folderCreated);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
}
