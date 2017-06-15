package edu.hendrix.csci235.creator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeMap;

public class Transition {
	
	// Maps a condition to a mode for a transition table.
	
	public ArrayList<ConditionModePair> transitions = new ArrayList<ConditionModePair>();
	
	public void add(String condition, String mode){
		transitions.add(new ConditionModePair(condition, mode));
	}
	
	public ArrayList<ConditionModePair> getTransitions(){
		return transitions;
	}
	
	public int size(){
		return transitions.size();
	}
	
	public ConditionModePair get(int index){
		return transitions.get(index);
	}
	
	public boolean isEmpty(){
		if(transitions.size() == 0){ 
			return true; 
		} else{ 
			return false;
		}
	}
	/*public Set<String> getKeys(){
		return transitions.keySet();
	}
	
	public Collection<String> getValues(){
		return transitions.values();
	}*/
	

}
