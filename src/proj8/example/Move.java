package proj8.example;

import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.motor.Motor;
import modeselection.cluster.train.RobotKey;

public enum Move implements RobotKey {
	FORWARD {
		@Override
		public Key getKey() {
			return Button.UP;
		}

		@Override
		public void act() {
			Motor.A.forward();
			Motor.D.forward();
		}
	},
	LEFT {
		@Override
		public Key getKey() {
			return Button.LEFT;
		}

		@Override
		public void act() {
			Motor.A.backward();
			Motor.D.forward();
		}
	};
}
