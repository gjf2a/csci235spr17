package modeselection.visualizer;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ModeViewController {
	@FXML
	void initialize() {
		
	}
	
	@FXML
	VBox conditions;
	
	@FXML
	TextField currentMode;
	
	@FXML
	ListView<String> reachableModes;
	
	@FXML
	ListView<String> unreachableModes;
	
	@FXML
	ListView<String> unusedConditions;
}
