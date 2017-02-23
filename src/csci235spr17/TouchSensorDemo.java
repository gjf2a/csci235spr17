package csci235spr17;

import lejos.hardware.Button;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;

public class TouchSensorDemo {
	public static void main(String[] args) {
		EV3TouchSensor bumper = new EV3TouchSensor(SensorPort.S1);
		float[] value = new float[1];
		while (!Button.ESCAPE.isDown()) {
			bumper.fetchSample(value, 0);
			System.out.println(value[0]);
		}
		bumper.close();
	}
}
