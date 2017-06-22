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
		CameraFlagger<Condition> camera = new CameraFlagger<>();
		ColorCountFlagger<Condition> Blue = new ColorCountFlagger<>(121, 128, 118, 122);
		camera.addSub(Blue);

		Blue.add2(Condition.BLUE, Condition.NOTBLUE, v -> v.getTotal() >= 1);

		Transitions<Condition,Mode> transitions1 = new Transitions<>();
		transitions1.add(Condition.BLUE, Mode.STOP);
		transitions1.add(Condition.NOTBLUE, Mode.FORWARD);









		ModeSelector<Condition,Mode> controller = new ModeSelector<>(Condition.class, Mode.class, Mode.FORWARD)
			.flagger(Blue)
			.flagger(Blue)
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
		BLUE,
		NOTBLUE;
	}
	enum Mode{
		FORWARD,
		STOP;
	}
}
