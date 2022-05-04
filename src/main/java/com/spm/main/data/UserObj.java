package com.spm.main.data;

import java.io.IOException;

import org.apache.hc.core5.http.ParseException;

import com.spm.main.SpotifyHdlr;
import com.spm.main.app.MainWindow;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;
import se.michaelthelin.spotify.model_objects.specification.User;

public class UserObj {
	SpotifyApi sp = SpotifyHdlr.getSAPI();
	static User user;
	GetCurrentUsersProfileRequest currentUserProfileRequest = sp.getCurrentUsersProfile().build();
	
	Boolean needsUpdate = false;
	
	public UserObj() {
		needsUpdate = true;
		try {
			user = currentUserProfileRequest.execute();
		} catch (ParseException | SpotifyWebApiException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	public String getName() {
		return user.getDisplayName();
	}
	
	public Boolean updateStat() {
		return needsUpdate;
	}
	
	
	
	
	
	
}
