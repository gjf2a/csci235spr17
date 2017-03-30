package ideas.colorheight;

import lejos.hardware.video.YUYVImage;
import modeselection.vision.color.ColorFilter;
import modeselection.vision.config.VisionBot;

public class ColorHeight {
	private int[] heights;
	private int highest;
	
	public ColorHeight(ColorFilter color, YUYVImage img) {
		heights = new int[VisionBot.WIDTH / 2];
		int highestValue = 0;
		highest = size() / 2;
		for (int x = 0; x < VisionBot.WIDTH; x += 2) {
			int where = x / 2;
			heights[where] = 0;
			int y = VisionBot.HEIGHT - 1;
			while (y >= 1 && (color.contains(img, x, y) || color.contains(img, x, y-1))) {
				heights[where] += 1;
				y -= 1;
			}
			if (heights[where] > highestValue) {
				highest = where * 2;
				highestValue = heights[where];
			}
		}
	}
	
	@Override
	public String toString() {
		return String.format("ColorHeight: highest(): %d", highestX());
	}
	
	public int size() {
		return heights.length;
	}
	
	public int heightAt(int x) {
		return heights[x/2];
	}
	
	public int highestX() {
		return highest;
	}
	
	public int highestHeight() {
		return heightAt(highestX());
	}
}
