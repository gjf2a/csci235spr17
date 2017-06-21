package edu.hendrix.csci235;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;

public class ButtonDemo2 {
	public static void main(String[] args) {
		while (!Button.ESCAPE.isDown()) {
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
