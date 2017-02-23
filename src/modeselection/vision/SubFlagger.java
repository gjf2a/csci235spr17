package modeselection.vision;

import lejos.hardware.video.YUYVImage;
import modeselection.ConditionCounted;
import modeselection.SensedValues;
import modeselection.util.Logger;

public interface SubFlagger<C extends Enum<C>> extends ConditionCounted {	
	public void update(YUYVImage img, SensedValues<C> conditions);
	public void log(Logger logger);
}
