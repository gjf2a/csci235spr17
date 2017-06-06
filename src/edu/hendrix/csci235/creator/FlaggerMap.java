package edu.hendrix.csci235.creator;

import java.io.IOException;
import java.util.TreeMap;

public class FlaggerMap {
private TreeMap<String, TrueFalse> flagMapping = new TreeMap<String, TrueFalse>();
	
	public void add(String flaggerName, String trueCondition, String falseCondition, String inequality, String number) throws IOException{
		flagMapping.put(flaggerName, new TrueFalse(trueCondition, falseCondition, inequality, number));
	}
	
	public TreeMap<String, TrueFalse> getFlagMapping(){
		return flagMapping;
	}

}
