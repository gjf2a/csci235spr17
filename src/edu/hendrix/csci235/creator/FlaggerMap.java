package edu.hendrix.csci235.creator;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.TreeMap;

public class FlaggerMap {
	
	// This was a patch up class that made it easier to work with the flaggers. It just maps a condition to either being true or false.
	
	private TreeMap<String, TrueFalse> flagMapping = new TreeMap<String, TrueFalse>();
	
	public void add(String flaggerName, String flaggerType, String trueCondition, String falseCondition, String inequality, String number) throws IOException{
		flagMapping.put(flaggerName, new TrueFalse(flaggerType, trueCondition, falseCondition, inequality, number));
	}
	
	public TreeMap<String, TrueFalse> getFlagMapping(){
		return flagMapping;
	}

	public Set<String> getKeys(){
		return flagMapping.keySet();
	}
	
	public Collection<TrueFalse> getValues(){
		return flagMapping.values();
	}

	public void remove(String key) {
		flagMapping.remove(key);
		
	}
	

}
