package edu.hendrix.modeselection.cluster;

import java.util.ArrayList;

import edu.hendrix.modeselection.util.Duple;
import edu.hendrix.modeselection.util.Util;
import edu.hendrix.modeselection.vision.AdaptedYUYVImage;

public class ShrinkingImageBSOC implements Clusterer<AdaptedYUYVImage> {
	private BoundedSelfOrgCluster<AdaptedYUYVImage> bsoc;
	private int shrink;
	
	public ShrinkingImageBSOC(int nodes, int shrink) {
		bsoc = new BoundedSelfOrgCluster<>(nodes);
		this.shrink = shrink;
	}
	
	public ShrinkingImageBSOC(String src) {
		ArrayList<String> parts = Util.debrace(src);
		bsoc = new BoundedSelfOrgCluster<>(parts.get(0), AdaptedYUYVImage::fromString);
		shrink = Integer.parseInt(parts.get(1));		
	}
	
	public ShrinkingImageBSOC distanceFunc(DistanceFunc<AdaptedYUYVImage> alternative) {
		bsoc = bsoc.distanceFunc(alternative);
		return this;
	}
	
	@Override
	public String toString() {
		return "{" + bsoc.toString() + "}{" + shrink + "}";
	}
	
	@Override
	public int train(AdaptedYUYVImage example) {
		return bsoc.train(example.shrunken(shrink));
	}
	
	@Override
	public AdaptedYUYVImage getIdealInputFor(int node) {
		return bsoc.getIdealInputFor(node);
	}
	
	@Override
	public Duple<Integer,Long> getClosestNodeDistanceFor(AdaptedYUYVImage example) {
		return bsoc.getClosestNodeDistanceFor(example.shrunken(shrink));
	}
	
	@Override
	public int size() {
		return bsoc.size();
	}
	
	public int maxNumNodes() {
		return bsoc.maxNumNodes();
	}
	
	@Override
	public ArrayList<Integer> getClusterIds() {
		return bsoc.getClusterIds();
	}
	
	public int getNumMergesFor(int node) {
		return bsoc.getNumMergesFor(node);
	}
	
	public ArrayList<Duple<Integer,Integer>> transitionCountsFor(int node) {
		return bsoc.transitionCountsFor(node);
	}
}
