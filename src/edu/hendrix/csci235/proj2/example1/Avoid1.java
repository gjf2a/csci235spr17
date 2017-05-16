package edu.hendrix.csci235.proj2.example1;

import java.io.IOException;

import edu.hendrix.modeselection.ModeSelector;
import edu.hendrix.modeselection.SensorFlagger;
import edu.hendrix.modeselection.Transitions;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;

public class Avoid1 {
	public static void main(String[] args) throws IOException {
		SensorFlagger<Condition> sonarClose = new SensorFlagger<>(new EV3UltrasonicSensor(SensorPort.S2), s -> s.getDistanceMode());
		sonarClose.add2(Condition.CLEAR, Condition.PROBLEM, f -> f > 0.2);
		
		Transitions<Condition,Mode> transition = new Transitions<>();
		transition.add(Condition.CLEAR, Mode.FORWARD)
				  .add(Condition.PROBLEM, Mode.LEFT);
		
		ModeSelector<Condition,Mode> controller = 
				new ModeSelector<>(Condition.class, Mode.class, Mode.FORWARD)
				.sensor(sonarClose)
				.mode(Mode.FORWARD, 
						transition, 
						() -> {
							Motor.A.forward();
							Motor.D.forward();
							})
				.mode(Mode.LEFT, 
						transition, 
						() -> {
							Motor.A.backward();
							Motor.D.forward();
						});
		
		controller.control();
	}
}
