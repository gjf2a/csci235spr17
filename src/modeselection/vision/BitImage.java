package modeselection.vision;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

import lejos.hardware.lcd.LCD;
import lejos.hardware.video.YUYVImage;
import modeselection.util.DeepCopyable;
import modeselection.vision.features.Feature;
import modeselection.vision.features.StableMatchPrefs;

public class BitImage implements ImageOutline, DeepCopyable<BitImage> {
	private BitSet pixels;
	private int width, height;
	
	public BitImage(BitImage src) {
		this.width = src.width;
		this.height = src.height;
		this.pixels = src.pixels.get(0, width*height);
	}
	
	public BitImage(YUYVImage src) {
		this(src.getWidth(), src.getHeight());
	}
	
	public BitImage(int width, int height) {
		pixels = new BitSet(width * height);
		pixels.clear();
		this.width = width;
		this.height = height;
	}
	
	public BitImage(YUYVImage src, TriIntPredicate classifier) {
		this.width = src.getWidth();
		this.height = src.getHeight();
		pixels = new BitSet(width * height);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				set(x, y, 
					classifier.test(src.getY(x, y) & 0xFF,
									src.getU(x, y) & 0xFF,
									src.getV(x, y) & 0xFF));
			}
		}
	}
	
	public static BitImage basicView(YUYVImage src) {
		int mean = src.getMeanY();
		BitImage img = new BitImage(src);
		for (int y = 0; y < img.getHeight(); y++) {
			for (int x = 0; x < img.getWidth(); x++) {
				img.set(x, y, (src.getY(x, y) & 0xFF) > mean);
			}
		}
		return img;
	}
	
	public static BitImage colorView(YUYVImage src, BiIntPredicate colors) {
		BitImage img = new BitImage(src);
		for (int y = 0; y < img.getHeight(); y++) {
			for (int x = 0; x < img.getWidth(); x+=2) {
				boolean match = colors.test(src.getU(x, y) & 0xFF, src.getV(x, y) & 0xFF);
				img.set(x, y, match);
				img.set(x+1, y, match);
			}
		}
		return img;
	}
	
	public void applyToSubimage(int x, int y, int width, int height, BiConsumer<Integer,Integer> func) {
		int x1 = Math.max(0, x);
		int x2 = Math.min(getWidth() - 1, x + width - 1);
		int y1 = Math.max(0, y);
		int y2 = Math.min(getHeight() - 1, y + height - 1);
		for (int xi = x1; xi <= x2; xi++) {
			for (int yi = y1; yi <= y2; yi++) {
				if (isSet(xi, yi)) {func.accept(xi, yi);}
			}
		}
	}
	
	public void draw() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				LCD.setPixel(x, y, isSet(x, y) ? 1 : 0);
			}
		}
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				result.append(isSet(x, y) ? "1" : "0");
			}
			result.append("\n");
		}
		return result.toString();
	}
	
	@Override
	public int hashCode() {
		return pixels.hashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof BitImage) {
			BitImage that = (BitImage)other;
			return this.width == that.width && this.height == that.height && this.pixels.equals(that.pixels);
		} else {
			return false;
		}
	}
	
	public int getWidth() {return width;}
	public int getHeight() {return height;}
	
	public int size() {return pixels.cardinality();}
	
	public void set(int x, int y, boolean value) {
		pixels.set(index(x, y), value);
	}
	
	public void set(int x, int y) {
		set(x, y, true);
	}
	
	public void clear(int x, int y) {
		set(x, y, false);
	}
	
	public void flip(int x, int y) {
		set(x, y, !isSet(x, y));
	}
	
	public void clearAll() {
		pixels.clear();
	}
	
	public boolean isSet(int x, int y) {
		return pixels.get(index(x, y));
	}
	
	int index(int x, int y) {
		return y * width + x;
	}
	
	int xPart(int index) {
		return index % width;
	}
	
	int yPart(int index) {
		return index / width;
	}
	
	public ArrayList<Feature> allSet() {
		ArrayList<Feature> result = new ArrayList<>();
		for (int i = pixels.nextSetBit(0); i >= 0; i = pixels.nextSetBit(i+1)) {
			result.add(new Feature(xPart(i), yPart(i)));
		}
		return result;
	}

	@Override
	public BitImage deepCopy() {
		return new BitImage(this);
	}
	
	public static LinkedHashMap<Feature,Feature> getStableMatches(BitImage img1, BitImage img2) {
		return StableMatchPrefs.makeStableMatches(img1.allSet(), img2.allSet(), (m, w) -> (int)(Feature.euclideanDistanceSquared(m, w)));
	}
	
	public static LinkedHashMap<Feature,Feature> getGreedyMatches(BitImage img1, BitImage img2, int searchBound) {
		LinkedHashMap<Feature,Long> bestDistances = new LinkedHashMap<>();
		LinkedHashMap<Feature,Feature> result = new LinkedHashMap<>();
		visitNeighbors(img1, img2, searchBound, (f1, f2) -> {
			long f2distance = Feature.euclideanDistanceSquared(f1, f2);
			if (!bestDistances.containsKey(f1) || f2distance < bestDistances.get(f1)) {
				result.put(f1, f2);
				bestDistances.put(f1, f2distance);
			}
		});
		return result;
	}
	
	public static void visitNeighbors(BitImage img1, BitImage img2, int searchBound, BiConsumer<Feature,Feature> neighborFunc) {
		for (Feature f1: img1.allSet()) {
			img2.applyToSubimage(f1.X() - searchBound/2, f1.Y() - searchBound/2, searchBound, searchBound, (x,y) -> {
				neighborFunc.accept(f1, new Feature(x, y));
			});
		}
	}
}
