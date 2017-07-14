package edu.hendrix.csci235.creator;

import java.util.ArrayList;
import java.util.Iterator;

public class Transition implements Iterable<ConditionModePair> {
	
	// Maps a condition to a mode for a transition table.
	
	public ArrayList<ConditionModePair> transitions = new ArrayList<ConditionModePair>();
	
	public void add(String condition, String mode){
		transitions.add(new ConditionModePair(condition, mode));
	}
	
	public void regenerate(Iterable<TransitionsTableData> data) {
		transitions.clear();
		for (TransitionsTableData dat: data) {
			transitions.add(dat.getPair());
		}
	}
	
	public void removeAll(){
		transitions.clear();
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
	
	public ConditionModePair getLast() {
		return get(size() - 1);
	}
	
	public boolean isEmpty(){
		if(transitions.size() == 0){ 
			return true; 
		} else{ 
			return false;
		}
	}
	
	public boolean contains(String condition){
		for(int i = 0; i < transitions.size(); i++){
			if(transitions.get(i).getCondition().equals(condition)){
				return true;
			}
		}
		
		return false;
	}
	
	public int whichIndex(String condition){
		for(int i = 0; i < transitions.size(); i++){
			if(transitions.get(i).getCondition().equals(condition)){
				return i;
			}
		}
		
		return -1;
	}
	
	public ArrayList<ConditionModePair> clear(){
		transitions.clear();
		return transitions;
	}
	
	public void replace(int i, ConditionModePair cmp){
		for(int m = 0; m < transitions.size(); m++){
			if(m == i){
				transitions.set(i, cmp);
			}
		}
	}
	
	@Override
	public String toString(){
		String toReturn = "";
		for(int i = 0; i < transitions.size(); i++){
			toReturn = toReturn + transitions.get(i).toString();
		}
		
		return toReturn;
	}

	@Override
	public Iterator<ConditionModePair> iterator() {
		return transitions.iterator();
	}

}
