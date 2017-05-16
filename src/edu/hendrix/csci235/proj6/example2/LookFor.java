package edu.hendrix.csci235.proj6.example2;

import java.io.IOException;

import edu.hendrix.modeselection.ModeSelector;
import edu.hendrix.modeselection.Transitions;
import edu.hendrix.modeselection.vision.BitSceneFlagger;
import edu.hendrix.modeselection.vision.CameraFlagger;
import lejos.hardware.motor.Motor;

public class LookFor {
	public static void main(String[] args) throws IOException {
		CameraFlagger<Condition> camera = new CameraFlagger<>();
		BitSceneFlagger<Condition> image = new BitSceneFlagger<>("img1.txt");
		camera.addSub(image);
		image.add2(Condition.FOUND, Condition.LOST, i -> i < 7500);
		
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
