package edu.hendrix.csci235.proj10.patrol;

import java.io.IOException;

import edu.hendrix.modeselection.StateClassifier;
import edu.hendrix.modeselection.planning.Executor;
import edu.hendrix.modeselection.planning.GoalPicker;
import edu.hendrix.modeselection.planning.Planner;
import edu.hendrix.modeselection.vision.CameraFlagger;
import edu.hendrix.modeselection.vision.landmarks.LandmarkFlagger;
import lejos.hardware.motor.Motor;

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
