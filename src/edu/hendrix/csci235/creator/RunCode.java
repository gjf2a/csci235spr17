package edu.hendrix.csci235.creator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

public class RunCode {
	private String programName, code;
	FileChooser chooser = new FileChooser();
	
	public RunCode(String programName, String code){
		this.programName = programName;
		this.code = code;
	}
	
	public void writeToFile() throws FileNotFoundException, UnsupportedEncodingException {
		chooser.setTitle("Save Program");
		chooser.setInitialFileName(programName + ".java");
		File chosen = chooser.showSaveDialog(null);
		PrintWriter out = new PrintWriter(chosen.getAbsolutePath());
		out.println(code);
		out.close();
	}
	
	
	private static void runProcess(String command) throws Exception {
		Process pro = Runtime.getRuntime().exec(command);
		printLines(command + " stdout:", pro.getInputStream());
		printLines(command + " stderr:", pro.getErrorStream());
		pro.waitFor();
		System.out.println(command + " exitValue() " + pro.exitValue());
	}
	
	private static void printLines(String name, InputStream ins) throws Exception {
	        String line = null;
	        BufferedReader in = new BufferedReader(
	                new InputStreamReader(ins));
	        while ((line = in.readLine()) != null) {
	            System.out.println(name + " " + line);
	        }
	 }
	
	public void run(){
		   try {
			   //System.out.println(programName + "\n" + code);
	           //runProcess("javac " + programName + ".java");
			   runProcess("javac -cp \".;c:\\Program Files\\leJOS EV3\\lib\\ev3\\ev3classes.jar\"" + programName + ".java");
	           runProcess("java " + programName);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	}

}
