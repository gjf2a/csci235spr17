package ideas.colorheight.demo;

import modeselection.vision.color.ColorFilter;
import modeselection.vision.color.config.ColorConcentrationViewer;

public class CarpetDensityViewer {
	public static void main(String[] args) {
		new ColorConcentrationViewer(new ColorFilter(121, 128, 121, 128), 1).run();
	}
}
