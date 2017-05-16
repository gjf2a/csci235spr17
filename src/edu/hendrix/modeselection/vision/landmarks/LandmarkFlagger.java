package edu.hendrix.modeselection.vision.landmarks;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import edu.hendrix.modeselection.SensedValues;
import edu.hendrix.modeselection.cluster.ShrinkingImageBSOC;
import edu.hendrix.modeselection.util.Duple;
import edu.hendrix.modeselection.util.Logger;
import edu.hendrix.modeselection.util.Util;
import edu.hendrix.modeselection.vision.AdaptedYUYVImage;
import edu.hendrix.modeselection.vision.SubFlagger;

public class LandmarkFlagger<C extends Enum<C>> implements SubFlagger<C> {
	private ShrinkingImageBSOC bsoc;
	private Map<Integer,LandmarkPredicate<C>> conditions;
	private Duple<Integer,Long> prevMatch;
	private C prevCondition;
	
	public LandmarkFlagger(int nodes, int shrink) {
		this(new ShrinkingImageBSOC(nodes, shrink));
	}
	
	public LandmarkFlagger(ShrinkingImageBSOC bsoc) {
		this.bsoc = bsoc;
		conditions = new HashMap<>();
	}
	
	public LandmarkFlagger(String bsocFile) throws FileNotFoundException {
		this(Util.fileToObject(new File(bsocFile), ShrinkingImageBSOC::new));
	}
	
	public void train(AdaptedYUYVImage img) {
		bsoc.train(img);
	}
	
	public LandmarkFlagger<C> add(C flag, int node, Predicate<Long> matcher) {
		conditions.put(node, new LandmarkPredicate<>(matcher, flag));
		return this;
	}
	
	public LandmarkFlagger<C> add(C flag, int... nodes) {
		for (int node: nodes) {
			add(flag, node, d -> true);
		}
		return this;
	}
	
	@Override
	public int numConditions() {
		return conditions.size();
	}

	@Override
	public void update(AdaptedYUYVImage img, SensedValues<C> conditions) {
		prevMatch = bsoc.getClosestNodeDistanceFor(img);
		if (this.conditions.containsKey(prevMatch.getFirst())) {
			LandmarkPredicate<C> pred = this.conditions.get(prevMatch.getFirst());			
			if (pred.matches(prevMatch.getSecond())) {
				prevCondition = pred.getFlag();
				conditions.add(prevCondition);	
				return;
			} 
		} 
		prevCondition = null;
	}

	@Override
	public void log(Logger logger) {
		logger.format("node: %d distance: %d", prevMatch.getFirst(), prevMatch.getSecond());
		if (prevCondition == null) {
			logger.log("No landmark match");
		} else {
			logger.format("Landmark: %s", prevCondition.name());
		}
	}
}
