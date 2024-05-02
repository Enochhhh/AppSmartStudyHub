package com.focusedapp.smartstudyhub.exception;

import lombok.Getter;

@Getter
public class ISException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	Exception e;
	
	public ISException(Exception e) {
		this.e = e;
	}
}
