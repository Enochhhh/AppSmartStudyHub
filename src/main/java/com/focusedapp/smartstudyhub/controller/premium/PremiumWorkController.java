package com.focusedapp.smartstudyhub.controller.premium;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.focusedapp.smartstudyhub.controller.BaseController;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.custom.Result;
import com.focusedapp.smartstudyhub.model.custom.WorkDTO;
import com.focusedapp.smartstudyhub.service.WorkService;
import com.focusedapp.smartstudyhub.util.enumerate.StatusCode;

@RestController
@RequestMapping("/mobile/v1/user/premium/work")
@CrossOrigin(origins ="*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE, RequestMethod.PUT})
public class PremiumWorkController extends BaseController {

	@Autowired WorkService workService;
	
	/**
	 * API Repeat Work
	 * 
	 * @param workId
	 * @return
	 */
	@PutMapping("/repeat")
	public ResponseEntity<Result<WorkDTO>> repeatWork(@RequestParam Integer workId) {
		Result<WorkDTO> result = new Result<>();
		
		if (workId == null || workId < 1) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("Data Invalid!");
			return createResponseEntity(result);
		}
		User user = getAuthenticatedUser();
		WorkDTO data = workService.repeat(workId, user);
		
		if (data == null) {
			result.getMeta().setStatusCode(StatusCode.REPEAT_WORK_FAILURE.getCode());
			result.getMeta().setMessage(StatusCode.REPEAT_WORK_FAILURE.getMessage());
			return createResponseEntity(result, HttpStatus.FORBIDDEN);
		}
		
		result.setData(data);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
}
