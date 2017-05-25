package edu.hendrix.modeselection.vision.landmarks;

import java.util.function.Predicate;

public class LandmarkPredicate<C extends Enum<C>> {
	private Predicate<Double> checker;
	private C flag;
	
	public LandmarkPredicate(Predicate<Double> checker, C flag) {
		this.checker = checker;
		this.flag = flag;
	}
	
	public boolean matches(double distance) {
		return checker.test(distance);
	}
	
	public C getFlag() {return flag;}
}
