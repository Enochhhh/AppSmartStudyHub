package com.focusedapp.smartstudyhub.util.enumerate;

public enum EnumTypeReport {
	HELP("HELP"),
	REPORTPROBLEM("REPORTPROBLEM"),
	REPORTUSER("REPORTUSER"),
	FEEDBACK("FEEDBACK");
	
	private final String value;
	
	EnumTypeReport(String value) {	
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}	
}
