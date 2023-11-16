package com.focusedapp.smartstudyhub.exception;

import lombok.Getter;

@Getter
public class AccountDeletedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final String path;

	public AccountDeletedException(String message, String path) {
		super(message);
		this.path = path;
	}
	
}
