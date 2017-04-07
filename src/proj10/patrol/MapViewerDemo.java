package proj10.patrol;

import java.io.FileNotFoundException;

import modeselection.vision.landmarks.LandmarkViewer;

public class MapViewerDemo {
	public static void main(String[] args) throws FileNotFoundException {
		new LandmarkViewer(MapTrainerDemo.FILENAME).run();
	}
}
