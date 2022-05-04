package com.spm.main.spring;

import java.net.URI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.spm.main.ota;

@SpringBootApplication
public class AuthHandeling {
	static URI uri;
	static ConfigurableApplicationContext ctx;

	public void runAuthHandeling(String[] args, URI uri) {
		System.setProperty("java.awt.headless", "false");
		AuthHandeling.uri = uri;
		ctx = SpringApplication.run(AuthHandeling.class, args);
		ota.openToAuth();
	}

	public static URI getUri() {
		return uri;
	}

	public static void stopSpring() {
		ctx.close();
		
		System.out.println("Server Shut Down");
	}
}
