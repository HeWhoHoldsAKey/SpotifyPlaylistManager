package com.spm.main;

import java.io.IOException;
import java.net.URI;

import org.apache.hc.core5.http.ParseException;

import com.spm.main.app.MainWindow;
import com.spm.main.spring.AuthHandeling;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

public class SpotifyHdlr {
	//Yeah so this is fun.. Oauth 2.0 authentication is great...
	private final static String client_id = "f9c527b8e9c54d399a192f6dbb5fea0d";
	private final static String clientSecret = "ef2dc527f7a64222af08276edbf079cd";
	private static String code = "";
	
	//Callback stuff
	private final static URI redirectUri = SpotifyHttpManager.makeUri("http://localhost:8080/callback");

	//WHY DOES EVERYTHING HAVE A BUILDER >:(
	private final static SpotifyApi spotifyApi = new SpotifyApi.Builder().setClientId(client_id)
			.setClientSecret(clientSecret).setRedirectUri(redirectUri).build();

	//Yes. i want ALL permissions
	static AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
			.scope("user-read-private," + "user-read-email," + "ugc-image-upload," + "user-modify-playback-state,"
					+ "user-read-playback-state," + "user-read-currently-playing," + "user-follow-modify,"
					+ "user-follow-read," + "user-read-recently-played," + "user-read-playback-position,"
					+ "user-top-read," + "playlist-read-collaborative," + "playlist-modify-public,"
					+ "playlist-read-private," + "playlist-modify-private," + "app-remote-control," + "streaming,"
					+ "user-library-modify," + "user-library-read")
			.show_dialog(true).build();

	//GeT ThE uRi
	public static URI getURI() {

		return authorizationCodeUriRequest.getUri();
	}
	
	//This one is actually VERY USEFUL
	public static SpotifyApi getSAPI() {
		return spotifyApi;
	}
	
	//Yeah, now reset the credentials... why BECAUSE OAUTH 2.0 AUTH HATES YOU... at least its mildly secure
	public static void setCredentials(String cd) {
		code = cd;
		final AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(code).build();
		AuthorizationCodeCredentials authorizationCodeCredentials = null;
		try {
			authorizationCodeCredentials = authorizationCodeRequest.execute();
		} catch (ParseException | SpotifyWebApiException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Set access and refresh token for further "spotifyApi" object usage
		spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
		spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

		System.out.println("Access Token: " + spotifyApi.getAccessToken());
		System.out.println("Refreash Token: " + spotifyApi.getRefreshToken());

		AuthHandeling.stopSpring();

		System.out.println("Alot Happened...");
		MainWindow.initUser();
		MainWindow.isSpringOn(false);
		
	}
}
