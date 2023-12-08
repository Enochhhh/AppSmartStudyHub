package com.focusedapp.smartstudyhub.util.enumerate;

public enum EnumModePomo {
	
	SPECIFIED("SPECIFIED"),
	NONSPECIFIED("NONSPECIFIED");

	private final String value;
	
	private EnumModePomo(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}	
	
}
