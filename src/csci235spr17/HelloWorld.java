package csci235spr17;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;

public class HelloWorld {
	public static void main(String[] args) {
		LCD.drawString("Hello, world!", 0, 0);
		while (!Button.ESCAPE.isDown()) {}
	}
}
