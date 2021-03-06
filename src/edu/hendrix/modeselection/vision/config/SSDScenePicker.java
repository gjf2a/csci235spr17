package edu.hendrix.modeselection.vision.config;

import java.io.File;
import java.io.IOException;

import edu.hendrix.modeselection.util.Util;
import edu.hendrix.modeselection.vision.AdaptedYUYVImage;
import edu.hendrix.modeselection.vision.BitImage;
import edu.hendrix.modeselection.vision.config.VisionBot;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;

public class SSDScenePicker extends VisionBot {
	public static final String FILENAME = "ssd1.txt";
	
	public static void main(String[] args) {
		new SSDScenePicker().run();
	}
	
	private AdaptedYUYVImage picked;
	
	@Override
	public BitImage processImage(AdaptedYUYVImage img) {
		BitImage show = BitImage.intensityView(img);
		if (Button.ENTER.isDown()) {
			picked = img;
		}
		return show;
	}

	@Override
	public void displayFinalInfo() {
		if (picked == null) {
			LCD.drawString("Finished; no selection", 0, 3); 
		} else {
			try {
				Util.stringToFile(new File(FILENAME), picked.toString());
				LCD.drawString("Saved:" + FILENAME, 0, 3);
			} catch (IOException e) {
				LCD.drawString("Fail:" + FILENAME, 0, 3);
			}
		}
	}
}
