package proj2.example3;

import java.io.IOException;

import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import modeselection.ModeSelector;
import modeselection.MotorFlagger;
import modeselection.SensorFlagger;
import modeselection.Transitions;

public class Avoid3 {
	public static void main(String[] args) throws IOException {
		SensorFlagger<Condition> sonarClose = new SensorFlagger<>(new EV3UltrasonicSensor(SensorPort.S2), s -> s.getDistanceMode());
		sonarClose.add2(Condition.CLEAR, Condition.PROBLEM, f -> f > 0.2);

		MotorFlagger<Condition> backupFlag = new MotorFlagger<>(Motor.D);
		backupFlag.add2(Condition.IN_TURN, Condition.TURN_DONE, i -> i < 360);
		
		Transitions<Condition,Mode> avoidTransitions = new Transitions<>();
		avoidTransitions.add(Condition.CLEAR, Mode.FORWARD);
		avoidTransitions.add(Condition.PROBLEM, Mode.LEFT);

		Transitions<Condition,Mode> turnTransitions = new Transitions<>();
		turnTransitions.add(Condition.TURN_DONE, Mode.FORWARD);
		turnTransitions.add(Condition.IN_TURN, Mode.LEFT);
		
		ModeSelector<Condition,Mode> controller = 
				new ModeSelector<>(Condition.class, Mode.class, Mode.FORWARD)
				.sensor(sonarClose)
				.flagger(backupFlag)
				.mode(Mode.FORWARD, avoidTransitions, () -> {
					Motor.A.forward();
					Motor.D.forward();
				})
				.mode(Mode.LEFT, turnTransitions, () -> {
					Motor.D.resetTachoCount();
					Motor.A.backward();
					Motor.D.forward();
				});
		
		controller.control();
	}
}
