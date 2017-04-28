package colorsensordemo;

import java.io.IOException;

import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import modeselection.ModeSelector;
import modeselection.Transitions;
import modeselection.TripleSensorFlagger;

public class ColorDemo {
	public static void main(String[] args) throws IOException {
		TripleSensorFlagger<Condition> color = new TripleSensorFlagger<>(new EV3ColorSensor(SensorPort.S3), s -> s.getRGBMode());
		color.add(Condition.RED, c -> c.r > c.b && c.r > c.g)
		     .add(Condition.GREEN, c -> c.g > c.r && c.g > c.b)
		     .add(Condition.BLUE, c -> c.b > c.r && c.b > c.g);
		
		Transitions<Condition,Mode> table = new Transitions<>();
		table.add(Condition.RED, Mode.LEFT)
		 	 .add(Condition.GREEN, Mode.FORWARD)
		 	 .add(Condition.BLUE, Mode.RIGHT);
	
		ModeSelector<Condition,Mode> controller = 
				new ModeSelector<>(Condition.class,Mode.class,Mode.FORWARD);
		controller.flagger(color)
				  .mode(Mode.LEFT, table, () -> {
					  Motor.A.backward();
					  Motor.D.forward();})
				  .mode(Mode.FORWARD, table, () -> {
					  Motor.A.forward();
					  Motor.D.forward();})
				  .mode(Mode.RIGHT, table, () -> {
					  Motor.A.forward();
					  Motor.D.backward();});
	
		controller.control();
	}
}
