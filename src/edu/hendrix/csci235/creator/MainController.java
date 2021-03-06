package edu.hendrix.csci235.creator;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;

import edu.hendrix.modeselection.Transitions;
import javafx.beans.value.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

// TODO: find list of java reserved words and keep users from naming their variables those words.

public class MainController {
	TreeMap<String, FlaggerInfo> conditions = new TreeMap<String, FlaggerInfo>();
	
	TreeMap<String, MotorInfo> modes =  new TreeMap<String, MotorInfo>();
	
	// There's some technical debt to be paid off here -- need to think more about it.
	//Transition[] transitions = new Transition[]();//{new Transition(), new Transition(), new Transition(), new Transition(), new Transition()};
	
	List<List<ConditionModePair>> transitions = new ArrayList<>();
	
	TreeMap<String, TrueFalse> flaggerMap = new TreeMap<String, TrueFalse>();
	
	RunCode codeRunner;
	
	@FXML
	MenuBar menuBar;
	
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
	Button addCondition, addMode, addTransitionTable1, addTransitionTable2, executeCode;
	
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
	ChoiceBox<String> transitionCondition;
	
	@FXML
	ChoiceBox<String> transitionMode;
	
	@FXML
	TextArea codeOutput;

	@FXML
	Label sensorPort, motor;
	
	@FXML
	Spinner<Integer> tableNumber;
	
	@FXML
	Spinner<Integer> modeTableNumber;
	
	@FXML
	TableView<TransitionsTableData> transitionTableViewer;
	
	@FXML
	TableColumn<TransitionsTableData,String> transitionTableCondition;
	
	@FXML
	TableColumn<TransitionsTableData,String> transitionTableMode;
	
	@FXML
	Button selectImage;
	
	@FXML
	ImageView selectedImage;
	
	@FXML 
	MenuItem open;
	
	@FXML
	MenuItem newProgram;
	
	@FXML
	MenuItem buildJar;

	Image img;
	File imgFile = new File("random.txt");
	
	int spinnerEndValue = 1;
	
	@FXML
	void openHandler() {
		FileChooser chooser = new FileChooser();
		File chosen = chooser.showOpenDialog(null);
		System.out.println(chosen.getAbsolutePath());
	}
	
	@FXML
	void buildJarHandler() {
		executeCodeHandler();
	}
	
	@FXML
	void newHandler(){
		conditions.clear();
		modes.clear();
		flaggerMap.clear();
		//transitions.add(new ConditionModePair());
		transitions.clear();
		System.out.println(conditions.keySet());
		theCode = "";
		codeOutput.setText("");
		programName.setText("ProgramName");
		modeName.getItems().removeAll(modes.keySet());
		flaggerName.getItems().removeAll(flaggerMap.keySet());
	}
	
	// open file chooser and write to a .txt file
	@FXML
	void save() throws FileNotFoundException{
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Save Program");
		chooser.setInitialFileName(programName + ".txt");
		File chosen = chooser.showSaveDialog(null);
		PrintWriter out = new PrintWriter(chosen.getAbsolutePath());
		out.println(codeOutput.getText());
		out.close();
	}
	
	
	final ToggleGroup motorGroup1 = new ToggleGroup();
	final ToggleGroup motorGroup2 = new ToggleGroup();
	final ToggleGroup sensorFlaggerInfo = new ToggleGroup();
	final ToggleGroup codeView = new ToggleGroup();

	String theCode = "";
	
	private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");
	
	@FXML
	public void initialize() {
		programName.setText("ProgramName");
		
		transitionTableCondition.setCellValueFactory(
			    new PropertyValueFactory<TransitionsTableData,String>("Condition"));
		transitionTableMode.setCellValueFactory(
			    new PropertyValueFactory<TransitionsTableData,String>("Mode"));
		
		flaggerName.getItems().add("Flagger Name");
		modeName.getItems().add("Mode Name");
		flaggerName.getSelectionModel().select("Flagger Name");
		modeName.getSelectionModel().select("Mode Name");
		
		setButtonGroups();
		
		populateInequalitySelector();
		populateFlaggerSelector();
		populateSensorPortselector();
		populateMotorSelectors();
		
		addConditionButtonHandler();
		addModeButtonHandler();
		addTransitionHandler();
		addCodeViewHandler();
		executeCodeHandler();
		
		 
		
		tableNumber.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,1));
		modeTableNumber.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,1));
		
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
	    } else if(flaggerSelector.getSelectionModel().getSelectedItem().equals("ImageMatching")){
	    	setFalseHelper();
	    	selectImage.setVisible(true);	    	
	    }
	    else{
	    	setFalseHelper();
	    }
	    });
		
		tableNumber.valueProperty().addListener((obs, oldValue, newValue) -> {
		transitionTableViewer.getItems().clear();
		});
		
		programName.textProperty().addListener((observable, oldValue, newValue) -> {
		    previewCode();
		});
		
		tableNumber.valueProperty().addListener((obs, oldValue, newValue) -> {
			int tableNum = tableNumber.getValueFactory().getValue() - 1;
			if (tableNum >= 0 && tableNum < transitions.size()) {
				if(transitions.get(tableNum).size() > 0){
					for(int i = 0; i < transitions.get(tableNum).size(); i++){
						transitionTableViewer.getItems().add(new TransitionsTableData(transitions.get(tableNum).get(i)));
					}
				}
			}
		});
		
		flaggerName.getSelectionModel().selectedItemProperty()
	    .addListener((obs, oldV, newV) -> {
	    	if(flaggerMap.containsKey(flaggerName.getSelectionModel().getSelectedItem().toString())){
	    		flaggerSelector.getSelectionModel().select(flaggerMap.get(flaggerName.getSelectionModel().
	    				getSelectedItem().toString()).getFlaggerType());
	    		trueCondition.setText(flaggerMap.get(flaggerName.getSelectionModel().
	    				getSelectedItem().toString()).getTrueCondition());
	    		falseCondition.setText(flaggerMap.get(flaggerName.getSelectionModel().
	    				getSelectedItem().toString()).getFalseCondition());
	    		inequalitySelector.getSelectionModel().select(flaggerMap.get(flaggerName.getSelectionModel().
	    				getSelectedItem().toString()).getInequality());
	    		value.setText(flaggerMap.get(flaggerName.getSelectionModel().
	    				getSelectedItem().toString()).getNumber());
	    	}
	    });
		
		modeName.getSelectionModel().selectedItemProperty()
	    .addListener((obs, oldV, newV) -> {
	    	if(modes.containsKey(modeName.getSelectionModel().getSelectedItem().toString())){
	    		if(modes.get(modeName.getSelectionModel().getSelectedItem().toString()).getStartingOrNot().equals("Starting Mode")){
	    			startMode.setSelected(true);
	    		}
	    		motor1.getSelectionModel().select(modes.get(modeName.getSelectionModel().getSelectedItem().toString()).getMotor1());
	    		motor2.getSelectionModel().select(modes.get(modeName.getSelectionModel().getSelectedItem().toString()).getMotor2());
	    		
	    		if(modes.get(modeName.getSelectionModel().getSelectedItem().toString()).getForwardOrBackward1().equals("Backward")){
	    			motorGroup1.selectToggle(backwardMotor1);
	    		} else if(modes.get(modeName.getSelectionModel().getSelectedItem().toString()).getForwardOrBackward1().equals("Forward")){
	    			motorGroup1.selectToggle(forwardMotor1);
	    		} else if(modes.get(modeName.getSelectionModel().getSelectedItem().toString()).getForwardOrBackward1().equals("Stop")){
	    			motorGroup1.selectToggle(stopMotor1);
	    		} 
	    		
	    		if(modes.get(modeName.getSelectionModel().getSelectedItem().toString()).getForwardOrBackward2().equals("Backward")){
	    			motorGroup2.selectToggle(backwardMotor2);
	    		} else if(modes.get(modeName.getSelectionModel().getSelectedItem().toString()).getForwardOrBackward2().equals("Forward")){
	    			motorGroup2.selectToggle(forwardMotor2);
	    		} else if(modes.get(modeName.getSelectionModel().getSelectedItem().toString()).getForwardOrBackward2().equals("Stop")){
	    			motorGroup2.selectToggle(stopMotor2);
	    		} 
	    		
	    		modeTableNumber.getValueFactory().setValue(modes.get(modeName.getSelectionModel().getSelectedItem()).getTransitionTableNumber());
	    		
	    	}
	   });
		
		// Resource: https://stackoverflow.com/questions/28603224/sort-tableview-with-drag-and-drop-rows
		// Thank you James_D!!!!! 
		transitionTableViewer.setRowFactory(tv -> {
            TableRow<TransitionsTableData> row = new TableRow<>();

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
                    TransitionsTableData draggedItem1 = transitionTableViewer.getItems().remove(draggedIndex);

                    int dropIndex ; 

                    if (row.isEmpty()) {
                        dropIndex = transitionTableViewer.getItems().size() ;
                    } else {
                        dropIndex = row.getIndex();
                    }

                    transitionTableViewer.getItems().add(dropIndex, draggedItem1);
                    transitions.get(tableNumber.getValue() - 1).clear();
                    Iterable<TransitionsTableData> data = transitionTableViewer.getItems();
            		for (TransitionsTableData dat: data) {
            			transitions.get(tableNumber.getValue() - 1).addAll((List<ConditionModePair>) dat.getPair());//add(dat.getPair());
            		}
                    
                    //.regenerate(transitionTableViewer.getItems());
                    event.setDropCompleted(true);
                    transitionTableViewer.getSelectionModel().select(dropIndex);
                    event.consume();
                    previewCode();
                }
            });        
            return row ;
        });
		
	}
	
	@FXML
	void selectImageHandler(){
		FileChooser chooser = new FileChooser();
		FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        chooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
		File chosen = chooser.showOpenDialog(null);
        try {
            BufferedImage bufferedImage = ImageIO.read(chosen);
            WritableImage image = SwingFXUtils.toFXImage(bufferedImage, null);
            imgFile = chosen;
            selectedImage.setImage(image);
            selectedImage.setVisible(true);
        } catch (IOException ex) {
            System.out.println("It didn't work.");
        }
		
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
    	selectImage.setVisible(false);
    	selectedImage.setVisible(false);
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
	
	private void throwErrorAlert(String msg){
		Alert alert = new Alert(AlertType.ERROR, msg, ButtonType.OK);
		alert.showAndWait();
	}
	
	private void addConditionButtonHandler(){
		addCondition.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	try {
            		if(trueCondition.getText().equals("") || falseCondition.getText().equals("")){
            			throwErrorAlert("Please enter a name for both conditions."); 
            		} else if( flaggerSelector.getSelectionModel().getSelectedItem().equals("Flagger")){
            			throwErrorAlert("Please select a flagger type.");
            		} else if(flaggerName.getSelectionModel().getSelectedItem().equals("Flagger Name")){
            			throwErrorAlert("Please enter a valid flagger Name.");
            		} else {
            			if(validateValue(value.getText())){
                			if(flaggerMap.containsKey(flaggerName.getSelectionModel().getSelectedItem().toString())){
                				conditions.remove(flaggerMap.get(flaggerName.getSelectionModel().getSelectedItem().toString()).getTrueCondition());
                				conditions.remove(flaggerMap.get(flaggerName.getSelectionModel().getSelectedItem().toString()).getFalseCondition());
                				flaggerMap.remove(flaggerName.getSelectionModel().getSelectedItem().toString());
                			}
                			putConditions();
                			populateFlaggerNameSelector();
                			flaggerMap.toString();
                			previewCode();
                			clearAllCondition();
                		} 
            		}
					populateConditionTransition();
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        });
	}
	
	private void putConditions() throws IOException{
		conditions.put(trueCondition.getText().toUpperCase(),
				new FlaggerInfo(
				flaggerName.getSelectionModel().getSelectedItem().toString(),
				flaggerSelector.getSelectionModel().getSelectedItem().toString(),
				sensorPortSelector.getSelectionModel().getSelectedItem().toString(),
				sensorFlaggerInfo.getSelectedToggle().toString(),
				motorSelector.getSelectionModel().getSelectedItem().toString(),
				true,
				inequalitySelector.getSelectionModel().getSelectedItem().toString(),
				Integer.parseInt(uLowText.getText()),
				Integer.parseInt(uHighText.getText()),
				Integer.parseInt(vLowText.getText()),
				Integer.parseInt(vHighText.getText()), 
				Double.parseDouble(value.getText()),
				imgFile.getName()));
		conditions.put(falseCondition.getText().toUpperCase(), new FlaggerInfo(
				flaggerName.getSelectionModel().getSelectedItem().toString(),
				flaggerSelector.getSelectionModel().getSelectedItem().toString(),
				sensorPortSelector.getSelectionModel().getSelectedItem().toString(),
				sensorFlaggerInfo.getSelectedToggle().toString(),
				motorSelector.getSelectionModel().getSelectedItem().toString(),
				false,
				inequalitySelector.getSelectionModel().getSelectedItem().toString(), 
				Integer.parseInt(uLowText.getText()),
				Integer.parseInt(uHighText.getText()),
				Integer.parseInt(vLowText.getText()),
				Integer.parseInt(vHighText.getText()),
				Double.parseDouble(value.getText()),
				imgFile.getName()));
		flaggerMap.put(flaggerName.getSelectionModel().getSelectedItem().toString(),
				new TrueFalse(
				(String) flaggerSelector.getSelectionModel().getSelectedItem(),
				trueCondition.getText().toUpperCase(), 
				falseCondition.getText().toUpperCase(),
				inequalitySelector.getSelectionModel().getSelectedItem().toString(), 
				value.getText(), 
				imgFile.getName()));
	}
	
	private boolean validateValue(String value){
		try{
			Double.parseDouble(value);
			return true;
		} catch(NumberFormatException nfe){
			throwErrorAlert("Please enter a valid number in the value text box.");
			return false;
		}
	}
	
	private void addModeButtonHandler(){
		addMode.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	try {
            		if(modeName.getSelectionModel().getSelectedItem().equals("Mode Name")){
            			throwErrorAlert("Please enter a mode name.");
            		} else {
            			if(modes.isEmpty()){
            				startMode.setSelected(true);
            			}
            			if(modes.containsKey(modeName.getSelectionModel().getSelectedItem().toString())){
            				modes.remove(modeName.getSelectionModel().getSelectedItem().toString());
            			}
            			if(checkModes() == true){
            				RadioButton selectedRadioButton1 = (RadioButton) motorGroup1.getSelectedToggle();
            				String toogleGroupValue1 = selectedRadioButton1.getText();

            				RadioButton selectedRadioButton2 = (RadioButton) motorGroup2.getSelectedToggle();
            				String toogleGroupValue2 = selectedRadioButton2.getText();

            				String isStart = getStartMode();
            				modes.put(modeName.getSelectionModel().getSelectedItem().toString().toUpperCase(), 
            						new MotorInfo(motor1.getSelectionModel().getSelectedItem().toString(),
            						toogleGroupValue1,
            						motor2.getSelectionModel().getSelectedItem().toString(),
            						toogleGroupValue2,
            						isStart, (int) modeTableNumber.getValue()));
            				previewCode();
            				clearAllMode();
            				populateModeTransition();
            				populateModeNameSelector();
            			}
            		}
            		
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
            }
        });
	}	
	
	private String getStartMode(){
		String isStart = "";
		if(startMode.isSelected()){
			isStart = "Starting Mode";
		}
		
		return isStart;
	}
	
	private void clearAllMode() {
		forwardMotor1.setSelected(true);
		forwardMotor2.setSelected(true);
		startMode.setSelected(false);
	}
	
	private void clearAllCondition() {
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
				"Sensor", "ColorCount", "ImageMatching"));
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
		Collection<String> flags = flaggerMap.keySet();
		flaggerName.getItems().removeAll(flags);
		
		flaggerName.getSelectionModel().select(0);
		
        for(String flag: flags){
			flaggerName.getItems().add(flag);		
		}
	}
	
	private void populateModeNameSelector() {
		Collection<String> modesCollection = modes.keySet();
		modeName.getItems().removeAll(modesCollection);
		
		modeName.getSelectionModel().select(0);
		
        for(String mode : modesCollection){
			modeName.getItems().add(mode);		
		}
	
	}

	private void populateConditionTransition() {
		transitionCondition.getItems().removeAll(conditions.keySet());
		
		Set<String> conditionsTransition = conditions.keySet();
		
		transitionCondition.getItems().add("Condition");
		
        for(String condition: conditionsTransition){
			transitionCondition.getItems().add(condition);
		}
		transitionCondition.getSelectionModel().select(0);
	}
	
	private void populateModeTransition() {
		transitionMode.getItems().removeAll(modes.keySet());
		
		Set<String> modesTransition = modes.keySet();
		
		transitionMode.getItems().add("Mode");
		
		for(String mode: modesTransition){
			transitionMode.getItems().add(mode);
		}
		transitionMode.getSelectionModel().select(0);

	}
	
	private void addTransitionHandler(){
		addTransitionTable1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	try {
            		if(!transitionCondition.getSelectionModel().getSelectedItem().equals("Condition") && !transitionMode.getSelectionModel().getSelectedItem().equals("Mode")){
            			int tableNum = tableNumber.getValue() - 1;
            			if (tableNum < transitions.size() && transitions.size() > 0) {
            				if(transitions.get(tableNum).contains(transitionCondition.getSelectionModel().getSelectedItem())){
            					int num = transitions.get(tableNum).indexOf(transitionCondition.getSelectionModel().getSelectedItem());
            					transitions.get(tableNum).set(num, new ConditionModePair(transitionCondition.getSelectionModel().getSelectedItem().toString(),
        							transitionMode.getSelectionModel().getSelectedItem().toString()));
            					transitionTableViewer.getItems().clear();
            					for (ConditionModePair cmp: transitions.get(tableNum)) {
            						transitionTableViewer.getItems().add(new TransitionsTableData(cmp));
            					}
            				} else{
            					transitions.get(tableNum).add(new ConditionModePair(transitionCondition.getSelectionModel().getSelectedItem(),
            							transitionMode.getSelectionModel().getSelectedItem()));
            					transitionCondition.getSelectionModel().select(0);
                				transitionMode.getSelectionModel().select(0);
                				transitionTableViewer.getItems().clear();
                				for (ConditionModePair cmp: transitions.get(tableNum)) {
                					transitionTableViewer.getItems().add(new TransitionsTableData(cmp));
                				}
            				}
            			}
            			else {
            				transitions.add(new ArrayList<ConditionModePair>());
            				transitions.get(transitions.size() - 1).add(new ConditionModePair(transitionCondition.getSelectionModel().getSelectedItem(),
            						transitionMode.getSelectionModel().getSelectedItem()));
            				transitionCondition.getSelectionModel().select(0);
            				transitionMode.getSelectionModel().select(0);
            				transitionTableViewer.getItems().clear();
            				for (ConditionModePair cmp: transitions.get(tableNum)) {
            					transitionTableViewer.getItems().add(new TransitionsTableData(cmp));
            				}
            				tableNumber.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,spinnerEndValue + 1));
            				modeTableNumber.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,spinnerEndValue + 1));
            				spinnerEndValue += 1;
            			}
            		}
            		
					previewCode();
				} catch (NumberFormatException e) {
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
						transitions,
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
						transitions,
						conditions,
						flaggerMap,
						modes);
				GenerateSimpleCode codeSimpleGenerator = new GenerateSimpleCode(
						className,
						transitions,
						conditions,
						flaggerMap,
						modes);
				theCode = codeSourceGenerator.generate().toString();
				codeOutput.setText(codeSimpleGenerator.toString());
			}
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}
		
	private boolean checkModes(){
		if(modes.keySet().contains(modeName.getSelectionModel().getSelectedItem())){
			throwErrorAlert("That mode already exists - No two modes should have the same name.\nPlease try again.");
			return false;

		} else {
			return true;
		}
	}

	private void executeCodeHandler(){
		buildJar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	try {
            		codeRunner = new RunCode(programName.getText(), theCode);
            		codeRunner.run();
            		if(codeRunner.isJarExist(programName.getText() + ".jar") == false){
            			throwErrorAlert("There was an error creating your JAR file.");
            		}
					
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
            }
        });
	}


}
