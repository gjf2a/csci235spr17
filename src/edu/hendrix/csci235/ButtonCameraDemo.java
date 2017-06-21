package edu.hendrix.csci235;

import java.io.IOException;

import edu.hendrix.modeselection.vision.AdaptedYUYVImage;
import edu.hendrix.modeselection.vision.BitImage;
import edu.hendrix.modeselection.vision.config.BasicVisionBot;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.hardware.video.Video;

public class ButtonCameraDemo {
	
	
	public static void main(String[] args) throws IOException {
		Video wc = BrickFinder.getDefault().getVideo();
		wc.open(BasicVisionBot.WIDTH, BasicVisionBot.HEIGHT);
		byte [] frame = wc.createFrame();
		
		while (!Button.ESCAPE.isDown()) {
			wc.grabFrame(frame);
			
			BitImage.intensityView(new AdaptedYUYVImage(frame, BasicVisionBot.WIDTH, BasicVisionBot.HEIGHT)).draw();
			
			if (Button.UP.isDown()) {
				Motor.A.forward();
				Motor.D.forward();
			} else if (Button.DOWN.isDown()) {
				Motor.A.backward();
				Motor.D.backward();
			} else if (Button.LEFT.isDown()) {
				Motor.A.backward();
				Motor.D.forward();
			} else if (Button.RIGHT.isDown()) {
				Motor.A.forward();
				Motor.D.backward();
			} else if (Button.ENTER.isDown()) {
				Motor.A.stop();
				Motor.D.stop();
			}
		}
	}
}
