package edu.hendrix.csci235.proj10.patrol;

import edu.hendrix.modeselection.vision.landmarks.LandmarkTrainer;

public class MapTrainerDemo {
	public static final String FILENAME = "plan1.txt";
	
	public static void main(String[] args) {
		new LandmarkTrainer(FILENAME, 16, 8).run();
	}
}
