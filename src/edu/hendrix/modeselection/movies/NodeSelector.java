package edu.hendrix.modeselection.movies;

import java.util.TreeMap;

import edu.hendrix.modeselection.cluster.ShrinkingImageBSOC;
import edu.hendrix.modeselection.util.Util;
import edu.hendrix.modeselection.vision.AdaptedYUYVImage;

public class NodeSelector {
	private ShrinkingImageBSOC bsoc;
	private int index;
	private TreeMap<Integer,Integer> node2index = new TreeMap<>(); 
	
	public NodeSelector(ShrinkingImageBSOC bsoc) {
		this.bsoc = bsoc;
		for (int i = 0; i < bsoc.getClusterIds().size(); i++) {
			node2index.put(bsoc.getClusterIds().get(i), i);
		}
		this.index = 0;
	}
	
	public void next() {
		index = Util.modInc(index, bsoc.size());
	}
	
	public void prev() {
		index = Util.modDec(index, bsoc.size());
	}
	
	public void jumpTo(AdaptedYUYVImage input) {
		index = node2index.get(bsoc.getClosestMatchFor(input));
	}
	
	public int nodeNum() {
		return bsoc.getClusterIds().get(index);
	}
	
	public AdaptedYUYVImage referenceInput() {
		return bsoc.getIdealInputFor(nodeNum());
	}
}
