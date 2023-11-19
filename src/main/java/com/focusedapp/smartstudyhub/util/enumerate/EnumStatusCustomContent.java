package com.focusedapp.smartstudyhub.util.enumerate;

public enum EnumStatusCustomContent {
	
	DEFAULT("DEFAULT"),
	PREMIUM("PREMIUM"),
	OWNED("OWNED");	
	
	private final String value;
	
	EnumStatusCustomContent(String value) {	
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}	
	
}
