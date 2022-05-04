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

import com.spm.main.SpotifyHdlr;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Image;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.requests.data.playlists.GetPlaylistCoverImageRequest;

public class PlaylistObj {
	SpotifyApi sp = SpotifyHdlr.getSAPI();
	PlaylistSimplified playlist;
	Image[] playlistImagesApi;
	org.eclipse.swt.graphics.Image[] playlistImages;
	GetPlaylistCoverImageRequest getPlaylistCoverImageRequest;

	public PlaylistObj(PlaylistSimplified p) {
		this.playlist = p;

		getPlaylistCoverImageRequest = sp.getPlaylistCoverImage(playlist.getId()).build();
		try {
			playlistImagesApi = getPlaylistCoverImageRequest.execute();
		} catch (ParseException | SpotifyWebApiException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		initPlaylistImages();
	}

	public String getPlaylistName() {
		return playlist.getName();
	}

	public void initPlaylistImages() {
		ArrayList<org.eclipse.swt.graphics.Image> imagesTemp = new ArrayList<org.eclipse.swt.graphics.Image>();

		// I dont wanna talk about it...
		for (Image i : playlistImagesApi) {
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

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (imgTemp != null) {
				if (imgTemp.getColorModel() instanceof DirectColorModel) {
					/*
					 * DirectColorModel colorModel =
					 * (DirectColorModel)bufferedImage.getColorModel(); PaletteData palette = new
					 * PaletteData( colorModel.getRedMask(), colorModel.getGreenMask(),
					 * colorModel.getBlueMask()); ImageData data = new
					 * ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(),
					 * colorModel.getPixelSize(), palette); WritableRaster raster =
					 * bufferedImage.getRaster(); int[] pixelArray = new int[3]; for (int y = 0; y <
					 * data.height; y++) { for (int x = 0; x < data.width; x++) { raster.getPixel(x,
					 * y, pixelArray); int pixel = palette.getPixel(new RGB(pixelArray[0],
					 * pixelArray[1], pixelArray[2])); data.setPixel(x, y, pixel); } }
					 */
					DirectColorModel colorModel = (DirectColorModel) imgTemp.getColorModel();
					PaletteData palette = new PaletteData(colorModel.getRedMask(), colorModel.getGreenMask(),
							colorModel.getBlueMask());
					ImageData data = new ImageData(imgTemp.getWidth(), imgTemp.getHeight(), colorModel.getPixelSize(),
							palette);
					for (int y = 0; y < data.height; y++) {
						for (int x = 0; x < data.width; x++) {
							int rgb = imgTemp.getRGB(x, y);
							int pixel = palette.getPixel(new RGB((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF));
							data.setPixel(x, y, pixel);
							if (colorModel.hasAlpha()) {
								data.setAlpha(x, y, (rgb >> 24) & 0xFF);
							}
						}
					}
					imagesTemp.add(new org.eclipse.swt.graphics.Image(null, data));
				} else if (imgTemp.getColorModel() instanceof IndexColorModel) {
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
					ImageData data = new ImageData(imgTemp.getWidth(), imgTemp.getHeight(), colorModel.getPixelSize(),
							palette);
					data.transparentPixel = colorModel.getTransparentPixel();
					WritableRaster raster = imgTemp.getRaster();
					int[] pixelArray = new int[1];
					for (int y = 0; y < data.height; y++) {
						for (int x = 0; x < data.width; x++) {
							raster.getPixel(x, y, pixelArray);
							data.setPixel(x, y, pixelArray[0]);
						}
					}
					imagesTemp.add(new org.eclipse.swt.graphics.Image(null, data));
				} else if (imgTemp.getColorModel() instanceof ComponentColorModel) {
					ComponentColorModel colorModel = (ComponentColorModel) imgTemp.getColorModel();
					// ASSUMES: 3 BYTE BGR IMAGE TYPE
					PaletteData palette = new PaletteData(0x0000FF, 0x00FF00, 0xFF0000);
					ImageData data = new ImageData(imgTemp.getWidth(), imgTemp.getHeight(), colorModel.getPixelSize(),
							palette);
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
					imagesTemp.add(new org.eclipse.swt.graphics.Image(null, data));
				}

			}
		}

		playlistImages = imagesTemp.toArray(new org.eclipse.swt.graphics.Image[0]);
	}

	public org.eclipse.swt.graphics.Image[] getPlaylistImg() {
		return playlistImages;
	}
}