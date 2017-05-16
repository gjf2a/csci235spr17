package edu.hendrix.modeselection.vision.color.config;

import edu.hendrix.modeselection.vision.AdaptedYUYVImage;
import edu.hendrix.modeselection.vision.BitImage;
import edu.hendrix.modeselection.vision.color.ColorCount;
import edu.hendrix.modeselection.vision.color.ColorFilter;
import edu.hendrix.modeselection.vision.config.VisionBot;

public class ColorConcentrationViewer extends VisionBot {
	public static void main(String[] args) {
		new ColorConcentrationViewer(new ColorFilter(89, 106, 190, 213), 20).run(); // Banana
	}
	
	public ColorConcentrationViewer(ColorFilter model, int minPixels) {
		this.model = model;
		this.minPixels = minPixels;
	}
	
	private ColorFilter model;
	private int minPixels;

	@Override
	public BitImage processImage(AdaptedYUYVImage img) {
		BitImage result = model.filtered(img);
		ColorCount colors = new ColorCount(model, img);
		if (colors.getTotal() >= minPixels) {
			int best = colors.densest();
			result.drawVerticalLine(best);
		}
		return result;
	}

	@Override
	public void displayFinalInfo() {
		displayFrameRate(2);
	}
}
