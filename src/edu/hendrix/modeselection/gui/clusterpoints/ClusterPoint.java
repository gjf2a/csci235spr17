package edu.hendrix.modeselection.gui.clusterpoints;

import edu.hendrix.modeselection.cluster.Clusterable;
import edu.hendrix.modeselection.util.DeepCopyable;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

public class ClusterPoint implements Clusterable<ClusterPoint>, DeepCopyable<ClusterPoint> {
	private double x, y;
	
	public ClusterPoint(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public static ClusterPoint from(String s) {
		String[] parts = s.substring(1, s.length() - 1).split(",");
		return new ClusterPoint(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]));
	}
	
	@Override
	public String toString() {
		return String.format("(%8.4f,%8.4f)", x, y);
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof ClusterPoint) {
			ClusterPoint that = (ClusterPoint)other;
			return this.x == that.x && this.y == that.y;
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return (int)(x + 10000 * y);
	}
	
	public void plot(Canvas canvas, Color color, double radius) {
		canvas.getGraphicsContext2D().setFill(color);
		canvas.getGraphicsContext2D().fillOval(x, y, radius/2, radius/2);
	}
	
	public double getX() {return x;}
	public double getY() {return y;}

	@Override
	public ClusterPoint weightedCentroidWith(ClusterPoint other, long thisCount, long otherCount) {
		return new ClusterPoint(Clusterable.weightedMean(this.x, other.x, thisCount, otherCount),
				Clusterable.weightedMean(this.y, other.y, thisCount, otherCount));
	}

	@Override
	public ClusterPoint deepCopy() {
		return new ClusterPoint(x, y);
	}
	
	public static double distance(ClusterPoint p1, ClusterPoint p2) {
		return Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2);
	}
}
