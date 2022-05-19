package com.spm.main.spring;

import java.net.URI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.spm.main.ota;
//Yeah... this essentially is a web app. pretty cool. is it running on multiple threads so the gui dosnt lock up? no.
@SpringBootApplication
public class AuthHandeling {
	static URI uri;
	static ConfigurableApplicationContext ctx;

	public void runAuthHandeling(String[] args, URI uri) {
		//funny story... WITHOUT THIS LINE OF CODE IT ASSUMES FOR WHAT EVER GOD FORSAKEN REASON THEY YOU DONT HAVE A MONITOR OR MOUSE OR ANYTHING... AND IT JUST BREAKS...
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
