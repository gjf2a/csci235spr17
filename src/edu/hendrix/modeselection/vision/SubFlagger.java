package edu.hendrix.modeselection.vision;

import edu.hendrix.modeselection.ConditionCounted;
import edu.hendrix.modeselection.SensedValues;
import edu.hendrix.modeselection.util.Logger;

public interface SubFlagger<C extends Enum<C>> extends ConditionCounted {	
	public void update(AdaptedYUYVImage img, SensedValues<C> conditions);
	public void log(Logger logger);
}
