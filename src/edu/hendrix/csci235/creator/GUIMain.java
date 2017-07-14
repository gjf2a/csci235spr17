package edu.hendrix.csci235.creator;


import java.awt.ScrollPane;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class GUIMain extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception{
		FXMLLoader loader = new FXMLLoader();
		//loader.setLocation(GUIMain.class.getResource("Main.fxml"));
		//ScrollPane root = (ScrollPane) loader.load();
		
		Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
	    Scene scene = new Scene(root);

		//Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
