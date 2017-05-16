package edu.hendrix.csci235.ideas.colorheight.demo;

import edu.hendrix.csci235.ideas.colorheight.ColorHeightViewer;
import edu.hendrix.modeselection.vision.color.ColorFilter;

public class CarpetHeightViewer {
	public static void main(String[] args) {
		new ColorHeightViewer(new ColorFilter(121, 128, 121, 128)).run();
	}
}
