package com.focusedapp.smartstudyhub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.focusedapp.smartstudyhub.model.custom.HistoryDailyDTO;
import com.focusedapp.smartstudyhub.model.custom.Result;
import com.focusedapp.smartstudyhub.service.HistoryDailyService;
import com.focusedapp.smartstudyhub.util.enumerate.StatusCode;
import com.nimbusds.oauth2.sdk.util.CollectionUtils;

@RestController
@RequestMapping("/mobile/v1/user/guest/historydaily")
@CrossOrigin(origins ="*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE, RequestMethod.PUT})
public class HistoryDailyController extends BaseController {

	@Autowired
	HistoryDailyService historyDailyService;
	
	/**
	 * Get folder of user
	 * 
	 * @param userId
	 * @return
	 */
	@GetMapping("/get/{userId}")
	public ResponseEntity<Result<List<HistoryDailyDTO>>> getHistoryDailyOfUser(@PathVariable Integer userId, Integer page, Integer size) {
		Result<List<HistoryDailyDTO>> result = new Result<>();
		
		if (userId == null || userId < 1) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("userId Invalid!");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);
		}
		
		List<HistoryDailyDTO> historyDailyDTOs = historyDailyService.getListHistoryDailyOfUser(userId, page, size);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		if (CollectionUtils.isEmpty(historyDailyDTOs)) {
			result.getMeta().setStatusCode(StatusCode.GET_HISTORY_DAILY_FAILURE.getCode());
			result.getMeta().setMessage(StatusCode.GET_HISTORY_DAILY_FAILURE.getMessage());
		}
		
		result.setData(historyDailyDTOs);
		return createResponseEntity(result);
	}
}
