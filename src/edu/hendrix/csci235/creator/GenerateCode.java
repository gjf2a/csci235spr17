package edu.hendrix.csci235.creator;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class GenerateCode {
	private String programName;
	private Transition transitions1;
	private Transition transitions2;
	private Condition conditions;
	private FlaggerMap flagMapping;
	private Mode modes;
	public Collection<FlaggerInfo> rawFlaggers;
	
	public GenerateCode(String programName, Transition transitions1, Transition transitions2, 
			Condition conditions, FlaggerMap flagMapping, Mode modes ){
		this.programName = programName;
		this.transitions1 = transitions1;
		this.transitions2 = transitions2;
		this.conditions = conditions;
		this.flagMapping = flagMapping;
		this.modes = modes;
	}
	
	public String addImports(){
		return "import java.io.IOException;\nimport lejos.hardware.Button;"
				+ "\nimport lejos.hardware.motor.Motor;\nimport lejos.hardware.port.SensorPort;"
				+ "\nimport lejos.hardware.sensor.EV3UltrasonicSensor;\nimport lejos.hardware.sensor.EV3TouchSensor;"
				+ "\nimport modeselection.ModeSelector;\nimport modeselection.SensorFlagger;\nimport modeselection.Transitions;\n";
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
					", v -> v " + inequality + " " + number + ");\n";
					
		}
		code = code + flaggerConditions;
		
		return code;
	}
	
	public String generateTransitionTables(){
		TreeMap<String, String> rawTransitions1 = transitions1.transitions; 
		TreeMap<String, String> rawTransitions2 = transitions2.transitions;
		String toReturn1 =  "\n		Transitions<Condition,Mode> transitions1 = new Transitions<>();";
		String toReturn2 =  "\n		Transitions<Condition,Mode> transitions2 = new Transitions<>();";
		
		for(Map.Entry<String, String> entry : rawTransitions1.entrySet()){
			String condition = entry.getKey().toString();
			String mode = entry.getValue().toString();
			
			toReturn1 = toReturn1 + "\n		transitions1.add(Condition." + condition.toUpperCase() + ", Mode." +
					mode.toUpperCase() + ")";
		}
		
		for(Map.Entry<String, String> entry : rawTransitions2.entrySet()){
			String condition = entry.getKey().toString();
			String mode = entry.getValue().toString();
			
			toReturn2 = toReturn2 + "\n		transitions2.add(Condition." + condition.toUpperCase() + ", Mode." +
					mode.toUpperCase() + ")";
		}
		
		return toReturn1 + ";\n\n" + toReturn2 + ";\n";
		
		

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
			
			thirdPart = thirdPart + "\n			.mode(Mode." + key + ",\n				transitionTable,\n				() ->{"
					+ "\n					Motor." + motor1 + "." + forwardOrBackward1.toLowerCase() + "();\n					"
					+ "Motor." + motor2 + "." + forwardOrBackward2.toLowerCase() + "\n				})";
			
			if(startingOrNot.equals("Starting Mode")){
				firstPart = "\n		ModeSelector<Condition,Mode> controller = new ModeSelector<>"
						+ "(Condition.class, Mode.class, Mode." + key + ")";
			} 
		}
		
		
		return firstPart + thirdPart + ";\n			controller.control();"
				+ "\n	}\n}";
	}
	
	public String generate(){
		String code = "";
		code = code + addImports() + generateFlaggers() + generateTransitionTables() + generateModeSelector();
		
		return code;
	}

}
