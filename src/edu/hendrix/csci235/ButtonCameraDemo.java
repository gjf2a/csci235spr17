package edu.hendrix.csci235;

import java.io.IOException;

import edu.hendrix.modeselection.vision.AdaptedYUYVImage;
import edu.hendrix.modeselection.vision.BitImage;
import edu.hendrix.modeselection.vision.config.BasicVisionBot;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.video.Video;

public class ButtonCameraDemo {
	
	
	public static void main(String[] args) throws IOException {
		Video wc = BrickFinder.getDefault().getVideo();
		wc.open(BasicVisionBot.WIDTH, BasicVisionBot.HEIGHT);
		byte [] frame = wc.createFrame();
		
		while (!Button.ESCAPE.isDown()) {
			wc.grabFrame(frame);
			
			BitImage.intensityView(new AdaptedYUYVImage(frame, BasicVisionBot.WIDTH, BasicVisionBot.HEIGHT));
			
			if (Button.UP.isDown()) {
				LCD.drawString("up   ", 3, 3);
				Motor.A.forward();
				Motor.D.forward();
			} else if (Button.DOWN.isDown()) {
				LCD.drawString("down ", 3, 3);
				Motor.A.backward();
				Motor.D.backward();
			} else if (Button.LEFT.isDown()) {
				LCD.drawString("left ", 3, 3);
				Motor.A.backward();
				Motor.D.forward();
			} else if (Button.RIGHT.isDown()) {
				LCD.drawString("right", 3, 3);
				Motor.A.forward();
				Motor.D.backward();
			} else if (Button.ENTER.isDown()) {
				LCD.drawString("enter", 3, 3);
				Motor.A.stop();
				Motor.D.stop();
			}
		}
	}
}
