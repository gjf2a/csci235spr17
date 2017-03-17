package proj9.demo;

import ideas.vision.landmarks.LandmarkTrainer;

public class MapTrainerDemo {
	public static void main(String[] args) {
		new LandmarkTrainer("landmarks1.txt", 32, 8).run();
	}
}
