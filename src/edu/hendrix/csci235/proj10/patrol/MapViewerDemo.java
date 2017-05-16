package edu.hendrix.csci235.proj10.patrol;

import java.io.FileNotFoundException;

import edu.hendrix.modeselection.vision.landmarks.LandmarkViewer;

public class MapViewerDemo {
	public static void main(String[] args) throws FileNotFoundException {
		new LandmarkViewer(MapTrainerDemo.FILENAME).run();
	}
}
