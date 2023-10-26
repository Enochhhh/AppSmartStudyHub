package com.focusedapp.smartstudyhub.util.enumerate;

public enum EnumStatusCustomContent {
	
	DEFAULT("DEFAULT"),
	OWNED("OWNED");	
	
	private final String value;
	
	EnumStatusCustomContent(String value) {	
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}	
	
}
