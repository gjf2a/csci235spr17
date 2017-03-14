package ideas.vision;

import lejos.hardware.video.YUYVImage;
import modeselection.vision.color.ColorFilter;
import modeselection.vision.config.VisionBot;

public class ColorHeight {
	private int[] heights;
	private int highest;
	private int total;
	
	public ColorHeight(ColorFilter color, YUYVImage img) {
		heights = new int[VisionBot.WIDTH / 2];
		int highestValue = 0;
		total = 0;
		highest = size() / 2;
		for (int x = 0; x < VisionBot.WIDTH; x += 2) {
			int where = x / 2;
			for (int y = VisionBot.HEIGHT - 1; y >= 0; y--) {
				if (color.contains(img, x, y)) {
					heights[where] = VisionBot.HEIGHT - y;
					total += 1;
				}
			}
			if (heights[where] > highestValue) {
				highest = where;
			}
		}
	}
	
	@Override
	public String toString() {
		return String.format("ColorCount: highest(): %d", highest());
	}
	
	public int size() {
		return heights.length;
	}
	
	public int get(int x) {
		return heights[x];
	}
	
	public int getTotal() {return total;}
	
	public int highest() {
		return highest;
	}
}
