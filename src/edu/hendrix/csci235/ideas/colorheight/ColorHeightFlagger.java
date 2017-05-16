package edu.hendrix.csci235.ideas.colorheight;

import edu.hendrix.modeselection.vision.AdaptedYUYVImage;
import edu.hendrix.modeselection.vision.BaseSubFlagger;
import edu.hendrix.modeselection.vision.color.ColorFilter;

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
