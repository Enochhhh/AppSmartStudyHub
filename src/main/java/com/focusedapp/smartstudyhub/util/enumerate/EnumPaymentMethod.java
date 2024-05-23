package com.focusedapp.smartstudyhub.util.enumerate;

public enum EnumPaymentMethod {

	VNPAY("VNPAY"),
	PAYPAL("PAYPAL");

	private final String value;
	
	private EnumPaymentMethod(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}	
}
