package edu.hendrix.modeselection.cluster.experiments;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.Function;

import javax.imageio.ImageIO;

import edu.hendrix.modeselection.cluster.BSOCSimpleEdge;
import edu.hendrix.modeselection.cluster.BoundedSelfOrgCluster;
import edu.hendrix.modeselection.cluster.Clusterer;
import edu.hendrix.modeselection.cluster.kmeans.KMeans;
import edu.hendrix.modeselection.cluster.kmeans.PlusPlusSeed;

public class ColorExperiment extends Experiment<ColorCluster> {
	public static void main(String[] args) throws IOException {
		if (args.length < 2) {
			System.out.println("Usage: ColorExperiment k (imageFile|imageDirectory) [outfile]");
			System.exit(1);
		}
		
		ColorExperiment expr = new ColorExperiment(i -> new PlusPlusSeed<>(i, ColorCluster::distance, x -> x),
				i -> new KMeans<>(i, ColorCluster::distance, x -> x),
				i -> new BoundedSelfOrgCluster<>(i, ColorCluster::distance, x -> x),
				i -> new BSOCSimpleEdge<>(i, ColorCluster::distance, x -> x));
		expr.addAllImagesFrom(new File(args[1]));
		
		expr.runAndSave(Integer.parseInt(args[0]), args, 2);
	}
	
	@SafeVarargs
	public ColorExperiment(Function<Integer,Clusterer<ColorCluster,ColorCluster>>... makers) {
		super(makers);
	}
	
	public void addPointsFrom(File imgFile) throws IOException {
		System.out.println("Loading " + imgFile);
		BufferedImage img = ImageIO.read(imgFile);
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				addExample(new ColorCluster(img.getRGB(x, y)));
			}
		}
		System.out.println("loaded");
	}
	
	public void addAllImagesFrom(File fin) throws IOException {
		if (fin.isDirectory()) {
			for (File file: fin.listFiles()) {
				if (file.isDirectory()) {
					addAllImagesFrom(file);
				} else {
					for (String suffix: ImageIO.getWriterFileSuffixes()) {
						if (file.getName().endsWith(suffix)) {
							addPointsFrom(file);
						}
					}
				}
			}
		} else {
			addPointsFrom(fin);
		}
	}
}
