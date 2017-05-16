package edu.hendrix.csci235.proj9.demo;

import java.io.IOException;

import edu.hendrix.modeselection.ModeSelector;
import edu.hendrix.modeselection.Transitions;
import edu.hendrix.modeselection.vision.CameraFlagger;
import edu.hendrix.modeselection.vision.landmarks.LandmarkFlagger;
import lejos.hardware.motor.Motor;

public class LandmarkDemo {
	public static void main(String[] args) throws IOException {
		CameraFlagger<Condition> camera = new CameraFlagger<>();
		LandmarkFlagger<Condition> landmarker = new LandmarkFlagger<>("landmarks1.txt");
		landmarker.add(Condition.CARPET, 0, d -> d < 5000000)
				  .add(Condition.CORNER, 1, 2);
		camera.addSub(landmarker);
		
		Transitions<Condition,Mode> table = new Transitions<>();
		table.add(Condition.CARPET, Mode.FORWARD)
			 .add(Condition.CORNER, Mode.LEFT);
		
		ModeSelector<Condition,Mode> controller =
				new ModeSelector<>(Condition.class, Mode.class, Mode.FORWARD)
				.flagger(camera)
				.mode(Mode.FORWARD, table, () -> {
					Motor.A.forward();
					Motor.D.forward();
				})
				.mode(Mode.LEFT, table, () -> {
					Motor.A.backward();
					Motor.D.forward();
				});
		
		controller.control();
	}
}
