package ideas.colorheight;

import modeselection.vision.AdaptedYUYVImage;
import modeselection.vision.BitImage;
import modeselection.vision.color.ColorFilter;
import modeselection.vision.config.VisionBot;

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
