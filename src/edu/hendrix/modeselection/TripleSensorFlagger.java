package edu.hendrix.modeselection;

import java.util.function.Function;

import lejos.hardware.sensor.BaseSensor;
import lejos.robotics.SampleProvider;

public class TripleSensorFlagger<C extends Enum<C>> extends BaseSensorFlagger<C,ColorTriple> {
	public TripleSensorFlagger(BaseSensor sensor) {
		super(sensor);
	}
	
	public <S extends BaseSensor> TripleSensorFlagger(S sensor, Function<S,SampleProvider> invocation) {
		super(sensor, invocation);
	}
	
	@Override
	public void update(SensedValues<C> conditions) {
		fetcher.fetchSample(values, 0);
		update(new ColorTriple(values), conditions);
	}
	
	public float getLastValue() {return values[0];}

	@Override
	protected String getLogMsg() {
		return String.format("%s: %5.2f", sensor.getName(), values[0]);
	}
}
