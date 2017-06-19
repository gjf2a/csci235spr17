package edu.hendrix.csci235.creator;

import javafx.beans.property.SimpleStringProperty;

public class TempTableData {
	
	private SimpleStringProperty condition;
	private SimpleStringProperty mode;
	public int begIndex = 23;

	public TempTableData(String condition, String mode){
	    this.condition = new SimpleStringProperty(condition);
	    this.mode = new SimpleStringProperty(mode);
	}

	public String getCondition() {
		return condition.toString().substring(begIndex, condition.toString().length() - 1);
	}

	public void setCondition(SimpleStringProperty condition) {
		this.condition = condition;
	}

	public String getMode() {
		return mode.toString().substring(begIndex, mode.toString().length() - 1);
	}

	public void setMode(SimpleStringProperty mode) {
		this.mode = mode;
	}
	
	public String toString(){
	    String s = String.format("[condition: %s | mode: %s ]", condition, mode);
	    return s;
	}

}
