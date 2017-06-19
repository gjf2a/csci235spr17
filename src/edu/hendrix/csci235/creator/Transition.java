package edu.hendrix.csci235.creator;

import java.util.ArrayList;

public class Transition {
	
	// Maps a condition to a mode for a transition table.
	
	public ArrayList<ConditionModePair> transitions = new ArrayList<ConditionModePair>();
	
	public void add(String condition, String mode){
		transitions.add(new ConditionModePair(condition, mode));
	}
	
	public void regenerate(Iterable<TempTableData> data) {
		transitions.clear();
		for (TempTableData dat: data) {
			transitions.add(dat.getPair());
		}
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
	
	/*public Set<String> getKeys(){
		return transitions.keySet();
	}
	
	public Collection<String> getValues(){
		return transitions.values();
	}*/
	
	@Override
	public String toString(){
		String toReturn = "";
		for(int i = 0; i < transitions.size(); i++){
			toReturn = toReturn + transitions.get(i).toString();
		}
		
		return toReturn;
	}

}
