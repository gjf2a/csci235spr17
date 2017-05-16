package edu.hendrix.csci235.proj7.colorfollow;

import java.io.IOException;

import edu.hendrix.modeselection.ModeSelector;
import edu.hendrix.modeselection.Transitions;
import edu.hendrix.modeselection.fuzzy.Defuzzifier;
import edu.hendrix.modeselection.fuzzy.Fuzzy;
import edu.hendrix.modeselection.util.Util;
import edu.hendrix.modeselection.vision.CameraFlagger;
import edu.hendrix.modeselection.vision.color.ColorCountFlagger;
import lejos.hardware.motor.Motor;

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
