package com.focusedapp.smartstudyhub.util.enumerate;

public enum EnumTypeReact {
	LIKE("LIKE"),
	LOVE("LOVE"),
	SMILE("SMILE"),
	SAD("SAD"),
	WOW("WOW"),
	ANGRY("ANGRY");
	
	private final String value;
	
	EnumTypeReact(String value) {	
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}	
}
