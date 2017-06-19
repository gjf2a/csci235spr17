package edu.hendrix.csci235.creator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class GenerateSimpleCode {

	private String programName;
	private Transition transitions1;
	private Transition transitions2;
	private Transition transitions3;
	private Transition transitions4;
	private Transition transitions5;
	private Condition conditions;
	private FlaggerMap flagMapping;
	private Mode modes;
	public Collection<FlaggerInfo> rawFlaggers;
	
	public GenerateSimpleCode(String programName, Transition transitions1, Transition transitions2, 
			Transition transitions3,Transition transitions4,Transition transitions5, Condition conditions, FlaggerMap flagMapping, Mode modes ){
		this.programName = programName;
		this.transitions1 = transitions1;
		this.transitions2 = transitions2;
		this.transitions3 = transitions3;
		this.transitions4 = transitions4;
		this.transitions5 = transitions5;
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
			conditionsString = conditionsString + "- " + condition.toUpperCase() + "\n";
		}
		
		return conditionsString;
	}
	
	public String modes(){
		String modesString = "MODES:\n";
		
		Set<String> modeSet = modes.getModes().keySet();
		ArrayList<String> modesArray = new ArrayList<String>();
		for(String mode : modeSet){
			modesArray.add(mode.toUpperCase());
		}
		
		
		Collection<MotorInfo> modeInfo = modes.getModes().values();
		ArrayList<String> info = new ArrayList<String>();
		for(MotorInfo motorInformation : modeInfo){
			info.add(motorInformation.toString());
		}
		
		
		for(int i = 0; i < modeSet.size() ; i++){
			modesString = modesString + "- " + modesArray.get(i) + info.get(i) + "\n";
		}
		
		return modesString;
	}
	
	
	// TODO: add in second transition table
	public String transitions(){
		String transitionsTable2 = "";
		String transitionsTable3 = "";
		String transitionsTable4 = "";
		String transitionsTable5 = "";
		
		
		String transitionsTable1 = "\nTRANSITION TABLE 1:\n	Condition  ------>  Mode       \n" + 
				"-------------------------------------\n	";
		ArrayList<ConditionModePair> t1 = transitions1.getTransitions();
		for(ConditionModePair cmp : t1){
			String mode = cmp.getMode().toUpperCase();
			String condition = cmp.getCondition().toUpperCase();
			transitionsTable1 = transitionsTable1  + condition + 
					"     ------>     "+ mode + "\n	";
		}
		if(transitions2.isEmpty() == false){
			transitionsTable2 = "\nTRANSITION TABLE 2:\n	Condition  ------>  Mode       \n" + 
					"-------------------------------------\n	";
			ArrayList<ConditionModePair> t2 = transitions2.getTransitions();
			for(ConditionModePair cmp : t2){
				String mode = cmp.getMode().toUpperCase();
				String condition = cmp.getCondition().toUpperCase();
				transitionsTable2 = transitionsTable2  + condition + 
						"     ------>     "+ mode + "\n	";
			}
		}
		
		if(transitions3.isEmpty() == false){
			transitionsTable3 = "\nTRANSITION TABLE 3:\n	Condition  ------>  Mode       \n" + 
					"-------------------------------------\n	";
			ArrayList<ConditionModePair> t3 = transitions3.getTransitions();
			for(ConditionModePair cmp : t3){
				String mode = cmp.getMode().toUpperCase();
				String condition = cmp.getCondition().toUpperCase();
				transitionsTable3 = transitionsTable3  + condition + 
						"     ------>     "+ mode + "\n	";
			}
		}
		
		if(transitions4.isEmpty() == false){
			transitionsTable4 = "\nTRANSITION TABLE 4:\n	Condition  ------>  Mode       \n" + 
					"-------------------------------------\n	";
			ArrayList<ConditionModePair> t4 = transitions4.getTransitions();
			for(ConditionModePair cmp : t4){
				String mode = cmp.getMode().toUpperCase();
				String condition = cmp.getCondition().toUpperCase();
				transitionsTable4 = transitionsTable4  + condition + 
						"     ------>     "+ mode + "\n	";
			}
		}
		
		if(transitions5.isEmpty() == false){
			transitionsTable5 = "\nTRANSITION TABLE 5:\n	Condition  ------>  Mode       \n" + 
					"-------------------------------------\n	";
			ArrayList<ConditionModePair> t5 = transitions5.getTransitions();
			for(ConditionModePair cmp : t5){
				String mode = cmp.getMode().toUpperCase();
				String condition = cmp.getCondition().toUpperCase();
				transitionsTable5 = transitionsTable5  + condition + 
						"     ------>     "+ mode + "\n	";
			}
		}		
		
		return transitionsTable1 + "\n" + transitionsTable2 + "\n" + transitionsTable3 + "\n" + transitionsTable4+ "\n" + transitionsTable5; 
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
			flagString = flagString + "- " + flagArray.get(i).toUpperCase() + ": " + flagInfoArray.get(i) + "\n";
		}
		
		return flagString;
	}
		
	@Override
	public String toString(){
		return programNameView() + "\n" + flaggers() +  "\n" + conditions() + "\n" + modes() + "\n" + 
				transitions() + "\n" ;
	}
	
	
}
