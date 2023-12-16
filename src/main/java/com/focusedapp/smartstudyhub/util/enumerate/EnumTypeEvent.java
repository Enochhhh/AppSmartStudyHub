package com.focusedapp.smartstudyhub.util.enumerate;

public enum EnumTypeEvent {
	EVENT("EVENT"),
	MEETING("MEETING");
	
	private final String value;
	
	EnumTypeEvent(String value) {	
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}	
}
