import java.io.IOException;
import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import edu.hendrix.modeselection.ModeSelector;
import edu.hendrix.modeselection.SensorFlagger;
import edu.hendrix.modeselection.Transitions;
import edu.hendrix.modeselection.vision.CameraFlagger;
import edu.hendrix.modeselection.vision.color.ColorCountFlagger;
public class ProgramName{
	public static void main(String[] args) throws IOException {

		flag.add2(Condition.TRUE, Condition.FALSE, v -> v == 1);

		Transitions<Condition,Mode> transitions1 = new Transitions<>();
		transitions1.add(Condition.FALSE, Mode.FORWARD);
		transitions1.add(Condition.TRUE, Mode.FORWARD);









		ModeSelector<Condition,Mode> controller = new ModeSelector<>(Condition.class, Mode.class, Mode.FORWARD)
			.flagger(flag)
			.flagger(flag)
			.mode(Mode.FORWARD,
				transitions1,
				() ->{
					Motor.A.forward();
					Motor.B.forward();
				});
			controller.control();
	}

	enum Condition{
		FALSE,
		TRUE;
	}
	enum Mode{
		FORWARD;
	}
}
