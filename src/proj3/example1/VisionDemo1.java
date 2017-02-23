package proj3.example1;

import java.io.IOException;

import lejos.hardware.motor.Motor;
import modeselection.ModeSelector;
import modeselection.Transitions;
import modeselection.vision.CameraFlagger;
import modeselection.vision.color.ColorCountFlagger;

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
