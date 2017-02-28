package proj5.floor;

import modeselection.vision.color.ColorFilter;
import modeselection.vision.color.config.ColorConcentrationViewer;

public class FloorConcentration {
	public static void main(String[] args) {
		new ColorConcentrationViewer(new ColorFilter(117, 128, 119, 127), 20).run();
	}
}
