package modeselection.vision.features;

public class ThreshCounter {
	public static final byte ABOVE = 0, BELOW = 1, WITHIN = 2;
	
	private int[] counts = new int[3];
	
	public void bump(byte which) {
		counts[which] += 1;
	}
	
	public void reset() {
		counts[0] = counts[1] = counts[2] = 0;
	}
	
	public int getCountFor(byte which) {
		return counts[which];
	}
}
