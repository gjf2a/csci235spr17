package edu.hendrix.modeselection.vision.distances;

import edu.hendrix.modeselection.vision.ImageDistanceFunc;

public class DimInvSimMeasure extends ImageDistanceFunc {
	public DimInvSimMeasure() {
		super(new DimInvSimMeasurePixel());
	}
}
