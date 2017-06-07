import java.io.IOException;
import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import modeselection.ModeSelector;
import modeselection.SensorFlagger;
import modeselection.Transitions;
public class Taylor{
	public static void main(String[] args) throws IOException {
		SensorFlagger<Condition> bump = new SensorFlagger<>(new EV3TouchSensor(SensorPort.S2));

		bump.add2(Condition.CLEAR, Condition.HIT, v -> v == 12);

		Transitions<Condition,Mode> transitions1 = new Transitions<>();
		transitions1.add(Condition.CLEAR, Mode.FD);
		transitions1.add(Condition.HIT, Mode.BD);


		Transitions<Condition,Mode> transitions2 = new Transitions<>();

		ModeSelector<Condition,Mode> controller = new ModeSelector<>(Condition.class, Mode.class, Mode.FD)
			.flagger(bump)
			.flagger(bump)
			.mode(Mode.BD,
				transitions1,
				() ->{
					Motor.A.backward();
					Motor.B.backward();
				})
			.mode(Mode.FD,
				transitions1,
				() ->{
					Motor.A.forward();
					Motor.B.forward();
				});
			controller.control();
	}

	enum Condition{
		CLEAR,
		HIT;
	}
	enum Mode{
		BD,
		FD;
	}
}
