package com.spm.main.data;

import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import se.michaelthelin.spotify.model_objects.specification.Image;

public class convertAwtToSwt extends Thread {
	Image image;
	BufferedImage imgTemp;

	public convertAwtToSwt(Image i, BufferedImage t) {
		this.image = i;
		this.imgTemp = t;
	}

	public void run() {
		
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				System.out.println("Well it made it to here... pt 2");
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

				PlaylistObj.setSwtImg(imagesTemp);
			}
		});
	}

}
