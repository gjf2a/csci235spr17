package edu.hendrix.modeselection.gui.clusterpoints;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import edu.hendrix.modeselection.cluster.Clusterer;
import edu.hendrix.modeselection.util.Stats;

public class ClusterStats {
	private ArrayList<ClusterPoint> points = new ArrayList<>();
	
	private ArrayList<ArrayList<ClusterPoint>> centers = new ArrayList<>();
	private ArrayList<Boolean> isScrambled = new ArrayList<>();
	private ArrayList<String> algorithmLabels = new ArrayList<>();
	
	public ClusterStats() {}
	
	public ClusterStats(Iterable<ClusterPoint> points) {
		for (ClusterPoint cp: points) {this.points.add(cp);}
	}
	
	public ArrayList<ClusterPoint> getPoints() {
		return new ArrayList<>(points);
	}
	
	public void addClusterSolution(Clusterer<ClusterPoint,ClusterPoint> solution, boolean scrambled, String name) {
		centers.add(new ArrayList<>(solution.getAllIdealInputs()));
		isScrambled.add(scrambled);
		algorithmLabels.add(name);
	}
	
	public int numClusterings() {
		return centers.size();
	}
	
	public String scrambledLabel(int i) {
		return isScrambled.get(i) ? "scrambled" : "original";
	}
	
	public void toFile(File f) throws FileNotFoundException {
		PrintStream fout = new PrintStream(f);
		printPoints(fout, points);
		for (int i = 0; i < numClusterings(); i++) {
			fout.printf("%s:%s:", algorithmLabels.get(i), scrambledLabel(i));
			printPoints(fout, centers.get(i));
		}
 		fout.close();
	}
	
	public static ClusterStats fromFile(File f) throws IOException {
		BufferedReader fin = new BufferedReader(new FileReader(f));
		String line = fin.readLine();
		ClusterStats result = new ClusterStats(readPoints(line));
		while ((line = fin.readLine()) != null) {
			String[] parts = line.split(":");
			result.algorithmLabels.add(parts[0]);
			result.isScrambled.add(parts[1].equals("scrambled"));
			result.centers.add(readPoints(parts[2]));
		}
		fin.close();
		return result;
	}
	
	public static void printPoints(PrintStream pout, ArrayList<ClusterPoint> points) {
		for (ClusterPoint cp: points) {
			pout.print(cp);
			pout.print(';');
		}
		pout.println();
	}
	
	public static ArrayList<ClusterPoint> readPoints(String line) {
		ArrayList<ClusterPoint> points = new ArrayList<>();
		for (String pointStr: line.split(";")) {
			points.add(ClusterPoint.from(pointStr));
		}
		return points;
	}
	
	public static ArrayList<Double> shortestDistancesTo(ArrayList<ClusterPoint> sources, ArrayList<ClusterPoint> destinations) {
		ArrayList<Double> result = new ArrayList<>();
		for (ClusterPoint source: sources) {
			result.add(destinations.stream()
					.map(dest -> ClusterPoint.distance(source, dest))
					.reduce(Double.POSITIVE_INFINITY, (r, e) -> Math.min(r, e)));
		}
		return result;
	}
	
	public void createSummaryReport(File destination) throws FileNotFoundException {
		PrintStream fout = new PrintStream(destination);
		for (int i = 0; i < numClusterings(); i++) {
			Stats s = new Stats(shortestDistancesTo(points, centers.get(i)));
			fout.println(algorithmLabels.get(i) + " (" + scrambledLabel(i) + "): " + s.normalSummary());
		}
		fout.close();
	}
}
