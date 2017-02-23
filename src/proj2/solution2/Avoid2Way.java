package proj2.solution2;

import java.io.IOException;

import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import modeselection.ModeSelector;
import modeselection.SensorFlagger;
import modeselection.Transitions;

public class Avoid2Way {
	public static void main(String[] args) throws IOException {
		SensorFlagger<Condition> sonarClose = new SensorFlagger<>(new EV3UltrasonicSensor(SensorPort.S2), s -> s.getDistanceMode());
		sonarClose.add2(Condition.CLEAR, Condition.PROBLEM, f -> f > 0.2);
		
		SensorFlagger<Condition> bumpLeft = new SensorFlagger<>(new EV3TouchSensor(SensorPort.S1));
		bumpLeft.add2(Condition.PROBLEM_LEFT, Condition.CLEAR, f -> f == 1.0);
		SensorFlagger<Condition> bumpRight = new SensorFlagger<>(new EV3TouchSensor(SensorPort.S4));
		bumpRight.add2(Condition.PROBLEM_RIGHT, Condition.CLEAR, f -> f == 1.0);
		
		Transitions<Condition,Mode> transition1 = new Transitions<>();
		transition1.add(Condition.CLEAR, Mode.FORWARD)
				  .add(Condition.PROBLEM_LEFT, Mode.LEFT)
				  .add(Condition.PROBLEM_RIGHT, Mode.RIGHT)
				  .add(Condition.PROBLEM, Mode.LEFT);
		
		Transitions<Condition,Mode> transition2 = new Transitions<>();
		transition2.add(Condition.CLEAR, Mode.FORWARD)
				  .add(Condition.PROBLEM_LEFT, Mode.LEFT)
				  .add(Condition.PROBLEM_RIGHT, Mode.RIGHT)
				  .add(Condition.PROBLEM, Mode.RIGHT);
		
		ModeSelector<Condition,Mode> controller = 
				new ModeSelector<>(Condition.class, Mode.class, Mode.FORWARD)
				.sensor(sonarClose)
				.sensor(bumpLeft)
				.sensor(bumpRight)
				.mode(Mode.FORWARD, 
						transition1, 
						() -> {
							Motor.A.forward();
							Motor.D.forward();
							})
				.mode(Mode.RIGHT, 
						transition2, 
						() -> {
							Motor.A.forward();
							Motor.D.backward();
							})
				.mode(Mode.LEFT, 
						transition1, 
						() -> {
							Motor.A.backward();
							Motor.D.forward();
						});
		
		controller.control();
	}
}
