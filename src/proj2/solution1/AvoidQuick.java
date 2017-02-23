package proj2.solution1;

import java.io.IOException;

import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import modeselection.ModeSelector;
import modeselection.SensorFlagger;
import modeselection.Transitions;
import proj2.example1.Condition;
import proj2.example1.Mode;

public class AvoidQuick {
	public static void main(String[] args) throws IOException {
		SensorFlagger<Condition> sonarClose = new SensorFlagger<>(new EV3UltrasonicSensor(SensorPort.S2), s -> s.getDistanceMode());
		sonarClose.add2(Condition.CLEAR, Condition.PROBLEM, f -> f > 0.2);
		
		SensorFlagger<Condition> bumpLeft = new SensorFlagger<>(new EV3TouchSensor(SensorPort.S1));
		bumpLeft.add2(Condition.PROBLEM, Condition.CLEAR, f -> f == 1.0);
		SensorFlagger<Condition> bumpRight = new SensorFlagger<>(new EV3TouchSensor(SensorPort.S4));
		bumpRight.add2(Condition.PROBLEM, Condition.CLEAR, f -> f == 1.0);
		
		Transitions<Condition,Mode> transition = new Transitions<>();
		transition.add(Condition.CLEAR, Mode.FORWARD)
				  .add(Condition.PROBLEM, Mode.LEFT);
		
		ModeSelector<Condition,Mode> controller = 
				new ModeSelector<>(Condition.class, Mode.class, Mode.FORWARD)
				.sensor(sonarClose)
				.sensor(bumpLeft)
				.sensor(bumpRight)
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
