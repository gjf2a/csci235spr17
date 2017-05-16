package edu.hendrix.modeselection;

import java.util.function.Function;

import lejos.hardware.sensor.BaseSensor;
import lejos.robotics.SampleProvider;

public class SensorFlagger<C extends Enum<C>> extends BaseSensorFlagger<C,Float> {
	public SensorFlagger(BaseSensor sensor) {
		super(sensor);
	}
	
	public <S extends BaseSensor> SensorFlagger(S sensor, Function<S,SampleProvider> invocation) {
		super(sensor, invocation);
	}
	
	public SensorFlagger<C> addValue(C valueFlag) {
		addValue(valueFlag, d -> (double)d);
		return this;
	}
	
	@Override
	public void update(SensedValues<C> conditions) {
		fetcher.fetchSample(values, 0);
		update(values[0], conditions);
	}
	
	public float getLastValue() {return values[0];}

	@Override
	protected String getLogMsg() {
		return String.format("%s: %5.2f", sensor.getName(), values[0]);
	}
}
