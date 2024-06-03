package com.focusedapp.smartstudyhub.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.focusedapp.smartstudyhub.controller.BaseController;
import com.focusedapp.smartstudyhub.model.custom.AllResponseTypeDTO;
import com.focusedapp.smartstudyhub.model.custom.ReportDTO;
import com.focusedapp.smartstudyhub.model.custom.Result;
import com.focusedapp.smartstudyhub.service.ReportService;
import com.focusedapp.smartstudyhub.util.enumerate.StatusCode;
import com.nimbusds.oauth2.sdk.util.CollectionUtils;

import net.minidev.json.JSONObject;

@RestController
@RequestMapping("/mobile/v1/admin/report")
@CrossOrigin(origins ="*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE, RequestMethod.PUT})
public class AdminReportController extends BaseController {
	
	@Autowired ReportService reportService;

	/**
	 * Get all reports and feedbacks of User
	 * 
	 * @param userId
	 * @return
	 */
	@GetMapping("/get")
	public ResponseEntity<Result<List<ReportDTO>>> getAllReports(Pageable pageable) {
		Result<List<ReportDTO>> result = new Result<>();
		
		List<ReportDTO> reports = reportService.getAllReportsForAdmin(pageable);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		if (CollectionUtils.isEmpty(reports)) {
			result.getMeta().setStatusCode(StatusCode.ADMIN_GET_REPORTS_FAILURE.getCode());
			result.getMeta().setMessage(StatusCode.ADMIN_GET_REPORTS_FAILURE.getMessage());
		}
		result.setData(reports);
		JSONObject jSon = new JSONObject();
		jSon.put("totalReports", reportService.getTotalReportsForAdmin());
		result.setExtendProp(jSon);
		return createResponseEntity(result);
	}
	
	/**
	 * Get detail report
	 * 
	 * @param id
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
	
	/**
	 * Delete report and feedback of User
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Result<AllResponseTypeDTO>> deleteReport(@PathVariable Integer id) {
		Result<AllResponseTypeDTO> result = new Result<>();
		
		if (id == null || id < 1) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("Data Invalid!");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);
		}
		
		Boolean isDeleted = reportService.deleteById(id);
		AllResponseTypeDTO data = new AllResponseTypeDTO();
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		data.setStringType("Deleted Successfully!");
		if (isDeleted == false) {
			result.getMeta().setStatusCode(StatusCode.ADMIN_DELETE_REPORT_FAILURE.getCode());
			result.getMeta().setMessage(StatusCode.ADMIN_DELETE_REPORT_FAILURE.getMessage());
			data.setStringType("The requested data could not be found!");
		}
		result.setData(data);
		
		return createResponseEntity(result);
	}
	
	/**
	 * Update status report and feedback of User
	 * 
	 * @param id
	 * @return
	 */
	@PutMapping("/update/{id}")
	public ResponseEntity<Result<AllResponseTypeDTO>> updateReport(@PathVariable Integer id) {
		Result<AllResponseTypeDTO> result = new Result<>();
		
		if (id == null || id < 1) {
			result.getMeta().setStatusCode(StatusCode.PARAMETER_INVALID.getCode());
			result.getMeta().setMessage(StatusCode.PARAMETER_INVALID.getMessage());
			result.getMeta().setDetails("Data Invalid!");
			return createResponseEntity(result, HttpStatus.BAD_REQUEST);
		}
		
		Boolean isDeleted = reportService.updateStatus(id);
		AllResponseTypeDTO data = new AllResponseTypeDTO();
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		data.setStringType("Updated Successfully!");
		if (isDeleted == false) {
			result.getMeta().setStatusCode(StatusCode.ADMIN_DELETE_REPORT_FAILURE.getCode());
			result.getMeta().setMessage(StatusCode.ADMIN_DELETE_REPORT_FAILURE.getMessage());
			data.setStringType("The requested data could not be found!");
		}
		result.setData(data);
		
		return createResponseEntity(result);
	}
	
}
