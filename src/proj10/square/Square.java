package proj10.square;

import ideas.planning.Planner;

public class Square {
	public static void main(String[] args) {
		Planner<Condition,Mode> planner = new Planner<>(Condition.class);
		planner.add(Condition.LL_90, Mode.FORWARD, Condition.LEFT_U);
	}
}
