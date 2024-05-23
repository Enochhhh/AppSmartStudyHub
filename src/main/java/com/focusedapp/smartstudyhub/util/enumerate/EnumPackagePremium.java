package com.focusedapp.smartstudyhub.util.enumerate;

public enum EnumPackagePremium {

	THREEMONTHS("THREEMONTHS"),
	ONEYEAR("ONEYEAR");

	private final String value;
	
	private EnumPackagePremium(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}	
}
