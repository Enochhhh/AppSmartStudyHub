package com.focusedapp.smartstudyhub.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.focusedapp.smartstudyhub.model.Files;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.custom.AllResponseTypeDTO;
import com.focusedapp.smartstudyhub.model.custom.FilesDTO;
import com.focusedapp.smartstudyhub.model.custom.Result;
import com.focusedapp.smartstudyhub.service.CloudinaryService;
import com.focusedapp.smartstudyhub.service.FilesService;
import com.focusedapp.smartstudyhub.util.enumerate.StatusCode;

@RestController
@RequestMapping("/mobile/v1/user")
@CrossOrigin(origins = "*", methods = { RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE,
		RequestMethod.PUT })
public class FilesController extends BaseController {

	@Autowired
	CloudinaryService cloudinaryService;
	
	@Autowired
	FilesService filesService;

	/**
	 * Upload avatar user
	 * 
	 * @param files
	 * @param type
	 * @param userId
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/customer/files/upload")
	public ResponseEntity<Result<AllResponseTypeDTO>> uploadFile(@RequestParam("files") MultipartFile files,
			@RequestParam String type) throws IOException {

		Result<AllResponseTypeDTO> result = new Result<>();

		User user = getAuthenticatedUser(); 
		String filesUploaded = cloudinaryService.uploadFile(files, type, user);
		AllResponseTypeDTO allResponseTypeDTO = new AllResponseTypeDTO();
		allResponseTypeDTO.setStringType(filesUploaded);

		result.setData(allResponseTypeDTO);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}

	/**
	 * Delete File
	 * 
	 * @param fileDelete
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/customer/files/delete")
	public ResponseEntity<Result<FilesDTO>> deleteFile(@RequestBody Files fileDelete) throws IOException {

		Result<FilesDTO> result = new Result<>();

		FilesDTO filesDeleted = cloudinaryService.deleteFileInCloudinary(fileDelete.getPublicId());

		result.setData(filesDeleted);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Get Files uploaded of User
	 * 
	 * @param type
	 * @return
	 */
	@GetMapping("/customer/files/get-files-uploaded-user")
	public ResponseEntity<Result<List<FilesDTO>>> getImageUploadedOfUser(@RequestParam String type) {

		Result<List<FilesDTO>> result = new Result<>();

		User user = getAuthenticatedUser();
		List<FilesDTO> filesUploaded = filesService.getFilesUploadedOfUser(user, type);

		result.setData(filesUploaded);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
}
