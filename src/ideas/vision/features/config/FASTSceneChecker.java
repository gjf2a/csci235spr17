package ideas.vision.features.config;

import java.io.File;
import java.io.FileNotFoundException;

import ideas.vision.features.FAST;
import modeselection.util.Util;
import modeselection.vision.AdaptedYUYVImage;
import modeselection.vision.BitImage;
import modeselection.vision.config.VisionBot;

public class FASTSceneChecker extends VisionBot {
	public static final String FILENAME = "img1.txt";

	private BitImage ref;
	
	public static void main(String[] args) throws FileNotFoundException {
		FASTSceneChecker checker = new FASTSceneChecker(FILENAME);
		checker.run();
	}
	
	public FASTSceneChecker(String filename) throws FileNotFoundException {
		ref = Util.fileToObject(new File(filename), s -> BitImage.from(s));
	}
	
	@Override
	public BitImage processImage(AdaptedYUYVImage img) {
		BitImage proc = new FAST(img);
		return proc.xored(ref);
	}
}
