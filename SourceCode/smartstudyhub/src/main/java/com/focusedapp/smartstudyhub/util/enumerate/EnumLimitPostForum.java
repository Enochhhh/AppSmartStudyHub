package com.focusedapp.smartstudyhub.util.enumerate;

public enum EnumLimitPostForum {

	PUBLIC("PUBLIC"),
	PRIVATE("PRIVATE"),
	FRIEND("FRIEND"),
	CUSTOM("CUSTOM");
	
	private final String value;
	
	EnumLimitPostForum(String value) {	
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}	
	
}
