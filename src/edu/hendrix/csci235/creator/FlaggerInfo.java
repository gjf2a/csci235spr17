package edu.hendrix.csci235.creator;

import java.util.Optional;

public class FlaggerInfo {
	private String flaggerName, flaggerType, sensorPort, bumpOrSonar, motor, inequality;
	private Boolean trueOrFalse;
	private int value;

	public FlaggerInfo(String flaggerName, String flaggerType, String sensorPort, String bumpOrSonar, String motor, Boolean trueOrFalse,
			String inequality, int value){
		this.flaggerName = flaggerName;
		this.flaggerType = flaggerType;
		this.sensorPort = sensorPort;
		this.motor = motor;
		this.trueOrFalse = trueOrFalse;
		this.bumpOrSonar = bumpOrSonar;
		this.inequality = inequality;
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

	public void setValue(int value) {
		this.value = value;
	}
	
	@Override
	public String toString(){
		if(flaggerType.equals("Sensor")){
			if(bumpOrSonar.equals("RadioButton[id=bump, styleClass=radio-button]'Bump'")){
				return(flaggerType + "Flagger<Condition> " + flaggerName + " = new " +
						flaggerType +"Flagger<>(new EV3touchSensor(SensorPort." + sensorPort +");");
			} else{
				return(flaggerType + "Flagger<Condition> " + flaggerName + " = new " +
						flaggerType +"Flagger<>(new EV3UltraSonicSensor(SensorPort." + sensorPort +"), s -> s.getDistanceMode());");
			}
		} else if(flaggerType.equals("Motor")){
			return(flaggerType + "Flagger<Condition> " + flaggerName + " = new " +
					flaggerType +"Flagger<>(Motor " + motor + ");");
		}
		else{
			return("~ something is wrong ~");
		}
		//return(flaggerType + "Flagger<Condition> " + flaggerName + " = new " +
				//flaggerType +"Flagger<>(new EV3UltraSonicSensor(SensorPort.S2), s -> s.getDistanceMode());");
	}
	
	
}
