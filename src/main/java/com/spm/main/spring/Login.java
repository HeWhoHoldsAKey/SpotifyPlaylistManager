package com.spm.main.spring;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "login")
public class Login {
	
	
	@GetMapping
	public ResponseEntity<Object> loginPage() {
		return ResponseEntity.status(HttpStatus.FOUND)
		        .location(AuthHandeling.getUri())
		        .build();
	}
}
