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
	
	private ThreshCounter threshCounts = new ThreshCounter();
	private byte[] comparisons = new byte[CIRCLE_POINTS.length];
	private int[] countFor = new int[comparisons.length];
	
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
					threshCounts.reset();
					threshCounts.bump(i0);
					threshCounts.bump(i4);
					threshCounts.bump(i8);
					threshCounts.bump(i12);
					boolean found = false;
					if (threshCounts.getCountFor(ThreshCounter.ABOVE) >= 3) {
						found = longestOf(ThreshCounter.ABOVE, img, x, y) >= N;
					} else if (threshCounts.getCountFor(ThreshCounter.BELOW) >= 3) {
						found = longestOf(ThreshCounter.BELOW, img, x, y) >= N;
					}
					if (found) {
						set(x * featureScale, y * featureScale);
					}
				}
			}
		}
	}
	
	<T extends ProcessableImage<T>> int longestOf(byte of, ProcessableImage<T> img, int x, int y) {
		evalCircleAt(img, x, y);
		findCountsFor(of);
		return longestCountAt();
	}
	
	private <T extends ProcessableImage<T>> void evalCircleAt(ProcessableImage<T> img, int x, int y) {
		for (int i = 0; i < CIRCLE_POINTS.length; i++) {
			comparisons[i] = eval(img, x, y, i);
			countFor[i] = 0;
		}		
	}
	
	private void findCountsFor(byte of) {
		for (int i = 0; i < comparisons.length; i++) {
			int j = 0;
			while (j < comparisons.length && comparisons[(i+j) % comparisons.length] == of) {
				j += 1;
			}
			countFor[i] = j;
		}		
	}
	
	private int longestCountAt() {
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
