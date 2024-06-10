package com.focusedapp.smartstudyhub.util.enumerate;

public enum EnumStatusFile {

	DEFAULT("DEFAULT"),
	PREMIUM("PREMIUM");
	
	private final String value;
	
	EnumStatusFile(String value) {	
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}	
	
}
