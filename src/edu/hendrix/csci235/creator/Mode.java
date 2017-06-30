package edu.hendrix.csci235.creator;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.TreeMap;

public class Mode {
	
	// Maps a mode to all of its relevant information.
	
	private TreeMap<String, MotorInfo> modes = new TreeMap<String, MotorInfo>();
	
	public void add(String mode, String motor1, String forwardStopBackward1, 
			String motor2, String forwardStopBackward2, String startingOrNot, int transitionTableNumber) throws IOException{
		modes.put(mode, new MotorInfo(motor1, forwardStopBackward1, motor2, forwardStopBackward2, startingOrNot, transitionTableNumber));
	}
	
	public TreeMap<String, MotorInfo> getModes(){
		return modes;
	}
	
	public void removeAll(){
		modes.clear();
	}
	
	public Set<String> getKeys(){
		return modes.keySet();
	}
	
	public Collection<MotorInfo> getValues(){
		return modes.values();
	}
	
	public void printKeys() {
		for (String key: modes.keySet()) {
			System.out.println(key);
			printValues(key);
		}
	}
	
	public void printValues(String key) {
		MotorInfo info = modes.get(key);
		System.out.println(info.toString());
	}

	public void remove(String key) {
		modes.remove(key);
		
	}

}
