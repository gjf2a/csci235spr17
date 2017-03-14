package modeselection.cluster.train;

import java.io.File;
import java.io.IOException;
import java.util.function.Function;

import lejos.hardware.Button;
import modeselection.cluster.LabeledBSOC;
import modeselection.util.Util;
import modeselection.vision.AdaptedYUYVImage;
import modeselection.vision.BitImage;
import modeselection.vision.config.VisionBot;

public class BSOCTrainer<E extends Enum<E> & RobotKey> extends VisionBot {
	private LabeledBSOC<AdaptedYUYVImage,E> bsoc;
	private Function<AdaptedYUYVImage,AdaptedYUYVImage> transform;
	private Class<E> enumClass;
	private String filename;
	private boolean learning;
	
	public BSOCTrainer(Class<E> enumClass, int nodes, Function<AdaptedYUYVImage,AdaptedYUYVImage> transform, E unclassified, String filename) {
		bsoc = new LabeledBSOC<>(enumClass, nodes, unclassified);
		this.transform = transform;
		this.enumClass = enumClass;
		this.filename = filename;
		learning = true;
	}
	
	public BSOCTrainer(Class<E> enumClass, int nodes, int shrink, E unclassified, String filename) {
		this(enumClass, nodes, img -> img.shrunken(shrink), unclassified, filename);
	}

	@Override
	public BitImage processImage(AdaptedYUYVImage img) {
		pickAction(transform.apply(img));
		return BitImage.intensityView(img);
	}
	
	private void pickAction(AdaptedYUYVImage img) {
		for (E candidate: enumClass.getEnumConstants()) {
			if (candidate.getKey().isDown()) {
				bsoc.train(img, candidate);		
				candidate.act();
				learning = true;
				return;
			}
		}
		
		if (Util.isClicked(Button.ENTER)) {
			learning = !learning;
		}
		
		if (learning) {
			Util.stopAllMotors();
		} else {
			bsoc.bestMatchFor(img).act();
		}
	}
	
	@Override
	public void displayFinalInfo() {
		Util.stopAllMotors();
		super.displayFinalInfo();
		try {
			Util.objectToFile(new File(filename), bsoc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
