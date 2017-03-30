package ideas.colorheight;

import modeselection.vision.AdaptedYUYVImage;
import modeselection.vision.BaseSubFlagger;
import modeselection.vision.color.ColorFilter;

public class ColorHeightFlagger<C extends Enum<C>> extends BaseSubFlagger<C,ColorHeight> {
	private ColorFilter filter;
	
	public ColorHeightFlagger(int uLo, int uHi, int vLo, int vHi) {
		filter = new ColorFilter(uLo, uHi, vLo, vHi);
	}

	@Override
	public ColorHeight getSample(AdaptedYUYVImage img) {
		return new ColorHeight(filter, img);
	}
}
