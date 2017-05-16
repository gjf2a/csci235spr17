package edu.hendrix.csci235;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;

public class HelloWorld {
	public static void main(String[] args) {
		LCD.drawString("Hello, world!", 0, 0);
		while (!Button.ESCAPE.isDown()) {}
	}
}
