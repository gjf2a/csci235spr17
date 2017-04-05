package ideas.planning;

import java.util.ArrayList;
import java.util.stream.Collectors;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import modeselection.SensedValues;
import modeselection.StateClassifier;
import modeselection.util.Util;

public class GoalPicker<C extends Enum<C>, M extends Enum<M>> {
	private Planner<C,M> planner;
	private ArrayList<C> allPossibleGoals;
	private StateClassifier<C> sensors;
	
	public GoalPicker<C,M> planner(Planner<C,M> planner)  {
		this.planner = planner;
		allPossibleGoals = planner.getAllPossibleGoals().stream().collect(Collectors.toCollection(ArrayList::new));
		return this;
	}
	
	public GoalPicker<C,M> sensors(StateClassifier<C> sensors) { 
		this.sensors = sensors;
		return this;
	}
	
	public void control() {
		Util.assertState(planner != null, "No planner in place");
		while (Button.ESCAPE.isUp()) {
			C goal = showGoals();
			if (goal == null) {
				return;
			} else {
				while (Button.ESCAPE.isUp()) {
					SensedValues<C> state = sensors.getCurrentState();
					Plan<C,M> p = planner.depthFirstSearch(state, goal);
					if (p == null) {
						LCD.drawString("No plan exists", 0, 2);
						LCD.drawString("Press ENTER", 0, 3);
						while (Button.ENTER.isUp());
						LCD.clear();
					} else {
						planner.getAction(p.getAction()).accept(state);
					}
				}
			}
		}
	}
	
	C showGoals() {
		int goal = 0;
		for (;;) {
			LCD.drawString(String.format("Goal:%s     ", allPossibleGoals.get(goal).name()), 0, 2);
			if (Button.ESCAPE.isDown()) {
				return null;
			} else if (Button.ENTER.isDown()) {
				return allPossibleGoals.get(goal);
			} else if (Button.LEFT.isDown() || Button.UP.isDown()) {
				goal = (goal - 1 + allPossibleGoals.size()) % allPossibleGoals.size();
			} else if (Button.RIGHT.isDown() || Button.DOWN.isDown()) {
				goal = (goal + 1) % allPossibleGoals.size();
			}
		}
	}
}
