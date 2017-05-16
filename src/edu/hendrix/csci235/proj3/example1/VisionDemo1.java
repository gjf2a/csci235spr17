package edu.hendrix.csci235.proj3.example1;

import java.io.IOException;

import edu.hendrix.modeselection.ModeSelector;
import edu.hendrix.modeselection.Transitions;
import edu.hendrix.modeselection.vision.CameraFlagger;
import edu.hendrix.modeselection.vision.color.ColorCountFlagger;
import lejos.hardware.motor.Motor;

public class VisionDemo1 {
	public static void main(String[] args) throws IOException {
		CameraFlagger<Condition> camera = new CameraFlagger<>();
		ColorCountFlagger<Condition> counter = new ColorCountFlagger<>(89, 106, 190, 213);
		camera.addSub(counter);
		counter.add2(Condition.RED_PRESENT, Condition.RED_ABSENT, c -> c.getTotal() > 20);
		
		Transitions<Condition,Mode> table = new Transitions<>();
		table.add(Condition.RED_PRESENT, Mode.STOP)
			 .add(Condition.RED_ABSENT, Mode.FORWARD);
		
		ModeSelector<Condition,Mode> controller =
				new ModeSelector<>(Condition.class, Mode.class, Mode.FORWARD)
				.flagger(camera)
				.mode(Mode.FORWARD, table, () -> {
					Motor.A.forward();
					Motor.D.forward();
				})
				.mode(Mode.STOP, table, () -> {
					Motor.A.stop();
					Motor.D.stop();
				});
		
		controller.control();
	}
}
