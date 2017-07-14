package edu.hendrix.csci235.creator;

public class TrueFalse {
	
	// patch up class that helped to identify a flags relevant information.
	
	private String flaggerType, trueCondition, falseCondition, inequality, number;
	
	public TrueFalse(String flaggerType, String trueCondition, String falseCondition, String inequality, String number){
		this.flaggerType = flaggerType;
		this.trueCondition = trueCondition;
		this.falseCondition = falseCondition;
		this.inequality = inequality;
		this.number = number;
		
	}

	public String getTrueCondition() {
		return trueCondition;
	}

	public String getFalseCondition() {
		return falseCondition;
	}

	public String getInequality() {
		return inequality;
	}
	
	public String getNumber() {
		return number;
	}

	@Override
	public String toString(){
		return "\n      > True condition: " + trueCondition.toUpperCase() + "\n      > False condition: " + falseCondition.toUpperCase() + 
				"\n      > " + trueCondition.toUpperCase() + " will become true when v " + inequality + " "  + number + ".";
	}

	public String getFlaggerType() {
		return flaggerType;
	}
	
	

}
