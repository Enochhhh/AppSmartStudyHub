package com.focusedapp.smartstudyhub.util.enumerate;

public enum EnumSortType {

	PROJECT("PROJECT"),
	DUEDATE("DUEDATE"),
	PRIORITY("PRIORITY");
	
	private final String value;
	
	EnumSortType(String value) {	
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}	
	
}
