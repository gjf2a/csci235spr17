package proj7.backup;

import java.io.IOException;

import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import modeselection.ModeSelector;
import modeselection.SensorFlagger;
import modeselection.Transitions;
import modeselection.fuzzy.Defuzzifier;
import modeselection.fuzzy.Fuzzy;
import modeselection.fuzzy.FuzzyRuleBase;
import modeselection.vision.CameraFlagger;
import modeselection.vision.color.ColorCountFlagger;

public class Backup {
	public static void main(String[] args) throws IOException {
		CameraFlagger<Condition> camera = new CameraFlagger<>();
		ColorCountFlagger<Condition> counter = new ColorCountFlagger<>(89, 106, 190, 213);
		counter.addValue(Condition.RED, colorCount -> Fuzzy.rising(colorCount.getTotal(), 0, 50));
		camera.addSub(counter);
		
		SensorFlagger<Condition> sonar = new SensorFlagger<>(new EV3UltrasonicSensor(SensorPort.S2), s -> s.getDistanceMode());
		sonar.add2(Condition.PRESENT, Condition.ABSENT, s -> s < 3)
			 .addValue(Condition.CLOSE, s -> Fuzzy.falling(s, 0.0, 0.5));

		FuzzyRuleBase<Condition> bothPresent = new FuzzyRuleBase<>(Condition.class);
		bothPresent.addRule(Condition.EITHER, values -> Fuzzy.or(values, Condition.RED, Condition.CLOSE));
		
		Defuzzifier<Condition> defuzz = new Defuzzifier<>(Condition.class);
		defuzz.addDefuzzer(Condition.EITHER, -300, 0, i -> {
			Motor.A.setSpeed(i);
			Motor.D.setSpeed(i);
			Motor.A.backward();
			Motor.D.backward();
		});
		
		Transitions<Condition,Mode> table = new Transitions<>();
		table.add(Condition.ABSENT, Mode.STOP)
			 .add(Condition.PRESENT, Mode.BACK);
		
		ModeSelector<Condition,Mode> controller = new ModeSelector<>(Condition.class, Mode.class, Mode.STOP);
		controller.flagger(camera)
				  .flagger(sonar)
				  .flagger(bothPresent)
				  .mode(Mode.STOP, table, () -> {
					  Motor.A.stop(true);
					  Motor.D.stop();
				  })
				  .subSelector(Mode.BACK, table, defuzz);
		
		controller.control();
	}
}
