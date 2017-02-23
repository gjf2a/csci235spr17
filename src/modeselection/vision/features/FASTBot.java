package modeselection.vision.features;

import modeselection.util.Logger;
import modeselection.vision.AdaptedYUYVImage;
import modeselection.vision.BitImage;
import modeselection.vision.config.VisionBot;

// Shi-Thomasi
// 500 => 0.18 Hz
// 100 => 0.21 Hz

// (100, 4) => 0.12 hz
// (100, 20) => 0.17 hz

// BSOC 
// 100 => 0.01 hz

// No filter
// s = 1 => 0.19 hz

// Ditching Thresh enum
// 1.24 hz
// 1.89 hz
// 2.51 hz
// Very irregular though...

public class FASTBot extends VisionBot {
	private int n, s;
	
	public FASTBot(int n, int s) {this.n = n; this.s = s;}
	
	public static void main(String[] args) {
		new FASTBot(100, 1).run();
	}
	
	@Override
	public BitImage processImage(AdaptedYUYVImage img) {
		Logger.EV3Log.format("cycle:%d time:%d", getCycles(), getLastCycleTime());
		return new FAST(new AdaptedYUYVImage(img), s);
	}
}
