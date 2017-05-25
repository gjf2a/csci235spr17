package edu.hendrix.csci235.creator;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class GUIMain extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(GUIMain.class.getResource("Main.fxml"));
		AnchorPane root = (AnchorPane) loader.load();

		Scene scene = new Scene(root, 279, 341);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
