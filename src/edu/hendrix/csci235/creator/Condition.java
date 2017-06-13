package edu.hendrix.csci235.creator;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;


public class Condition {
	// This maps a condition to its flagger information
	
	private TreeMap<String, FlaggerInfo> conditions = new TreeMap<String, FlaggerInfo>();
	
	public void add(String condition, String flaggerName, String flaggerType, String sensorPort, String bumpOrSensor, String motor, Boolean trueOrFalse, int uLow, int uHigh, int vLow, int vHigh, String inequality,
			int value) throws IOException{
		conditions.put(condition, new FlaggerInfo(flaggerName, flaggerType, sensorPort, bumpOrSensor, motor, trueOrFalse,
				inequality, uLow, uHigh, vLow, vHigh, value));
	}
	
	public TreeMap<String, FlaggerInfo> getConditions(){
		return conditions;
	}
	
	public Set<String> getKeys(){
		return conditions.keySet();
	}
	
	public Collection<FlaggerInfo> getValues(){
		return conditions.values();
	}
	
	public int size(){
		return conditions.size();
	}
	
	public void printKeys() {
		for (String key: conditions.keySet()) {
			System.out.println(key.toString());
			printValues(key);
		}
	}
	
	public void printValues(String key) {
		FlaggerInfo info = conditions.get(key);
		System.out.println(info.toString());
	}
	
	public ArrayList<String> conditionsArrayList(){
		Set<String> keySet = conditions.keySet();
		Object[] keys = keySet.toArray();
		
		Collection<FlaggerInfo> valueSet = conditions.values();
		Object[] vals = valueSet.toArray();
		ArrayList<String> toReturn = new ArrayList<String>();
		for(int i = 0; i < keys.length; i++){
			String toAdd = keys[i] + "-> " + vals[i];
			toReturn.add(toAdd);
		}
		
		return toReturn;
	}
}
