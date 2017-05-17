package edu.hendrix.modeselection.vision.landmarks;

import java.io.File;
import java.io.IOException;

import edu.hendrix.modeselection.cluster.DistanceFunc;
import edu.hendrix.modeselection.cluster.ShrinkingImageBSOC;
import edu.hendrix.modeselection.util.ButtonDriver;
import edu.hendrix.modeselection.util.CycleTimer;
import edu.hendrix.modeselection.util.Util;
import edu.hendrix.modeselection.vision.AdaptedYUYVImage;
import edu.hendrix.modeselection.vision.BitImage;
import edu.hendrix.modeselection.vision.config.VisionBot;
import lejos.hardware.motor.Motor;

public class LandmarkTrainer extends VisionBot {	
	private ShrinkingImageBSOC bsoc;
	private ButtonDriver driver = new ButtonDriver(Motor.A, Motor.D);
	private CycleTimer trainRate = new CycleTimer();
	private String filename;
	
	public LandmarkTrainer(String filename, int nodes, int shrink) {
		bsoc = new ShrinkingImageBSOC(nodes, shrink);
		this.filename = filename;
	}
	
	public LandmarkTrainer distanceFunc(DistanceFunc<AdaptedYUYVImage> dist) {
		bsoc = bsoc.distanceFunc(dist);
		return this;
	}
	
	public LandmarkTrainer(String src) {
		bsoc = new ShrinkingImageBSOC(src);
		this.filename = src;
	}
	
	@Override
	public void displayFinalInfo() {
		trainRate.display(2, "rate");
		try {
			Util.objectToFile(new File(filename), bsoc);
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalStateException(e.getMessage());
		}
	}

	@Override
	public BitImage processImage(AdaptedYUYVImage img) {
		driver.drive(() -> {
			trainRate.start();
			bsoc.train(img);
			trainRate.bumpCycle();
		});
		return BitImage.intensityView(img);
	}
}
