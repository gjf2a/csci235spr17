package edu.hendrix.modeselection.movies;

import edu.hendrix.modeselection.util.ButtonDriver;
import edu.hendrix.modeselection.util.CycleTimer;
import edu.hendrix.modeselection.util.Logger;
import edu.hendrix.modeselection.vision.AdaptedYUYVImage;
import edu.hendrix.modeselection.vision.BitImage;
import edu.hendrix.modeselection.vision.config.VisionBot;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;

public class MovieRecorder extends VisionBot {
	
	private MovieSaver saver;
	private ButtonDriver driver = new ButtonDriver(Motor.A, Motor.D);
	private CycleTimer frameRate = new CycleTimer();
	
	public MovieRecorder(String movieName) {
		saver = new MovieSaver(movieName);
	}

	@Override
	public BitImage processImage(AdaptedYUYVImage img) {
		driver.drive(() -> {
			frameRate.start();
			try {
				saver.save(img);
			} catch (Exception e) {
				Logger.EV3Log.format("Trouble saving image; frame %d", saver.getNumSaved());
			}
			frameRate.bumpCycle();
		});
		return BitImage.intensityView(img);
	}
	
	@Override
	public void displayFinalInfo() {
		super.displayFinalInfo();
		LCD.drawString(String.format("frames: %d", saver.getNumSaved()), 0, 4);
	}
}
