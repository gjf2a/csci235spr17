package edu.hendrix.csci235.proj2.example2;

import lejos.hardware.motor.Motor;

public class SpinLeft implements Runnable {

	@Override
	public void run() {
		Motor.A.backward();
		Motor.D.forward();
	}

}
