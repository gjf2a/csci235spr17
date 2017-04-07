package ideas.planning;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import modeselection.SensedValues;
import modeselection.StateClassifier;

public class GoalPicker<C extends Enum<C>, M extends Enum<M>> {
	private Planner<C,M> planner;
	private Executor<C,M> executor;
	private ArrayList<C> allPossibleGoals;
	private StateClassifier<C> sensors;
	
	public GoalPicker(Planner<C,M> planner, Executor<C,M> executor, StateClassifier<C> sensors)  {
		this.planner = planner;
		this.executor = executor;
		this.sensors = sensors;
		allPossibleGoals = planner.getAllPossibleGoals().stream().collect(Collectors.toCollection(ArrayList::new));
	}
	
	public void control() {
		while (Button.ESCAPE.isUp()) {
			Optional<C> goal = showGoals();
			if (goal.isPresent()) {
				goalSelectedLoop(goal.get());
			} else {
				return;
			}
		}
	}
	
	void enterPause(String msg) {
		LCD.drawString(msg, 0, 2);
		LCD.drawString("Press ENTER", 0, 3);
		while (Button.ENTER.isUp());
		LCD.clear();				
	}
	
	void goalSelectedLoop(C goal) {
		while (Button.ESCAPE.isUp()) {
			SensedValues<C> state = sensors.getCurrentState();
			Optional<Plan<C,M>> p = planner.depthFirstSearch(state, goal);
			if (p.isPresent()) {
				executor.executeFirstAction(p, state);
			} else {
				enterPause("No plan exists");
				return;
			}
		}
		enterPause(String.format("Goal %s abandoned", goal.name()));
	}
 	
	Optional<C> showGoals() {
		int goal = 0;
		for (;;) {
			LCD.drawString(String.format("Goal:%s     ", allPossibleGoals.get(goal).name()), 0, 2);
			if (Button.ESCAPE.isDown()) {
				return Optional.empty();
			} else if (Button.ENTER.isDown()) {
				return Optional.of(allPossibleGoals.get(goal));
			} else if (Button.LEFT.isDown() || Button.UP.isDown()) {
				goal = (goal - 1 + allPossibleGoals.size()) % allPossibleGoals.size();
			} else if (Button.RIGHT.isDown() || Button.DOWN.isDown()) {
				goal = (goal + 1) % allPossibleGoals.size();
			}
		}
	}
}
