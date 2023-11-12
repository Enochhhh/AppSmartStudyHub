package com.focusedapp.smartstudyhub.util.enumerate;

public enum Provider {
	
	LOCAL("LOCAL"),
	GOOGLE("GOOGLE");
	
	private final String value;
	
	private Provider(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}	
}
