package com.focusedapp.smartstudyhub.controller.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.focusedapp.smartstudyhub.controller.BaseController;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.custom.Result;
import com.focusedapp.smartstudyhub.model.custom.TransactionPaymentDTO;
import com.focusedapp.smartstudyhub.service.TransactionPaymentService;
import com.focusedapp.smartstudyhub.util.enumerate.StatusCode;
import com.nimbusds.oauth2.sdk.util.CollectionUtils;

@RestController
@RequestMapping("/mobile/v1/user/customer/transaction-payment")
@CrossOrigin(origins ="*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE, RequestMethod.PUT})
public class TransactionPaymentController extends BaseController {

	@Autowired TransactionPaymentService transactionPaymentService;
	
	/**
	 * Get all Transaction payment info
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/get")
	public ResponseEntity<Result<List<TransactionPaymentDTO>>> getTransactionPayments() {
		Result<List<TransactionPaymentDTO>> result = new Result<>();		
		
		User user = getAuthenticatedUser();
		List<TransactionPaymentDTO> data = transactionPaymentService.getTransactionPayments(user);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		if (CollectionUtils.isEmpty(data)) {
			result.getMeta().setStatusCode(StatusCode.GET_TRANSACTION_PAYMENTS_FAILURE.getCode());
			result.getMeta().setMessage(StatusCode.GET_TRANSACTION_PAYMENTS_FAILURE.getMessage());
			result.getMeta().setDetails("Data is empty!");
		}
		result.setData(data);
			
		return createResponseEntity(result);
	}
	
	/**
	 * Get detail Transaction payment info
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/get/{id}")
	public ResponseEntity<Result<TransactionPaymentDTO>> getDetailTransactionPayment(@PathVariable Integer id) {
		Result<TransactionPaymentDTO> result = new Result<>();		
		
		TransactionPaymentDTO data = transactionPaymentService.getById(id);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		if (data == null) {
			result.getMeta().setStatusCode(StatusCode.GET_DETAIL_TRANSACTION_PAYMENT_FAILURE.getCode());
			result.getMeta().setMessage(StatusCode.GET_DETAIL_TRANSACTION_PAYMENT_FAILURE.getMessage());
			result.getMeta().setDetails("Data is empty!");
		}
		result.setData(data);
			
		return createResponseEntity(result);
	}
}
