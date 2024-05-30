package com.focusedapp.smartstudyhub.controller.customer;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.focusedapp.smartstudyhub.controller.BaseController;
import com.focusedapp.smartstudyhub.exception.ISException;
import com.focusedapp.smartstudyhub.model.TransactionPayment;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.custom.AllResponseTypeDTO;
import com.focusedapp.smartstudyhub.model.custom.PaymentDTO;
import com.focusedapp.smartstudyhub.model.custom.Result;
import com.focusedapp.smartstudyhub.service.UserService;
import com.focusedapp.smartstudyhub.service.payment.PaypalService;
import com.focusedapp.smartstudyhub.service.payment.VNPayService;
import com.focusedapp.smartstudyhub.util.constant.ConstantUrl;
import com.focusedapp.smartstudyhub.util.enumerate.EnumPaymentStatus;
import com.focusedapp.smartstudyhub.util.enumerate.StatusCode;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/mobile/v1/user")
@CrossOrigin(origins = "*", methods = { RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE,
		RequestMethod.PUT })
@Slf4j
public class PaymentController extends BaseController {
	
	@Autowired VNPayService vnPayService;
	@Autowired UserService userService;
	@Autowired PaypalService paypalService;

	/**
	 * Payment with VNPay
	 * 
	 * @param paymentDTO
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/customer/payment/vnpay")
	public ResponseEntity<Result<AllResponseTypeDTO>> payVnpay(@RequestBody PaymentDTO paymentDTO, HttpServletRequest request) throws IOException {
		Result<AllResponseTypeDTO> result = new Result<>();
		
		User user = getAuthenticatedUser(); 
	    AllResponseTypeDTO dataResponse = vnPayService.createOrder(paymentDTO, request, user);
	    String vnpayUrl = dataResponse.getStringType();
	    result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
	    result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
	    AllResponseTypeDTO allResponseTypeDTO = new AllResponseTypeDTO();
	    allResponseTypeDTO.setStringType(vnpayUrl);
	    result.setData(allResponseTypeDTO);
	    return createResponseEntity(result);
	}
	
	/**
	 * Get info after paying VNPay
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/guest/payment/vnpay-payment")
    public void responseAfterPayment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AllResponseTypeDTO data = vnPayService.orderReturn(request);
        StringBuilder url = new StringBuilder();
        if (data.getIntegerType() == 1) {
        	url.append(ConstantUrl.CLIENT_URL);
        	url.append("?status=");
        	url.append(EnumPaymentStatus.SUCCESS.getValue());
        	url.append("&transactionPaymentId=");
        	url.append(data.getTransactionId().toString());
        	url.append("&token=");
        	url.append(userService.generateToken(data.getUser()));
        } else {
        	url.append(ConstantUrl.CLIENT_URL);
        	url.append("?status=");
        	url.append(EnumPaymentStatus.FAILURE.getValue());
        }
        response.sendRedirect(url.toString());
    }
	
	@PostMapping("/customer/payment/paypal")
	public ResponseEntity<Result<AllResponseTypeDTO>> createPaypalPayment(@RequestBody PaymentDTO paymentDTO, HttpServletRequest request, 
			HttpServletResponse response) {
		Result<AllResponseTypeDTO> result = new Result<>();
		User user = getAuthenticatedUser();
		AllResponseTypeDTO data = new AllResponseTypeDTO();
		String urlServer = request.getScheme()
				.concat("://")
				.concat(request.getServerName())
				.concat(":")
				.concat(String.valueOf(request.getServerPort()));
		String cancelUrl = urlServer.concat("/mobile/v1/user/guest/payment/paypal/cancel");
		String successUrl = urlServer.concat("/mobile/v1/user/guest/payment/paypal/success/")
				.concat(user.getId().toString());
		try {
			AllResponseTypeDTO dataResponse = paypalService.createPayment(paymentDTO.getPaypalAmount(), "USD", "paypal", 
					"sale", paymentDTO.getPaypalOrderInfo(), cancelUrl, successUrl, user, paymentDTO.getPackagePremium(), 
					request);
			Payment payment = dataResponse.getPayment();
			
			for (Links links : payment.getLinks()) {
				if (links.getRel().equals("approval_url")) {
					data.setStringType(links.getHref());
					data.setBooleanType(true);
					result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
					result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
					result.setData(data);
					return createResponseEntity(result);
				}
			}
		} catch (PayPalRESTException e) {
			log.error("Error occurred:: ", e);
		}
		data.setStringType("Error when create payment with PayPal");
		data.setBooleanType(false);
		result.getMeta().setStatusCode(StatusCode.SUCCESS.getCode());
		result.getMeta().setMessage(StatusCode.SUCCESS.getMessage());
		result.setData(data);
		return createResponseEntity(result);
	}
	
	@GetMapping("/guest/payment/paypal/success/{userId}/{transactionPaymentId}")
	public void paymentPaypalSuccess(
			@PathVariable("userId") Integer userId,
			@PathVariable("transactionPaymentId") Integer transactionPaymentId,
			@RequestParam("paymentId") String paymentId,
			@RequestParam("PayerID") String PayerID,
			@RequestParam("token") String token,
			HttpServletRequest request,
			HttpServletResponse response
			) throws IOException {
		StringBuilder url = new StringBuilder();
		try {
			Payment payment = paypalService.executePayment(paymentId, PayerID);
			if (payment.getState().equals("approved")) {
				TransactionPayment transactionPayment = paypalService.saveOrderSuccess(payment, userId, transactionPaymentId);
				url.append(ConstantUrl.CLIENT_URL);
	        	url.append("?status=");
	        	url.append(EnumPaymentStatus.SUCCESS.getValue());
	        	url.append("&transactionPaymentId=");
	        	url.append(transactionPayment.getId().toString());
	        	url.append("&token=");
	        	url.append(userService.generateToken(transactionPayment.getUser()));
            }
		} catch (PayPalRESTException e) {
			throw new ISException(e);
		}
		response.sendRedirect(url.toString());
	}
	
	@GetMapping("/guest/payment/paypal/cancel/{transactionPaymentId}")
	public void paymentPaypalCancel(@PathVariable("transactionPaymentId") Integer transactionPaymentId, 
			HttpServletResponse response) 
			throws IOException {
		StringBuilder url = new StringBuilder();
		paypalService.saveOrderFailure(transactionPaymentId);
		url.append(ConstantUrl.CLIENT_URL);
    	url.append("?status=");
    	url.append(EnumPaymentStatus.FAILURE.getValue());
		response.sendRedirect(url.toString());
	}

}
