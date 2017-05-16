package edu.hendrix.csci235.proj2.example2;

import java.io.IOException;

import edu.hendrix.modeselection.ModeSelector;

public class Avoid2 {
	public static void main(String[] args) throws IOException {
		SonarClose sonar = new SonarClose();
		ModeSelector<Condition,Mode> controller = 
				new ModeSelector<>(Condition.class, Mode.class, Mode.FORWARD)
				.sensor(sonar)
				.mode(Mode.FORWARD, new AvoidTransitions(), new Forward())
				.mode(Mode.LEFT, new AvoidTransitions(), new SpinLeft());
		
		controller.control();
	}
}
