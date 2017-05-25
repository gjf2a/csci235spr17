package edu.hendrix.modeselection.gui;

import edu.hendrix.modeselection.util.Util;

abstract public class Navigator<T> {
	private int index;
	
	public void next() {
		index = Util.modInc(index, size());
	}
	
	public void prev() {
		index = Util.modDec(index, size());
	}
	
	public int getCurrentIndex() {
		return index;
	}
	
	public void setIndexTo(int target) {
		Util.assertArgument(target >= 0 && target < size(), "Index " + target + " is not valid");
		index = target;
	}
	
	abstract public int size();
	
	abstract public T getCurrent();
}
