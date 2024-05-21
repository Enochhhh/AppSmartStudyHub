package com.focusedapp.smartstudyhub.exception;

import lombok.Getter;

@Getter
public class EnvironmentVariableNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EnvironmentVariableNotFoundException(String message) {
		super(message);
	}
}
