package edu.hendrix.csci235.proj2.example2;

import edu.hendrix.modeselection.Transitions;

public class AvoidTransitions extends Transitions<Condition,Mode> {
	public AvoidTransitions() {
		add(Condition.PROBLEM, Mode.LEFT);
		add(Condition.CLEAR, Mode.FORWARD);
	}
}
