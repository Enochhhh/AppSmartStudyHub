package com.focusedapp.smartstudyhub.util.enumerate;

public enum EnumRole {
	
	ADMIN("ADD"),
	CUSTOMER("CUSTOMER"),
	PREMIUM("PREMIUM"),
	GUEST("GUEST");
	
	private final String value;
	
	EnumRole(String value) {	
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}	
	
}
