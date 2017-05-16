package edu.hendrix.csci235.proj2.example2;

import edu.hendrix.modeselection.SensorFlagger;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;

public class SonarClose extends SensorFlagger<Condition> {
	public SonarClose() {
		super(new EV3UltrasonicSensor(SensorPort.S2), s -> s.getDistanceMode());
		add2(Condition.CLEAR, Condition.PROBLEM, f -> f > 0.2);
	}
}
