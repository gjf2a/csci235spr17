package edu.hendrix.csci235.creator;

import java.io.IOException;
import java.util.Set;
import java.util.TreeMap;

import javafx.scene.Node;

public class FlaggerMap {
	
	// This was a patch up class that made it easier to work with the flaggers. It just maps a condition to either being true or false.
	
	private TreeMap<String, TrueFalse> flagMapping = new TreeMap<String, TrueFalse>();
	
	public void add(String flaggerName, String trueCondition, String falseCondition, String inequality, String number) throws IOException{
		flagMapping.put(flaggerName, new TrueFalse(trueCondition, falseCondition, inequality, number));
	}
	
	public TreeMap<String, TrueFalse> getFlagMapping(){
		return flagMapping;
	}

	public Set<String> getKeys(){
		return flagMapping.keySet();
	}
	

}
