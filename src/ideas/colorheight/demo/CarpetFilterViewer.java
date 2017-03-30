package ideas.colorheight.demo;

import modeselection.vision.color.ColorFilter;
import modeselection.vision.color.config.ColorFilterViewer;

public class CarpetFilterViewer {
	public static void main(String[] args) {
		new ColorFilterViewer(new ColorFilter(121, 128, 121, 128)).run();
	}
}
