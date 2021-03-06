package edu.hendrix.csci235.proj6.example1;

import java.io.IOException;

import edu.hendrix.modeselection.ModeSelector;
import edu.hendrix.modeselection.Transitions;
import edu.hendrix.modeselection.vision.BitSceneFlagger;
import edu.hendrix.modeselection.vision.CameraFlagger;
import lejos.hardware.motor.Motor;

public class RunAway {
	public static void main(String[] args) throws IOException {
		CameraFlagger<Condition> camera = new CameraFlagger<>();
		BitSceneFlagger<Condition> image = new BitSceneFlagger<>("run1.txt", "run2.txt");
		camera.addSub(image);
		image.add2(Condition.SCARED, Condition.OKAY, i -> i < 7000);
		
		Transitions<Condition,Mode> table = new Transitions<>();
		table.add(Condition.SCARED, Mode.BACKWARD)
			 .add(Condition.OKAY, Mode.FORWARD);

		ModeSelector<Condition,Mode> controller =
				new ModeSelector<>(Condition.class, Mode.class, Mode.FORWARD)
				.flagger(camera)
				.mode(Mode.FORWARD, table, () -> {
					Motor.A.forward();
					Motor.D.forward();
				})
				.mode(Mode.BACKWARD, table, () -> {
					Motor.A.backward();
					Motor.D.backward();
				});
		
		controller.control();
	}

}
