package proj2.example2;

import lejos.hardware.motor.Motor;

public class Forward implements Runnable {

	@Override
	public void run() {
		Motor.A.forward();
		Motor.D.forward();
	}

}
