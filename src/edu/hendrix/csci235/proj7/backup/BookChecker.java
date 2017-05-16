package edu.hendrix.csci235.proj7.backup;

import edu.hendrix.modeselection.vision.color.ColorFilter;
import edu.hendrix.modeselection.vision.color.config.ColorFilterViewer;

public class BookChecker {
	public static void main(String[] args) {
		ColorFilterViewer viewer = new ColorFilterViewer(new ColorFilter(128, 133, 114, 122));
		viewer.run();
	}
}
