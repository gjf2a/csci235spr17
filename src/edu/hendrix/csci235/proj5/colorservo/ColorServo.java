package edu.hendrix.csci235.proj5.colorservo;

import java.io.IOException;

import edu.hendrix.modeselection.ModeSelector;
import edu.hendrix.modeselection.Transitions;
import edu.hendrix.modeselection.pid.PIDCalculator;
import edu.hendrix.modeselection.vision.CameraFlagger;
import edu.hendrix.modeselection.vision.color.ColorCountFlagger;
import lejos.hardware.motor.Motor;

public class ColorServo {
	public static void main(String[] args) throws IOException {
		CameraFlagger<Condition> camera = new CameraFlagger<>();
		ColorCountFlagger<Condition> counter = new ColorCountFlagger<>(89, 106, 190, 213);
		counter.addIntValue(Condition.RED_POS, colorCount -> colorCount.densest());
		counter.add2(Condition.HAS_RED, Condition.NO_RED, colorCount -> colorCount.getTotal() > 20);
		camera.addSub(counter);
		
		Transitions<Condition,Mode> table = new Transitions<>();
		table.add(Condition.HAS_RED, Mode.SERVO)
			 .add(Condition.NO_RED, Mode.FORWARD);
		
		PIDCalculator<Condition> pid = new PIDCalculator<Condition>()
				.targetValue(80)
				.P(8)
				.I(0.01)
				.D(1)
				.action(Condition.RED_POS, s -> {
					Motor.A.setSpeed(s);
					Motor.D.setSpeed(s);
					if (s < 0) {
						Motor.A.forward();
						Motor.D.backward();
					} else {
						Motor.A.backward();
						Motor.D.forward();
					}
		});
		
		ModeSelector<Condition,Mode> controller = new ModeSelector<>(Condition.class, Mode.class, Mode.FORWARD); 
		controller.flagger(camera)
				  .mode(Mode.FORWARD, table, () -> {
					  Motor.A.setSpeed(180);
					  Motor.D.setSpeed(180);
					  Motor.A.forward();
					  Motor.D.forward();
				  })
				  .subSelector(Mode.SERVO, table, pid);
	
		controller.control();
	}
}
