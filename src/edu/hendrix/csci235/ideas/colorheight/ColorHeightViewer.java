package edu.hendrix.csci235.ideas.colorheight;

import edu.hendrix.modeselection.vision.AdaptedYUYVImage;
import edu.hendrix.modeselection.vision.BitImage;
import edu.hendrix.modeselection.vision.color.ColorFilter;
import edu.hendrix.modeselection.vision.config.VisionBot;

public class ColorHeightViewer extends VisionBot {
	public ColorHeightViewer(ColorFilter model) {
		this.model = model;
	}
	
	private ColorFilter model;

	@Override
	public BitImage processImage(AdaptedYUYVImage img) {
		BitImage result = model.filtered(img);
		ColorHeight height = new ColorHeight(model, img);
		if (height.highestHeight() > 1) {
			result.drawVerticalLine(height.highestX());			
		}
		return result;
	}

	@Override
	public void displayFinalInfo() {
		displayFrameRate(2);
	}
}
