package edu.hendrix.csci235.creator;

import javafx.beans.property.SimpleStringProperty;

public class TransitionsTableData {
	
	private SimpleStringProperty condition;
	private SimpleStringProperty mode;
	public int begIndex = 23;
	
	public TransitionsTableData(ConditionModePair cmp) {
		this(cmp.getCondition(), cmp.getMode());
	}

	public TransitionsTableData(String condition, String mode){
	    this.condition = new SimpleStringProperty(condition);
	    this.mode = new SimpleStringProperty(mode);
	}
	
	public ConditionModePair getPair() {
		return new ConditionModePair(getCondition(), getMode());
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
	
	//public Iterable<TempTableData> returnData(){
		//return 
	//}
	
	public String toString(){
	    String s = String.format("[condition: %s | mode: %s ]", condition, mode);
	    return s;
	}

}
