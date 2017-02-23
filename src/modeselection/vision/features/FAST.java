package modeselection.vision.features;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import modeselection.cluster.BoundedSelfOrgCluster;
import modeselection.vision.BitImage;
import modeselection.vision.ProcessableImage;

public class FAST extends BitImage {
	
	public static final boolean SMOOTH_PYRAMID = false;
	
	public static final int RADIUS = 3, 
			N = 12, 
			MIN_DIMENSION = RADIUS * 3,
			INTENSITY_THRESHOLD = 5;
	
	public static final Feature[] CIRCLE_POINTS = 
			new Feature[]{new Feature(0, 3), new Feature(1, 3), new Feature(2, 2), new Feature(3, 1),
					new Feature(3, 0), new Feature(3, -1), new Feature(2, -2), new Feature(1, -3), 
					new Feature(0, -3), new Feature(-1, -3), new Feature(-2, -2), new Feature(-3, -1),
					new Feature(-3, 0), new Feature(-3, 1), new Feature(-2, 2), new Feature(-1, 3)};
	
	public static <T extends ProcessableImage<T>> FAST nFeatures(T img, int n, int scale) {
		FAST features = new FAST(img, scale);
		features.retainBestFeatures(img, n);
		return features;
	}
	
	public static <T extends ProcessableImage<T>> FAST nClusters(T img, int n, int scale) {
		FAST features = new FAST(img, scale);
		features.clusterFeatures(img, n);
		return features;
	}
	
	public static long totalDistance(LinkedHashMap<Feature,Feature> matches) {
		long total = 0;
		for (Entry<Feature, Feature> match: matches.entrySet()) {
			total += Feature.euclideanDistanceSquared(match.getKey(), match.getValue());
		}
		return total;
	}
	
	public <T extends ProcessableImage<T>> FAST(T img, int scale) {
		super(img.getWidth(), img.getHeight());
		scalePyramid(img, scale);
	}
	
	private <T extends ProcessableImage<T>> void scalePyramid(T img, int scale) {
		while (img.canShrinkBy(2) && img.getWidth() >= MIN_DIMENSION && img.getHeight() >= MIN_DIMENSION) {
			addFeaturesFor(img, scale);
			if (SMOOTH_PYRAMID) {
				img = img.gaussianSmoothed().shrunken(2); 
			} else {
				img = img.shrunken(2);
			}
			scale *= 2;
		}
	}
	
	public <T extends ProcessableImage<T>> void addFeaturesFor(ProcessableImage<T> img, int featureScale) {
		for (int x = RADIUS; x < img.getWidth() - RADIUS - 1; x++) {
			for (int y = RADIUS; y < img.getHeight() - RADIUS - 1; y++) {
				byte i0 = eval(img, x, y, 0);
				byte i8 = eval(img, x, y, 8);
				if (i0 != ThreshCounter.WITHIN || i8 != ThreshCounter.WITHIN) {
					byte i4 = eval(img, x, y, 4);
					byte i12 = eval(img, x, y, 12);
					ThreshCounter counts = new ThreshCounter();
					counts.bump(i0);
					counts.bump(i4);
					counts.bump(i8);
					counts.bump(i12);
					boolean found = false;
					if (counts.getCountFor(ThreshCounter.ABOVE) >= 3) {
						found = longestSequenceOf(getComparisons(img, x, y), ThreshCounter.ABOVE) >= N;
					} else if (counts.getCountFor(ThreshCounter.BELOW) >= 3) {
						found = longestSequenceOf(getComparisons(img, x, y), ThreshCounter.BELOW) >= N;
					}
					if (found) {
						set(x * featureScale, y * featureScale);
					}
				}
			}
		}
	}
	
	// def gaussian_kernels(sigma, max_width):
	//     if max_width % 2 == 0:
	//         raise ValueError("max_width should be odd")
	//     hw = max_width // 2
	//     max_gauss = 1.0
	//     max_gauss_deriv = sigma * math.exp(-0.5)
	// 
	//     gauss = []
	//     gauss_deriv = []
	// 
	//     for i in range(max_width):
	//         v = i - hw
	//         gauss.append(math.exp(-v**2 / (2*sigma**2)))
	//         gauss_deriv.append(-v * gauss[i])
	// 
	//     return gauss, gauss_deriv
	// 
	// >>> gaussian_kernels(1, 5)
	// ([0.1353352832366127, 0.6065306597126334, 1.0, 0.6065306597126334, 0.1353352832366127], 
	//  [0.2706705664732254, 0.6065306597126334, 0.0, -0.6065306597126334, -0.2706705664732254])
	//
	public static final int[] gaussian = new int[]{14, 61, 100, 61, 14};
	public static final int[] gaussianDerivative = new int[]{27, 61, 0, -61, -27};

	// This is a Shi-Thomasi filter applied to the FAST features (presumably) found already.
	public <T extends ProcessableImage<T>> void retainBestFeatures(ProcessableImage<T> img, int n) {
		if (size() > n) {
			ProcessableImage<T> xGradient = img.twoPassConvolve(gaussian, gaussianDerivative);
			ProcessableImage<T> yGradient = img.twoPassConvolve(gaussianDerivative, gaussian);
			PriorityQueue<ScoredFeature> scoredFeatures = new PriorityQueue<>((f1, f2) -> f1.getScore() > f2.getScore() ? -1 : f1.getScore() < f2.getScore() ? 1 : 0);
			for (Feature f: allSet()) {
				double gxx = 0;
				double gyy = 0;
				double gxy = 0;
				for (int i = 0; i < RADIUS; i++) {
					int offset = i - RADIUS/2;
					int fx = f.X() + offset;
					int fy = f.Y() + offset;
					if (fx >= 0 && fy >= 0 && fx < img.getWidth() && fy < img.getHeight()) {
						double gx = xGradient.getIntensity(fx, fy);
						double gy = yGradient.getIntensity(fx, fy);
						gxx += gx*gx;
						gxy += gx*gy;
						gyy += gy*gy;
					}
				}
				double minEigenvalue = (gxx + gyy - Math.sqrt((gxx - gyy)*(gxx - gyy) + 4*gxy*gxy))/2.0;
				scoredFeatures.add(new ScoredFeature(f.X(), f.Y(), minEigenvalue));
			}

			clearAll();
			while (size() < n) {
				ScoredFeature sf = scoredFeatures.remove();
				set(sf.X(), sf.Y());
			}
		}
	}
	
	public <T extends ProcessableImage<T>> void clusterFeatures(ProcessableImage<T> img, int n) {
		BoundedSelfOrgCluster<Feature> bsoc = new BoundedSelfOrgCluster<>(n, Feature::euclideanDistanceSquared);
		for (Feature f: allSet()) {
			bsoc.train(f);
		}
		clearAll();
		for (Feature f: bsoc.getIdealInputs()) {
			set(f.X(), f.Y());
		}
	}
	
	<T extends ProcessableImage<T>> byte[] getComparisons(ProcessableImage<T> img, int x, int y) {
		byte[] result = new byte[CIRCLE_POINTS.length];
		for (int i = 0; i < CIRCLE_POINTS.length; i++) {
			result[i] = eval(img, x, y, i);
		}
		return result;
	}
	
	int longestSequenceOf(byte[] threshes, byte of) {
		int[] countFor = new int[threshes.length];
		for (int i = 0; i < threshes.length; i++) {
			int j = 0;
			while (j < threshes.length && threshes[(i+j) % threshes.length] == of) {
				j += 1;
			}
			countFor[i] = j;
		}
		int max = countFor[0];
		for (int i = 1; i < countFor.length; i++) {
			if (countFor[i] > max) {
				max = countFor[i];
			}
		}
		return max;
	}
	
	<T extends ProcessableImage<T>> int findFirstOf(ProcessableImage<T> img, int x, int y, int intensityThreshold, byte of) {
		for (int i = 0; i < CIRCLE_POINTS.length; i++) {
			byte iThresh = eval(img, x + CIRCLE_POINTS[i].X(), y + CIRCLE_POINTS[i].Y(), i);
			if (iThresh == of) {
				return i;
			}
		}
		return -1;
	}
	
	static <T extends ProcessableImage<T>> byte eval(ProcessableImage<T> img, int x, int y, int circPt) {
		Feature f = CIRCLE_POINTS[circPt];
		int diff = img.getIntensity(x + f.X(), y + f.Y()) - img.getIntensity(x, y);
		return diff > INTENSITY_THRESHOLD 
				? ThreshCounter.ABOVE 
				: (diff < -INTENSITY_THRESHOLD ? ThreshCounter.BELOW : ThreshCounter.WITHIN);
	}
}
