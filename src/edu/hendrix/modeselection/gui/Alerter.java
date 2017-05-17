package edu.hendrix.modeselection.gui;

import javafx.scene.control.Alert;

public class Alerter {
	public static void errorBox(String errorMsg) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setContentText(errorMsg);
		alert.show();
	}
	
	public static void infoBox(String infoMsg) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setContentText(infoMsg);
		alert.show();
	}
}
