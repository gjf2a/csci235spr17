package edu.hendrix.csci235.creator;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeMap;

public class GenerateCode {
	private String programName;
	private Transition transitions;
	private Condition conditions;
	private Mode modes;
	public Collection<FlaggerInfo> rawFlaggers;
	
	public GenerateCode(String programName, Transition transitions, 
			Condition conditions, Mode modes ){
		this.programName = programName;
		this.transitions = transitions;
		this.conditions = conditions;
		this.modes = modes;
	}
	
	public String generate(){
		String code = "public class " + programName + "{\n	public static void main(String[] args) throws IOException {";
		
		String addFlaggers = "";
		rawFlaggers = conditions.getValues();
		int i = 0;
		for(FlaggerInfo f : rawFlaggers){
			if(i % 2 == 0){
				addFlaggers = addFlaggers + "\n		" + f.toString();
			}	
			i += 1;
		}
		code = code + addFlaggers; 

		return code;
	}

}
