package com.focusedapp.smartstudyhub.util.enumerate;

public enum EnumPriority {

	HIGH("HIGH"),
	NORMAL("NORMAL"),
	LOW("NORMAL");
	
	private final String value;
	
	EnumPriority(String value) {	
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}	
	
}
