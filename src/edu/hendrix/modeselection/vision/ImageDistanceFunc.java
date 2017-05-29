package edu.hendrix.modeselection.vision;

import edu.hendrix.modeselection.cluster.DistanceFunc;

public class ImageDistanceFunc implements DistanceFunc<AdaptedYUYVImage> {
	private PixelDistanceFunc pixDist;
	
	public ImageDistanceFunc(PixelDistanceFunc pixDist) {
		this.pixDist = pixDist;
	}
	
	@Override
	public double distance(AdaptedYUYVImage img1, AdaptedYUYVImage img2) {
		double total = 0.0;
		for (int x = 0; x < img1.getWidth(); ++x) {
			for (int y = 0; y < img1.getHeight(); ++y) {
				total += pixDist.pixelDistance(img1.getY(x, y), img2.getY(x, y));
				total += pixDist.pixelDistance(img1.getU(x, y), img2.getU(x, y));
				total += pixDist.pixelDistance(img1.getV(x, y), img2.getV(x, y));
			}
		}
		return total;
	}
}
