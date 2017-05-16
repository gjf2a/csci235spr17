package edu.hendrix.csci235.proj4.example1;

import java.io.IOException;

import edu.hendrix.modeselection.SensorFlagger;
import edu.hendrix.modeselection.StateClassifier;
import edu.hendrix.modeselection.qlearning.QController;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;

public class LearnAvoid {
	public static void main(String[] args) throws IOException {
		SensorFlagger<Condition> sonar = new SensorFlagger<>(new EV3UltrasonicSensor(SensorPort.S2), s -> s.getDistanceMode());
		sonar.add(Condition.CLOSE, f -> f < 0.2)
			 .add(Condition.MID, f -> f >= 0.2 && f < 0.5)
			 .add(Condition.FAR, f -> f >= 0.5);
		
		SensorFlagger<Condition> bumper1 = new SensorFlagger<>(new EV3TouchSensor(SensorPort.S1));
		bumper1.add2(Condition.BUMPED, Condition.OPEN, f -> f == 1.0);
		
		SensorFlagger<Condition> bumper2 = new SensorFlagger<>(new EV3TouchSensor(SensorPort.S4));
		bumper2.add2(Condition.BUMPED, Condition.OPEN, f -> f == 1.0);
		
		StateClassifier<Condition> classifier = new StateClassifier<>(Condition.class);
		classifier.add(sonar)
				  .add(bumper1)
				  .add(bumper2);
		
		QController<Condition,Mode> learner = new QController<>(Mode.class);
		learner.sensors(classifier)
			   .targetVisits(3)
			   .halfLife(100)
			   .discount(0.5)
			   .rewards((state, mode) -> {
				   if (state.contains(Condition.BUMPED)) {
					   return -1.0;
				   } else if (mode == Mode.BACK) {
					   return 0.0;
				   } else {
					   return 1.0;
				   }
			   })
			   .action(Mode.STOP, () -> {
				   Motor.A.stop(true);
				   Motor.D.stop();
			   })
			   .action(Mode.BACK, () -> {
				   Motor.A.backward();
				   Motor.D.backward();
			   });
		
		learner.control();
	}
}
