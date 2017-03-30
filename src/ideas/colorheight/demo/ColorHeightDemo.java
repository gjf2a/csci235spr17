package ideas.colorheight.demo;

import java.io.IOException;

import ideas.colorheight.ColorHeightFlagger;
import lejos.hardware.motor.Motor;
import modeselection.ModeSelector;
import modeselection.Transitions;
import modeselection.vision.CameraFlagger;

public class ColorHeightDemo {
	public final static int MIN_RED = 5;
	
	public static void main(String[] args) throws IOException {
		CameraFlagger<Condition> camera = new CameraFlagger<>();
		ColorHeightFlagger<Condition> counter = new ColorHeightFlagger<>(89, 103, 190, 213);
		camera.addSub(counter);
		counter.add(Condition.RED_ABSENT, c -> c.heightAt(c.highestX()) < MIN_RED);
		counter.add(Condition.RED_RIGHT, c -> c.highestX() < 60);
		counter.add(Condition.RED_CENTER, c -> c.highestX() >= 60 && c.highestX() < 120);
		counter.add(Condition.RED_LEFT, c -> c.highestX() >= 120);
		
		Transitions<Condition,Mode> table = new Transitions<>();
		table.add(Condition.RED_ABSENT, Mode.STOP)
			 .add(Condition.RED_CENTER, Mode.FORWARD)
			 .add(Condition.RED_LEFT, Mode.LEFT)
			 .add(Condition.RED_RIGHT, Mode.RIGHT);
		
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
