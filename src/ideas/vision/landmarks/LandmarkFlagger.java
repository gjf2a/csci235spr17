package ideas.vision.landmarks;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import modeselection.SensedValues;
import modeselection.cluster.ShrinkingImageBSOC;
import modeselection.util.Duple;
import modeselection.util.Logger;
import modeselection.vision.AdaptedYUYVImage;
import modeselection.vision.SubFlagger;

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
	
	public void train(AdaptedYUYVImage img) {
		bsoc.train(img);
	}
	
	public void add(C flag, int node, Predicate<Long> matcher) {
		conditions.put(node, new LandmarkPredicate<>(matcher, flag));
	}
	
	@Override
	public int numConditions() {
		return conditions.size();
	}

	@Override
	public void update(AdaptedYUYVImage img, SensedValues<C> conditions) {
		prevMatch = bsoc.getClosestNodeDistanceFor(img);
		LandmarkPredicate<C> pred = this.conditions.get(prevMatch.getFirst());
		if (pred.matches(prevMatch.getSecond())) {
			prevCondition = pred.getFlag();
			conditions.add(prevCondition);
		} else {
			prevCondition = null;
		}
	}

	@Override
	public void log(Logger logger) {
		logger.format("node: %d distance: %d", prevMatch.getFirst(), prevMatch.getSecond());
		if (prevCondition != null) {
			logger.format("Landmark: %s", prevCondition.name());
		}
	}
}
