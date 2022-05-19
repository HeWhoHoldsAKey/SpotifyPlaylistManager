package com.spm.main.spring;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spm.main.SpotifyHdlr;
//Fairly simple is the callback page. Gets the code from the url.
@RestController
@RequestMapping(path = "callback")
public class CallBack {
	
	@GetMapping
	public void callback(HttpServletRequest req) {
		//Gets the code here very simple
		String code = req.getQueryString().substring(5);
		SpotifyHdlr.setCredentials(code);
	}
	
	
}
