package edu.hendrix.csci235.creator;

public class ConditionModePair {
	
	private String condition, mode;
	
	public ConditionModePair(String condition, String mode ){
		this.setCondition(condition);
		this.setMode(mode);
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}
	
	@Override
	public String toString(){
		return "Condition: " + condition + "\nMode: " + mode + "\n\n";
	}

}
