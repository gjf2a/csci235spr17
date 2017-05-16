package edu.hendrix.csci235.proj6.example3;

import java.io.IOException;

import edu.hendrix.modeselection.ModeSelector;
import edu.hendrix.modeselection.Transitions;
import edu.hendrix.modeselection.vision.CameraFlagger;
import edu.hendrix.modeselection.vision.SSDSceneFlagger;
import lejos.hardware.motor.Motor;

public class LookForSSD {
	public static void main(String[] args) throws IOException {
		CameraFlagger<Condition> camera = new CameraFlagger<>();
		SSDSceneFlagger<Condition> image = new SSDSceneFlagger<>("ssd1.txt");
		camera.addSub(image);
		image.add2(Condition.FOUND, Condition.LOST, i -> i < 5500);
		
		Transitions<Condition,Mode> table = new Transitions<>();
		table.add(Condition.FOUND, Mode.STOP)
			 .add(Condition.LOST, Mode.SPIN);

		ModeSelector<Condition,Mode> controller =
				new ModeSelector<>(Condition.class, Mode.class, Mode.SPIN)
				.flagger(camera)
				.mode(Mode.SPIN, table, () -> {
					Motor.A.setSpeed(150);
					Motor.D.setSpeed(150);
					Motor.A.forward();
					Motor.D.backward();
				})
				.mode(Mode.STOP, table, () -> {
					Motor.A.stop(true);
					Motor.D.stop();
				});
		
		controller.control();
	}

}
