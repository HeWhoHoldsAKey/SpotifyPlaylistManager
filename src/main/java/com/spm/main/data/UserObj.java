package com.spm.main.data;

import java.io.IOException;

import org.apache.hc.core5.http.ParseException;

import com.spm.main.SpotifyHdlr;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.requests.data.playlists.GetListOfCurrentUsersPlaylistsRequest;
import se.michaelthelin.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.model_objects.specification.User;

public class UserObj {
	SpotifyApi sp = SpotifyHdlr.getSAPI();
	static User user;
	static Paging<PlaylistSimplified> playlists;
	GetCurrentUsersProfileRequest currentUserProfileRequest = sp.getCurrentUsersProfile().build();
	GetListOfCurrentUsersPlaylistsRequest currentUsersPlaylistRequest = sp.getListOfCurrentUsersPlaylists().limit(50).build();
	PlaylistSimplified[] playlistsList;
	public UserObj() {
		try {
			user = currentUserProfileRequest.execute();
			playlists = currentUsersPlaylistRequest.execute();
			playlistsList = playlists.getItems();
			
		} catch (ParseException | SpotifyWebApiException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getName() {
		return user.getDisplayName();
	}
	
//	public Paging<PlaylistSimplified> getUsersPlaylists() {
//		return playlists;
//	}
	
	public PlaylistSimplified[] getUsersPlaylistsList() {
		return playlistsList;
	}
}
