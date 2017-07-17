package edu.hendrix.csci235.creator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public class GenerateSimpleCode {

	private String programName;
	private List<List<ConditionModePair>> transitions;
	private TreeMap<String, FlaggerInfo> conditions;
	private TreeMap<String, TrueFalse> flagMapping;
	private TreeMap<String, MotorInfo> modes;
	public Collection<FlaggerInfo> rawFlaggers;
	
	public GenerateSimpleCode(String programName, List<List<ConditionModePair>> transitions, TreeMap<String, FlaggerInfo> conditions, TreeMap<String, TrueFalse> flaggerMap, TreeMap<String, MotorInfo> modes ){
		this.programName = programName;
		this.transitions = transitions;
		this.conditions = conditions;
		this.flagMapping = flaggerMap;
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
		Set<String> conditionSet = conditions.keySet();
		
		for(String condition : conditionSet){
			conditionsString = conditionsString + "- " + condition.toUpperCase() + "\n";
		}
		
		return conditionsString;
	}
	
	public String modes(){
		String modesString = "MODES:\n";
		
		Set<String> modeSet = modes.keySet();
		ArrayList<String> modesArray = new ArrayList<String>();
		for(String mode : modeSet){
			modesArray.add(mode.toUpperCase());
		}
		
		
		Collection<MotorInfo> modeInfo = modes.values();
		ArrayList<String> info = new ArrayList<String>();
		for(MotorInfo motorInformation : modeInfo){
			info.add(motorInformation.toString());
		}
		
		
		for(int i = 0; i < modeSet.size() ; i++){
			modesString = modesString + "- " + modesArray.get(i) + info.get(i) + "\n";
		}
		
		return modesString;
	}
	
	public String transitions(){
		String toReturn = "";
		
		for(int i = 0; i < transitions.size(); i++){
			int tableNum = i+1;
			toReturn = toReturn + "TRANSITION TABLE " + tableNum + ":\n	Condition  ------>  Mode       \n" + 
					"-------------------------------------\n	";
			for(ConditionModePair cmp : transitions.get(i)){
				String mode = cmp.getMode().toUpperCase();
				String condition = cmp.getCondition().toUpperCase();
				toReturn = toReturn  + condition + 
						"     ------>     "+ mode + "\n	";
			}
			
			toReturn = toReturn + "\n\n";
		}
		
		return toReturn;
	}
	
	public String flaggers(){
		String flagString = "FLAGGERS:\n";
		
		Set<String> flags = flagMapping.keySet();
		ArrayList<String> flagArray = new ArrayList<String>();
		for(String flag : flags){
			flagArray.add(flag);
		}
		
		Collection<TrueFalse> flagInfo = flagMapping.values();
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
