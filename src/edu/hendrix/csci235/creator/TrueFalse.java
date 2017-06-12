package edu.hendrix.csci235.creator;

public class TrueFalse {
	
	// patch up class that helped to identify a flags relevant information.
	
	private String trueCondition, falseCondition, inequality, number;
	
	public TrueFalse(String trueCondition, String falseCondition, String inequality, String number){
		this.trueCondition = trueCondition;
		this.falseCondition = falseCondition;
		this.inequality = inequality;
		this.number = number;
		
	}

	public String getTrueCondition() {
		return trueCondition;
	}

	public void setTrueCondition(String trueCondition) {
		this.trueCondition = trueCondition;
	}

	public String getFalseCondition() {
		return falseCondition;
	}

	public void setFalseCondition(String falseCondition) {
		this.falseCondition = falseCondition;
	}

	public String getInequality() {
		return inequality;
	}

	public void setInequality(String inequality) {
		this.inequality = inequality;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
	

}
