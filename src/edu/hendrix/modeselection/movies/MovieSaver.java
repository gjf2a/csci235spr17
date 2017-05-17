package edu.hendrix.modeselection.movies;

import java.io.File;
import java.io.IOException;

import edu.hendrix.modeselection.util.Util;
import edu.hendrix.modeselection.vision.AdaptedYUYVImage;

public class MovieSaver {
	private File destinationDir;
	private int numSaved;
	
	public MovieSaver(String dirName) {
		destinationDir = new File(dirName);
		Util.assertArgument(!destinationDir.exists(), "Movie " + dirName + " already exists");
		Util.assertState(destinationDir.mkdirs(), "Can't create directory " + dirName);
		numSaved = 0;
	}
	
	public void save(AdaptedYUYVImage img) throws IOException {
		String imgName = (numSaved + 1) + ".yuyv";
		File output = new File(destinationDir.getAbsolutePath() + File.separator + imgName);
		Util.objectToFile(output, img);
		numSaved += 1;
	}
	
	public int getNumSaved() {return numSaved;}
}
