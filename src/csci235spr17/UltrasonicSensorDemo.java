package csci235spr17;

import lejos.hardware.Button;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;

public class UltrasonicSensorDemo {
	public static void main(String[] args) {
		EV3UltrasonicSensor sonar = new EV3UltrasonicSensor(SensorPort.S2);
		float[] value = new float[1];
		while (!Button.ESCAPE.isDown()) {
			sonar.getDistanceMode().fetchSample(value, 0);
			System.out.println(value[0]);
		}
		sonar.close();
	}
}
