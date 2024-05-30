package com.focusedapp.smartstudyhub.service.payment;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.focusedapp.smartstudyhub.config.payment.PaypalConfig;
import com.focusedapp.smartstudyhub.exception.ISException;
import com.focusedapp.smartstudyhub.model.TransactionPayment;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.custom.AllResponseTypeDTO;
import com.focusedapp.smartstudyhub.service.TransactionPaymentService;
import com.focusedapp.smartstudyhub.service.UserService;
import com.focusedapp.smartstudyhub.util.DateUtils;
import com.focusedapp.smartstudyhub.util.enumerate.EnumPackagePremium;
import com.focusedapp.smartstudyhub.util.enumerate.EnumPaymentMethod;
import com.focusedapp.smartstudyhub.util.enumerate.EnumPaymentStatus;
import com.focusedapp.smartstudyhub.util.enumerate.EnumRole;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class PaypalService {

	@Autowired private APIContext apiContext;
	@Autowired private UserService userService;
	@Autowired private TransactionPaymentService transactionPaymentService;
	
	public AllResponseTypeDTO createPayment(Double total, String currency, String method, String intent, String description,
			String cancelUrl, String successUrl, User user, String packagePremium, HttpServletRequest request) 
					throws PayPalRESTException {	
		TransactionPayment transactionPayment = new TransactionPayment();
    	SimpleDateFormat formatterToCreateOrderId = new SimpleDateFormat("yyyyMMddHHmmss");
    	formatterToCreateOrderId.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Asia/Ho_Chi_Minh")));
		String txtRef = "O".concat(PaypalConfig.getRandomNumber(10))
        		.concat(formatterToCreateOrderId.format(new Date()))
        		.concat("US").concat(user.getId().toString());
		transactionPayment.setOrderId(txtRef);
		transactionPayment.setTypePayment("pay");
		transactionPayment.setInfo(description);
		transactionPayment.setAmount(total.intValue());
		transactionPayment.setIpAddress(PaypalConfig.getIpAddress(request));
		transactionPayment.setStatus(EnumPaymentStatus.WAITING.getValue());
		transactionPayment.setUnit("VND");
		transactionPayment.setMethodPayment(EnumPaymentMethod.PAYPAL.getValue());
		transactionPayment.setPackagePremium(packagePremium);
		transactionPayment.setUser(user);
		transactionPayment = transactionPaymentService.persistent(transactionPayment);
		
		successUrl = successUrl.concat("/").concat(transactionPayment.getId().toString());
		cancelUrl = cancelUrl.concat("/").concat(transactionPayment.getId().toString());
		
		Amount amount = new Amount();
		amount.setCurrency(currency);
		amount.setTotal(String.format(Locale.forLanguageTag(currency), "%.0f", total * (1/25500D)));
		
		Transaction transaction = new Transaction();
		transaction.setDescription(description);
		transaction.setAmount(amount);
		
		List<Transaction> transactions = new ArrayList<>();
		transactions.add(transaction);
		
		Payer payer = new Payer();
		payer.setPaymentMethod(method);
		
		Payment payment = new Payment();
		payment.setIntent(intent);
		payment.setPayer(payer);
		payment.setTransactions(transactions);
		
		RedirectUrls redirectUrls = new RedirectUrls();
		redirectUrls.setCancelUrl(cancelUrl);
		redirectUrls.setReturnUrl(successUrl);
		
		payment.setRedirectUrls(redirectUrls);
		
		AllResponseTypeDTO data = new AllResponseTypeDTO();
		data.setIntegerType(transactionPayment.getId());
		data.setPayment(payment.create(apiContext));
		return data;
	}
	
	public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
		Payment payment = new Payment();
		payment.setId(paymentId);
		
		
		PaymentExecution paymentExecution = new PaymentExecution();
		paymentExecution.setPayerId(payerId);
		
		return payment.execute(apiContext, paymentExecution);
	}
	
	public TransactionPayment saveOrderSuccess(Payment payment, Integer userId, Integer transactionPaymentId) {
		User user = userService.findById(userId);
		
		TransactionPayment transactionPayment = transactionPaymentService.findById(transactionPaymentId);
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    	try {
    		transactionPayment.setPayDate(formatter.parse(payment.getCreateTime()));
    	} catch (Exception e) {
			throw new ISException(e);
		}
		transactionPayment.setTransactionNo(payment.getId());
		transactionPayment.setStatus(EnumPaymentStatus.SUCCESS.getValue());
		
    	Date dueDatePremium = user.getDueDatePremium();
    	if (dueDatePremium == null) {
    		dueDatePremium = DateUtils.addDaysForDate(new Date(), 1);
    		dueDatePremium = DateUtils.setTimeOfDateToMidnight(dueDatePremium.getTime());
    	}
		if (transactionPayment.getPackagePremium().equals(EnumPackagePremium.THREEMONTHS.getValue())) {
			dueDatePremium = DateUtils.addMonthsForDate(dueDatePremium, 3);
		} else {
			dueDatePremium = DateUtils.addYearsForDate(dueDatePremium, 1);
		}      		
		user.setDueDatePremium(dueDatePremium);
		user.setRole(EnumRole.PREMIUM.getValue());
		transactionPayment.setUser(user);
		transactionPaymentService.persistent(transactionPayment);
		return transactionPayment;
	}
	
	public TransactionPayment saveOrderFailure(Integer transactionPaymentId) {
		TransactionPayment transactionPayment = transactionPaymentService.findById(transactionPaymentId);
		transactionPayment.setStatus(EnumPaymentStatus.FAILURE.getValue());
		transactionPaymentService.persistent(transactionPayment);
		return transactionPayment;
	}
}
