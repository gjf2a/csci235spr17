package edu.hendrix.modeselection.vision.config;

import edu.hendrix.modeselection.vision.AdaptedYUYVImage;
import edu.hendrix.modeselection.vision.BitImage;

abstract public class VisionBot extends BasicVisionBot {
	public static final int WIDTH = 160, HEIGHT = 120;

	abstract public BitImage processImage(AdaptedYUYVImage img);
	
	@Override
	public void grabImage(AdaptedYUYVImage img) {
		BitImage proc = processImage(img);
		proc.draw();
	}
}
