package modeselection.vision;

import java.io.File;
import java.io.FileNotFoundException;

import lejos.hardware.video.YUYVImage;
import modeselection.util.Util;

public class SSDSceneFlagger<C extends Enum<C>> extends BaseSubFlagger<C,Long> {

	private AdaptedYUYVImage img1;
	
	public SSDSceneFlagger(String filename) throws FileNotFoundException {
		img1 = Util.fileToObject(new File(filename), s -> AdaptedYUYVImage.fromString(s));
	}
	
	@Override
	public Long getSample(YUYVImage img2) {
		return img1.getDistanceTo(img2);
	}
}
