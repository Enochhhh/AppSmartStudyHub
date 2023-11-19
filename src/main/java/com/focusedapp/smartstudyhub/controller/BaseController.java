package com.focusedapp.smartstudyhub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.service.AuthenticationService;

public class BaseController {
	
	@Autowired
	AuthenticationService authenticationService;
	
	/**
	 * Get Authenticated User
	 * 
	 * @return
	 */
	public User getAuthenticatedUser() {
		
		User user = authenticationService.getAuthenticatedUser();
		
		if (user == null) {
			throw new RuntimeException("Login is required");
		}
		return user;
	}
	
	/**
	 * Create ResponseEntity default
	 * 
	 * @param <T>
	 * @param obj
	 * @return
	 */
	public <T> ResponseEntity<T> createResponseEntity(T obj) {
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}
	
	/**
	 * Create ResponseEntity with HttpStatus
	 * 
	 * @param <T>
	 * @param obj
	 * @param status
	 * @return
	 */
	public <T> ResponseEntity<T> createResponseEntity(T obj, HttpStatus status) {
		return new ResponseEntity<>(obj, status);
	}

}
