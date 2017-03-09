package proj7.colorservo;

import java.io.IOException;

import lejos.hardware.motor.Motor;
import modeselection.ModeSelector;
import modeselection.Transitions;
import modeselection.fuzzy.Defuzzifier;
import modeselection.fuzzy.Fuzzy;
import modeselection.fuzzy.FuzzyRuleBase;
import modeselection.util.Util;
import modeselection.vision.CameraFlagger;
import modeselection.vision.color.ColorCountFlagger;

public class ColorServo {
	public static final int SPEED = 180;
	
	public static void main(String[] args) throws IOException {
		CameraFlagger<Condition> camera = new CameraFlagger<>();
		ColorCountFlagger<Condition> counter = new ColorCountFlagger<>(89, 106, 190, 213);
		counter.addValue(Condition.RED_POS, colorCount -> Fuzzy.rising(colorCount.densest(), 0, 160));
		counter.add2(Condition.HAS_RED, Condition.NO_RED, colorCount -> colorCount.getTotal() > 20);
		camera.addSub(counter);
		
		FuzzyRuleBase<Condition> rules = new FuzzyRuleBase<>(Condition.class);
		rules.addRule(Condition.NEG_RED_POS, sensed -> Fuzzy.not(sensed, Condition.RED_POS));
		
		Defuzzifier<Condition> defuzz = new Defuzzifier<>(Condition.class);
		defuzz.addDefuzzer(Condition.RED_POS, -SPEED, SPEED, i -> Util.motorAt(Motor.A, i))
		      .addDefuzzer(Condition.NEG_RED_POS, -SPEED, SPEED, i -> Util.motorAt(Motor.D, i));
	
		Transitions<Condition,Mode> table = new Transitions<>();
		table.add(Condition.HAS_RED, Mode.SERVO)
		     .add(Condition.NO_RED, Mode.STOP);

		ModeSelector<Condition,Mode> controller = new ModeSelector<>(Condition.class, Mode.class, Mode.STOP);
		controller.flagger(camera)
				  .flagger(rules)
				  .mode(Mode.STOP, table, () -> {
					  Motor.A.stop(true);
					  Motor.D.stop();
				  })
				  .subSelector(Mode.SERVO, table, defuzz);
		controller.control();

	}
}
