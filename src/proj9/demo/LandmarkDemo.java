package proj9.demo;

import java.io.IOException;

import lejos.hardware.motor.Motor;
import modeselection.ModeSelector;
import modeselection.Transitions;
import modeselection.vision.CameraFlagger;
import modeselection.vision.landmarks.LandmarkFlagger;

public class LandmarkDemo {
	public static void main(String[] args) throws IOException {
		CameraFlagger<Condition> camera = new CameraFlagger<>();
		LandmarkFlagger<Condition> landmarker = new LandmarkFlagger<>("landmarks1.txt");
		landmarker.add(Condition.CARPET, 0, d -> d < 5000000);
		landmarker.add(Condition.CORNER, 1);
		landmarker.add(Condition.CORNER, 2);
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
