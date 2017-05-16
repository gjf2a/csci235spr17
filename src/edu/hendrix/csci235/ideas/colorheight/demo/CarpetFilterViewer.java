package edu.hendrix.csci235.ideas.colorheight.demo;

import edu.hendrix.modeselection.vision.color.ColorFilter;
import edu.hendrix.modeselection.vision.color.config.ColorFilterViewer;

public class CarpetFilterViewer {
	public static void main(String[] args) {
		new ColorFilterViewer(new ColorFilter(121, 128, 121, 128)).run();
	}
}
