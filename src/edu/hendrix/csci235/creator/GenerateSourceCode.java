package edu.hendrix.csci235.creator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class GenerateSourceCode {
	
	// This class builds all of the programs using strings. 
	
	private String programName;
	private List<List<ConditionModePair>> transitions;
	private TreeMap<String, FlaggerInfo> conditions;
	private TreeMap<String, TrueFalse> flagMapping;
	private TreeMap<String, MotorInfo> modes;
	public Collection<FlaggerInfo> rawFlaggers;
	
	public GenerateSourceCode(String programName, List<List<ConditionModePair>> transitions, TreeMap<String, FlaggerInfo> conditions, TreeMap<String, TrueFalse> flaggerMap, TreeMap<String, MotorInfo> modes ){
		this.programName = programName.replaceAll(" ", "_");
		this.transitions = transitions;
		this.conditions = conditions;
		this.flagMapping = flaggerMap;
		this.modes = modes;
	}
	
	public String addImports(){
		return "import java.io.IOException;\nimport lejos.hardware.Button;"
				+ "\nimport lejos.hardware.motor.Motor;\nimport lejos.hardware.port.SensorPort;"
				+ "\nimport lejos.hardware.sensor.EV3UltrasonicSensor;\nimport lejos.hardware.sensor.EV3TouchSensor;"
				+ "\nimport edu.hendrix.modeselection.ModeSelector;\nimport edu.hendrix.modeselection.SensorFlagger;"
				+ "\nimport edu.hendrix.modeselection.Transitions;"
				+ "\nimport edu.hendrix.modeselection.vision.CameraFlagger;"
				+ "\nimport edu.hendrix.modeselection.vision.color.ColorCountFlagger;\n";
	}
	
	public String addFlaggers(){
		String addingFlaggers= "";
		rawFlaggers = conditions.values();
		for(FlaggerInfo f : rawFlaggers){
			addingFlaggers = addingFlaggers + f.addFlaggers();
		}
		return addingFlaggers;
	}
	
	public String addFlaggerConditions(){
		String flaggerConditions = "";
		TreeMap<String, TrueFalse> flaggers = flagMapping;
		for(Map.Entry<String, TrueFalse> entry : flaggers.entrySet()){
			String key = entry.getKey().toString();
			TrueFalse value = entry.getValue();
			String trueCondition = value.getTrueCondition();
			String falseCondition = value.getFalseCondition();
			String inequality = value.getInequality();
			String number = value.getNumber();
			String flaggerType = value.getFlaggerType();
			
			if(!flaggerType.equals("ColorCount")){
				flaggerConditions = flaggerConditions + "\n		" + key + ".add2(Condition." +
						trueCondition.toUpperCase() + ", Condition." + falseCondition.toUpperCase() + 
						", v -> v " + inequality + " " + number + ");\n";
			} else{
				flaggerConditions = flaggerConditions + "\n		" + key + ".add2(Condition." +
						trueCondition.toUpperCase() + ", Condition." + falseCondition.toUpperCase() + 
						", v -> v.getTotal() " + inequality + " " + number + ");\n";
			}
			
					
		}
		return flaggerConditions;
	}
	
	public String generateFlaggers(){
		String code = "public class " + programName + "{\n	public static void main(String[] args) throws IOException {\n";
		
		String addingFlaggers= addFlaggers();
		code = code + addingFlaggers; 
		
		String flaggerConditions = addFlaggerConditions();
		code = code + flaggerConditions;
		
		return code;
	}
	
	public String generateTransitionTables(){
		String toReturn = "";
		for(int i = 0; i < transitions.size(); i++){
			for(ConditionModePair cmp : transitions.get(i)){
				String condition = cmp.getCondition();
				String mode = cmp.getMode();
				int tableNum = i+1;
				
				toReturn = toReturn + "\n		transitions" + tableNum + ".add(Condition." + condition.toUpperCase() + ", Mode." +
						mode.toUpperCase() + ");";
			}
			
			toReturn = toReturn + "\n\n";
		}
		
		return toReturn; 
	}
	
	public String generateModeSelector(){
		TreeMap<String, MotorInfo> rawModes = modes;
		String firstPart = "";
		String thirdPart = "";
		for(Map.Entry<String, MotorInfo> entry : rawModes.entrySet()){
			String key = entry.getKey().toString();
			MotorInfo value = entry.getValue();
			String motor1 = value.getMotor1();
			String motor2 = value.getMotor2();
			String forwardOrBackward1 = value.getForwardOrBackward1();
			String forwardOrBackward2 = value.getForwardOrBackward2();
			String startingOrNot = value.getStartingOrNot();
			int transitionTableNumber = value.getTransitionTableNumber();
			
			
			thirdPart = thirdPart + "\n			.mode(Mode." + key.toUpperCase() + ",\n				transitions"+ transitionTableNumber + ",\n				() ->{"
					+ "\n					Motor." + motor1 + "." + forwardOrBackward1.toLowerCase() + "();\n					"
					+ "Motor." + motor2 + "." + forwardOrBackward2.toLowerCase() + "();\n				})";
			
			if(startingOrNot.equals("Starting Mode")){
				firstPart = "\n		ModeSelector<Condition,Mode> controller = new ModeSelector<>"
						+ "(Condition.class, Mode.class, Mode." + key.toUpperCase() + ")";
			} 
		}
		
		Collection<FlaggerInfo> flaggerSet = conditions.values();
	
		for(FlaggerInfo flagger : flaggerSet){
			firstPart = firstPart + "\n			.flagger(" + flagger + ")";
		}
		
		return firstPart + thirdPart + ";\n			controller.control();"
				+ "\n	}\n";
	}
	
	public String generateModeEnum() {
		String code = "	enum Mode{";
		Set<String> modeSet = modes.keySet();
		
		int i = 0;
		for(String mode : modeSet){
			if(i != modeSet.size() - 1){
				code = code + "\n		" + mode.toUpperCase() + ",";
			} else{
				code = code + "\n		" + mode.toUpperCase() + ";";
			}
			
			i++;
			
		}
				
		return code + "\n	}";
	}

	public String generateConditionEnum() {
		String code = "	enum Condition{";
		Set<String> conditionSet = conditions.keySet();
		
		int i = 0;
		for(String condition : conditionSet){
			if(i != conditionSet.size() - 1){
				code = code + "\n		" + condition.toUpperCase() + ",";
			} else{
				code = code + "\n		" + condition.toUpperCase() + ";";
			}
			
			i++;
			
		}
				
		return code + "\n	}";
	}
	
	public String generate(){
		String code = "";
		code = code + addImports() + generateFlaggers() + generateTransitionTables() + generateModeSelector() + "\n" + generateConditionEnum() + "\n" + generateModeEnum() +"\n}";
		
		return code;
	}


}
