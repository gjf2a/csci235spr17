package edu.hendrix.csci235.creator;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;


public class Condition {
	private TreeMap<String, FlaggerInfo> conditions = new TreeMap<String, FlaggerInfo>();
	
	public void add(String condition, String flaggerName, String flaggerType, Boolean trueOrFalse, String inequality,
			int value) throws IOException{
		conditions.put(condition, new FlaggerInfo(flaggerName, flaggerType, trueOrFalse,
				inequality, value));
	}
	
	public TreeMap<String, FlaggerInfo> getConditions(){
		return conditions;
	}
	
	public Set<String> getKeys(){
		return conditions.keySet();
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
}
