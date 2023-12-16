package com.focusedapp.smartstudyhub.controller;

import java.util.List;
import java.util.stream.Collectors;

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

import com.focusedapp.smartstudyhub.model.Tag;
import com.focusedapp.smartstudyhub.model.custom.AllResponseTypeDTO;
import com.focusedapp.smartstudyhub.model.custom.Result;
import com.focusedapp.smartstudyhub.model.custom.TagDTO;
import com.focusedapp.smartstudyhub.service.TagService;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatus;
import com.focusedapp.smartstudyhub.util.enumerate.StatusCode;
import com.nimbusds.oauth2.sdk.util.CollectionUtils;

@RestController
@RequestMapping("/mobile/v1/user/guest/tag")
@CrossOrigin(origins ="*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE, RequestMethod.PUT})
public class TagController extends BaseController {

	@Autowired
	TagService tagService;
	
	@PostMapping("/create")
	public ResponseEntity<Result<TagDTO>> create(@RequestBody TagDTO tagRequest) {
		
		Result<TagDTO> result = new Result<>();
		
		if (tagRequest == null || tagRequest.getUserId() == null || tagRequest.getUserId() < 1) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("Data Invalid!");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);
		}
		
		TagDTO tagCreated = tagService.create(tagRequest);
		
		result.setData(tagCreated);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Update Tag Controller
	 * 
	 * @param tagRequest
	 * @return
	 */
	@PutMapping("/update")
	public ResponseEntity<Result<TagDTO>> update(@RequestBody TagDTO tagRequest) {
		
		Result<TagDTO> result = new Result<>();
		
		if (tagRequest == null || tagRequest.getId() == null || tagRequest.getId() < 1) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("Data Invalid!");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);
		}
		
		TagDTO tagUpdated = tagService.update(tagRequest);
		
		result.setData(tagUpdated);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	@Transactional(propagation=Propagation.REQUIRED, noRollbackFor=Exception.class)
	@DeleteMapping("/delete/{tagId}")
	public ResponseEntity<Result<TagDTO>> delete(@PathVariable Integer tagId) {
		
		Result<TagDTO> result = new Result<>();
		
		if (tagId == null || tagId < 1) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("Tag Id Invalid!");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);
		}
		
		TagDTO tagDeleted = tagService.delete(tagId);
		
		if (tagDeleted == null) {
			result.getMeta().setStatusCode(StatusCode.DELETE_TAG_FAILURE.getCode());
			result.getMeta().setMessage(StatusCode.DELETE_TAG_FAILURE.getMessage());
			return createResponseEntity(result);
		}
		
		result.setData(tagDeleted);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Get Detail Tag
	 * 
	 * @param tagId
	 * @return
	 */
	@GetMapping("/detail")
	public ResponseEntity<Result<TagDTO>> getDetailTag(@RequestParam Integer tagId) {
		Result<TagDTO> result = new Result<>();
		
		Tag tag = tagService.findById(tagId);
		
		result.setData(new TagDTO(tag));
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Get all Tag active of User
	 * 
	 * @param tagId
	 * @return
	 */
	@GetMapping("/get-active")
	public ResponseEntity<Result<List<TagDTO>>> getTagsActive(@RequestParam Integer userId) {
		Result<List<TagDTO>> result = new Result<>();
		
		List<Tag> tags = tagService.findByUserIdAndStatus(userId, EnumStatus.ACTIVE.getValue());
		
		if (CollectionUtils.isEmpty(tags)) {
			result.getMeta().setStatusCode(StatusCode.FAIL.getCode());
			result.getMeta().setMessage(StatusCode.FAIL.getMessage());
			return createResponseEntity(result, HttpStatus.FORBIDDEN);
		}
		List<TagDTO> tagsResponse = tags.stream()
				.map(tag -> new TagDTO(tag))
				.collect(Collectors.toList());
		result.setData(tagsResponse);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
	/**
	 * Search Tag by name
	 * 
	 * @param req
	 * @return
	 */
	@PostMapping("/search-by-name")
	public ResponseEntity<Result<List<TagDTO>>> findByName(@RequestBody AllResponseTypeDTO req) {
		Result<List<TagDTO>> result = new Result<>();
		
		if (req == null || req.getStringType() == null) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("Data Invalid!");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);							
		}
		
		List<TagDTO> tags = tagService.searchByName(req.getStringType(), req.getIntegerType());
		
		result.setData(tags);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
}
