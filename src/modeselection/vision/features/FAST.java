package modeselection.vision.features;

import modeselection.vision.BitImage;
import modeselection.vision.ProcessableImage;

public class FAST extends BitImage {
	public static final byte ABOVE = 0, BELOW = 1, WITHIN = 2;
	
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
	
	private CircularSequenceCounter counter = new CircularSequenceCounter(CIRCLE_POINTS.length);
	
	public <T extends ProcessableImage<T>> FAST(T img) {
		super(img.getWidth(), img.getHeight());
		scalePyramid(img, 1);
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
				if (i0 != WITHIN || i8 != WITHIN) {
					byte i4 = eval(img, x, y, 4);
					byte i12 = eval(img, x, y, 12);
					boolean above = any3(i0, i4, i8, i12, ABOVE);
					boolean below = any3(i0, i4, i8, i12, BELOW);
					if (above || below) {
						evalCircleAt(img, x, y);
						counter.countAll(above ? ABOVE : BELOW);
						if (counter.getLongest() >= N) {
							set(x * featureScale, y * featureScale);
						}
					}
				}
			}
		}
	}
	
	static boolean any3(byte a, byte b, byte c, byte d, byte target) {
		return a == target && b == target && c == target 
				|| a == target && b == target && d == target
				|| a == target && c == target && d == target
				|| b == target && c == target && d == target;
	}
	
	private <T extends ProcessableImage<T>> void evalCircleAt(ProcessableImage<T> img, int x, int y) {
		for (int i = 0; i < CIRCLE_POINTS.length; i++) {
			counter.set(i, eval(img, x, y, i));
		}		
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
				? ABOVE 
				: (diff < -INTENSITY_THRESHOLD ? BELOW : WITHIN);
	}
}
