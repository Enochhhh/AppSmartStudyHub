package com.focusedapp.smartstudyhub.service.payment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.focusedapp.smartstudyhub.config.payment.VNPayConfig;
import com.focusedapp.smartstudyhub.exception.ISException;
import com.focusedapp.smartstudyhub.model.TransactionPayment;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.custom.AllResponseTypeDTO;
import com.focusedapp.smartstudyhub.model.custom.PaymentDTO;
import com.focusedapp.smartstudyhub.service.TransactionPaymentService;
import com.focusedapp.smartstudyhub.service.UserService;
import com.focusedapp.smartstudyhub.util.DateUtils;
import com.focusedapp.smartstudyhub.util.enumerate.EnumPackagePremium;
import com.focusedapp.smartstudyhub.util.enumerate.EnumPaymentMethod;
import com.focusedapp.smartstudyhub.util.enumerate.EnumPaymentStatus;
import com.focusedapp.smartstudyhub.util.enumerate.EnumRole;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class VNPayService {
	
	@Autowired TransactionPaymentService transactionPaymentService;
	
	@Autowired UserService userService;
	
	@Value("${vnpay.tmn-code}")
	private String VNP_TMNCODE;

	@Value("${vnpay.hash-secret}")
	private String VNP_HASHSECRET;
	
	/**
	 * Create Order to pay with VNPay
	 * 
	 * @param paymentDTO
	 * @param request
	 * @param user
	 * @return
	 */
	public AllResponseTypeDTO createOrder(PaymentDTO paymentDTO, HttpServletRequest request, User user){
		String urlReturn = request.getScheme()
				.concat("://")
				.concat(request.getServerName())
				.concat(":")
				.concat(String.valueOf(request.getServerPort()));
        
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VNPayConfig.VNP_VERSION);
        vnp_Params.put("vnp_Command", VNPayConfig.VNP_COMMAND_PAY);
        vnp_Params.put("vnp_TmnCode", VNP_TMNCODE);
        vnp_Params.put("vnp_Amount", String.valueOf(paymentDTO.getVnpAmount() * 100));
        vnp_Params.put("vnp_CurrCode", VNPayConfig.VNP_CURR_CODE);
        
        vnp_Params.put("vnp_OrderInfo", paymentDTO.getVnpOrderInfo());
        vnp_Params.put("vnp_OrderType", VNPayConfig.VNP_ORDERTYPE);

        vnp_Params.put("vnp_Locale", VNPayConfig.VNP_LOCALE);

        urlReturn = urlReturn.concat(VNPayConfig.VNP_RETURNURL);
        vnp_Params.put("vnp_ReturnUrl", urlReturn);
        vnp_Params.put("vnp_IpAddr", VNPayConfig.getIpAddress(request));

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Asia/Ho_Chi_Minh")));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        formatter.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Asia/Ho_Chi_Minh")));
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        
        String txtRef = "O".concat(VNPayConfig.getRandomNumber(10))
        		.concat(vnp_CreateDate)
        		.concat("US").concat(user.getId().toString());
        vnp_Params.put("vnp_TxnRef", txtRef);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
        
        // Create model Transaction to save in database
        TransactionPayment transactionPayment = new TransactionPayment();
        transactionPayment.setUser(user);
        transactionPayment.setOrderId(vnp_Params.get("vnp_TxnRef"));
        transactionPayment.setMethodPayment(EnumPaymentMethod.VNPAY.getValue());
        transactionPayment.setTypePayment(vnp_Params.get("vnp_Command"));
        transactionPayment.setInfo(vnp_Params.get("vnp_OrderInfo"));
        transactionPayment.setAmount(paymentDTO.getVnpAmount());
        transactionPayment.setIpAddress(vnp_Params.get("vnp_IpAddr"));
        transactionPayment.setStatus(EnumPaymentStatus.WAITING.getValue());
        transactionPayment.setUnit(vnp_Params.get("vnp_CurrCode"));
        transactionPayment.setPackagePremium(paymentDTO.getPackagePremium());
        transactionPayment = transactionPaymentService.persistent(transactionPayment);
        
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                try {
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    //Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNP_HASHSECRET, hashData.toString());
        queryUrl = queryUrl.concat("&vnp_SecureHash=")
        		.concat(vnp_SecureHash);
        String paymentUrl = VNPayConfig.VNP_PAYURL + "?" + queryUrl;
        
        AllResponseTypeDTO data = new AllResponseTypeDTO();
        data.setBooleanType(true);
        data.setStringType(paymentUrl);
        return data;
    }
	
	public AllResponseTypeDTO orderReturn(HttpServletRequest request){
        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
            String fieldName = null;
            String fieldValue = null;
            try {
                fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII.toString());
                fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII.toString());
            } catch (UnsupportedEncodingException e) {
                throw new ISException(e);
            }
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }
        TransactionPayment transactionPayment = transactionPaymentService
        		.findByOrderId(request.getParameter("vnp_TxnRef"));
        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        if (fields.containsKey("vnp_SecureHashType")) {
            fields.remove("vnp_SecureHashType");
        }
        if (fields.containsKey("vnp_SecureHash")) {
            fields.remove("vnp_SecureHash");
        }
        String signValue = VNPayConfig.hashAllFields(fields, VNP_HASHSECRET);
        AllResponseTypeDTO data = new AllResponseTypeDTO();
        if (signValue.equals(vnp_SecureHash) 
        		&& String.valueOf(transactionPayment.getAmount() * 100).equals(request.getParameter("vnp_Amount"))
        		&& transactionPayment.getOrderId().equals(request.getParameter("vnp_TxnRef"))) {
            if ("00".equals(request.getParameter("vnp_ResponseCode")) 
            		&& "00".equals(request.getParameter("vnp_TransactionStatus"))) {
            	transactionPayment.setBankCode(request.getParameter("vnp_BankCode"));
            	transactionPayment.setBankTranNo(request.getParameter("vnp_BankTranNo"));
            	transactionPayment.setTransactionNo(request.getParameter("vnp_TransactionNo"));
            	transactionPayment.setCardType(request.getParameter("vnp_CardType"));
            	transactionPayment.setStatus(EnumPaymentStatus.SUCCESS.getValue());
            	
            	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            	try {
            		transactionPayment.setPayDate(formatter.parse(request.getParameter("vnp_PayDate")));
            	} catch (Exception e) {
					throw new ISException(e);
				}
            	transactionPayment.setSecureHash(vnp_SecureHash);
            	data.setIntegerType(1);
            	
            	User user = userService.findById(transactionPayment.getUser().getId());
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
        		userService.persistent(user);
            } else {
            	transactionPayment.setStatus(EnumPaymentStatus.FAILURE.getValue());
            	data.setIntegerType(0);
            }
        } else {
        	transactionPayment.setStatus(EnumPaymentStatus.FAILURE.getValue());
        	data.setIntegerType(-1);
        }
        transactionPayment = transactionPaymentService.persistent(transactionPayment);
        data.setTransactionId(transactionPayment.getId());
        data.setUser(transactionPayment.getUser());
        return data;
    }

}
