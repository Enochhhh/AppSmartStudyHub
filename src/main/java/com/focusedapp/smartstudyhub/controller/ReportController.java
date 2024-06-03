package com.focusedapp.smartstudyhub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.nimbusds.oauth2.sdk.util.CollectionUtils;

import net.minidev.json.JSONObject;

@RestController
@RequestMapping("/mobile/v1/user/guest/report")
@CrossOrigin(origins ="*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE, RequestMethod.PUT})
public class ReportController extends BaseController {

	@Autowired
	private ReportService reportService;
	
	/**
	 * Create report
	 * 
	 * @param request
	 * @param userId
	 * @return
	 */
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
	
	/**
	 * Get report and feedback of User
	 * 
	 * @param userId
	 * @return
	 */
	@GetMapping("/get-by-user/{userId}")
	public ResponseEntity<Result<List<ReportDTO>>> getReportsOfUser(@PathVariable Integer userId, Pageable pageable) {
		Result<List<ReportDTO>> result = new Result<>();
		
		if (userId == null || userId < 1) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("Data Invalid!");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);
		}
		
		List<ReportDTO> reports = reportService.getByUserId(userId, pageable);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		if (CollectionUtils.isEmpty(reports)) {
			result.getMeta().setStatusCode(StatusCode.GET_REPORTS_FAILURE.getCode());
			result.getMeta().setMessage(StatusCode.GET_REPORTS_FAILURE.getMessage());
		}
		result.setData(reports);
		Integer totalReports = reportService.getTotalReportsByUserId(userId);
		JSONObject jSon = new JSONObject();
		jSon.put("totalReports", totalReports);
		result.setExtendProp(jSon);
		return createResponseEntity(result);
	}
	
	/**
	 * Get report and feedback of User
	 * 
	 * @param userId
	 * @return
	 */
	@GetMapping("/get/{id}")
	public ResponseEntity<Result<ReportDTO>> getDetailReport(@PathVariable Integer id) {
		Result<ReportDTO> result = new Result<>();
		
		if (id == null || id < 1) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("Data Invalid!");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);
		}
		
		ReportDTO report = reportService.getById(id);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		if (report == null) {
			result.getMeta().setStatusCode(StatusCode.GET_DETAIL_REPORT_FAILURE.getCode());
			result.getMeta().setMessage(StatusCode.GET_DETAIL_REPORT_FAILURE.getMessage());
		}
		result.setData(report);
		return createResponseEntity(result);
	}
	
}
