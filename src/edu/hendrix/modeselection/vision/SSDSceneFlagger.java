package edu.hendrix.modeselection.vision;

import java.io.FileNotFoundException;

import edu.hendrix.modeselection.vision.distances.Euclidean;

public class SSDSceneFlagger<C extends Enum<C>> extends SceneFlagger<C,AdaptedYUYVImage,Double> {
	
	private static final int SHRINK = 2;
	private final static Euclidean DIST_FUNC = new Euclidean();
	
	public static AdaptedYUYVImage adapt(AdaptedYUYVImage src) {
		return src.shrunken(SHRINK);
	}
	
	public SSDSceneFlagger(String... filenames) throws FileNotFoundException {
		super((img1, img2) -> DIST_FUNC.distance(img1, img2), 
				img -> adapt(img), 
				s -> adapt(AdaptedYUYVImage.fromString(s)), 
				filenames);
	}
}
