package proj7.backup;

import modeselection.vision.color.ColorFilter;
import modeselection.vision.color.config.ColorFilterViewer;

public class BookChecker {
	public static void main(String[] args) {
		ColorFilterViewer viewer = new ColorFilterViewer(new ColorFilter(128, 133, 114, 122));
		viewer.run();
	}
}
