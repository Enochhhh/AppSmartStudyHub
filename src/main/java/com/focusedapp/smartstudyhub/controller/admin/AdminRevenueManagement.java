package com.focusedapp.smartstudyhub.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.focusedapp.smartstudyhub.controller.BaseController;
import com.focusedapp.smartstudyhub.model.custom.Result;
import com.focusedapp.smartstudyhub.model.custom.StatisticalRevenueDTO;
import com.focusedapp.smartstudyhub.service.TransactionPaymentService;
import com.focusedapp.smartstudyhub.util.enumerate.StatusCode;
import com.nimbusds.oauth2.sdk.util.CollectionUtils;

@RestController
@RequestMapping("/mobile/v1/admin/revenue")
@CrossOrigin(origins ="*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE, RequestMethod.PUT})
public class AdminRevenueManagement extends BaseController {

	@Autowired TransactionPaymentService transactionPaymentService;
	
	@GetMapping("/statistical")
	public ResponseEntity<Result<List<StatisticalRevenueDTO>>> statisticalRevenue(@RequestParam String typeQuery,
			Integer year, Integer month) {
		Result<List<StatisticalRevenueDTO>> result = new Result<>();
		
		List<StatisticalRevenueDTO> data = transactionPaymentService.statisticalRevenue(typeQuery, year, month);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		if (CollectionUtils.isEmpty(data)) {
			result.getMeta().setStatusCode(StatusCode.REVENUE_NOT_HAVE_DATA.getCode());
			result.getMeta().setMessage(StatusCode.REVENUE_NOT_HAVE_DATA.getMessage());
		}
		result.setData(data);
		return createResponseEntity(result);
		
	}
}
