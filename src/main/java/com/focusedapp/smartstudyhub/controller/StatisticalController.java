package com.focusedapp.smartstudyhub.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.focusedapp.smartstudyhub.model.custom.Result;
import com.focusedapp.smartstudyhub.model.custom.StatisticalDTO;
import com.focusedapp.smartstudyhub.model.custom.TimeLineStatisticalDTO;
import com.focusedapp.smartstudyhub.service.StatisticalService;
import com.focusedapp.smartstudyhub.util.enumerate.StatusCode;


@RestController
@RequestMapping("/mobile/v1/user")
@CrossOrigin(origins = "*", methods = { RequestMethod.POST, RequestMethod.PUT, RequestMethod.GET, RequestMethod.DELETE })
public class StatisticalController extends BaseController {
	
	@Autowired StatisticalService statisticalService;
	
	
	/**
	 * Statistical Time Focus
	 * 
	 * @param startDate
	 * @param endDate
	 * @param userId
	 * @return
	 */
	@GetMapping("/guest/statistical/time-focus")
	public ResponseEntity<Result<TimeLineStatisticalDTO>> statisticalTimeFocus(@RequestParam Long startDate, @RequestParam Long endDate, 
			@RequestParam Integer userId, @RequestParam String type) {
		Result<TimeLineStatisticalDTO> result = new Result<>();
		
		TimeLineStatisticalDTO data = statisticalService.statisticalTimeFocus(startDate, endDate, userId, type);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		if (data == null) {
			result.getMeta().setStatusCode(StatusCode.STATISTICAL_TIME_FOCUS_FAILURE.getCode());
			result.getMeta().setMessage(StatusCode.STATISTICAL_TIME_FOCUS_FAILURE.getMessage());
		}

		result.setData(data);
		return createResponseEntity(result);
	}
	
	/**
	 * Statistical Time Focus by work
	 * 
	 * @param startDate
	 * @param endDate
	 * @param userId
	 * @return
	 */
	@GetMapping("/guest/statistical/time-focus-by-work")
	public ResponseEntity<Result<StatisticalDTO>> statisticalTimeFocusByWork(@RequestParam Long startDate, @RequestParam Long endDate, 
			@RequestParam Integer userId, @RequestParam String unit) {
		Result<StatisticalDTO> result = new Result<>();
		
		StatisticalDTO data = statisticalService.statisticalTimeFocusByWork(startDate, endDate, userId, unit);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		if (data == null) {
			result.getMeta().setStatusCode(StatusCode.STATISTICAL_TIME_FOCUS_BY_WORK_FAILURE.getCode());
			result.getMeta().setMessage(StatusCode.STATISTICAL_TIME_FOCUS_BY_WORK_FAILURE.getMessage());
		}

		result.setData(data);
		return createResponseEntity(result);
	}
	
	/**
	 * Statistical Time Focus
	 * 
	 * @param startDate
	 * @param endDate
	 * @param userId
	 * @return
	 */
	@GetMapping("/guest/statistical/work-completed")
	public ResponseEntity<Result<TimeLineStatisticalDTO>> statisticalWork(@RequestParam Long startDate, @RequestParam Long endDate, 
			@RequestParam Integer userId, @RequestParam String type) {
		Result<TimeLineStatisticalDTO> result = new Result<>();
		
		TimeLineStatisticalDTO data = statisticalService.statisticalWork(startDate, endDate, userId, type);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		if (data == null) {
			result.getMeta().setStatusCode(StatusCode.STATISTICAL_WORK_FAILURE.getCode());
			result.getMeta().setMessage(StatusCode.STATISTICAL_WORK_FAILURE.getMessage());
		}

		result.setData(data);
		return createResponseEntity(result);
	}
	
}
