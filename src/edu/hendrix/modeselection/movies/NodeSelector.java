package edu.hendrix.modeselection.movies;

import java.util.TreeMap;

import edu.hendrix.modeselection.cluster.ShrinkingImageBSOC;
import edu.hendrix.modeselection.gui.Navigator;
import edu.hendrix.modeselection.util.Util;
import edu.hendrix.modeselection.vision.AdaptedYUYVImage;

public class NodeSelector extends Navigator<AdaptedYUYVImage> {
	private ShrinkingImageBSOC bsoc;
	private TreeMap<Integer,Integer> node2index = new TreeMap<>(); 
	
	public NodeSelector(ShrinkingImageBSOC bsoc) {
		this.bsoc = bsoc;
		for (int i = 0; i < bsoc.getClusterIds().size(); i++) {
			node2index.put(bsoc.getClusterIds().get(i), i);
		}
	}
	
	public void remove() {
		Util.assertState(bsoc.size() > 1, "Only one node left!\nTry starting over.");
		bsoc.delete(getCurrentNode());
		next();
	}
	
	public void jumpTo(AdaptedYUYVImage input) {
		setIndexTo(node2index.get(bsoc.getClosestMatchFor(input)));
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
