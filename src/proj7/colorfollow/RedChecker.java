package proj7.colorfollow;

import modeselection.vision.color.ColorFilter;
import modeselection.vision.color.config.ColorFilterViewer;

public class RedChecker {
	public static void main(String[] args) {
		ColorFilterViewer viewer = new ColorFilterViewer(new ColorFilter(89, 106, 190, 213));
		viewer.run();
	}
}
