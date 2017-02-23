package proj3.example2;

import java.io.IOException;

import lejos.hardware.motor.Motor;
import modeselection.ModeSelector;
import modeselection.Transitions;
import modeselection.vision.CameraFlagger;
import modeselection.vision.color.ColorCountFlagger;

public class VisionDemo2 {
	public final static int MIN_RED = 20, MIN_DENSITY = 5;
	
	public static void main(String[] args) throws IOException {
		CameraFlagger<Condition> camera = new CameraFlagger<>();
		ColorCountFlagger<Condition> counter = new ColorCountFlagger<>(89, 103, 190, 213);
		camera.addSub(counter);
		counter.add(Condition.RED_ABSENT, c -> c.getTotal() < MIN_RED);
		counter.add(Condition.RED_RIGHT, c -> c.densest(MIN_DENSITY) < 60);
		counter.add(Condition.RED_CENTER, c -> c.densest(MIN_DENSITY) >= 60 && c.densest(MIN_RED) < 120);
		counter.add(Condition.RED_LEFT, c -> c.densest(MIN_DENSITY) >= 120);
		
		Transitions<Condition,Mode> table = new Transitions<>();
		table.add(Condition.RED_ABSENT, Mode.FORWARD)
			 .add(Condition.RED_CENTER, Mode.STOP)
			 .add(Condition.RED_LEFT, Mode.RIGHT)
			 .add(Condition.RED_RIGHT, Mode.LEFT);
		
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
