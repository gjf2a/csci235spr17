package edu.hendrix.modeselection.movies;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

import edu.hendrix.modeselection.cluster.ShrinkingImageBSOC;
import edu.hendrix.modeselection.util.Util;
import edu.hendrix.modeselection.vision.AdaptedYUYVImage;

public class Movie {
	private ArrayList<AdaptedYUYVImage> frames = new ArrayList<>();
	private int current;
	
	public static int getIntPart(String s) {
		return Integer.parseInt(s.substring(0, s.indexOf('.')));
	}
	
	public Movie(File dir) throws FileNotFoundException {
		Util.assertArgument(dir.exists(), dir + " does not exist");
		Util.assertArgument(dir.isDirectory(), dir + " is not a directory");
		String[] names = dir.list();
		Arrays.sort(names, (s1, s2) -> getIntPart(s1) - getIntPart(s2));
		for (String name: names) {
			frames.add(Util.fileToObject(new File(dir, name), AdaptedYUYVImage::fromString));
		}
		
		current = 0;
	}
	
	public void next() {
		current = Util.modInc(current, frames.size());
	}
	
	public void prev() {
		current = Util.modDec(current, frames.size());
	}
	
	public AdaptedYUYVImage getFrame() {
		return frames.get(current);
	}
	
	public int getFrameIndex() {
		return current + 1;
	}
	
	public ShrinkingImageBSOC createFrom(int nodes, int shrink) {
		ShrinkingImageBSOC result = new ShrinkingImageBSOC(nodes, shrink);
		for (int i = 0; i < frames.size(); i++) {
			result.train(frames.get(i));
		}
		return result;
	}
}
