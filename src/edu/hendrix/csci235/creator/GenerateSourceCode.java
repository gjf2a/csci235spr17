package edu.hendrix.csci235.creator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class GenerateSourceCode {
	
	// This class builds all of the programs using strings. 
	
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
	
	public GenerateSourceCode(String programName, Transition transitions1, Transition transitions2, 
			Transition transitions3, Transition transitions4, Transition transitions5, Condition conditions, FlaggerMap flagMapping, Mode modes ){
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
	
	public String addImports(){
		return "import java.io.IOException;\nimport lejos.hardware.Button;"
				+ "\nimport lejos.hardware.motor.Motor;\nimport lejos.hardware.port.SensorPort;"
				+ "\nimport lejos.hardware.sensor.EV3UltrasonicSensor;\nimport lejos.hardware.sensor.EV3TouchSensor;"
				+ "\nimport edu.hendrix.modeselection.ModeSelector;\nimport edu.hendrix.modeselection.SensorFlagger;"
				+ "\nimport edu.hendrix.modeselection.Transitions;"
				+ "\nimport edu.hendrix.modeselection.vision.CameraFlagger;"
				+ "\nimport edu.hendrix.modeselection.vision.color.ColorCountFlagger;\n";
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
		code = code + flaggerConditions;
		
		return code;
	}
	
	public String generateTransitionTables(){
		//TreeMap<String, String> rawTransitions1 = transitions1.transitions; 
		//TreeMap<String, String> rawTransitions2 = transitions2.transitions;
		ArrayList<ConditionModePair> rawTransitions1 = transitions1.getTransitions();
		ArrayList<ConditionModePair> rawTransitions2 = transitions2.getTransitions();
		ArrayList<ConditionModePair> rawTransitions3 = transitions3.getTransitions();
		ArrayList<ConditionModePair> rawTransitions4 = transitions4.getTransitions();
		ArrayList<ConditionModePair> rawTransitions5 = transitions5.getTransitions();
		
		String toReturn2 = "";
		String toReturn3 = "";
		String toReturn4 = "";
		String toReturn5 = "";
		
		if(transitions2.isEmpty() == false){
			toReturn2 =  "\n		Transitions<Condition,Mode> transitions2 = new Transitions<>();";
		}
		if(transitions3.isEmpty() == false){
			toReturn3 =  "\n		Transitions<Condition,Mode> transitions3 = new Transitions<>();";
		}
		if(transitions4.isEmpty() == false){
			toReturn4 =  "\n		Transitions<Condition,Mode> transitions4 = new Transitions<>();";
		}
		if(transitions5.isEmpty() == false){
			toReturn5 =  "\n		Transitions<Condition,Mode> transitions5 = new Transitions<>();";
		}

		String toReturn1 =  "\n		Transitions<Condition,Mode> transitions1 = new Transitions<>();";
		
		
		
		for(ConditionModePair cmp : rawTransitions1){
			String condition = cmp.getCondition();
			String mode = cmp.getMode();
			
			toReturn1 = toReturn1 + "\n		transitions1.add(Condition." + condition.toUpperCase() + ", Mode." +
					mode.toUpperCase() + ");";
		}
		
		if(transitions2.isEmpty() == false){
			for(ConditionModePair cmp : rawTransitions2){
				String condition = cmp.getCondition();
				String mode = cmp.getMode();
				
				toReturn2 = toReturn2 + "\n		transitions2.add(Condition." + condition.toUpperCase() + ", Mode." +
						mode.toUpperCase() + ");";
			}
		}
		
		if(transitions3.isEmpty() == false){
			for(ConditionModePair cmp : rawTransitions3){
				String condition = cmp.getCondition();
				String mode = cmp.getMode();
				
				toReturn3 = toReturn3 + "\n		transitions3.add(Condition." + condition.toUpperCase() + ", Mode." +
						mode.toUpperCase() + ");";
			}
		}
		
		if(transitions4.isEmpty() == false){
			for(ConditionModePair cmp : rawTransitions4){
				String condition = cmp.getCondition();
				String mode = cmp.getMode();
				
				toReturn4 = toReturn4 + "\n		transitions4.add(Condition." + condition.toUpperCase() + ", Mode." +
						mode.toUpperCase() + ");";
			}
		}
		
		if(transitions5.isEmpty() == false){
			for(ConditionModePair cmp : rawTransitions5){
				String condition = cmp.getCondition();
				String mode = cmp.getMode();
				
				toReturn5 = toReturn5 + "\n		transitions5.add(Condition." + condition.toUpperCase() + ", Mode." +
						mode.toUpperCase() + ");";
			}
		}
		
		return toReturn1 + "\n\n" + toReturn2 + "\n\n" + toReturn3 + "\n\n" + toReturn4 + "\n\n" + toReturn5 +"\n";
	}
	
	public String generateModeSelector(){
		TreeMap<String, MotorInfo> rawModes = modes.getModes();
		/*String firstPart = "\n		ModeSelector<Condition,Mode> controller = new ModeSelector<>"
				+ "(Condition.class, Mode.class, Mode." ;*/
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
		
		Collection<FlaggerInfo> flaggerSet = conditions.getValues();
	
		for(FlaggerInfo flagger : flaggerSet){
			if (flagger.shouldAdd()) {
				firstPart = firstPart + "\n			.flagger(" + flagger + ")";
			}
		}
		
		return firstPart + thirdPart + ";\n			controller.control();"
				+ "\n	}\n";
	}
	
	public String generateModeEnum() {
		String code = "	enum Mode{";
		Set<String> modeSet = modes.getModes().keySet();
		
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
		Set<String> conditionSet = conditions.getConditions().keySet();
		
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
