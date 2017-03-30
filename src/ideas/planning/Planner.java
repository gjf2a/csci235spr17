package ideas.planning;

import java.util.EnumMap;
import java.util.Map.Entry;

public class Planner<C extends Enum<C>, M extends Enum<M>> {
	private EnumMap<C,EnumMap<C,M>> stateActionGraph;
	private Class<C> condClass;

	public Planner(Class<C> condClass) {
		stateActionGraph = new EnumMap<>(condClass);
		this.condClass = condClass;
	}
	
	public Planner<C,M> add(C startState, M transition, C endState) {
		if (!stateActionGraph.containsKey(startState)) {
			stateActionGraph.put(startState, new EnumMap<>(condClass));
		}
		stateActionGraph.get(startState).put(endState, transition);
		return this;
	}
	
	public Plan<C,M> depthFirstSearch(C start, C goal) {
		if (stateActionGraph.containsKey(start)) {
			for (Entry<C, M> next: stateActionGraph.get(start).entrySet()) {
				if (next.getKey() == goal) {
					return new Plan<>(next.getValue(), next.getKey());
				} else {
					Plan<C,M> suffix = depthFirstSearch(next.getKey(), goal);
					if (suffix != null && !suffix.contains(next.getKey())) {
						return new Plan<>(next.getValue(), next.getKey(), suffix);
					}
				}
			}
		} 
		return null;
	}
}
