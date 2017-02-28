package modeselection.vision.features;

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
