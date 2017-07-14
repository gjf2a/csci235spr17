package edu.hendrix.csci235.creator;

public class FlaggerInfo {
	
	// Holds all of the relevant info for a specific flag
	
	private String flaggerName, flaggerType, sensorPort, bumpOrSonar, motor, inequality;
	private Boolean trueOrFalse;
	private int uLow, uHigh, vLow, vHigh;
	private double value;

	public FlaggerInfo(String flaggerName, String flaggerType, String sensorPort, String bumpOrSonar, String motor, Boolean trueOrFalse,
			String inequality, int uLow, int uHigh, int vLow, int vHigh, double value){
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

	public double getValue() {
		return value;
	}

	public String getFlaggerName() {
		return flaggerName;
	}
	
	public String getFlaggerType() {
		return flaggerType;
	}

	public String getSensorPort() {
		return sensorPort;
	}

	public String getBumpOrSonar() {
		return bumpOrSonar;
	}

	public Boolean getTrueOrFalse() {
		return trueOrFalse;
	}

	public String getInequality() {
		return inequality;
	}

	public int getuLow() {
		return uLow;
	}

	public int getuHigh() {
		return uHigh;
	}

	public int getvLow() {
		return vLow;
	}

	public int getvHigh() {
		return vHigh;
	}

	public String addFlaggers(){
		if(trueOrFalse == true){
			if(flaggerType.equals("Sensor")){
				if(bumpOrSonar.equals("RadioButton[id=bump, styleClass=radio-button]'Bump'")){
						return( "		"  + flaggerType + "Flagger<Condition> " + flaggerName + " = new " +
								flaggerType +"Flagger<>(new EV3TouchSensor(SensorPort.S" + sensorPort +"));\n");
				} else {
					return( "		"  + flaggerType + "Flagger<Condition> " + flaggerName + " = new " +
							flaggerType +"Flagger<>(new EV3UltrasonicSensor(SensorPort.S" + sensorPort +"), s -> s.getDistanceMode());\n");
				}
			} else if(flaggerType.equals("Motor")){
				return( "		"  + flaggerType + "Flagger<Condition> " + flaggerName + " = new " +
						flaggerType +"Flagger<>(Motor " + motor + ");\n");
			} else if(flaggerType.equals("ColorCount")){
				return( "		CameraFlagger<Condition> camera = new CameraFlagger<>();\n" + "		"  + flaggerType + "Flagger<Condition> " + flaggerName + " = new " +
						flaggerType +"Flagger<>(" + uLow + ", " + uHigh + ", " + vLow + ", " + vHigh + ");\n		camera.addSub(" + flaggerName + ");\n");
			}
		}
		else{
			return("");
		} return("");
	}
	
	@Override
	public String toString(){
		if (flaggerType.equals("ColorCount")) {
			return "camera";
		}
		return flaggerName;
	}
	
	
	
}
