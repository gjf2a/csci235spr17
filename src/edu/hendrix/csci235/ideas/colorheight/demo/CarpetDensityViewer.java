package edu.hendrix.csci235.ideas.colorheight.demo;

import edu.hendrix.modeselection.vision.color.ColorFilter;
import edu.hendrix.modeselection.vision.color.config.ColorConcentrationViewer;

public class CarpetDensityViewer {
	public static void main(String[] args) {
		new ColorConcentrationViewer(new ColorFilter(121, 128, 121, 128), 1).run();
	}
}
