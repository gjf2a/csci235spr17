package ideas.vision.features.config;

import java.io.File;
import java.io.IOException;

import ideas.vision.features.FAST;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import modeselection.util.Util;
import modeselection.vision.AdaptedYUYVImage;
import modeselection.vision.BitImage;
import modeselection.vision.config.VisionBot;

public class FASTScenePicker extends VisionBot {
	public static final String FILENAME = "img1.txt";
	
	public static void main(String[] args) {
		new FASTScenePicker().run();
	}
	
	private BitImage picked;
	
	@Override
	public BitImage processImage(AdaptedYUYVImage img) {
		BitImage show = BitImage.intensityView(img);
		pick(img);
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
	
	private void pick(AdaptedYUYVImage img) {
		if (Button.ENTER.isDown()) {
			picked = new FAST(img);
		}
	}
}
