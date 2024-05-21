package com.focusedapp.smartstudyhub.util.enumerate;

public enum EnumStatus {
	
	ACTIVE("ACTIVE"),
	DELETED("DELETED"),
	BANNED("BANNED"),
	COMPLETED("COMPLETED"),
	LOGIN("LOGIN"),
	LOGOUT("LOGOUT");
	
	private final String value;
	
	EnumStatus(String value) {	
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}	
	
}
