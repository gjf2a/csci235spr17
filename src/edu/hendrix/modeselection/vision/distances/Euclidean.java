package edu.hendrix.modeselection.vision.distances;

import edu.hendrix.modeselection.vision.ImageDistanceFunc;

public class Euclidean extends ImageDistanceFunc {
	public Euclidean() {
		super(new EuclideanPixel());
	}
}
