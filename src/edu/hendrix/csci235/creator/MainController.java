package edu.hendrix.csci235.creator;


import java.io.IOException;
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
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class MainController {
	Condition conditions = new Condition();
	
	Mode modes = new Mode();
	
	Transition transitions = new Transition();
	
	FlaggerMap flaggerMap = new FlaggerMap();
	
	@FXML
	TextField programName;
	
	@FXML
	TextField flaggerName;
	
	@FXML
	ChoiceBox flaggerSelector;
	
	@FXML
	ChoiceBox sensorPortSelector;
	
	@FXML
	ChoiceBox motorSelector;
	
	@FXML
	TextField trueCondition;
	
	@FXML
	TextField falseCondition;
	
	@FXML
	ChoiceBox inequalitySelector;
	
	@FXML
	TextField value;
	
	@FXML
	Button addCondition;
	
	@FXML
	TextField modeName;
	
	@FXML
	ChoiceBox motor1;
	
	@FXML
	ChoiceBox motor2;
	
	@FXML
	RadioButton forwardMotor1;
	
	@FXML
	RadioButton backwardMotor1;
	
	@FXML
	RadioButton forwardMotor2;
	
	@FXML
	RadioButton backwardMotor2;
	
	@FXML
	RadioButton bump;
	
	@FXML
	RadioButton sonar;
	
	@FXML 
	Button addMode;
	
	@FXML
	ChoiceBox transitionCondition;
	
	@FXML
	ChoiceBox transitionMode;
	
	@FXML
	Button addTransitionTable;
	
	@FXML
	Button previewCode;
	
	@FXML
	Button executeCode;
	
	@FXML
	TextArea codeOutput;

	@FXML
	Label sensorPort;
	
	@FXML
	Label motor;
	
	final ToggleGroup motorGroup1 = new ToggleGroup();
	final ToggleGroup motorGroup2 = new ToggleGroup();
	final ToggleGroup sensorFlaggerInfo = new ToggleGroup();

	
	@FXML
	public void initialize() {
		conditions.getConditions();
		modes.getModes();
		
		setButtonGroups();
		
		populateInequalitySelector();
		populateFlaggerSelector();
		populateSensorPortselector();
		populateMotorSelectors();
		
		addConditionButtonHandler();
		addModeButtonHandler();
		addTransitionHandler();
		previewCodeHandler();
		
		flaggerSelector.getSelectionModel().selectedItemProperty()
	    .addListener((obs, oldV, newV) -> {
	    if(flaggerSelector.getSelectionModel().getSelectedItem().equals("Sensor")){
	    	sensorPortSelector.setVisible(true);
	    	sensorPort.setVisible(true);
	    	bump.setVisible(true);
	    	sonar.setVisible(true);
	    }
	    else if(flaggerSelector.getSelectionModel().getSelectedItem().equals("Motor")){
	    	motorSelector.setVisible(true);
	    	motor.setVisible(true);
	    }});
	}
		
	private void setButtonGroups(){
		forwardMotor1.setToggleGroup(motorGroup1);
		backwardMotor1.setToggleGroup(motorGroup1);
		forwardMotor1.setSelected(true);
		
		forwardMotor2.setToggleGroup(motorGroup2);
		backwardMotor2.setToggleGroup(motorGroup2);
		forwardMotor2.setSelected(true);
		
		bump.setToggleGroup(sensorFlaggerInfo);
		sonar.setToggleGroup(sensorFlaggerInfo);
		bump.setSelected(true);
	}
	
	private void addConditionButtonHandler(){
		addCondition.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	try {
            		// There could be a bug here... not sure yet
					conditions.add(trueCondition.getText(), 
							flaggerName.getText(),
							flaggerSelector.getSelectionModel().getSelectedItem().toString(),
							sensorPortSelector.getSelectionModel().getSelectedItem().toString(),
							sensorFlaggerInfo.getSelectedToggle().toString(),
							motorSelector.getSelectionModel().getSelectedItem().toString(),
							true,
							inequalitySelector.getSelectionModel().getSelectedItem().toString(), 
							Integer.parseInt(value.getText()));
					conditions.add(falseCondition.getText(), 
							flaggerName.getText(),
							flaggerSelector.getSelectionModel().getSelectedItem().toString(),
							sensorPortSelector.getSelectionModel().getSelectedItem().toString(),
							sensorFlaggerInfo.getSelectedToggle().toString(),
							motorSelector.getSelectionModel().getSelectedItem().toString(),
							false,
							inequalitySelector.getSelectionModel().getSelectedItem().toString(), 
							Integer.parseInt(value.getText()));
					flaggerMap.add(flaggerName.getText(), 
							trueCondition.getText(), 
							falseCondition.getText(),
							inequalitySelector.getSelectionModel().getSelectedItem().toString(), 
							value.getText());
					//conditions.printKeys();
					flaggerMap.toString();
					clearAllCondition();
					
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
	
	private void addModeButtonHandler(){
		addMode.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	try {
            		// There could be a bug here... not sure yet
            		RadioButton selectedRadioButton1 = (RadioButton) motorGroup1.getSelectedToggle();
            		String toogleGroupValue1 = selectedRadioButton1.getText();
            		
            		RadioButton selectedRadioButton2 = (RadioButton) motorGroup2.getSelectedToggle();
            		String toogleGroupValue2 = selectedRadioButton1.getText();
            		
					modes.add(modeName.getText(), 
							motor1.getSelectionModel().getSelectedItem().toString(),
							toogleGroupValue1,
							motor2.getSelectionModel().getSelectedItem().toString(),
							toogleGroupValue2
							);
					//modes.printKeys();
					//System.out.println(modes.getModes().toString());
					clearAllMode();
					populateModeTransition();
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
	
	private void populateSensorPortselector() {
		List<String> ports = new ArrayList<>(Arrays.asList("Port", "1", "2"
				, "3", "4"));
        for(String port: ports){
			sensorPortSelector.getItems().add(port);
		}
		sensorPortSelector.getSelectionModel().select(0);
	}

	private void populateInequalitySelector() {
		List<String> inequalities = new ArrayList<>(Arrays.asList("==", "<=", 
				">="));
        for(String inequality: inequalities){
			inequalitySelector.getItems().add(inequality);
		}
		inequalitySelector.getSelectionModel().select(0);
		
	}

	private void populateFlaggerSelector() {
		List<String> flaggerTypes = new ArrayList<>(Arrays.asList("Flagger", "Motor", 
				"Sensor", "Button", "Camera"));
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

	private void populateConditionTransition() {
		Set<String> conditionsTransition = conditions.getKeys();
		
        for(String condition: conditionsTransition){
			transitionCondition.getItems().add(condition);
		}
		transitionCondition.getSelectionModel().select(0);
	}
	
	private void populateModeTransition() {
		Set<String> modesTransition = modes.getKeys();
		
		for(String mode: modesTransition){
			transitionMode.getItems().add(mode);
		}
		transitionMode.getSelectionModel().select(0);

	}
	
	private void addTransitionHandler(){
		addTransitionTable.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	try {
					transitions.add(
							transitionCondition.getSelectionModel().getSelectedItem().toString(),
							transitionMode.getSelectionModel().getSelectedItem().toString());
					//System.out.println(transitions.transitions.toString());
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
	}
	
	private void previewCodeHandler(){
		previewCode.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	try {
            		GenerateCode codeGenerator = new GenerateCode(
            				programName.getText(),
            				transitions,
            				conditions,
            				flaggerMap,
            				modes);
					codeOutput.setText(codeGenerator.generate().toString());
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
	}



}
