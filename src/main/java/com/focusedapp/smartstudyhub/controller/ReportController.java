package com.focusedapp.smartstudyhub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.focusedapp.smartstudyhub.model.custom.ReportDTO;
import com.focusedapp.smartstudyhub.model.custom.Result;
import com.focusedapp.smartstudyhub.service.ReportService;
import com.focusedapp.smartstudyhub.util.enumerate.StatusCode;

@RestController
@RequestMapping("/mobile/v1/user/guest/report")
@CrossOrigin(origins ="*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE, RequestMethod.PUT})
public class ReportController extends BaseController {

	@Autowired
	private ReportService reportService;
	
	@PostMapping("/create")
	public ResponseEntity<Result<ReportDTO>> createReport(@RequestBody ReportDTO request, @RequestParam Integer userId) {
		
		Result<ReportDTO> result = new Result<>();
				
		ReportDTO reportCreated= reportService.create(request, userId);
		if (reportCreated == null) {
			result.getMeta().setStatusCode(StatusCode.FAIL.getCode());
			result.getMeta().setMessage(StatusCode.FAIL.getMessage());
			result.getMeta().setDetails("Create Report Failed");
			return createResponseEntity(result, HttpStatus.METHOD_NOT_ALLOWED);
		}
		result.setData(reportCreated);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		return createResponseEntity(result);
	}
	
}
