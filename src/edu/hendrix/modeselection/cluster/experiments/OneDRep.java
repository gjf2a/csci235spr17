package edu.hendrix.modeselection.cluster.experiments;

import java.io.FileNotFoundException;

import edu.hendrix.modeselection.cluster.BSOCSimpleEdge;
import edu.hendrix.modeselection.cluster.BoundedSelfOrgCluster;
import edu.hendrix.modeselection.cluster.kmeans.KMeans;
import edu.hendrix.modeselection.cluster.kmeans.PlusPlusSeed;

public class OneDRep {
	public static void main(String[] args) throws NumberFormatException, FileNotFoundException {
		if (args.length < 2) {
			System.out.println("Usage: OneDRep numClusters numValues numReps [outfile.csv]");
			System.exit(1);
		}
		
		OneDExperiment expr = new OneDExperiment(Integer.parseInt(args[1]), Integer.parseInt(args[2]),
				i -> new PlusPlusSeed<>(i, SimpleValue::distance, x -> x),
				i -> new KMeans<>(i, SimpleValue::distance, x -> x),
				i -> new BoundedSelfOrgCluster<>(i, SimpleValue::distance, x -> x),
				i -> new BSOCSimpleEdge<>(i, SimpleValue::distance, x -> x));
		
		expr.runAndSave(Integer.parseInt(args[0]), args, 3);
	}
}
