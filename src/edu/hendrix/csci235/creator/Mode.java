package edu.hendrix.csci235.creator;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public class Mode {
	private TreeMap<String, MotorInfo> modes = new TreeMap<String, MotorInfo>();
	
	public void add(String mode, String motor1, String forwardOrBackward1, 
			String motor2, String forwardOrBackward2) throws IOException{
		modes.put(mode, new MotorInfo(motor1, forwardOrBackward1, motor2, forwardOrBackward2));
	}
	
	public TreeMap<String, MotorInfo> getModes(){
		return modes;
	}
	
	public Set<String> getKeys(){
		return modes.keySet();
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

}
