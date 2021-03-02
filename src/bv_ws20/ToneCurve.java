// BV Ue4 SS2020 Vorgabe
//
// Copyright (C) 2017 by Klaus Jung
// All rights reserved.
// Date: 2017-07-16

package bv_ws20;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.*;

public class ToneCurve {
	
	private static final int grayLevels = 256;
	
    private GraphicsContext gc;
    
    private int brightness = 0;
    private double gamma = 1.0;
    private double contrast = 0;
    
    private int[] grayTable = new int[grayLevels];

	public ToneCurve(GraphicsContext gc) {
		this.gc = gc;
	}
	
	public void setBrightness(int brightness) {
		this.brightness = brightness;
		updateTable();
	}

	public void setContrast(double contrast){
		this.contrast = contrast;
		updateTable();
	}

	public void setGamma(double gamma) {
		this.gamma = gamma;
		updateTable();
	}



	private void updateTable() {
		// TODO: Fill the grayTable[] array to map gray input values to gray output values.
		// It will be used as follows: grayOut = grayTable[grayIn].
		//
		// Use brightness and gamma values.
		// See "Gammakorrektur" at slide no. 18 of 
		// http://home.htw-berlin.de/~barthel/veranstaltungen/GLDM/vorlesungen/04_GLDM_Bildmanipulation1_Bildpunktoperatoren.pdf
		//
		// First apply the brightness change, afterwards the gamma correction.
		for(int grayIn = 0; grayIn < grayLevels; grayIn++){
			grayTable[grayIn] = grayIn; //fill the array
			int grayOut = grayTable[grayIn];

			grayOut += brightness;
			grayOut += contrast*(grayOut-128);
			if(grayOut < 0){
				grayOut = 0;
			}
			if(grayOut > 255){
				grayOut = 255;
			}
			//formel = 255 mal x hoch 1/y das alles durch 255 hoch 1/y
			grayOut = (int)((255 * Math.pow(grayOut, 1/gamma)) / Math.pow(255, 1/gamma));

			grayTable[grayIn] = grayOut;
		}
		
		
	}
	
	public int mappedGray(int inputGray) {
		return grayTable[inputGray];
	}
	
	public void draw() {
		gc.clearRect(0, 0, grayLevels, grayLevels);
		gc.setStroke(Color.BLUE);
		gc.setLineWidth(3);

		// TODO: draw the tone curve into the gc graphic context
		// Remark: This is some dummy code to give you an idea for graphics drawing with pathes

		gc.beginPath();
		for (int i = 0; i < grayLevels; i++) {
			gc.lineTo(i, 255 - grayTable[i]);
		}
		gc.stroke();
	}

	
}
