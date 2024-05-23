package com.focusedapp.smartstudyhub.util.enumerate;

public enum EnumPaymentStatus {

	WAITING("WAITING"),
	FAILURE("FAILURE"),
	SUCCESS("SUCCESS");

	private final String value;
	
	private EnumPaymentStatus(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}	
}
