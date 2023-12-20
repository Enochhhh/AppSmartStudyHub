package com.focusedapp.smartstudyhub.util.enumerate;

public enum EnumTypeFile {

	USER("USER"),
	SOUNDCONCENTRATION("SOUNDCONCENTRATION"),
	SOUNDDONE("SOUNDDONE"),
	THEME("THEME");
	
	private final String value;
	
	EnumTypeFile(String value) {	
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}	
}
