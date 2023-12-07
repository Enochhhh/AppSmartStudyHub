package com.focusedapp.smartstudyhub.util.enumerate;

public enum EnumStatusWork {
	
	TODAY("TODAY"),
	TOMORROW("TOMORROW"),
	THISWEEK("THISWEEK"),
	OUTOFDATE("OUTOFDATE"),
	NEXT7DAY("NEXT7DAY"),
	SOMEDAY("SOMEDAY"),
	PARTICULARDAY("PARTICULARDAY");
	
	private final String value;
	
	EnumStatusWork(String value) {	
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}	
	
}
