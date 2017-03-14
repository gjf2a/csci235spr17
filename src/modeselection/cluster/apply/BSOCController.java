package modeselection.cluster.apply;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.function.Consumer;

import modeselection.SensedValues;
import modeselection.cluster.LabeledBSOC;
import modeselection.cluster.train.RobotKey;
import modeselection.util.Util;
import modeselection.vision.AdaptedYUYVImage;

public class BSOCController<C extends Enum<C>, M extends Enum<M> & RobotKey> implements Consumer<SensedValues<C>> {

	private LabeledBSOC<AdaptedYUYVImage,M> bsoc;
	
	public BSOCController(Class<M> mClass, String filename) throws FileNotFoundException {
		bsoc = Util.fileToObject(new File(filename), s -> LabeledBSOC.from(mClass, s, sub -> AdaptedYUYVImage.fromString(sub)));
	}
	
	@Override
	public void accept(SensedValues<C> t) {
		M action = bsoc.bestMatchFor(t.getRawImage());
		action.act();
	}
}
