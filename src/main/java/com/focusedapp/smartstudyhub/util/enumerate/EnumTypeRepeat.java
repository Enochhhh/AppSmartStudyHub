package com.focusedapp.smartstudyhub.util.enumerate;

public enum EnumTypeRepeat {
	
	DAILY("DAILY"),
	ORDINARY_DAY("ORDINARY DAY"),
	WEEKEND("WEEKEND"),
	WEEKLY("WEEKLY"),
	ONCE_EVERY_TWO_WEEKS("ONE EVERY TWO WEEKS"),
	MONTHLY("MONTHLY"),
	EVERY_THREE_MONTH("EVERY THREE MONTH"),
	EVERY_SIX_MONTH("EVERY SIX MONTH"),
	CUSTOM("CUSTOM");

	private final String value;
	
	EnumTypeRepeat(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public static EnumTypeRepeat getByValue(String value) {
		return EnumTypeRepeat.valueOf(value);
	}
}
