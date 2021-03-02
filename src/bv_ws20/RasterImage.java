// BV Ue4 SS2020 Vorgabe
//
// Copyright (C) 2017 by Klaus Jung
// All rights reserved.
// Date: 2017-07-16

package bv_ws20;

import java.io.File;
import java.util.Arrays;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class RasterImage {
	
	private static final int gray  = 0xffa0a0a0;

	public int[] argb;	// pixels represented as ARGB values in scanline order
	public int width;	// image width in pixels
	public int height;	// image height in pixels
	
	public RasterImage(int width, int height) {
		// creates an empty RasterImage of given size
		this.width = width;
		this.height = height;
		argb = new int[width * height];
		Arrays.fill(argb, gray);
	}
	
	public RasterImage(RasterImage image) {
		// copy constuctor
		this.width = image.width;
		this.height = image.height;
		argb = image.argb.clone();
	}
	
	public RasterImage(File file) {
		// creates an RasterImage by reading the given file
		Image image = null;
		if(file != null && file.exists()) {
			image = new Image(file.toURI().toString());
		}
		if(image != null && image.getPixelReader() != null) {
			width = (int)image.getWidth();
			height = (int)image.getHeight();
			argb = new int[width * height];
			image.getPixelReader().getPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), argb, 0, width);
		} else {
			// file reading failed: create an empty RasterImage
			this.width = 256;
			this.height = 256;
			argb = new int[width * height];
			Arrays.fill(argb, gray);
		}
	}
	
	public RasterImage(ImageView imageView) {
		// creates a RasterImage from that what is shown in the given ImageView
		Image image = imageView.getImage();
		width = (int)image.getWidth();
		height = (int)image.getHeight();
		argb = new int[width * height];
		image.getPixelReader().getPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), argb, 0, width);
	}
	
	public void setToView(ImageView imageView) {
		// sets the current argb pixels to be shown in the given ImageView
		if(argb != null) {
			WritableImage wr = new WritableImage(width, height);
			PixelWriter pw = wr.getPixelWriter();
			pw.setPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), argb, 0, width);
			imageView.setImage(wr);
		}
	}
	
	
	// image point operations to be added here

	public void convertToGray() {
		// TODO: convert the image to grayscale
		for(int y=0; y < height;y++){
			for(int x=0; x < width;x++){
				int pos = y * width + x;

				int r = (argb[pos] >> 16) & 0xff;
				int g = (argb[pos] >>  8) & 0xff;
				int b =  argb[pos]        & 0xff;

				int Y = (r+g+b)/3;
				argb[pos] = (0xFF<<24) | (Y<<16) | (Y<<8) | Y;
			}
		}
		
	}
	
	public void applyToneCurve(ToneCurve curve) {
		// TODO: apply the gray value mapping using the tone curve's mappedGray() method
		for(int y=0; y < height;y++){
			for(int x=0; x < width;x++){
				int pos = y * width + x;

				int r = (argb[pos] >> 16) & 0xff;
				int g = (argb[pos] >>  8) & 0xff;
				int b =  argb[pos]        & 0xff;

				int Y = (r+g+b)/3;
				int curved = curve.mappedGray(Y);

				argb[pos] = (0xFF<<24) | (curved<<16) | (curved<<8) | curved;

			}
		}
	}
	
	
}
