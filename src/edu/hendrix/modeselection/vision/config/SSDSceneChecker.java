package edu.hendrix.modeselection.vision.config;

import java.io.File;
import java.io.FileNotFoundException;

import edu.hendrix.modeselection.util.Util;
import edu.hendrix.modeselection.vision.AdaptedYUYVImage;
import edu.hendrix.modeselection.vision.SSDSceneFlagger;
import edu.hendrix.modeselection.vision.distances.Euclidean;
import lejos.hardware.lcd.LCD;

public class SSDSceneChecker extends BasicVisionBot {
	public static final String FILENAME = "ssd1.txt";

	private AdaptedYUYVImage ref;
	private double total, min, max;
	private final static Euclidean DIST_FUNC = new Euclidean();
	
	public static void main(String[] args) throws FileNotFoundException {
		SSDSceneChecker checker = new SSDSceneChecker(FILENAME);
		checker.run();
	}
	
	public SSDSceneChecker(String filename) throws FileNotFoundException {
		ref = Util.fileToObject(new File(filename), s -> SSDSceneFlagger.adapt(AdaptedYUYVImage.fromString(s)));
		min = Double.MAX_VALUE;
		max = Double.MIN_VALUE;
		total = 0;
	}
	
	@Override
	public void grabImage(AdaptedYUYVImage img) {
		double dist = DIST_FUNC.distance(ref, SSDSceneFlagger.adapt(img));
		if (dist < min) {min = dist;}
		if (dist > max) {max = dist;}
		total += dist;
		LCD.drawString(String.format("%16.6e", dist), 0, 3);
	}
	
	@Override
	public void displayFinalInfo() {
		displayFrameRate(2);
		LCD.drawString(String.format("min: %d", min), 0, 3);
		LCD.drawString(String.format("max: %d", max), 0, 4);
		LCD.drawString(String.format("mean:%d", total/getCycles()), 0, 5);		
	}
}
