// BV Ue4 SS2020 Vorgabe
//
// Copyright (C) 2019 by Klaus Jung
// All rights reserved.
// Date: 2019-05-12

package bv_ws20;

import bv_ws20.ImageAnalysisAppController.StatsProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Arrays;

public class Histogram {

	private static final int grayLevels = 256;
	
    private GraphicsContext gc;
    private int maxHeight;
    
    private int[] histogram = new int[grayLevels];

	int level =0, min =0, max =0,median=0;
	double variance=0, mean =0,entropy=0;

	public Histogram(GraphicsContext gc, int maxHeight) {
		this.gc = gc;
		this.maxHeight = maxHeight;
	}
	
	public void update(RasterImage image, Point2D ellipseCenter, Dimension2D ellipseSize, int selectionMax, ObservableList<StatsProperty> statsData) {
		// TODO: calculate histogram[] out of the gray values of the image for pixels inside the given ellipse
		for(int i=0; i < grayLevels;i++) {
			histogram[i] = 0; // remove this line when implementing the proper calculations
		}
		int CX = (int)Math.round(ellipseCenter.getX());
		int CY = (int)Math.round(ellipseCenter.getY());
		int Eheight = (int)Math.round(ellipseSize.getHeight());
		int Ewidth = (int)Math.round(ellipseSize.getWidth());

			for(int x=0; x < image.width;x++){
				for(int y=0; y < image.height;y++){
				int pos = y * image.width + x;

				if(1 > (Math.pow(x - CX,2) / Math.pow(Ewidth,2)) + (Math.pow(y - CY , 2) / Math.pow(Eheight,2))){
						int b =  image.argb[pos] & 0xff;
						histogram[b]++;
					}

				}
			}
		// Remark: Please ignore selectionMax and statsData in Exercise 4. It will be used in Exercise 5.
		 	level =0;
			min =0;
			max =0;
			median=0;
			variance=0;
			mean =0;
			entropy=0;

			if(selectionMax == -1){
				selectionMax = 255;
			}

		for(StatsProperty property : statsData){
			switch (property) {

				case Level:
					for(int i = 0; i < selectionMax+1; i++){
						level += histogram[i];
					}
					property.setValueInPercent((double)level/image.argb.length);
					break;

				case Minimum:

					for (int i = selectionMax; i >= 0; i--) {
						if (histogram[i] != 0) {
							min = i;
						}
					}
					property.setValue(min);
					break;

				case Maximum:
					for (int i = 0; i <= selectionMax; i++) {
						if (histogram[i] != 0) {
							max = i;
						}
					}
					property.setValue(max);
					break;

				case Mean:
					for(int i =0; i < selectionMax;i++){
						mean += i * histogram[i];
					}
					mean = mean/image.argb.length;
					property.setValue(mean);
					break;

				case Variance:
					for(int i =0; i < selectionMax; i++){
						variance += ((i-mean)*(i-mean)) * (histogram[i]/(double)image.argb.length);
					}
					property.setValue(variance);
					break;

				case Median:

					int pixel = 0, pixelcount =0, temp = 0;

					for(int i =0; i <= selectionMax; i++) {
						pixel += histogram[i];
					}
					int[] toSort = new int[pixel];

					if(pixel >0) {
						for (int j = 0; j <= selectionMax; j++) {
							pixelcount += histogram[j];
							for (int i = temp; i < pixelcount; i++) {
								toSort[i] = j;
								temp++;
							}
						}
						Arrays.sort(toSort);
						median = toSort[toSort.length / 2];
						property.setValue(median);
					}else{
						property.setValue(0);
					}
					break;

				case Entropy:
					// p(j) * log2p(j)
					double j = image.argb.length;

					for(int i = 0; i < selectionMax;i++){
					double p = histogram[i] / j;
						if( histogram[i] > 0){
					entropy += -p * (Math.log(p) / Math.log(2));
						}
					}
					property.setValue(entropy);
					break;
			}
		}
	}
	    
	public void draw() {
		gc.clearRect(0, 0, grayLevels, maxHeight);
		gc.setLineWidth(1);
		// TODO: draw histogram[] into the gc graphic context
		// Remark: This is some dummy code to give you an idea for graphics drawing		
		double shift = 0.5;
		// note that we need to add 0.5 to all coordinates to get a one pixel thin line
		gc.setStroke(Color.BLACK);
		int max = histogram[0];
		for(int H = 0; H < histogram.length;H++){
			if(max < histogram[H]){
				max = histogram[H];
			}

		}
		for(int i = 0; i < grayLevels;i++){
			if(max >0 ){
			gc.strokeLine(i + shift, 200 - (int)(histogram[i] * 200/(max)), i + shift, 200 + shift);
		}
		}


	}
    
}
