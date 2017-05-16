package edu.hendrix.csci235;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;

public class MotorDemo2 {
	public static void main(String[] args) {
		Motor.A.forward();
		Motor.D.forward();
		while (!Button.ESCAPE.isDown()) {
			LCD.drawInt(Motor.A.getTachoCount(), 1, 1);
			LCD.drawInt(Motor.D.getTachoCount(), 1, 2);
		}
	}
}
