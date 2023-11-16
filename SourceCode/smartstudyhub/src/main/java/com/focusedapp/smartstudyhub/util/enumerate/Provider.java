package com.focusedapp.smartstudyhub.util.enumerate;

public enum Provider {
	
	LOCAL("local"),
	GOOGLE("google"),
	GITHUB("github"),
	FACEBOOK("facebook");
	
	private final String value;
	
	private Provider(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}	
}
