package edu.hendrix.modeselection;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.Function;

import lejos.hardware.sensor.BaseSensor;
import lejos.robotics.SampleProvider;

abstract public class BaseSensorFlagger<C extends Enum<C>, D> extends BaseFlagger<C,D> implements Flagger<C>, Closeable {
	protected BaseSensor sensor;
	protected SampleProvider fetcher;
	protected float[] values;
	
	public BaseSensorFlagger(BaseSensor sensor) {
		this(sensor, s -> s);
	}
	
	public <S extends BaseSensor> BaseSensorFlagger(S sensor, Function<S,SampleProvider> invocation) {
		this.sensor = sensor;
		this.fetcher = invocation.apply(sensor);
		values = new float[fetcher.sampleSize()];
	}
	
	@Override
	public void close() throws IOException {
		sensor.close();
	}

	@Override
	protected String getLogMsg() {
		return String.format("%s: %5.2f", sensor.getName(), values[0]);
	}
}
