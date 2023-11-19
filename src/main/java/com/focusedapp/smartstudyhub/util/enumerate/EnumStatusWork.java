package com.focusedapp.smartstudyhub.util.enumerate;

public enum EnumStatusWork {
	
	FINISHED("FINISHED"),
	UNFINISHED("UNFINISHED"),
	DELETED("DELETED");
	
	private final String value;
	
	EnumStatusWork(String value) {	
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}	
	
}
