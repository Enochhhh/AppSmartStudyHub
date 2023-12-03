package com.focusedapp.smartstudyhub.util.enumerate;

public enum EnumModeWork {
	
	SPECIFIED("SPECIFIED"),
	NONSPECIFIED("NONSPECIFIED");

	private final String value;
	
	private EnumModeWork(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}	
	
}
