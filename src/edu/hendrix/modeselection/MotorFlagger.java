package edu.hendrix.modeselection;

import edu.hendrix.modeselection.util.Logger;
import lejos.hardware.motor.NXTRegulatedMotor;

public class MotorFlagger<C extends Enum<C>> extends BaseFlagger<C,Integer> implements Flagger<C> {
	private NXTRegulatedMotor target;
	
	public MotorFlagger(NXTRegulatedMotor target) {
		this.target = target;
	}
	
	public MotorFlagger<C> addValue(C valueFlag) {
		addValue(valueFlag, i -> (double)i);
		return this;
	}
	
	@Override
	public void update(SensedValues<C> conditions) {
		update(target.getTachoCount(), conditions);
	}
	
	@Override
	public void log(Logger logger) {
		logger.format("Motor: %d", target.getTachoCount());
	}

	@Override
	protected String getLogMsg() {
		return String.format("Motor: %d", target.getTachoCount());
	}
}
