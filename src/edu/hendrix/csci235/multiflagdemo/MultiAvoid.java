package edu.hendrix.csci235.multiflagdemo;

import java.io.IOException;

import edu.hendrix.modeselection.ModeSelector;
import edu.hendrix.modeselection.MultiFlagger;
import edu.hendrix.modeselection.SensorFlagger;
import edu.hendrix.modeselection.Transitions;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;

public class MultiAvoid {
	public static void main(String[] args) throws IOException {
		SensorFlagger<Condition> bumpedLeft = new SensorFlagger<>(new EV3TouchSensor(SensorPort.S1));
		bumpedLeft.add2(Condition.LEFT_HIT, Condition.LEFT_CLEAR, f -> f > 0.5);

		SensorFlagger<Condition> bumpedRight = new SensorFlagger<>(new EV3TouchSensor(SensorPort.S4));
		bumpedRight.add2(Condition.RIGHT_HIT, Condition.RIGHT_CLEAR, f -> f > 0.5);

		MultiFlagger<Condition> bothHit = new MultiFlagger<>(Condition.class, Condition.BOTH_HIT);
		bothHit.add(Condition.LEFT_HIT).add(Condition.RIGHT_HIT);
		
		MultiFlagger<Condition> allClear = new MultiFlagger<>(Condition.class, Condition.ALL_CLEAR);
		allClear.add(Condition.LEFT_CLEAR).add(Condition.RIGHT_CLEAR);
		
		Transitions<Condition,Mode> transition = new Transitions<>();
		transition.add(Condition.ALL_CLEAR, Mode.FORWARD)
				  .add(Condition.LEFT_CLEAR, Mode.LEFT)
				  .add(Condition.RIGHT_CLEAR, Mode.RIGHT)
				  .add(Condition.BOTH_HIT, Mode.BACK);
		
		ModeSelector<Condition,Mode> controller = 
				new ModeSelector<>(Condition.class, Mode.class, Mode.FORWARD)
				.sensor(bumpedLeft)
				.sensor(bumpedRight)
				.flagger(allClear)
				.flagger(bothHit)
				.mode(Mode.FORWARD, 
						transition, 
						() -> {
							Motor.A.forward();
							Motor.D.forward();
							})
				.mode(Mode.RIGHT, 
						transition, 
						() -> {
							Motor.A.forward();
							Motor.D.backward();
							})
				.mode(Mode.BACK, 
						transition, 
						() -> {
							Motor.A.backward();
							Motor.D.backward();
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
