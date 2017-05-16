package edu.hendrix.csci235.ideas;

import lejos.hardware.Button;
import lejos.hardware.motor.Motor;

public class SimpleSpin {
	public static void main(String[] args) {
		Motor.A.backward();
		Motor.D.forward();
		while (Button.ESCAPE.isUp()) {}
	}
}
