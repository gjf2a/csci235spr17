package edu.hendrix.csci235.proj7.colorfollow;

import edu.hendrix.modeselection.vision.color.ColorFilter;
import edu.hendrix.modeselection.vision.color.config.ColorFilterViewer;

public class RedChecker {
	public static void main(String[] args) {
		ColorFilterViewer viewer = new ColorFilterViewer(new ColorFilter(89, 106, 190, 213));
		viewer.run();
	}
}
