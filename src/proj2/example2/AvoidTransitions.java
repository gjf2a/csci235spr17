package proj2.example2;

import modeselection.Transitions;

public class AvoidTransitions extends Transitions<Condition,Mode> {
	public AvoidTransitions() {
		add(Condition.PROBLEM, Mode.LEFT);
		add(Condition.CLEAR, Mode.FORWARD);
	}
}
