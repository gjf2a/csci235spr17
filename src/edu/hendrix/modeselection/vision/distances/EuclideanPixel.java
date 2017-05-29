package edu.hendrix.modeselection.vision.distances;

import edu.hendrix.modeselection.vision.PixelDistanceFunc;

public class EuclideanPixel implements PixelDistanceFunc {

	@Override
	public double pixelDistance(double a, double b) {
		return Math.pow(b - a, 2);
	}

}
