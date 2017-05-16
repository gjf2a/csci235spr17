package edu.hendrix.csci235.ideas;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;

public class ScreenTest {
	public static void main(String[] args) {
		for (int i = 0; i < 8; i++) {
			LCD.drawString("abcdefghijklmnopqr", 0, i);
		}
		while (Button.ESCAPE.isUp()) {}
	}
}
