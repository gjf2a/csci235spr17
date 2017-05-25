package edu.hendrix.modeselection.vision.distances;

import edu.hendrix.modeselection.vision.ImageDistanceFunc;

public class Euclidean implements ImageDistanceFunc {
	@Override
	public double calculationAt(int a, int b) {
		return Math.pow(b - a, 2);
	}
}
