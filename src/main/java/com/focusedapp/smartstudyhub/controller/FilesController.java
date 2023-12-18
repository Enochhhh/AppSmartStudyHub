package com.focusedapp.smartstudyhub.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.focusedapp.smartstudyhub.model.custom.AllResponseTypeDTO;
import com.focusedapp.smartstudyhub.model.custom.Result;
import com.focusedapp.smartstudyhub.service.CloudinaryService;
import com.focusedapp.smartstudyhub.util.enumerate.StatusCode;

@RestController
@RequestMapping("/mobile/v1/user/guest/files")
@CrossOrigin(origins ="*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE, RequestMethod.PUT})
public class FilesController extends BaseController {
	
	@Autowired
	CloudinaryService cloudinaryService;

	/**
	 * Upload avatar user
	 * 
	 * @param files
	 * @param type
	 * @param userId
	 * @return
	 * @throws IOException
	 */
	 @PostMapping("/upload")
    public ResponseEntity<Result<AllResponseTypeDTO>> uploadImageUser(@RequestParam("files") MultipartFile files, 
    		@RequestParam String type, @RequestParam Integer userId) throws IOException{
        
		Result<AllResponseTypeDTO> result = new Result<>();		
			
		String filesUploaded = cloudinaryService.uploadFile(files, type, userId);
		AllResponseTypeDTO allResponseTypeDTO = new AllResponseTypeDTO();
		allResponseTypeDTO.setStringType(filesUploaded);
		
		result.setData(allResponseTypeDTO);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
}
