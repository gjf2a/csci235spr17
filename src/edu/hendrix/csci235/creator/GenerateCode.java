package edu.hendrix.csci235.creator;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class GenerateCode {
	private String programName;
	private Transition transitions;
	private Condition conditions;
	private FlaggerMap flagMapping;
	private Mode modes;
	public Collection<FlaggerInfo> rawFlaggers;
	
	public GenerateCode(String programName, Transition transitions, 
			Condition conditions, FlaggerMap flagMapping, Mode modes ){
		this.programName = programName;
		this.transitions = transitions;
		this.conditions = conditions;
		this.flagMapping = flagMapping;
		this.modes = modes;
	}
	
	public String generateFlaggers(){
		String code = "public class " + programName + "{\n	public static void main(String[] args) throws IOException {\n";
		
		// adding flaggers /////////////////////////////////////////////////
		String addingFlaggers= "";
		rawFlaggers = conditions.getValues();
		for(FlaggerInfo f : rawFlaggers){
			addingFlaggers = addingFlaggers + f.addFlaggers();
		}
		code = code + addingFlaggers; 
		
		// adding flagger conditions ///////////////////////////////////////
		String flaggerConditions = "";
		TreeMap<String, TrueFalse> flaggers = flagMapping.getFlagMapping();
		for(Map.Entry<String, TrueFalse> entry : flaggers.entrySet()){
			String key = entry.getKey().toString();
			TrueFalse value = entry.getValue();
			String trueCondition = value.getTrueCondition();
			String falseCondition = value.getFalseCondition();
			String inequality = value.getInequality();
			String number = value.getNumber();
			
			flaggerConditions = flaggerConditions + "\n		" + key + ".add2(Condition." +
					trueCondition.toUpperCase() + ", Condition." + falseCondition.toUpperCase() + 
					", v -> v " + inequality + " " + number + ");";
					
		}
		code = code + flaggerConditions;
		
		return code;
	}
	
	public String generate(){
		String code = "";
		code = code + generateFlaggers();
		
		return code;
	}

}
