package proj9.demo;

import java.io.FileNotFoundException;

import modeselection.vision.landmarks.LandmarkViewer;

public class MapViewerDemo {
	public static void main(String[] args) throws FileNotFoundException {
		new LandmarkViewer("landmarks1.txt").run();
	}
}
