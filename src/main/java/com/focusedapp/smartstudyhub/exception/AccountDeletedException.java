package com.focusedapp.smartstudyhub.exception;

import lombok.Getter;

@Getter
public class AccountDeletedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final String path;
	private final Integer numberDateDeleted;

	public AccountDeletedException(String message, String path, Integer numberDateDeleted) {
		super(message);
		this.path = path;
		this.numberDateDeleted = numberDateDeleted;
	}
	
}
