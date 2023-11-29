package com.focusedapp.smartstudyhub.service;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.focusedapp.smartstudyhub.dao.OtpCodeDAO;
import com.focusedapp.smartstudyhub.model.OtpCode;

@Service
public class OtpCodeService {

	@Autowired
	OtpCodeDAO otpCodeDAO;
	
	/**
	 * Save OtpCode
	 * 
	 * @param otpCode
	 * @return
	 */
	public OtpCode persistent(OtpCode otpCode) {
		return otpCodeDAO.save(otpCode);
	}
	
	/**
	 * Generate OTP Code
	 * 
	 * @return
	 */
	public String generateOtpCode() {
		Integer number = new Random().nextInt(900000) + 100000;
		return number.toString();
	}
	
	/**
	 * Find OTP Code by Email
	 * 
	 * @return
	 */
	public OtpCode findByEmail(String email) {
		Optional<OtpCode> otpCode = otpCodeDAO.findByEmail(email);
		
		if (otpCode.isEmpty()) {
			return null;
		}
		
		return otpCode.get();
	}
}
