package com.spm.main.data;

import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;

import org.apache.hc.core5.http.ParseException;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import com.spm.main.SpotifyHdlr;
import com.spm.main.app.MainWindow;

import net.coobird.thumbnailator.Thumbnails;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Image;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.playlists.GetPlaylistCoverImageRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetPlaylistsItemsRequest;
import se.michaelthelin.spotify.requests.data.tracks.GetTrackRequest;

public class PlaylistObj extends Thread {
	SpotifyApi sp = SpotifyHdlr.getSAPI();
	PlaylistSimplified playlist;
	Image[] playlistImagesApi;
	org.eclipse.swt.graphics.Image playlistImages;
	GetPlaylistCoverImageRequest getPlaylistCoverImageRequest;
	GetPlaylistsItemsRequest getPlaylistItemsRequest;
	GetTrackRequest getTrackRequest;
	Paging<PlaylistTrack> playlistTrackPaging;
	ArrayList<Track> playlistTracks = new ArrayList<Track>();
	int imgScale;
	boolean tracksLoaded = false;

	TrackObj[] tracks;

	public PlaylistObj(PlaylistSimplified p, int s) {
		this.playlist = p;
		this.imgScale = s - 5;
		this.getPlaylistCoverImageRequest = sp.getPlaylistCoverImage(playlist.getId()).build();

		try {
			this.playlistImagesApi = getPlaylistCoverImageRequest.execute();
		} catch (ParseException | SpotifyWebApiException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void run() {

		Display.getDefault().asyncExec(new Runnable() {
			public void run() {

				Image i = playlistImagesApi[0];
				// I dont wanna talk about it...
				URL url = null;
				BufferedImage imgTemp = null;
				try {
					url = new URL(i.getUrl());
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					imgTemp = ImageIO.read(url);
					imgTemp = Thumbnails.of(imgTemp).size(imgScale, imgScale).keepAspectRatio(false).asBufferedImage();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("Immage:" + " Failed to get url");
					e.printStackTrace();

				}

				System.out.println("Well it made it to here... on playlist:" + getPlaylistName());
				org.eclipse.swt.graphics.Image imagesTemp = null;

				if (imgTemp != null) {
					if (imgTemp.getColorModel() instanceof DirectColorModel) {
						System.out.println("Went into first Check Direct color mo0del");
						DirectColorModel colorModel = (DirectColorModel) imgTemp.getColorModel();
						PaletteData palette = new PaletteData(colorModel.getRedMask(), colorModel.getGreenMask(),
								colorModel.getBlueMask());
						ImageData data = new ImageData(imgTemp.getWidth(), imgTemp.getHeight(),
								colorModel.getPixelSize(), palette);
						for (int y = 0; y < data.height; y++) {
							for (int x = 0; x < data.width; x++) {
								int rgb = imgTemp.getRGB(x, y);
								int pixel = palette
										.getPixel(new RGB((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF));
								data.setPixel(x, y, pixel);
								if (colorModel.hasAlpha()) {
									data.setAlpha(x, y, (rgb >> 24) & 0xFF);
								}
							}
						}
						imagesTemp = new org.eclipse.swt.graphics.Image(null, data);
					} else if (imgTemp.getColorModel() instanceof IndexColorModel) {
						System.out.println("Went into Second Check IndexColorModel");
						IndexColorModel colorModel = (IndexColorModel) imgTemp.getColorModel();
						int size = colorModel.getMapSize();
						byte[] reds = new byte[size];
						byte[] greens = new byte[size];
						byte[] blues = new byte[size];
						colorModel.getReds(reds);
						colorModel.getGreens(greens);
						colorModel.getBlues(blues);
						RGB[] rgbs = new RGB[size];
						for (int i1 = 0; i1 < rgbs.length; i1++) {
							rgbs[i1] = new RGB(reds[i1] & 0xFF, greens[i1] & 0xFF, blues[i1] & 0xFF);
						}
						PaletteData palette = new PaletteData(rgbs);
						ImageData data = new ImageData(imgTemp.getWidth(), imgTemp.getHeight(),
								colorModel.getPixelSize(), palette);
						data.transparentPixel = colorModel.getTransparentPixel();
						WritableRaster raster = imgTemp.getRaster();
						int[] pixelArray = new int[1];
						for (int y = 0; y < data.height; y++) {
							for (int x = 0; x < data.width; x++) {
								raster.getPixel(x, y, pixelArray);
								data.setPixel(x, y, pixelArray[0]);
							}
						}
						imagesTemp = new org.eclipse.swt.graphics.Image(null, data);
					} else if (imgTemp.getColorModel() instanceof ComponentColorModel) {

						// Hits this one every time

						System.out.println("Went into Third Check ComponentColorModel");
						ComponentColorModel colorModel = (ComponentColorModel) imgTemp.getColorModel();
						// ASSUMES: 3 BYTE BGR IMAGE TYPE
						PaletteData palette = new PaletteData(0x0000FF, 0x00FF00, 0xFF0000);
						ImageData data = new ImageData(imgTemp.getWidth(), imgTemp.getHeight(),
								colorModel.getPixelSize(), palette);
						// data = data.scaledTo(imgScale -50, imgScale - 50);
						// This is valid because we are using a 3-byte Data model with no transparent
						// pixels
						data.transparentPixel = -1;
						WritableRaster raster = imgTemp.getRaster();
						int[] pixelArray = new int[3];
						for (int y = 0; y < data.height; y++) {
							for (int x = 0; x < data.width; x++) {
								raster.getPixel(x, y, pixelArray);
								int pixel = palette.getPixel(new RGB(pixelArray[0], pixelArray[1], pixelArray[2]));
								data.setPixel(x, y, pixel);
							}
						}
						imagesTemp = new org.eclipse.swt.graphics.Image(null, data);
					}

				}

				playlistImages = imagesTemp;

				// MainWindow.buildPlaylistTable();
				MainWindow.setPlaylistImg(playlistImages, getPlaylistName());

			}
		});

	}

	public String getPlaylistName() {
		return this.playlist.getName();
	}

	public org.eclipse.swt.graphics.Image getPlaylistImg() {
		return playlistImages;
	}
	
	public PlaylistObj getSelf() {
		return this;
	}

	public void loadTracks() {
		if (tracksLoaded) {
			return;
		} else {
			try {
				do {
					this.playlistTrackPaging = sp.getPlaylistsItems(playlist.getId()).limit(50).build().execute();
					for (PlaylistTrack pt : this.playlistTrackPaging.getItems()) {
						playlistTracks.add(sp.getTrack(pt.getTrack().getId()).build().execute());
					}
					sp.getPlaylistsItems(playlist.getId())
							.offset(this.playlistTrackPaging.getOffset() + this.playlistTrackPaging.getLimit());

				} while (this.playlistTrackPaging.getNext() != null);

				System.out.println(this.playlist.getName());
			} catch (ParseException | SpotifyWebApiException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public void reloadTracks() {
		this.tracksLoaded = false;
		loadTracks();
	}

	public ArrayList<Track> getTracks() {
		return playlistTracks;
	}

}
