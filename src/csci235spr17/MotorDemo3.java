package csci235spr17;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;

public class MotorDemo3 {
	public static void main(String[] args) {
		LCD.drawString("Ready!", 3, 3);
		while (!Button.ESCAPE.isDown()) {
			if (Button.ENTER.isDown()) {
				Motor.A.stop();
				Motor.D.stop();
			} else if (Button.LEFT.isDown()) {
				Motor.A.backward();
				Motor.D.backward();
			} else if (Button.RIGHT.isDown()) {
				Motor.A.forward();
				Motor.D.forward();
			}
		}
	}
}
