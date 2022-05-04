package com.spm.main;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ota {

	public static void openToAuth() {
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
