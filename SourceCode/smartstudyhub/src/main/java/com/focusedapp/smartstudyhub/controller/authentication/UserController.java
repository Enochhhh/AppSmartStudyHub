package com.focusedapp.smartstudyhub.controller.authentication;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/mobile/v1/user")
public class UserController {
	@GetMapping("/demo")
	public ResponseEntity<String> sayHello() {
		return ResponseEntity.ok("Hi anh trai");
	}
}
