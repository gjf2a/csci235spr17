package edu.hendrix.csci235.creator;

import java.util.Optional;

public class FlaggerInfo {
	
	// Holds all of the relevant info for a specific flag
	
	private String flaggerName, flaggerType, sensorPort, bumpOrSonar, motor, inequality;
	private Boolean trueOrFalse;
	private int uLow, uHigh, vLow, vHigh, value;

	public FlaggerInfo(String flaggerName, String flaggerType, String sensorPort, String bumpOrSonar, String motor, Boolean trueOrFalse,
			String inequality, int uLow, int uHigh, int vLow, int vHigh, int value){
		this.flaggerName = flaggerName;
		this.flaggerType = flaggerType;
		this.sensorPort = sensorPort;
		this.motor = motor;
		this.trueOrFalse = trueOrFalse;
		this.bumpOrSonar = bumpOrSonar;
		this.inequality = inequality;
		this.uLow = uLow;
		this.uHigh = uHigh;
		this.vLow = vLow;
		this.vHigh = vHigh;
		this.value = value;
	}

	public void setInequality(String inequality) {
		this.inequality = inequality; 
	}

	public int getValue() {
		return value;
	}

	public String getFlaggerName() {
		return flaggerName;
	}

	public void setFlaggerName(String flaggerName) {
		this.flaggerName = flaggerName;
	}
	
	public String getFlaggerType() {
		return flaggerType;
	}

	public void setFlaggerType(String flaggerType) {
		this.flaggerType = flaggerType;
	}

	public String getSensorPort() {
		return sensorPort;
	}

	public void setSensorPort(String sensorPort) {
		this.sensorPort = sensorPort;
	}

	public String getBumpOrSonar() {
		return bumpOrSonar;
	}

	public void setBumpOrSonar(String bumpOrSonar) {
		this.bumpOrSonar = bumpOrSonar;
	}

	public Boolean getTrueOrFalse() {
		return trueOrFalse;
	}

	public void setTrueOrFalse(Boolean trueOrFalse) {
		this.trueOrFalse= trueOrFalse;
	}


	public String getInequality() {
		return inequality;
	}

	public int getuLow() {
		return uLow;
	}

	public void setuLow(int uLow) {
		this.uLow = uLow;
	}

	public int getuHigh() {
		return uHigh;
	}

	public void setuHigh(int uHigh) {
		this.uHigh = uHigh;
	}

	public int getvLow() {
		return vLow;
	}

	public void setvLow(int vLow) {
		this.vLow = vLow;
	}

	public int getvHigh() {
		return vHigh;
	}

	public void setvHigh(int vHigh) {
		this.vHigh = vHigh;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	public String addFlaggers(){
		if(trueOrFalse == true){
			if(flaggerType.equals("Sensor")){
				if(bumpOrSonar.equals("RadioButton[id=bump, styleClass=radio-button]'Bump'")){
						return( "		"  + flaggerType + "Flagger<Condition> " + flaggerName + " = new " +
								flaggerType +"Flagger<>(new EV3TouchSensor(SensorPort.S" + sensorPort +"));\n");
				} else {
					return( "		"  + flaggerType + "Flagger<Condition> " + flaggerName + " = new " +
							flaggerType +"Flagger<>(new EV3UltraSonicSensor(SensorPort.S" + sensorPort +"), s -> s.getDistanceMode());\n");
				}
			} else if(flaggerType.equals("Motor")){
				return( "		"  + flaggerType + "Flagger<Condition> " + flaggerName + " = new " +
						flaggerType +"Flagger<>(Motor " + motor + ");\n");
			} else if(flaggerType.equals("ColorCount")){
				return( "		CameraFlagger<Condition> camera = new CameraFlagger<>();\n" + "		"  + flaggerType + "Flagger<Condition> " + flaggerName + " = new " +
						flaggerType +"Flagger<>(" + uLow + ", " + uHigh + ", " + vLow + ", " + vHigh + ");\n		camera.addSub(" + flaggerName + ");");
			}
		}
		else{
			return("");
		} return("");
	}
	
	public String convertValueFormat(){
		if(flaggerType.equals("Sensor")){
			if(trueOrFalse == true){
				return  flaggerName + " - sensor - t";
			} else {
				return  flaggerName + " - sensor - f";
			}
		} else if(flaggerType.equals("Motor")){
			if(trueOrFalse == true){
				return  flaggerName + " - motor - t";
			} else{
				return  flaggerName + " - motor - f";
			}
		}else if(flaggerType.equals("Button")){
			if(trueOrFalse == true){
				return  flaggerName + " - motor - t";
			} else{
				return  flaggerName + " - motor - f";
			}
		}
		else{
			return("~ something is wrong ~");
		}
	}
	
	
	
	@Override
	public String toString(){
		if(flaggerType.equals("Sensor")){
			if(trueOrFalse == true){
				return  flaggerName;
			} else {
				return  flaggerName;
			}
		} else if(flaggerType.equals("Motor")){
			if(trueOrFalse == true){
				return  flaggerName;
			} else{
				return  flaggerName;
			}
		} else if(flaggerType.equals("Button")){
			if(trueOrFalse == true){
				return  flaggerName;
			} else{
				return  flaggerName;
			}
		}
		else{
			return("~ something is wrong ~");
		}
	}
	
	
	
}
