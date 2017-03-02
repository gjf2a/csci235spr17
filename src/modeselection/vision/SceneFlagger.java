package modeselection.vision;

import java.util.function.BiFunction;
import java.util.function.Function;

import modeselection.util.Util;

public class SceneFlagger<C extends Enum<C>,I,V extends Comparable<V>> extends BaseSubFlagger<C,V> {
	private I[] images;
	private BiFunction<I,I,V> distFunc;
	private Function<AdaptedYUYVImage,I> transform;
	
	@SafeVarargs
	public SceneFlagger(BiFunction<I,I,V> distFunc, Function<AdaptedYUYVImage,I> transform, I...images) {
		Util.assertArgument(images.length > 0, "No images provided");
		this.images = images;
		this.distFunc = distFunc;
	}

	@Override
	public V getSample(AdaptedYUYVImage img) {
		I transformed = transform.apply(img);
		V best = distFunc.apply(images[0], transformed);
		for (int i = 1; i < images.length; i++) {
			V current = distFunc.apply(images[i], transformed);
			if (current.compareTo(best) < 0) {
				best = current;
			}
		}
		return best;
	}
}
