package com.focusedapp.smartstudyhub.util.enumerate;

public enum EnumStatusReport {
	SEEN("SEEN"),
	NOT_SEEN("NOT SEEN");
	
	private final String value;
	
	EnumStatusReport(String value) {	
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}	
}
