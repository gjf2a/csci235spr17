package proj6.example;

import java.io.IOException;

import lejos.hardware.motor.Motor;
import modeselection.ModeSelector;
import modeselection.Transitions;
import modeselection.vision.CameraFlagger;
import modeselection.vision.SceneFlagger;

public class RunAway {
	public static void main(String[] args) throws IOException {
		CameraFlagger<Condition> camera = new CameraFlagger<>();
		SceneFlagger<Condition> image = new SceneFlagger<>("img1.txt");
		camera.addSub(image);
		image.add2(Condition.SCARED, Condition.OKAY, i -> i < 5000);
		
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
