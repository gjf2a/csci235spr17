package ideas.vision.features;

import modeselection.vision.Feature;

public class ScoredFeature extends Feature {
	private double score;

	public ScoredFeature(int x, int y, double score) {
		super(x, y);
		this.score = score;
	}
	
	public double getScore() {return score;}
}
