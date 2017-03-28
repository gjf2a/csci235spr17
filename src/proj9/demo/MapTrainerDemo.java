package proj9.demo;

import modeselection.vision.landmarks.LandmarkTrainer;

public class MapTrainerDemo {
	public static void main(String[] args) {
		new LandmarkTrainer("landmarks1.txt", 4, 8).run();
	}
}
