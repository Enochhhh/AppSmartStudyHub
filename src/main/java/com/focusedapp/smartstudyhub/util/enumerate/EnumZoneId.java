package com.focusedapp.smartstudyhub.util.enumerate;

public enum EnumZoneId {
	
	ASIA_HOCHIMINH("Asia/Ho_Chi_Minh");

	private final String nameZone;
	
	private EnumZoneId(String nameZone) {
		this.nameZone = nameZone;
	}

	public String getNameZone() {
		return nameZone;
	}
}
