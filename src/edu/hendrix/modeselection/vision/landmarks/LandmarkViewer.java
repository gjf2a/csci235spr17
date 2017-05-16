package edu.hendrix.modeselection.vision.landmarks;

import java.io.File;
import java.io.FileNotFoundException;

import edu.hendrix.modeselection.cluster.ShrinkingImageBSOC;
import edu.hendrix.modeselection.util.ButtonDriver;
import edu.hendrix.modeselection.util.Duple;
import edu.hendrix.modeselection.util.Util;
import edu.hendrix.modeselection.vision.AdaptedYUYVImage;
import edu.hendrix.modeselection.vision.config.BasicVisionBot;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;

public class LandmarkViewer extends BasicVisionBot {

	private ShrinkingImageBSOC bsoc;
	private ButtonDriver driver = new ButtonDriver(Motor.A, Motor.D);
	
	public LandmarkViewer(String filename) throws FileNotFoundException {
		bsoc = Util.fileToObject(new File(filename), ShrinkingImageBSOC::new);
	}
	
	@Override
	public void grabImage(AdaptedYUYVImage img) {
		Duple<Integer, Long> closest = bsoc.getClosestNodeDistanceFor(img);
		LCD.drawString(String.format("Node:%d     ", closest.getFirst()), 0, 0);
		LCD.drawString(String.format("Dist:%d     ", closest.getSecond()), 0, 1);
		driver.drive();
	}	
}
