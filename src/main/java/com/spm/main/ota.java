package com.spm.main;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ota {

	//You click a login button. or sum and it takes you here idk it just works.
	public static void openToAuth() {
		//THIS... THIS HORRID TERRIBLE LINE OF CODE... god it brings me rage
		System.out.println(java.awt.GraphicsEnvironment.isHeadless());
		Desktop d = Desktop.getDesktop();
		try {
			d.browse(new URI("http://localhost:8080/login"));
		} catch (IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
