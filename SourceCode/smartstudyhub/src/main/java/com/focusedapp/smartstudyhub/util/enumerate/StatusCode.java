package com.focusedapp.smartstudyhub.util.enumerate;

public enum StatusCode {
	
	// System - featureCode: 0
	SUCCESS("0_1_s", "Success"),
	SYSTEM_FAILURE("0_2_f", "System Failure"),
	PARAMETER_INVALID("0_3_f", "Parameter Invalid"),
	DATA_EMPTY("0_4_f", "Data Empty"),
	DATA_NOT_FOUND("0_5_f", "Data Not Found"),
	FAIL("0_6_f", "Failed"),
	DATA_EXISTED("0_7_f", "Data Existed"),
	AUTHENTICATED_FAILURE("0_8_f", "Authenticatted Failure"),
	
	// User - featureCode: 1
	/* Register */
	REGISTER_SUCCESS("1_1_s", "Register Successfully"),
	REGISTER_FAILURE("1_1_f", "Register Failure"),
	/* Login */
	LOGIN_SUCCESS("2_1_s", "Login Successfully"),
	LOGIN_FAILURE("2_1_f", "Login Failure");
	

	private final String code;
	private final String message;
	
	private StatusCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
}
