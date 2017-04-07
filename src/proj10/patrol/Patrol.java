package proj10.patrol;

import java.io.IOException;

import ideas.planning.Planner;
import modeselection.vision.CameraFlagger;
import modeselection.vision.landmarks.LandmarkFlagger;

public class Patrol {
	public static void main(String[] args) throws IOException {
		CameraFlagger<Condition> camera = new CameraFlagger<>();
		LandmarkFlagger<Condition> landmarker = new LandmarkFlagger<>(MapTrainerDemo.FILENAME);
		landmarker.add(Condition.CARPET, 0, 2, 4, 9, 11, 12, 14)
				  .add(Condition.MINT, 1)
				  .add(Condition.TAGALONG, 6)
				  .add(Condition.VEER_RIGHT, 5, 7)
				  .add(Condition.VEER_LEFT, 8, 13)
				  .add(Condition.OBSTACLE, 3, 10, 15);
		camera.addSub(landmarker);

		Planner<Condition,Mode> planner = new Planner<>(Condition.class);
		planner.add(Condition.CARPET, Mode.FORWARD, Condition.MINT)
			   .add(Condition.CARPET, Mode.FORWARD, Condition.TAGALONG)
			   .add(Condition.MINT, Mode.LEFT, Condition.OBSTACLE);
	}
}
