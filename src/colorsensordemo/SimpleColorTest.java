package colorsensordemo;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;

public class SimpleColorTest {
	public static void main(String[] args) {
		EV3ColorSensor color = new EV3ColorSensor(SensorPort.S3);
		float[] values = new float[3];
		while (Button.ESCAPE.isUp()) {
			color.getRGBMode().fetchSample(values, 0);
			LCD.drawString(String.format("%3.2f",  values[0]), 0, 0);
			LCD.drawString(String.format("%3.2f",  values[1]), 0, 1);
			LCD.drawString(String.format("%3.2f",  values[2]), 0, 2);
		}
		color.close();
	}
}
