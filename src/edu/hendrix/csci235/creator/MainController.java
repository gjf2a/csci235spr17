package edu.hendrix.csci235.creator;

import java.io.IOException;
import java.util.*;

import javafx.beans.value.*;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import javafx.collections.*;

public class MainController {
	Condition conditions = new Condition();
	
	Mode modes = new Mode();
	
	Transition transitions1 = new Transition();
	Transition transitions2 = new Transition();
	Transition transitions3 = new Transition();
	Transition transitions4 = new Transition();
	Transition transitions5 = new Transition();
	
	FlaggerMap flaggerMap = new FlaggerMap();
	
	RunCode codeRunner;
	
	@FXML
	TextField programName;
	
	@FXML
	ComboBox<String> flaggerName;
	
	@FXML
	ComboBox<String> modeName;
	
	@FXML
	ChoiceBox<String> flaggerSelector, sensorPortSelector, motorSelector, inequalitySelector;
	
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
	ChoiceBox<String> motor1, motor2;
	
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
	ChoiceBox<String> transitionCondition1, transitionMode1;
	
	@FXML
	TextArea codeOutput;

	@FXML
	Label sensorPort, motor;
	
	@FXML
	Spinner<Integer> tableNumber;
	
	@FXML
	Spinner<Integer> modeTableNumber;
	
	@FXML
	TableView<TempTableData> transitionTableViewer;
	
	@FXML
	TableColumn<TempTableData,String> transitionTableCondition;
	
	@FXML
	TableColumn<TempTableData,String> transitionTableMode;
	
	
	final ToggleGroup motorGroup1 = new ToggleGroup();
	final ToggleGroup motorGroup2 = new ToggleGroup();
	final ToggleGroup sensorFlaggerInfo = new ToggleGroup();
	final ToggleGroup codeView = new ToggleGroup();

	String theCode = "";
	
	ObservableList<TempTableData> tempDataList = FXCollections.observableArrayList();
	
	private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");
	
	@FXML
	public void initialize() {
		transitionTableCondition.setCellValueFactory(
			    new PropertyValueFactory<TempTableData,String>("Condition"));
		transitionTableMode.setCellValueFactory(
			    new PropertyValueFactory<TempTableData,String>("Mode"));
		
		
		conditions.getConditions();
		modes.getModes();
		transitions1.getTransitions();
		transitions2.getTransitions();
		
		flaggerName.getItems().add("");
		modeName.getItems().add("");
		
		setButtonGroups();
		
		populateInequalitySelector();
		populateFlaggerSelector();
		populateSensorPortselector();
		populateMotorSelectors();
		//populateFlaggerNameSelector();
		
		addConditionButtonHandler();
		addModeButtonHandler();
		addTransition1Handler();
		//addTransition2Handler();
		addCodeViewHandler();
		executeCodeHandler();
		
		tableNumber.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,5));
		modeTableNumber.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,5));
		
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
		
		tableNumber.valueProperty().addListener((obs, oldValue, newValue) -> {
			if(tableNumber.getValueFactory().getValue().equals(1)){
				if(transitions1.transitions.size() > 0){
					for(int i = 0; i < transitions1.getTransitions().size(); i++){
						tempDataList.add(new TempTableData(transitions1.getTransitions().get(i).getCondition(), transitions1.getTransitions().get(i).getMode()));
					}
				}
			} else if(tableNumber.getValueFactory().getValue().equals(2)){
				if(transitions2.transitions.size() > 0){
					for(int i = 0; i < transitions2.getTransitions().size(); i++){
						tempDataList.add(new TempTableData(transitions2.getTransitions().get(i).getCondition(), transitions2.getTransitions().get(i).getMode()));					
					}	
				}
			} else if(tableNumber.getValueFactory().getValue().equals(3)){
				if(transitions3.transitions.size() > 0){
					for(int i = 0; i < transitions3.getTransitions().size(); i++){
						tempDataList.add(new TempTableData(transitions3.getTransitions().get(i).getCondition(), transitions3.getTransitions().get(i).getMode()));
					}
				}
			} else if(tableNumber.getValueFactory().getValue().equals(4)){
				if(transitions4.transitions.size() > 0){
					for(int i = 0; i < transitions4.getTransitions().size(); i++){
						tempDataList.add(new TempTableData(transitions4.getTransitions().get(i).getCondition(), transitions4.getTransitions().get(i).getMode()));					
					}
				}
			} else if(tableNumber.getValueFactory().getValue().equals(5)){
				if(transitions5.transitions.size() > 0){
					for(int i = 0; i < transitions5.getTransitions().size(); i++){
						tempDataList.add(new TempTableData(transitions5.getTransitions().get(i).getCondition(), transitions5.getTransitions().get(i).getMode()));
					}
				}
			}
			
			transitionTableViewer.setItems(tempDataList);
			
		});
		
		flaggerName.getSelectionModel().selectedItemProperty()
	    .addListener((obs, oldV, newV) -> {
	    	if(flaggerMap.getFlagMapping().containsKey(flaggerName.getSelectionModel().getSelectedItem().toString())){
	    		flaggerSelector.getSelectionModel().select(flaggerMap.getFlagMapping().get(flaggerName.getSelectionModel().
	    				getSelectedItem().toString()).getFlaggerType());
	    		trueCondition.setText(flaggerMap.getFlagMapping().get(flaggerName.getSelectionModel().
	    				getSelectedItem().toString()).getTrueCondition());
	    		falseCondition.setText(flaggerMap.getFlagMapping().get(flaggerName.getSelectionModel().
	    				getSelectedItem().toString()).getFalseCondition());
	    		inequalitySelector.getSelectionModel().select(flaggerMap.getFlagMapping().get(flaggerName.getSelectionModel().
	    				getSelectedItem().toString()).getInequality());
	    		value.setText(flaggerMap.getFlagMapping().get(flaggerName.getSelectionModel().
	    				getSelectedItem().toString()).getNumber());
	    	}
	    	
	    });
		
		modeName.getSelectionModel().selectedItemProperty()
	    .addListener((obs, oldV, newV) -> {
	    	if(modes.getModes().containsKey(modeName.getSelectionModel().getSelectedItem().toString())){
	    		if(modes.getModes().get(modeName.getSelectionModel().getSelectedItem().toString()).getStartingOrNot().equals("Starting Mode")){
	    			startMode.setSelected(true);
	    		}
	    		motor1.getSelectionModel().select(modes.getModes().get(modeName.getSelectionModel().getSelectedItem().toString()).getMotor1());
	    		motor2.getSelectionModel().select(modes.getModes().get(modeName.getSelectionModel().getSelectedItem().toString()).getMotor2());
	    		
	    		if(modes.getModes().get(modeName.getSelectionModel().getSelectedItem().toString()).getForwardOrBackward1().equals("Backward")){
	    			motorGroup1.selectToggle(backwardMotor1);
	    		} else if(modes.getModes().get(modeName.getSelectionModel().getSelectedItem().toString()).getForwardOrBackward1().equals("Forward")){
	    			motorGroup1.selectToggle(forwardMotor1);
	    		} else if(modes.getModes().get(modeName.getSelectionModel().getSelectedItem().toString()).getForwardOrBackward1().equals("Stop")){
	    			motorGroup1.selectToggle(stopMotor1);
	    		} 
	    		
	    		if(modes.getModes().get(modeName.getSelectionModel().getSelectedItem().toString()).getForwardOrBackward2().equals("Backward")){
	    			motorGroup2.selectToggle(backwardMotor2);
	    		} else if(modes.getModes().get(modeName.getSelectionModel().getSelectedItem().toString()).getForwardOrBackward2().equals("Forward")){
	    			motorGroup2.selectToggle(forwardMotor2);
	    		} else if(modes.getModes().get(modeName.getSelectionModel().getSelectedItem().toString()).getForwardOrBackward2().equals("Stop")){
	    			motorGroup2.selectToggle(stopMotor2);
	    		} 
	    		
	    		modeTableNumber.getValueFactory().setValue(modes.getModes().get(modeName.getSelectionModel().getSelectedItem()).getTransitionTableNumber());
	    		
	    	}
	    	
	    	
	    });
		
		
		// Resource: https://stackoverflow.com/questions/28603224/sort-tableview-with-drag-and-drop-rows
		// Thank you James_D!!!!! 
		transitionTableViewer.setRowFactory(tv -> {
            TableRow<TempTableData> row = new TableRow<>();

            row.setOnDragDetected(event -> {
                if (! row.isEmpty()) {
                    Integer index = row.getIndex();
                    Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
                    db.setDragView(row.snapshot(null, null));
                    ClipboardContent cc = new ClipboardContent();
                    cc.put(SERIALIZED_MIME_TYPE, index);
                    db.setContent(cc);
                    event.consume();
                }
            });

            row.setOnDragOver(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    if (row.getIndex() != ((Integer)db.getContent(SERIALIZED_MIME_TYPE)).intValue()) {
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                        event.consume();
                    }
                }
            });

            row.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    int draggedIndex = (Integer) db.getContent(SERIALIZED_MIME_TYPE);
                    TempTableData draggedItem1 = transitionTableViewer.getItems().remove(draggedIndex);

                    int dropIndex ; 

                    if (row.isEmpty()) {
                        dropIndex = transitionTableViewer.getItems().size() ;
                    } else {
                        dropIndex = row.getIndex();
                    }

                    transitionTableViewer.getItems().add(dropIndex, draggedItem1);

                    event.setDropCompleted(true);
                    transitionTableViewer.getSelectionModel().select(dropIndex);
                    event.consume();
                    
                }
            });        
            return row ;
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
            		if(/*checkConditions() == true &&*/ validateValue(value.getText())){
            			if(flaggerMap.getFlagMapping().containsKey(flaggerName.getSelectionModel().getSelectedItem().toString())){
            				//System.out.println(flaggerMap.getFlagMapping().get(flaggerName.getSelectionModel().getSelectedItem().toString()).getTrueCondition());
            				conditions.remove(flaggerMap.getFlagMapping().get(flaggerName.getSelectionModel().getSelectedItem().toString()).getTrueCondition());
            				conditions.remove(flaggerMap.getFlagMapping().get(flaggerName.getSelectionModel().getSelectedItem().toString()).getFalseCondition());
            				flaggerMap.remove(flaggerName.getSelectionModel().getSelectedItem().toString());
            			}
            			conditions.add(trueCondition.getText().toUpperCase(), 
            					flaggerName.getSelectionModel().getSelectedItem().toString(),
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
            					Double.parseDouble(value.getText()));
            			conditions.add(falseCondition.getText().toUpperCase(), 
            					flaggerName.getSelectionModel().getSelectedItem().toString(),
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
            					Double.parseDouble(value.getText()));
            			flaggerMap.add(flaggerName.getSelectionModel().getSelectedItem().toString(),
            					(String) flaggerSelector.getSelectionModel().getSelectedItem(),
            					trueCondition.getText().toUpperCase(), 
            					falseCondition.getText().toUpperCase(),
            					inequalitySelector.getSelectionModel().getSelectedItem().toString(), 
            					value.getText());
            			//conditions.printKeys();
            			populateFlaggerNameSelector();
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
			Double.parseDouble(value);
			//Integer.parseInt(value);
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
            		if(modes.getModes().containsKey(modeName.getSelectionModel().getSelectedItem().toString())){
        				//System.out.println(flaggerMap.getFlagMapping().get(flaggerName.getSelectionModel().getSelectedItem().toString()).getTrueCondition());
        				modes.remove(modeName.getSelectionModel().getSelectedItem().toString());
        			}
            		if(checkModes() == true){
            			RadioButton selectedRadioButton1 = (RadioButton) motorGroup1.getSelectedToggle();
                		String toogleGroupValue1 = selectedRadioButton1.getText();
                		
                		RadioButton selectedRadioButton2 = (RadioButton) motorGroup2.getSelectedToggle();
                		String toogleGroupValue2 = selectedRadioButton2.getText();
                		//System.out.println(toogleGroupValue2);
                		
                		//RadioButton selectedRadioButton3 = (RadioButton) startingMode.getSelectedToggle();
                		//String toogleGroupValue3 = selectedRadioButton3.getText();
                		
                		String isStart = "";
                		if(startMode.isSelected()){
                			isStart = "Starting Mode";
                		}
                		
    					modes.add(modeName.getSelectionModel().getSelectedItem().toString().toUpperCase(), 
    							motor1.getSelectionModel().getSelectedItem().toString(),
    							toogleGroupValue1,
    							motor2.getSelectionModel().getSelectedItem().toString(),
    							toogleGroupValue2,
    							isStart, (int) modeTableNumber.getValue());
    					previewCode();
    					clearAllMode();
    					populateModeTransition();
    					populateModeNameSelector();
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
		motor1.getSelectionModel().select(0);
		forwardMotor1.setSelected(true);
		forwardMotor2.setSelected(true);
		motor2.getSelectionModel().select(0);
		startMode.setSelected(false);
		
	}
	
	
	private void clearAllCondition() {
		//flaggerName.setText("");
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
	
	
	private void populateFlaggerNameSelector() {
		Collection<String> flags = flaggerMap.getFlagMapping().keySet();
		flaggerName.getItems().removeAll(flags);
		
		flaggerName.getSelectionModel().select(0);
		
        for(String flag: flags){
			flaggerName.getItems().add(flag);		
		}
	
	}
	
	private void populateModeNameSelector() {
		Collection<String> modesCollection = modes.getKeys();
		modeName.getItems().removeAll(modesCollection);
		
		modeName.getSelectionModel().select(0);
		
        for(String mode : modesCollection){
			modeName.getItems().add(mode);		
		}
	
	}

	private void populateConditionTransition() {
		transitionCondition1.getItems().removeAll(conditions.getKeys());
		//transitionCondition2.getItems().removeAll(conditions.getKeys());
		
		Set<String> conditionsTransition = conditions.getKeys();
		
		transitionCondition1.getItems().add("Condition");
		//transitionCondition2.getItems().add("Condition");
		
        for(String condition: conditionsTransition){
			transitionCondition1.getItems().add(condition);
			//transitionCondition2.getItems().add(condition);
		}
		transitionCondition1.getSelectionModel().select(0);
		//transitionCondition2.getSelectionModel().select(0);
	}
	
	private void populateModeTransition() {
		transitionMode1.getItems().removeAll(modes.getKeys());
		//transitionMode2.getItems().removeAll(modes.getKeys());
		
		Set<String> modesTransition = modes.getKeys();
		
		transitionMode1.getItems().add("Mode");
		//transitionMode2.getItems().add("Mode");
		
		for(String mode: modesTransition){
			transitionMode1.getItems().add(mode);
			//transitionMode2.getItems().add(mode);
		}
		transitionMode1.getSelectionModel().select(0);
		//transitionMode2.getSelectionModel().select(0);

	}
	
	
	private void addTransition1Handler(){
		addTransitionTable1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	try {
            		tempDataList = transitionTableViewer.getItems();
            		if(!(transitionCondition1.getSelectionModel().getSelectedItem().equals("Condition") || transitionMode1.getSelectionModel().getSelectedItem().equals("Mode"))){
            			if(tableNumber.getValue().equals(1)){
                			transitions1.add(
        							transitionCondition1.getSelectionModel().getSelectedItem().toString(),
        							transitionMode1.getSelectionModel().getSelectedItem().toString());
        					transitionCondition1.getSelectionModel().select(0);
        					transitionMode1.getSelectionModel().select(0);
        					for(int i = 0; i < tempDataList.size(); i++){
        						transitions1.replace(i,new ConditionModePair(tempDataList.get(i).getCondition(), tempDataList.get(i).getMode() ));
        					}
                		} else if(tableNumber.getValue().equals(2)){
                			transitions2.add(
        							transitionCondition1.getSelectionModel().getSelectedItem().toString(),
        							transitionMode1.getSelectionModel().getSelectedItem().toString());
        					transitionCondition1.getSelectionModel().select(0);
        					transitionMode1.getSelectionModel().select(0);
        					for(int i = 0; i < tempDataList.size(); i++){
        						transitions2.replace(i,new ConditionModePair(tempDataList.get(i).getCondition(), tempDataList.get(i).getMode() ));
        					}
                		} else if(tableNumber.getValue().equals(3)){
                			transitions3.add(
        							transitionCondition1.getSelectionModel().getSelectedItem().toString(),
        							transitionMode1.getSelectionModel().getSelectedItem().toString());
        					transitionCondition1.getSelectionModel().select(0);
        					transitionMode1.getSelectionModel().select(0);
        					for(int i = 0; i < tempDataList.size(); i++){
        						transitions3.replace(i,new ConditionModePair(tempDataList.get(i).getCondition(), tempDataList.get(i).getMode() ));
        					}
                		} else if(tableNumber.getValue().equals(4)){
                			transitions4.add(
        							transitionCondition1.getSelectionModel().getSelectedItem().toString(),
        							transitionMode1.getSelectionModel().getSelectedItem().toString());
        					transitionCondition1.getSelectionModel().select(0);
        					transitionMode1.getSelectionModel().select(0);
        					for(int i = 0; i < tempDataList.size(); i++){
        						transitions4.replace(i,new ConditionModePair(tempDataList.get(i).getCondition(), tempDataList.get(i).getMode() ));
        					}
                		} else if(tableNumber.getValue().equals(5)){
                			transitions5.add(
        							transitionCondition1.getSelectionModel().getSelectedItem().toString(),
        							transitionMode1.getSelectionModel().getSelectedItem().toString());
        					transitionCondition1.getSelectionModel().select(0);
        					transitionMode1.getSelectionModel().select(0);
        					for(int i = 0; i < tempDataList.size(); i++){
        						transitions5.replace(i,new ConditionModePair(tempDataList.get(i).getCondition(), tempDataList.get(i).getMode() ));
        					}
                		}
            		}
            		
					previewCode();
					//tempDataList = transitionTableViewer.getItems();
					//System.out.println("In transition handler" + tempDataList);
					for(int i = 0; i < tempDataList.size(); i++){
						transitions1.replace(i,new ConditionModePair(tempDataList.get(i).getCondition(), tempDataList.get(i).getMode() ));
					}
					
					System.out.println(transitions1.toString());
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
	
	private void previewCode(){
		try {
			if(codeView.getSelectedToggle().equals(sourceCode)){
				String className = programName.getText();
				if(className.equals("")){
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
						transitions3,
						transitions4,
						transitions5,
						conditions,
						flaggerMap,
						modes);
				codeOutput.setText(codeGenerator.generate().toString());
				theCode = codeOutput.getText();
			} else {
				String className = programName.getText();
				if(className.equals("")){
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
						transitions3,
						transitions4,
						transitions5,
						conditions,
						flaggerMap,
						modes);
				GenerateSimpleCode codeSimpleGenerator = new GenerateSimpleCode(
						className,
						transitions1,
						transitions2,
						transitions3,
						transitions4,
						transitions5,
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
	
	
	private boolean checkModes(){
		if(modes.getKeys().contains(modeName.getSelectionModel().getSelectedItem())){
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
