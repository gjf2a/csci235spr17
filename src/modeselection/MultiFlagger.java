package modeselection;

import java.util.EnumSet;

public class MultiFlagger<C extends Enum<C>> implements Flagger<C> {
	private EnumSet<C> allNecessaryConditions;
	private C flag;
	
	public MultiFlagger(Class<C> condClass, C multiFlag) {
		allNecessaryConditions = EnumSet.noneOf(condClass);
		flag = multiFlag;
	}
	
	public MultiFlagger<C> add(C contributor) {
		allNecessaryConditions.add(contributor);
		return this;
	}
	
	@Override
	public int numConditions() {
		return allNecessaryConditions.size();
	}

	@Override
	public void update(SensedValues<C> conditions) {
		EnumSet<C> copy = allNecessaryConditions.clone();
		copy.retainAll(conditions.flagsOnly());
		if (copy.size() == numConditions()) {
			conditions.add(flag);
		}
	}

}
