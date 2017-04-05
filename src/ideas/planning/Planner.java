package ideas.planning;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map.Entry;
import java.util.function.Consumer;

import modeselection.SensedValues;

import java.util.Set;

public class Planner<C extends Enum<C>, M extends Enum<M>> {
	private EnumMap<C,EnumMap<C,M>> stateActionGraph;
	private Class<C> condClass;
	private EnumSet<C> allPossibleGoals, allPossibleStarts;
	private EnumMap<M,Consumer<SensedValues<C>>> subs;

	public Planner(Class<C> condClass, Class<M> modeClass) {
		stateActionGraph = new EnumMap<>(condClass);
		this.condClass = condClass;
		allPossibleGoals = EnumSet.noneOf(condClass);
		allPossibleStarts = EnumSet.noneOf(condClass);
		subs = new EnumMap<>(modeClass);
	}
	
	public Planner<C,M> add(C startState, M transition, C endState) {
		if (!stateActionGraph.containsKey(startState)) {
			stateActionGraph.put(startState, new EnumMap<>(condClass));
		}
		stateActionGraph.get(startState).put(endState, transition);
		allPossibleStarts.add(startState);
		allPossibleGoals.add(endState);
		return this;
	}
	
	public Planner<C,M> mode(M mode, Consumer<SensedValues<C>> action) {
		subs.put(mode, action);
		return this;
	}
	
	public Set<C> getAllPossibleGoals() {
		return Collections.unmodifiableSet(allPossibleGoals);
	}
	
	public Set<C> getAllPossibleStartStates() {
		return Collections.unmodifiableSet(allPossibleStarts);
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
	
	public Plan<C,M> depthFirstSearch(SensedValues<C> state, C goal) {
		for (C flag: state.flagsOnly()) {
			if (allPossibleStarts.contains(flag)) {
				return depthFirstSearch(flag, goal);
			}
		}
		return null;
	}
	
	public Consumer<SensedValues<C>> getAction(M mode) {
		return subs.get(mode);
	}
}
