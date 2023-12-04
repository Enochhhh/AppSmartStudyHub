package com.focusedapp.smartstudyhub.util.enumerate;

public enum EnumPriority {

	HIGH("HIGH"),
	NORMAL("NORMAL"),
	LOW("LOW"),
	NONE("NONE");
	
	private final String value;
	
	EnumPriority(String value) {	
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}	
	
}
