package edu.hendrix.csci235.creator;

import java.util.Collection;
import java.util.Set;
import java.util.TreeMap;

public class Transition {
	
	// Maps a condition to a mode for a transition table.
	
	public TreeMap<String, String> transitions = new TreeMap<String, String>();
	
	public void add(String condition, String mode){
		transitions.put(condition, mode);
	}
	
	public TreeMap<String, String> getTransitions(){
		return transitions;
	}
	
	public Set<String> getKeys(){
		return transitions.keySet();
	}
	
	public Collection<String> getValues(){
		return transitions.values();
	}
	

}
