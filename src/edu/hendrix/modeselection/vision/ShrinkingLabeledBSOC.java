package edu.hendrix.modeselection.vision;

import java.util.ArrayList;

import edu.hendrix.modeselection.cluster.LabeledBSOC;
import edu.hendrix.modeselection.util.EnumHistogram;
import edu.hendrix.modeselection.util.Util;
import edu.hendrix.modeselection.vision.distances.Euclidean;

public class ShrinkingLabeledBSOC<E extends Enum<E>> {
	private int shrink;
	private LabeledBSOC<AdaptedYUYVImage,AdaptedYUYVImage,E> bsoc;
	
	public ShrinkingLabeledBSOC(Class<E> enumClass, int maxNumNodes, E startLabel, int shrink) {
		bsoc = new LabeledBSOC<>(enumClass, new Euclidean(), img -> img.shrunken(shrink), maxNumNodes, startLabel);
		this.shrink = shrink;
	}
	
	public void train(AdaptedYUYVImage example, E label) {
		bsoc.train(example, label);
	}
	
	public E bestMatchFor(AdaptedYUYVImage example) {
		return bsoc.bestMatchFor(example);
	}
	
	public EnumHistogram<E> getCountsFor(AdaptedYUYVImage example) {
		return bsoc.getCountsFor(example);
	}
	
	public ShrinkingLabeledBSOC(Class<E> enumClass, String src) {
		ArrayList<String> parts = Util.debrace(src);
		shrink = Integer.parseInt(parts.get(1));
		bsoc = new LabeledBSOC<>(enumClass, parts.get(0), AdaptedYUYVImage::fromString, new Euclidean(), img -> img.shrunken(shrink));
	}
	
	@Override
	public String toString() {
		return '{' + bsoc.toString() + "}{" + shrink + "}";
	}
}
