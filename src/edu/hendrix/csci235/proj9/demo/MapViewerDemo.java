package edu.hendrix.csci235.proj9.demo;

import java.io.FileNotFoundException;

import edu.hendrix.modeselection.vision.landmarks.LandmarkViewer;

public class MapViewerDemo {
	public static void main(String[] args) throws FileNotFoundException {
		new LandmarkViewer("landmarks1.txt").run();
	}
}
