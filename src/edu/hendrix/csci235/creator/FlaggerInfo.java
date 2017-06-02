package edu.hendrix.csci235.creator;

public class FlaggerInfo {
	private String flaggerName, flaggerType, inequality;
	private Boolean trueOrFalse;
	private int value;

	public FlaggerInfo(String flaggerName, String flaggerType, Boolean trueOrFalse,
			String inequality, int value){
		this.flaggerName = flaggerName;
		this.flaggerType = flaggerType;
		this.trueOrFalse = trueOrFalse;
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
		return(flaggerType.toString() + ", " + trueOrFalse.toString() +
				", " + inequality + ", " + value);
	}
	
	
}
