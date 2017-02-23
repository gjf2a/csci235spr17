package modeselection.vision.features;

public class ThreshCounter {
	public static final byte ABOVE = 0, BELOW = 1, WITHIN = 2;
	
	private int[] counts = new int[3];
	
	public void bump(byte which) {
		counts[which] += 1;
	}
	
	public int getCountFor(byte which) {
		return counts[which];
	}
}
