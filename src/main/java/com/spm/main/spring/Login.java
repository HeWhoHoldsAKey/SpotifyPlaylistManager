package com.spm.main.spring;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "login")
public class Login {
	//The langing page that will redirect you straight to callback... Not too bad but was annoying
	
	@GetMapping
	public ResponseEntity<Object> loginPage() {
		return ResponseEntity.status(HttpStatus.FOUND)
		        .location(AuthHandeling.getUri())
		        .build();
	}
}
