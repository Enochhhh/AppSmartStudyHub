package com.focusedapp.smartstudyhub.controller.admin;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.focusedapp.smartstudyhub.controller.BaseController;
import com.focusedapp.smartstudyhub.model.custom.AllResponseTypeDTO;
import com.focusedapp.smartstudyhub.model.custom.FilesAdminDTO;
import com.focusedapp.smartstudyhub.model.custom.FilesDTO;
import com.focusedapp.smartstudyhub.model.custom.Result;
import com.focusedapp.smartstudyhub.service.FilesService;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatusFile;
import com.focusedapp.smartstudyhub.util.enumerate.EnumTypeFile;
import com.focusedapp.smartstudyhub.util.enumerate.StatusCode;
import com.nimbusds.oauth2.sdk.util.CollectionUtils;

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
		if (CollectionUtils.isEmpty(data)) {
			result.getMeta().setStatusCode(StatusCode.ADMIN_GET_FILES_UPLOADED_OF_USER_FAILURE.getCode());
			result.getMeta().setMessage(StatusCode.ADMIN_GET_FILES_UPLOADED_OF_USER_FAILURE.getMessage());
		}
		result.setData(data);
		return createResponseEntity(result);
	}
	
	/**
	 * Delete Files Uploaded Of User
	 * 
	 * @param id
	 * @return
	 * @throws IOException
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
	
	/**
	 * Get All Files in System uploaded by Admin
	 * 
	 * @param startDate
	 * @param endDate
	 * @param fileType
	 * @param fieldSort
	 * @param sortType
	 * @param page
	 * @param size
	 * @return
	 */
	@GetMapping("/getinsystem")
	public ResponseEntity<Result<List<FilesDTO>>> getAllFilesInSystem(Long startDate, Long endDate, 
			String fileType, String fieldSort, String sortType, Integer page, Integer size) {
		Result<List<FilesDTO>> result = new Result<>();
		
		List<FilesDTO> data = filesService.getFilesInSystem(startDate, endDate, fileType, fieldSort, 
				sortType , page, size);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		if (CollectionUtils.isEmpty(data)) {
			result.getMeta().setStatusCode(StatusCode.ADMIN_GET_FILES_IN_SYSTEM_FAILURE.getCode());
			result.getMeta().setMessage(StatusCode.ADMIN_GET_FILES_IN_SYSTEM_FAILURE.getMessage());
		}
		result.setData(data);
		return createResponseEntity(result);
	}
	
	/**
	 * Get All Files in System uploaded by Admin
	 * 
	 * @param startDate
	 * @param endDate
	 * @param fileType
	 * @param fieldSort
	 * @param sortType
	 * @param page
	 * @param size
	 * @return
	 */
	@GetMapping("/getinsystem/{id}")
	public ResponseEntity<Result<FilesDTO>> getSpecificFilesInSystem(@PathVariable Integer id) {
		Result<FilesDTO> result = new Result<>();
		
		FilesDTO data = filesService.getById(id);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		if (data == null) {
			result.getMeta().setStatusCode(StatusCode.ADMIN_GET_SPECIFIC_FILE_FAILURE.getCode());
			result.getMeta().setMessage(StatusCode.ADMIN_GET_SPECIFIC_FILE_FAILURE.getMessage());
		}
		result.setData(data);
		return createResponseEntity(result);
	}
	
	/**
	 * Upload file Admin
	 * 
	 * @param files
	 * @param type
	 * @param userId
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/upload")
	public ResponseEntity<Result<Object>> uploadFile(@RequestBody FilesAdminDTO filesAdminDTO) throws IOException {

		Result<Object> result = new Result<>();
		
		if ((filesAdminDTO.getType() == null || (!filesAdminDTO.getType().equals(EnumTypeFile.THEME.getValue()) 
				&& !filesAdminDTO.getType().equals(EnumTypeFile.SOUNDCONCENTRATION.getValue())
				&& !filesAdminDTO.getType().equals(EnumTypeFile.SOUNDDONE.getValue()))) 
				|| (filesAdminDTO.getStatus() == null || (!filesAdminDTO.getStatus().equals(EnumStatusFile.DEFAULT.getValue())
						&& !filesAdminDTO.getStatus().equals(EnumStatusFile.PREMIUM.getValue())))) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("Data Invalid!");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);
		}

		Object filesUploaded = filesService.uploadFileAdmin(filesAdminDTO);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		if (filesUploaded == null) {
			result.getMeta().setStatusCode(StatusCode.ADMIN_UPLOAD_FILE_FAILURE.getCode());
			result.getMeta().setMessage(StatusCode.ADMIN_UPLOAD_FILE_FAILURE.getMessage());
		}
		result.setData(filesUploaded);
		return createResponseEntity(result);
	}
	
	/**
	 * Get Themes, Sounds in System uploaded by Admin
	 * 
	 * @param startDate
	 * @param endDate
	 * @param fileType
	 * @param fieldSort
	 * @param sortType
	 * @param page
	 * @param size
	 * @return
	 */
	@GetMapping("/getthemeandsound")
	public ResponseEntity<Result<List<Object>>> getAllFilesInSystem(@RequestParam String fileType, String statusFile, 
			Long startDate, Long endDate, String fieldSort, String sortType, Integer page, Integer size) {
		Result<List<Object>> result = new Result<>();
		
		List<Object> data = filesService.getThemesAndSoundsInSystem(fileType, statusFile, startDate, endDate, fieldSort, 
				sortType , page, size);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		if (CollectionUtils.isEmpty(data)) {
			result.getMeta().setStatusCode(StatusCode.ADMIN_GET_THEMES_AND_SOUNDS_IN_SYSTEM_FAILURE.getCode());
			result.getMeta().setMessage(StatusCode.ADMIN_GET_THEMES_AND_SOUNDS_IN_SYSTEM_FAILURE.getMessage());
		}
		result.setData(data);
		return createResponseEntity(result);
	}
	
	/**
	 * Get Specific Theme, Sound in System uploaded by Admin
	 * 
	 * @param id
	 * @param fileType
	 * @return
	 */
	@GetMapping("/getthemeandsound/{fileType}/{id}")
	public ResponseEntity<Result<Object>> getSpecificFileInSystem(@PathVariable Integer id, 
			@PathVariable String fileType) {
		Result<Object> result = new Result<>();
		
		Object data = filesService.getSpecificFileInSystem(id, fileType);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		if (data == null) {
			result.getMeta().setStatusCode(StatusCode.ADMIN_GET_SPECIFIC_THEME_AND_SOUND_IN_SYSTEM_FAILURE.getCode());
			result.getMeta().setMessage(StatusCode.ADMIN_GET_SPECIFIC_THEME_AND_SOUND_IN_SYSTEM_FAILURE.getMessage());
		}
		result.setData(data);
		return createResponseEntity(result);
	}
	
	/**
	 * Delete Specific Theme, Sound in System uploaded by Admin
	 * 
	 * @param id
	 * @param fileType
	 * @return
	 */
	@DeleteMapping("/deletethemeandsound/{fileType}/{id}")
	public ResponseEntity<Result<AllResponseTypeDTO>> deleteSpecificFileInSystem(@PathVariable Integer id, 
			@PathVariable String fileType) throws IOException {
		Result<AllResponseTypeDTO> result = new Result<>();
		
		AllResponseTypeDTO data = filesService.deleteSpecificFileInSystem(id, fileType);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		if (data == null) {
			result.getMeta().setStatusCode(StatusCode.ADMIN_DELETE_THEME_AND_SOUND_IN_SYSTEM_FAILURE.getCode());
			result.getMeta().setMessage(StatusCode.ADMIN_DELETE_THEME_AND_SOUND_IN_SYSTEM_FAILURE.getMessage());
		}
		result.setData(data);
		return createResponseEntity(result);
	}
	
	/**
	 * Delete Specific Theme, Sound in System uploaded by Admin
	 * 
	 * @param id
	 * @param fileType
	 * @return
	 */
	@GetMapping("/searchthemeandsound/{fileType}")
	public ResponseEntity<Result<Object>> searchFileInSystem(@RequestParam String key, 
			@PathVariable String fileType) throws IOException {
		Result<Object> result = new Result<>();
		
		Object data = filesService.searchFileInSystem(key, fileType);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		if (data == null) {
			result.getMeta().setStatusCode(StatusCode.ADMIN_SEARCH_THEME_AND_SOUND_IN_SYSTEM_FAILURE.getCode());
			result.getMeta().setMessage(StatusCode.ADMIN_SEARCH_THEME_AND_SOUND_IN_SYSTEM_FAILURE.getMessage());
		}
		result.setData(data);
		return createResponseEntity(result);
	}
	
}
