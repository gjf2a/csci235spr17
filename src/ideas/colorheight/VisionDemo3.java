package ideas.colorheight;

import java.io.IOException;

import ideas.vision.ColorHeightFlagger;
import lejos.hardware.motor.Motor;
import modeselection.ModeSelector;
import modeselection.Transitions;
import modeselection.vision.CameraFlagger;

public class VisionDemo3 {
	public final static int MIN_RED = 5;
	
	public static void main(String[] args) throws IOException {
		CameraFlagger<Condition> camera = new CameraFlagger<>();
		ColorHeightFlagger<Condition> counter = new ColorHeightFlagger<>(89, 103, 190, 213);
		camera.addSub(counter);
		counter.add(Condition.RED_ABSENT, c -> c.getTotal() < MIN_RED);
		counter.add(Condition.RED_RIGHT, c -> c.highest() < 60);
		counter.add(Condition.RED_CENTER, c -> c.highest() >= 60 && c.highest() < 120);
		counter.add(Condition.RED_LEFT, c -> c.highest() >= 120);
		
		Transitions<Condition,Mode> table = new Transitions<>();
		table.add(Condition.RED_ABSENT, Mode.STOP)
			 .add(Condition.RED_CENTER, Mode.FORWARD)
			 .add(Condition.RED_LEFT, Mode.RIGHT)
			 .add(Condition.RED_RIGHT, Mode.LEFT);
		
		ModeSelector<Condition,Mode> controller =
				new ModeSelector<>(Condition.class, Mode.class, Mode.STOP)
				.flagger(camera)
				.mode(Mode.FORWARD, table, () -> {
					Motor.A.forward();
					Motor.D.forward();
				})
				.mode(Mode.LEFT, table, () -> {
					Motor.A.backward();
					Motor.D.forward();
				})
				.mode(Mode.RIGHT, table, () -> {
					Motor.A.forward();
					Motor.D.backward();
				})
				.mode(Mode.STOP, table, () -> {
					Motor.A.stop();
					Motor.D.stop();
				});
		
		controller.control();
	}
}
