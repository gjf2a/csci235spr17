package modeselection.vision;

import java.io.File;
import java.io.FileNotFoundException;

import lejos.hardware.video.YUYVImage;
import modeselection.util.Util;

public class BitSceneFlagger<C extends Enum<C>> extends BaseSubFlagger<C,Integer> {

	private BitImage reference;
	
	public BitSceneFlagger(String filename) throws FileNotFoundException {
		reference = Util.fileToObject(new File(filename), s -> BitImage.from(s));
	}
	
	@Override
	public Integer getSample(YUYVImage img) {
		BitImage xored = BitImage.intensityView(img).xored(reference);
		return xored.size();
	}
}
