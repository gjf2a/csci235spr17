package proj8.example;

import java.io.IOException;

import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import modeselection.ModeSelector;
import modeselection.SensorFlagger;
import modeselection.Transitions;
import modeselection.cluster.apply.TrainedController;
import modeselection.vision.CameraFlagger;

public class TrainedControllerDemo {
	public static void main(String[] args) throws IOException {
		CameraFlagger<Condition> camera = new CameraFlagger<>();
		TrainedController<Condition,Move> trained = new TrainedController<>(Move.class, TrainingDemo.FILENAME);
		
		SensorFlagger<Condition> sonar = new SensorFlagger<>(new EV3UltrasonicSensor(SensorPort.S2), s -> s.getDistanceMode());
		sonar.add2(Condition.PRESENT, Condition.ABSENT, f -> f < 3);
		
		Transitions<Condition,Mode> table = new Transitions<>();
		table.add(Condition.PRESENT, Mode.LEARNED)
				  .add(Condition.ABSENT, Mode.GOING);

		ModeSelector<Condition,Mode> controller = new ModeSelector<>(Condition.class, Mode.class, Mode.GOING);
		controller.flagger(sonar)
			      .flagger(camera)
				  .mode(Mode.GOING, table, () -> {
					  Motor.A.forward();
					  Motor.D.forward();
				  })
				  .subSelector(Mode.LEARNED, table, trained);
		
		controller.control();
	}
}
