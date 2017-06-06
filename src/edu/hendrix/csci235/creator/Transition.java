package edu.hendrix.csci235.creator;

import java.util.TreeMap;

public class Transition {
	public TreeMap<String, String> transitions = new TreeMap<String, String>();
	
	public void add(String condition, String mode){
		transitions.put(condition, mode);
	}
	
	public TreeMap<String, String> getTransitions(){
		return transitions;
	}
	

}
