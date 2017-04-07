package proj10.patrol;

import modeselection.vision.landmarks.LandmarkTrainer;

public class MapTrainerDemo {
	public static final String FILENAME = "plan1.txt";
	
	public static void main(String[] args) {
		new LandmarkTrainer(FILENAME, 16, 8).run();
	}
}
