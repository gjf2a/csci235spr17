package ideas.colorheight.demo;

import ideas.colorheight.ColorHeightViewer;
import modeselection.vision.color.ColorFilter;

public class CarpetHeightViewer {
	public static void main(String[] args) {
		new ColorHeightViewer(new ColorFilter(121, 128, 121, 128)).run();
	}
}
