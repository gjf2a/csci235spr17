package edu.hendrix.csci235.creator;

public class MotorInfo {
	
	// Holds all info about a specific mode.

	private String motor1, motor2, forwardOrBackward1, forwardOrBackward2, startingOrNot;
	
	public MotorInfo(String motor1, String forwardOrBackward1, String motor2, String forwardOrBackward2, String startingOrNot){
		this.motor1 = motor1;
		this.forwardOrBackward1 = forwardOrBackward1;
		this.motor2 = motor2;
		this.forwardOrBackward2 = forwardOrBackward2;
		this.startingOrNot = startingOrNot;
	}

	public String getMotor1() {
		return motor1;
	}

	public void setMotor1(String motor1) {
		this.motor1 = motor1;
	}

	public String getMotor2() {
		return motor2;
	}

	public void setMotor2(String motor2) {
		this.motor2 = motor2;
	}

	public String getForwardOrBackward1() {
		return forwardOrBackward1;
	}

	public void setForwardOrBackward1(String forwardOrBackward1) {
		this.forwardOrBackward1 = forwardOrBackward1;
	}

	public String getForwardOrBackward2() {
		return forwardOrBackward2;
	}

	public void setForwardOrBackward2(String forwardOrBackward2) {
		this.forwardOrBackward2 = forwardOrBackward2;
	}
	
	public String getStartingOrNot() {
		return startingOrNot;
	}

	public void setStartingOrNot(String startingOrNot) {
		this.startingOrNot = startingOrNot;
	}

	@Override
	public String toString(){
		return(motor1.toString() + ", " + forwardOrBackward1.toString() +
				", " + motor2.toString() + ", " + forwardOrBackward2.toString());
	}
	
	
	
}
