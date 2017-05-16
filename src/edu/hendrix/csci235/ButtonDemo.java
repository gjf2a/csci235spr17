package edu.hendrix.csci235;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;

public class ButtonDemo {
	public static void main(String[] args) {
		while (!Button.ESCAPE.isDown()) {
			if (Button.UP.isDown()) {
				LCD.drawString("up   ", 3, 3);
			} else if (Button.DOWN.isDown()) {
				LCD.drawString("down ", 3, 3);
			} else if (Button.LEFT.isDown()) {
				LCD.drawString("left ", 3, 3);
			} else if (Button.RIGHT.isDown()) {
				LCD.drawString("right", 3, 3);
			} else if (Button.ENTER.isDown()) {
				LCD.drawString("enter", 3, 3);
			}
		}
	}
}
