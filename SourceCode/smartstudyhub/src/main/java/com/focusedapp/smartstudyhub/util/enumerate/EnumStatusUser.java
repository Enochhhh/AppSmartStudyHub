package com.focusedapp.smartstudyhub.util.enumerate;

public enum EnumStatusUser {
	
	ACTIVE("ACTIVE"),
	DELETED("DELETED"),
	BANNED("BANNED");
	
	private final String value;
	
	EnumStatusUser(String value) {	
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}	
	
}
