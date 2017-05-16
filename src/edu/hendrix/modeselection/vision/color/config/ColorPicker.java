package edu.hendrix.modeselection.vision.color.config;

import edu.hendrix.modeselection.vision.AdaptedYUYVImage;
import edu.hendrix.modeselection.vision.BitImage;
import edu.hendrix.modeselection.vision.Dir;
import edu.hendrix.modeselection.vision.MovingSquare;
import edu.hendrix.modeselection.vision.color.ColorFilter;
import edu.hendrix.modeselection.vision.config.VisionBot;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.video.YUYVImage;

public class ColorPicker extends VisionBot {
	public static void main(String[] args) {
		new ColorPicker().run();
	}
	
	private MovingSquare cursor = new MovingSquare(WIDTH, HEIGHT);
	private ColorFilter selectedColors = new ColorFilter();
	
	@Override
	public BitImage processImage(AdaptedYUYVImage img) {
		BitImage show = BitImage.intensityView(img);
		moveCursor();		
		cursor.render(show);
		addColor(img);
		return show;
	}

	@Override
	public void displayFinalInfo() {
		if (selectedColors.defined()) {
			LCD.drawString(String.format("U:%d %d", selectedColors.getMinU(), selectedColors.getMaxU()), 0, 0);
			LCD.drawString(String.format("V:%d %d", selectedColors.getMinV(), selectedColors.getMaxV()), 0, 1);
		} else {
			LCD.drawString("Finished; no colors", 0, 3); 
		}
	}
	
	private void moveCursor() {
		if (Button.UP.isDown()) {
			cursor.move(Dir.UP);
		}
		if (Button.DOWN.isDown()) {
			cursor.move(Dir.DOWN);
		}
		if (Button.LEFT.isDown()) {
			cursor.move(Dir.LEFT);
		}
		if (Button.RIGHT.isDown()) {
			cursor.move(Dir.RIGHT);
		}		
	}
	
	private void addColor(YUYVImage img) {
		if (Button.ENTER.isDown()) {
			cursor.addColors(selectedColors, img);
		}
	}
}
