package com.focusedapp.smartstudyhub.controller.customer;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.focusedapp.smartstudyhub.controller.BaseController;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.custom.AllResponseTypeDTO;
import com.focusedapp.smartstudyhub.model.custom.PaymentDTO;
import com.focusedapp.smartstudyhub.service.payment.VNPayService;
import com.focusedapp.smartstudyhub.util.constant.ConstantUrl;
import com.focusedapp.smartstudyhub.util.enumerate.EnumPaymentStatus;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/mobile/v1/user")
@CrossOrigin(origins = "*", methods = { RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE,
		RequestMethod.PUT })
public class PaymentController extends BaseController {
	
	@Autowired VNPayService vnPayService;

	/**
	 * Payment with VNPay
	 * 
	 * @param paymentDTO
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/customer/payment/vnpay")
	public String payVnpay(@RequestBody PaymentDTO paymentDTO, HttpServletRequest request) throws IOException {
		User user = getAuthenticatedUser(); 
	    String vnpayUrl = vnPayService.createOrder(paymentDTO, request, user);
	    return "redirect:" + vnpayUrl;
	}
	
	/**
	 * Get info after paying VNPay
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/guest/payment/vnpay-payment")
    public String responseAfterPayment(HttpServletRequest request){
        AllResponseTypeDTO data = vnPayService.orderReturn(request);
        if (data.getIntegerType() == 1) {
        	return "redirect:".concat(ConstantUrl.CLIENT_URL)
        			.concat("?status=")
        			.concat(EnumPaymentStatus.SUCCESS.getValue())
        			.concat("&transactionPaymentId=")
        			.concat(data.getTransactionId().toString());
        } else {
        	return "redirect:".concat(ConstantUrl.CLIENT_URL)
        			.concat("?status=")
        			.concat(EnumPaymentStatus.FAILURE.getValue());
        }
    }

}
