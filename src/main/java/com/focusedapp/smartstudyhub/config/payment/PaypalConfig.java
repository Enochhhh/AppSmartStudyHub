package com.focusedapp.smartstudyhub.config.payment;

import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.paypal.base.rest.APIContext;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class PaypalConfig {

	@Value("${paypal.client-id}")
	private String clientId;
	@Value("${paypal.client-secret}")
	private String clientSecret;
	@Value("${paypal.mode}")
	private String mode;
	
	@Bean
	public APIContext apiContext() {
		return new APIContext(clientId, clientSecret, mode);
	}
	
	/**
	 * Get IP Address
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddress(HttpServletRequest request) {
		String ipAdress;
		try {
			ipAdress = request.getHeader("X-FORWARDED-FOR");
			if (ipAdress == null) {
				ipAdress = request.getLocalAddr();
			}
		} catch (Exception e) {
			ipAdress = "Invalid IP:" + e.getMessage();
		}
		return ipAdress;
	}
	
	/**
	 * Random Number
	 * 
	 * @param len
	 * @return
	 */
	public static String getRandomNumber(int len) {
        Random rnd = new Random();
        String chars = "0123456789";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
