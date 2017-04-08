package proj10.patrol;

import java.io.IOException;

import lejos.hardware.motor.Motor;
import modeselection.StateClassifier;
import modeselection.planning.Executor;
import modeselection.planning.GoalPicker;
import modeselection.planning.Planner;
import modeselection.vision.CameraFlagger;
import modeselection.vision.landmarks.LandmarkFlagger;

public class Patrol {
	public static void main(String[] args) throws IOException {
		CameraFlagger<Condition> camera = new CameraFlagger<>();
		LandmarkFlagger<Condition> landmarker = new LandmarkFlagger<>(MapTrainerDemo.FILENAME);
		landmarker.add(Condition.CARPET, 0, 1, 3)
				  .add(Condition.MINT, 6)
				  .add(Condition.TAGALONG, 14)
				  .add(Condition.VEER_RIGHT, 4, 8, 9, 10, 12)
				  .add(Condition.VEER_LEFT, 2, 5, 7, 11, 13, 15);
		camera.addSub(landmarker);
		StateClassifier<Condition> sensors = new StateClassifier<>(Condition.class);
		sensors.add(camera);

		Planner<Condition,Mode> planner = new Planner<>(Condition.class);
		planner.add(Condition.CARPET, Mode.FORWARD, Condition.MINT, Condition.TAGALONG)
			   .add(Condition.MINT, Mode.RIGHT, Condition.CARPET)
			   .add(Condition.TAGALONG, Mode.RIGHT, Condition.CARPET)
			   .add(Condition.VEER_LEFT, Mode.RIGHT, Condition.CARPET, Condition.MINT, Condition.TAGALONG)
			   .add(Condition.VEER_RIGHT, Mode.LEFT, Condition.CARPET, Condition.MINT, Condition.TAGALONG);
		
		Executor<Condition,Mode> executor = new Executor<>(Mode.class, Mode.FORWARD);
		executor.mode(Mode.FORWARD, () -> {
					Motor.A.forward();
					Motor.D.forward();
				})
				.mode(Mode.LEFT, () -> {
					Motor.A.backward();
					Motor.D.forward();
				})
				.mode(Mode.RIGHT, () -> {
					Motor.A.forward();
					Motor.D.backward();
				});
		
		GoalPicker<Condition,Mode> controller = new GoalPicker<>(planner, executor, sensors); 
		controller.control();
	}
}
