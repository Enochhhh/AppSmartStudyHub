package com.focusedapp.smartstudyhub.exception;

import lombok.Getter;

@Getter
public class ValueExistedException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	private final String path;

	public ValueExistedException(String message, String path) {
		super(message);
		this.path = path;
	}
	
}
