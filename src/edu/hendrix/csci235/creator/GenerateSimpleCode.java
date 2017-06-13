package edu.hendrix.csci235.creator;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class GenerateSimpleCode {

	private String programName;
	private Transition transitions1;
	private Transition transitions2;
	private Condition conditions;
	private FlaggerMap flagMapping;
	private Mode modes;
	public Collection<FlaggerInfo> rawFlaggers;
	
	public GenerateSimpleCode(String programName, Transition transitions1, Transition transitions2, 
			Condition conditions, FlaggerMap flagMapping, Mode modes ){
		this.programName = programName;
		this.transitions1 = transitions1;
		this.transitions2 = transitions2;
		this.conditions = conditions;
		this.flagMapping = flagMapping;
		this.modes = modes;
	}
	
	public String programNameView(){
		String programNameView = "";
		
		programNameView = "-------------------------------------------------------------------------------------------------------------------------------------------------------------" +
				"\n" + programName + "\n" + 
				"-------------------------------------------------------------------------------------------------------------------------------------------------------------";
		
		return programNameView;
	}
	
	public String conditions(){
		String conditionsString = "CONDITIONS:\n";
		Set<String> conditionSet = conditions.getConditions().keySet();
		
		for(String condition : conditionSet){
			conditionsString = conditionsString + "- " + condition + "\n";
		}
		
		return conditionsString;
	}
	
	public String modes(){
		String modesString = "MODES:\n";
		
		Set<String> modeSet = modes.getModes().keySet();
		ArrayList<String> modesArray = new ArrayList<String>();
		for(String mode : modeSet){
			modesArray.add(mode);
		}
		
		
		Collection<MotorInfo> modeInfo = modes.getModes().values();
		ArrayList<String> info = new ArrayList<String>();
		for(MotorInfo motorInformation : modeInfo){
			info.add(motorInformation.toString());
		}
		
		
		for(int i = 0; i < modeSet.size() ; i++){
			modesString = modesString + "- " + modesArray.get(i) + " - " + info.get(i) + "\n";
		}
		
		return modesString;
	}
	
	
	// TODO: add in second transition table
	public String transitions(){
		String transitionsTable1 = "\nTRANSITION TABLE:\n	Condition  ------>  Mode       \n" + 
				"-------------------------------------\n	";
		
		Set<String> conditionTransition1 = transitions1.getKeys();
		ArrayList<String> conditionsArray = new ArrayList<String>();
		for(String condition : conditionTransition1){
			conditionsArray.add(condition);
		}
		
		Collection<String> modeTransition1 = transitions1.getValues();
		ArrayList<String> modesArray = new ArrayList<String>();
		for(String mode : modeTransition1){
			modesArray.add(mode);
		}
		
		for(int i = 0; i < conditionTransition1.size() ; i++){
			
			
			
			transitionsTable1 = transitionsTable1  + conditionsArray.get(i) + 
					"     ------>     "+ modesArray.get(i) + "\n	";
		}
		
		
		return transitionsTable1;
	}
	
	public String flaggers(){
		String flagString = "FLAGGERS:\n";
		
		Set<String> flags = flagMapping.getKeys();
		ArrayList<String> flagArray = new ArrayList<String>();
		for(String flag : flags){
			flagArray.add(flag);
		}
		
		Collection<TrueFalse> flagInfo = flagMapping.getValues();
		ArrayList<String> flagInfoArray = new ArrayList<String>();
		for(TrueFalse info : flagInfo){
			flagInfoArray.add(info.toString());
		}
		
		for(int i = 0; i < flagArray.size(); i++){
			flagString = flagString + "- " + flagArray.get(i) + ": " + flagInfoArray.get(i) + "\n";
		}
		
		return flagString;
	}
		
	@Override
	public String toString(){
		return programNameView() + "\n" + conditions() + "\n" + modes() + "\n" + 
				transitions() + "\n" + flaggers();
	}
	
	
}
