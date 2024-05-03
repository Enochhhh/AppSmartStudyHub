package com.focusedapp.smartstudyhub.util.enumerate;

public enum EnumUnitRepeat {
	
	DAY("DAY"),
	WEEK("WEEK"),
	MONTH("MONTH"),
	YEAR("YEAR");
	
	private final String value;
	
	EnumUnitRepeat(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
}
