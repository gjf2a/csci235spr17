package edu.hendrix.modeselection.movies;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;

import edu.hendrix.modeselection.cluster.ShrinkingImageBSOC;
import edu.hendrix.modeselection.gui.ArrayNavigator;
import edu.hendrix.modeselection.util.Util;
import edu.hendrix.modeselection.vision.AdaptedYUYVImage;

public class Movie extends ArrayNavigator<AdaptedYUYVImage> {
	public static int getIntPart(String s) {
		return Integer.parseInt(s.substring(0, s.indexOf('.')));
	}
	
	public Movie(File dir) throws FileNotFoundException {
		super();
		Util.assertArgument(dir.exists(), dir + " does not exist");
		Util.assertArgument(dir.isDirectory(), dir + " is not a directory");
		String[] names = dir.list();
		Arrays.sort(names, (s1, s2) -> getIntPart(s1) - getIntPart(s2));
		for (String name: names) {
			add(Util.fileToObject(new File(dir, name), AdaptedYUYVImage::fromString));
		}
	}
	
	public int getFrameNumber() {
		return getCurrentIndex() + 1;
	}
	
	public ShrinkingImageBSOC createFrom(int nodes, int shrink) {
		ShrinkingImageBSOC result = new ShrinkingImageBSOC(nodes, shrink);
		doAll(frame -> result.train(frame));
		return result;
	}
}
