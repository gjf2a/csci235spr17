package edu.hendrix.csci235;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;

public class MotorDemo1 {
	public static void main(String[] args) {
		Motor.A.backward();
		Motor.D.backward();
		while (!Button.ESCAPE.isDown()) {
			LCD.drawInt(Motor.A.getTachoCount(), 1, 1);
			LCD.drawInt(Motor.D.getTachoCount(), 1, 2);
		}
	}
}
