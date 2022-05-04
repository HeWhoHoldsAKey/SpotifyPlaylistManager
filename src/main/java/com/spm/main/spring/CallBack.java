package com.spm.main.spring;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spm.main.SpotifyHdlr;

@RestController
@RequestMapping(path = "callback")
public class CallBack {
	
	@GetMapping
	public void callback(HttpServletRequest req) {
		String code = req.getQueryString().substring(5);
		SpotifyHdlr.setCredentials(code);
	}
	
	
}
