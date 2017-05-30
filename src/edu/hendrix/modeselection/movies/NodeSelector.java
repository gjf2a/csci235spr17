package edu.hendrix.modeselection.movies;

import java.util.TreeMap;

import edu.hendrix.modeselection.cluster.ShrinkingImageBSOC;
import edu.hendrix.modeselection.gui.Navigator;
import edu.hendrix.modeselection.util.Duple;
import edu.hendrix.modeselection.util.Util;
import edu.hendrix.modeselection.vision.AdaptedYUYVImage;

public class NodeSelector extends Navigator<AdaptedYUYVImage> {
	private ShrinkingImageBSOC bsoc;
	private TreeMap<Integer,Integer> node2index = new TreeMap<>(); 
	private double currentDistance;
	
	public NodeSelector(ShrinkingImageBSOC bsoc) {
		this.bsoc = bsoc;
		for (int i = 0; i < bsoc.getClusterIds().size(); i++) {
			node2index.put(bsoc.getClusterIds().get(i), i);
		}
	}
	
	public void remove() {
		Util.assertState(bsoc.size() > 1, "Only one node left!\nTry starting over.");
		bsoc.delete(getCurrentNode());
	}
	
	public void jumpTo(AdaptedYUYVImage input) {
		Duple<Integer,Double> match = bsoc.getClosestNodeDistanceFor(input);
		setIndexTo(node2index.get(match.getFirst()));
		currentDistance = match.getSecond();
	}
	
	public double getCurrentDistance() {
		return currentDistance;
	}
	
	public AdaptedYUYVImage getCurrent() {
		return bsoc.getIdealInputFor(getCurrentIndex());
	}
	
	public int getCurrentNode() {
		return bsoc.getClusterIds().get(getCurrentIndex());
	}
	
	public int size() {
		return bsoc.size();
	}
}
