package com.focusedapp.smartstudyhub.exception;

import lombok.Getter;

@Getter
public class NoRightToPerformException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final String path;
	
	public NoRightToPerformException(String message, String path) {
		super(message);
		this.path = path;
	}
}
