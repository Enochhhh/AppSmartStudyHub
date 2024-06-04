package com.focusedapp.smartstudyhub.util.enumerate;

public enum MessageType {

	CHAT("CHAT"),
	JOIN("JOIN"),
	LEAVE("LEAVE");
	
	private final String value;
	
	MessageType(String value) {	
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
