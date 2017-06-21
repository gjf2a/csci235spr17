import java.io.IOException;
import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import edu.hendrix.modeselection.ModeSelector;
import edu.hendrix.modeselection.SensorFlagger;
import edu.hendrix.modeselection.Transitions;
public class ProgramName{
	public static void main(String[] args) throws IOException {
		SensorFlagger<Condition> Bump = new SensorFlagger<>(new EV3TouchSensor(SensorPort.S1));

		Bump.add2(Condition.HIT, Condition.MISS, v -> v == 1);

		Transitions<Condition,Mode> transitions1 = new Transitions<>();
		transitions1.add(Condition.HIT, Mode.STOP);
		transitions1.add(Condition.MISS, Mode.FORWARD);









		ModeSelector<Condition,Mode> controller = new ModeSelector<>(Condition.class, Mode.class, Mode.FORWARD)
			.flagger(Bump)
			.flagger(Bump)
			.mode(Mode.FORWARD,
				transitions1,
				() ->{
					Motor.A.forward();
					Motor.D.forward();
				})
			.mode(Mode.STOP,
				transitions1,
				() ->{
					Motor.A.stop();
					Motor.D.stop();
				});
			controller.control();
	}

	enum Condition{
		HIT,
		MISS;
	}
	enum Mode{
		FORWARD,
		STOP;
	}
}
