package edu.hendrix.csci235.creator;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.TypeSpec;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import sun.reflect.generics.tree.IntSignature;

public class MainController {
	Condition conditions = new Condition();
	
	Mode modes = new Mode();
	
	Transition transitions1 = new Transition();
	Transition transitions2 = new Transition();
	
	FlaggerMap flaggerMap = new FlaggerMap();
	
	RunCode codeRunner;
	
	@FXML
	TextField programName, flaggerName, modeName;
	
	@FXML
	ChoiceBox flaggerSelector, sensorPortSelector, motorSelector, inequalitySelector;
	
	@FXML
	TextField trueCondition, falseCondition;
	
	@FXML
	TextField value;
	
	@FXML
	TextField uLowText, uHighText, vLowText, vHighText;
	
	@FXML
	Label uLow, uHigh, vLow, vHigh;
	
	@FXML
	Button addCondition, addMode, addTransitionTable1, addTransitionTable2, previewCode, executeCode;
	
	@FXML
	ChoiceBox motor1, motor2;
	
	@FXML
	RadioButton forwardMotor1, forwardMotor2, backwardMotor1, backwardMotor2;
	
	@FXML
	RadioButton stopMotor1, stopMotor2;
	
	@FXML
	RadioButton simplified;
	
	@FXML
	RadioButton sourceCode;
	
	@FXML
	RadioButton bump, sonar;
	
	@FXML
	CheckBox startMode;
	
	@FXML
	ChoiceBox transitionCondition1, transitionCondition2, transitionMode1, transitionMode2;
	
	@FXML
	TextArea codeOutput;

	@FXML
	Label sensorPort, motor;
	
	final ToggleGroup motorGroup1 = new ToggleGroup();
	final ToggleGroup motorGroup2 = new ToggleGroup();
	final ToggleGroup sensorFlaggerInfo = new ToggleGroup();
	final ToggleGroup codeView = new ToggleGroup();

	String theCode = "";
	
	@SuppressWarnings("unchecked")
	@FXML
	public void initialize() {
		conditions.getConditions();
		modes.getModes();
		transitions1.getTransitions();
		transitions2.getTransitions();
		
		setButtonGroups();
		
		populateInequalitySelector();
		populateFlaggerSelector();
		populateSensorPortselector();
		populateMotorSelectors();
		
		addConditionButtonHandler();
		addModeButtonHandler();
		addTransition1Handler();
		addTransition2Handler();
		addCodeViewHandler();
		executeCodeHandler();
		
		flaggerSelector.getSelectionModel().selectedItemProperty()
	    .addListener((obs, oldV, newV) -> {
	    if(flaggerSelector.getSelectionModel().getSelectedItem().equals("Sensor")){
	    	setFalseHelper();
	    	sensorPortSelector.setVisible(true);
	    	sensorPort.setVisible(true);
	    	bump.setVisible(true);
	    	sonar.setVisible(true);
	    }
	    else if(flaggerSelector.getSelectionModel().getSelectedItem().equals("Motor")){
	    	setFalseHelper();
	    	motorSelector.setVisible(true);
	    	motor.setVisible(true);
	    } else if(flaggerSelector.getSelectionModel().getSelectedItem().equals("ColorCount")){
	    	setFalseHelper();
	    	uLowText.setVisible(true); 
	    	uHighText.setVisible(true);
	    	vLowText.setVisible(true);
	    	vHighText.setVisible(true);
	    	uLow.setVisible(true); 
	    	uHigh.setVisible(true);
	    	vLow.setVisible(true);
	    	vHigh.setVisible(true);
	    } else{
	    	setFalseHelper();
	    }
	    });
	}
	
	private void setFalseHelper(){
		sensorPortSelector.setVisible(false);
    	sensorPort.setVisible(false);
    	bump.setVisible(false);
    	sonar.setVisible(false);
    	motorSelector.setVisible(false);
    	motor.setVisible(false);
    	uLowText.setVisible(false); 
    	uHighText.setVisible(false);
    	vLowText.setVisible(false);
    	vHighText.setVisible(false);
    	uLow.setVisible(false); 
    	uHigh.setVisible(false);
    	vLow.setVisible(false);
    	vHigh.setVisible(false);
	}
	
		
	private void setButtonGroups(){
		forwardMotor1.setToggleGroup(motorGroup1);
		backwardMotor1.setToggleGroup(motorGroup1);
		stopMotor1.setToggleGroup(motorGroup1);
		forwardMotor1.setSelected(true);
		
		forwardMotor2.setToggleGroup(motorGroup2);
		backwardMotor2.setToggleGroup(motorGroup2);
		stopMotor2.setToggleGroup(motorGroup2);
		forwardMotor2.setSelected(true);
		
		bump.setToggleGroup(sensorFlaggerInfo);
		sonar.setToggleGroup(sensorFlaggerInfo);
		bump.setSelected(true);
		
		sourceCode.setToggleGroup(codeView);
		simplified.setToggleGroup(codeView);
		simplified.setSelected(true);
	}
	
	
	private void addConditionButtonHandler(){
		addCondition.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	try {
            		if(checkConditions() == true && validateValue(value.getText()) && checkFlaggers() == true){
            			conditions.add(trueCondition.getText(), 
            					flaggerName.getText(),
            					flaggerSelector.getSelectionModel().getSelectedItem().toString(),
            					sensorPortSelector.getSelectionModel().getSelectedItem().toString(),
            					sensorFlaggerInfo.getSelectedToggle().toString(),
            					motorSelector.getSelectionModel().getSelectedItem().toString(),
            					true,
            					Integer.parseInt(uLowText.getText()),
            					Integer.parseInt(uHighText.getText()),
            					Integer.parseInt(vLowText.getText()),
            					Integer.parseInt(vHighText.getText()),
            					inequalitySelector.getSelectionModel().getSelectedItem().toString(), 
            					Integer.parseInt(value.getText()));
            			conditions.add(falseCondition.getText(), 
            					flaggerName.getText(),
            					flaggerSelector.getSelectionModel().getSelectedItem().toString(),
            					sensorPortSelector.getSelectionModel().getSelectedItem().toString(),
            					sensorFlaggerInfo.getSelectedToggle().toString(),
            					motorSelector.getSelectionModel().getSelectedItem().toString(),
            					false,
            					Integer.parseInt(uLowText.getText()),
            					Integer.parseInt(uHighText.getText()),
            					Integer.parseInt(vLowText.getText()),
            					Integer.parseInt(vHighText.getText()),
            					inequalitySelector.getSelectionModel().getSelectedItem().toString(), 
            					Integer.parseInt(value.getText()));
            			flaggerMap.add(flaggerName.getText(),
            					(String) flaggerSelector.getSelectionModel().getSelectedItem(),
            					trueCondition.getText(), 
            					falseCondition.getText(),
            					inequalitySelector.getSelectionModel().getSelectedItem().toString(), 
            					value.getText());
            			//conditions.printKeys();
            			flaggerMap.toString();
            			previewCode();
            			clearAllCondition();
            		} 
					
					populateConditionTransition();
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
	}
	
	
	private boolean validateValue(String value){
		try{
			Integer.parseInt(value);
			return true;
		} catch(NumberFormatException nfe){
			Alert alert = new Alert(AlertType.ERROR, "Please enter a valid number in the value text box.", ButtonType.OK);
			alert.showAndWait();
			return false;
		}
	}
	
	
	private void addModeButtonHandler(){
		addMode.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	try {
            		if(checkModes() == true){
            			RadioButton selectedRadioButton1 = (RadioButton) motorGroup1.getSelectedToggle();
                		String toogleGroupValue1 = selectedRadioButton1.getText();
                		
                		RadioButton selectedRadioButton2 = (RadioButton) motorGroup2.getSelectedToggle();
                		String toogleGroupValue2 = selectedRadioButton2.getText();
                		
                		//RadioButton selectedRadioButton3 = (RadioButton) startingMode.getSelectedToggle();
                		//String toogleGroupValue3 = selectedRadioButton3.getText();
                		
                		String isStart = "";
                		if(startMode.isSelected()){
                			isStart = "Starting Mode";
                		}
                		
    					modes.add(modeName.getText(), 
    							motor1.getSelectionModel().getSelectedItem().toString(),
    							toogleGroupValue1,
    							motor2.getSelectionModel().getSelectedItem().toString(),
    							toogleGroupValue2,
    							isStart);
    					previewCode();
    					clearAllMode();
    					populateModeTransition();
            		}
            		
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
	}
	
	
	private void clearAllMode() {
		modeName.setText("");
		motor1.getSelectionModel().select(0);
		forwardMotor1.setSelected(true);
		forwardMotor2.setSelected(true);
		motor2.getSelectionModel().select(0);
		startMode.setSelected(false);
		
	}
	
	
	private void clearAllCondition() {
		flaggerName.setText("");
		flaggerSelector.getSelectionModel().select(0);
		trueCondition.setText("");
		falseCondition.setText("");
		inequalitySelector.getSelectionModel().select(0);
		value.setText("");
		sensorPortSelector.setVisible(false);
    	sensorPort.setVisible(false);
    	bump.setVisible(false);
    	sonar.setVisible(false);
    	motorSelector.setVisible(false);
    	motor.setVisible(false);
		
		
	}
	
	
	@SuppressWarnings("unchecked")
	private void populateSensorPortselector() {
		List<String> ports = new ArrayList<>(Arrays.asList("Port", "1", "2"
				, "3", "4"));
        for(String port: ports){
			sensorPortSelector.getItems().add(port);
		}
		sensorPortSelector.getSelectionModel().select(0);
	}
	

	@SuppressWarnings("unchecked")
	private void populateInequalitySelector() {
		List<String> inequalities = new ArrayList<>(Arrays.asList("==", "<=", 
				">="));
        for(String inequality: inequalities){
			inequalitySelector.getItems().add(inequality);
		}
		inequalitySelector.getSelectionModel().select(0);
		
	}
	

	@SuppressWarnings("unchecked")
	private void populateFlaggerSelector() {
		List<String> flaggerTypes = new ArrayList<>(Arrays.asList("Flagger", "Motor", 
				"Sensor", "Button", "ColorCount"));
        for(String flagger: flaggerTypes){
			flaggerSelector.getItems().add(flagger);
		}
		flaggerSelector.getSelectionModel().select(0);
		
	}
	
	
	private void populateMotorSelectors() {
		List<String> motors = new ArrayList<>(Arrays.asList("A", "B", 
				"C", "D"));
        for(String motor: motors){
			motor1.getItems().add(motor);
			motor2.getItems().add(motor);
			motorSelector.getItems().add(motor);		
		}
        
		motor1.getSelectionModel().select(0);
		motor2.getSelectionModel().select(0);
		motorSelector.getSelectionModel().select(0);
	}
	

	@SuppressWarnings("unchecked")
	private void populateConditionTransition() {
		Set<String> conditionsTransition = conditions.getKeys();
		
		transitionCondition1.getItems().add("Condition");
		transitionCondition2.getItems().add("Condition");
		
        for(String condition: conditionsTransition){
			transitionCondition1.getItems().add(condition);
			transitionCondition2.getItems().add(condition);
		}
		transitionCondition1.getSelectionModel().select(0);
		transitionCondition2.getSelectionModel().select(0);
	}
	
	
	@SuppressWarnings("unchecked")
	private void populateModeTransition() {
		Set<String> modesTransition = modes.getKeys();
		
		transitionMode1.getItems().add("Mode");
		transitionMode2.getItems().add("Mode");
		
		for(String mode: modesTransition){
			transitionMode1.getItems().add(mode);
			//transitionMode2.getItems().add(mode);
		}
		transitionMode1.getSelectionModel().select(0);
		transitionMode2.getSelectionModel().select(0);

	}
	
	
	private void addTransition1Handler(){
		addTransitionTable1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	try {
					transitions1.add(
							transitionCondition1.getSelectionModel().getSelectedItem().toString(),
							transitionMode1.getSelectionModel().getSelectedItem().toString());
					transitionCondition1.getSelectionModel().select(0);
					transitionMode1.getSelectionModel().select(0);
					previewCode();
					//System.out.println(transitions.transitions.toString());
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
	}
	
	private void addCodeViewHandler(){
		codeView.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
		      public void changed(ObservableValue<? extends Toggle> ov,
		          Toggle old_toggle, Toggle new_toggle) {
		        if (codeView.getSelectedToggle() != null) {
		          previewCode();
		        }
		      }
		    });
	}
	
	private void addTransition2Handler(){
		addTransitionTable2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	try {
					transitions2.add(
							transitionCondition2.getSelectionModel().getSelectedItem().toString(),
							transitionMode2.getSelectionModel().getSelectedItem().toString());
					transitionCondition2.getSelectionModel().select(0);
					transitionMode2.getSelectionModel().select(0);
					previewCode();
					//System.out.println(transitions.transitions.toString());
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
	}
	
	
	private void previewCode(){
		try {
			if(codeView.getSelectedToggle().equals(sourceCode)){
				String className = programName.getText();
				if(className.equals(null)){
					className = "ProgramName";
				} else {
					String firstLetter = className.substring(0,1).toUpperCase();
					String restOfWord = className.substring(1);
					className = firstLetter + restOfWord;
				}
				GenerateSourceCode codeGenerator = new GenerateSourceCode(
						className,
						transitions1,
						transitions2,
						conditions,
						flaggerMap,
						modes);
				codeOutput.setText(codeGenerator.generate().toString());
				theCode = codeGenerator.generate().toString();
			} else {
				String className = programName.getText();
				if(className.equals(null)){
					className = "ProgramName";
				} else {
					String firstLetter = className.substring(0,1).toUpperCase();
					String restOfWord = className.substring(1);
					className = firstLetter + restOfWord;
				}
				GenerateSourceCode codeSourceGenerator = new GenerateSourceCode(
						className,
						transitions1,
						transitions2,
						conditions,
						flaggerMap,
						modes);
				GenerateSimpleCode codeSimpleGenerator = new GenerateSimpleCode(
						className,
						transitions1,
						transitions2,
						conditions,
						flaggerMap,
						modes);
				theCode = codeSourceGenerator.generate().toString();
				codeOutput.setText(codeSimpleGenerator.toString());
			}
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private boolean checkConditions(){
		if(conditions.getKeys().contains(trueCondition.getText()) || conditions.getKeys().contains(falseCondition.getText()) || 
				trueCondition.getText().equals(falseCondition.getText())){
			Alert alert = new Alert(AlertType.ERROR, "That condition already exists - No two conditions should have the same name.\nPlease try again.", ButtonType.OK);
			alert.showAndWait();
			return false;

		} else {
			return true;
		}
	}
	
	
	private boolean checkFlaggers(){
		if(flaggerMap.getKeys().contains(flaggerName.getText())){
			Alert alert = new Alert(AlertType.ERROR, "That flagger already exists - No two flaggers should have the same name.\nPlease try again.", ButtonType.OK);
			alert.showAndWait();
			return false;

		} else {
			return true;
		}
	}
	
	
	private boolean checkModes(){
		if(modes.getKeys().contains(modeName.getText())){
			Alert alert = new Alert(AlertType.ERROR, "That mode already exists - No two modes should have the same name.\nPlease try again.", ButtonType.OK);
			alert.showAndWait();
			return false;

		} else {
			return true;
		}
	}
	
	private void executeCodeHandler(){
		executeCode.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	try {
            		codeRunner = new RunCode(programName.getText(), theCode);
            		//codeRunner.writeToFile();
            		codeRunner.run();
					
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
	}


}
