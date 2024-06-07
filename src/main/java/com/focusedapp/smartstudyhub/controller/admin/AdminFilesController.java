package com.focusedapp.smartstudyhub.controller.admin;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.focusedapp.smartstudyhub.controller.BaseController;
import com.focusedapp.smartstudyhub.model.custom.AllResponseTypeDTO;
import com.focusedapp.smartstudyhub.model.custom.FilesDTO;
import com.focusedapp.smartstudyhub.model.custom.Result;
import com.focusedapp.smartstudyhub.service.FilesService;
import com.focusedapp.smartstudyhub.util.enumerate.StatusCode;

@RestController
@RequestMapping("/mobile/v1/admin/files")
@CrossOrigin(origins ="*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE, RequestMethod.PUT})
public class AdminFilesController extends BaseController {
	
	@Autowired FilesService filesService;

	/**
	 * Get All Files Uploaded Of User
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@GetMapping("/getofuser")
	public ResponseEntity<Result<List<FilesDTO>>> getAllFilesUploadedOfUser(Integer userId, Long startDate, Long endDate, 
			String fileType, String fieldSort, String sortType, Integer page, Integer size) {
		Result<List<FilesDTO>> result = new Result<>();
		
		List<FilesDTO> data = filesService.getFilesUploadedOfUser(userId, startDate, endDate, fileType, fieldSort, 
				sortType , page, size);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		result.setData(data);
		return createResponseEntity(result);
	}
	
	/**
	 * Delete Files Uploaded Of User
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Result<AllResponseTypeDTO>> deleteFileUploadedOfUser(@PathVariable Integer id) throws IOException {
		Result<AllResponseTypeDTO> result = new Result<>();
		
		Boolean isDeleted = filesService.deleteFileUploadedOfUser(id);
		AllResponseTypeDTO data = new AllResponseTypeDTO();
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		data.setStringType("Delete file uploaded of User success!");
		if (!isDeleted) {
			result.getMeta().setStatusCode(StatusCode.DELETE_FILE_UPLOADED_OF_USER_FAILURE.getCode());
			result.getMeta().setMessage(StatusCode.DELETE_FILE_UPLOADED_OF_USER_FAILURE.getMessage());
			data.setStringType("Delete file uploaded of User failure. Not found the file to delete or system error!");
		}
		data.setBooleanType(isDeleted);
		result.setData(data);
		return createResponseEntity(result);
	}
	
}
