package proj2.example2;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import modeselection.SensorFlagger;

public class SonarClose extends SensorFlagger<Condition> {
	public SonarClose() {
		super(new EV3UltrasonicSensor(SensorPort.S2), s -> s.getDistanceMode());
		add2(Condition.CLEAR, Condition.PROBLEM, f -> f > 0.2);
	}
}
