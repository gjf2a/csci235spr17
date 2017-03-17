package modeselection.util;

import lejos.hardware.Button;
import lejos.hardware.motor.NXTRegulatedMotor;

public class ButtonDriver {
	private NXTRegulatedMotor left, right;
	
	public ButtonDriver(NXTRegulatedMotor left, NXTRegulatedMotor right) {
		this.left = left;
		this.right = right;
	}
	
	public void drive() {
		drive(() -> {});
	}
	
	public void drive(Runnable whenDown) {
		if (Button.UP.isDown()) {
			left.forward();
			right.forward();
			whenDown.run();
		} else if (Button.DOWN.isDown()) {
			left.backward();
			right.backward();
			whenDown.run();
		} else if (Button.LEFT.isDown()) {
			left.backward();
			right.forward();
			whenDown.run();
		} else if (Button.RIGHT.isDown()) {
			left.forward();
			right.backward();
			whenDown.run();
		} else {
			left.stop(true);
			right.stop();
		}
	}
}
