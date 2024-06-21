package com.focusedapp.smartstudyhub.exception;

import lombok.Getter;

@Getter
public class AccountBannedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final String path;
	private final Integer numberDateBanned;

	public AccountBannedException(String message, String path, Integer numberDateBanned) {
		super(message);
		this.path = path;
		this.numberDateBanned = numberDateBanned;
	}
	
}
