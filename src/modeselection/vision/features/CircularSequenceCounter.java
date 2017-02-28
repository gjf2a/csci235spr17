package modeselection.vision.features;

public class CircularSequenceCounter {
	private byte[] comparisons;
	private int[] countFor;

	public CircularSequenceCounter(int length) {
		comparisons = new byte[length];
		countFor = new int[length];
	}
	
	public void set(int i, byte tag) {
		comparisons[i] = tag;
	}
	
	public void countAll(byte of) {
		boolean inMatch = false;
		int matchStart = 0;
		for (int i = 0; i < comparisons.length; i++) {
			if (comparisons[i] == of) {
				if (!inMatch) {
					inMatch = true;
					matchStart = i;
				}
				countFor[i] = i - matchStart + 1;
			} else {
				inMatch = false;
				countFor[i] = 0;
			}
		}
		
		for (int i = 0; i < countFor.length - 1 && countFor[i] > 0; i++) {
			countFor[i] += countFor[countFor.length - 1];
		}
	}
	
	public int getLongest() {
		int max = countFor[0];
		for (int i = 1; i < countFor.length; i++) {
			if (countFor[i] > max) {
				max = countFor[i];
			}
		}
		return max;
	}
	
	public byte getTag(int i) {return comparisons[i];}
}
