package proj7.colorfollow;

import java.io.IOException;

import lejos.hardware.motor.Motor;
import modeselection.ModeSelector;
import modeselection.Transitions;
import modeselection.fuzzy.Defuzzifier;
import modeselection.fuzzy.Fuzzy;
import modeselection.util.Util;
import modeselection.vision.CameraFlagger;
import modeselection.vision.color.ColorCountFlagger;

public class ColorFollow {
	public static void main(String[] args) throws IOException {
		CameraFlagger<Condition> camera = new CameraFlagger<>();
		ColorCountFlagger<Condition> counter = new ColorCountFlagger<>(89, 106, 190, 213);
		counter.addValue(Condition.RED_LEFT, colorCount -> Fuzzy.falling(colorCount.densest(), 0, 80));
		counter.addValue(Condition.RED_RIGHT, colorCount -> Fuzzy.rising(colorCount.densest(), 80, 160));
		counter.add2(Condition.HAS_RED, Condition.NO_RED, colorCount -> colorCount.getTotal() > 20);
		camera.addSub(counter);
		
		Defuzzifier<Condition> defuzz = new Defuzzifier<>(Condition.class);
		defuzz.addDefuzzer(Condition.RED_LEFT, 0, 360, i -> Util.motorAt(Motor.D, i))
			  .addDefuzzer(Condition.RED_RIGHT, 0, 360, i -> Util.motorAt(Motor.A, i));
		
		Transitions<Condition,Mode> table = new Transitions<>();
		table.add(Condition.HAS_RED, Mode.SWERVE)
		     .add(Condition.NO_RED, Mode.STOP);
		
		ModeSelector<Condition,Mode> controller = new ModeSelector<>(Condition.class, Mode.class, Mode.STOP);
		controller.flagger(camera)
				  .mode(Mode.STOP, table, () -> {
					  Motor.A.stop(true);
					  Motor.D.stop();
				  })
				  .subSelector(Mode.SWERVE, table, defuzz);
		controller.control();
	}
}
