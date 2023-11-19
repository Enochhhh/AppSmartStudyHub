package com.focusedapp.smartstudyhub.util.enumerate;

public enum EnumStatusGroup {

	PRIVATE("PRIVATE"),
	PUBLIC("PUBLIC");
	
	private final String value;
	
	EnumStatusGroup(String value) {	
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}	
	
}
