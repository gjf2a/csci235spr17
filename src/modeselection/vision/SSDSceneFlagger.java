package modeselection.vision;

import java.io.File;
import java.io.FileNotFoundException;

import modeselection.util.Util;

public class SSDSceneFlagger<C extends Enum<C>> extends BaseSubFlagger<C,Long> {

	private AdaptedYUYVImage img1;
	
	public static final int SHRINK = 2;
	
	public SSDSceneFlagger(String filename) throws FileNotFoundException {
		img1 = Util.fileToObject(new File(filename), s -> AdaptedYUYVImage.fromString(s)).shrunken(SHRINK);
	}
	
	@Override
	public Long getSample(AdaptedYUYVImage img2) {
		return img1.getDistanceTo(img2.shrunken(SHRINK));
	}
}
