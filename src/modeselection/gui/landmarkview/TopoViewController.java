package modeselection.gui.landmarkview;

import java.io.File;
import java.io.FileNotFoundException;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import modeselection.cluster.ShrinkingImageBSOC;
import modeselection.gui.AdaptedYUYVRenderer;
import modeselection.util.Util;

public class TopoViewController {
	@FXML
	MenuItem open;
	@FXML
	MenuItem close;
	
	@FXML
	ChoiceBox<Integer> nodeNum;
	
	@FXML
	Button switchTo;
	
	@FXML
	Canvas image;
	
	@FXML
	TextField numSources;
	
	FileChooser chooser = new FileChooser();
	ShrinkingImageBSOC map;
	
	@FXML
	void initialize() {
		System.out.println("Initializing");
		nodeNum.selectionModelProperty().addListener((obs, oldVal, newVal) -> switchNode(newVal.getSelectedItem()));
		switchTo.setOnAction(event -> switchNode(nodeNum.getSelectionModel().getSelectedItem()));
	}
	
	@FXML
	void open() {
		File file = chooser.showOpenDialog(null);
		if (file != null) {
			try {
				map = Util.fileToObject(file, ShrinkingImageBSOC::new);
				nodeNum.getItems().clear();
				for (int node: map.getClusterIds()) {
					nodeNum.getItems().add(node);
				}
				nodeNum.getSelectionModel().select(0);
				switchNode(nodeNum.getValue());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	void switchNode(int updated) {
		System.out.println("Switching to " + updated);
		AdaptedYUYVRenderer.placeOnCanvas(map.getIdealInputFor(updated), image);
		numSources.setText(Integer.toString(map.getNumMergesFor(updated)));
	}
}
