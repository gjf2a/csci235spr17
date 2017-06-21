package edu.hendrix.csci235.creator;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

//import javafx.stage.FileChooser;

public class RunCode {
	private String programName, code;
	//FileChooser chooser = new FileChooser();
	
	public String pathInfo;
	public ArrayList<String> pathInfoArray = new ArrayList<String>();
	public ArrayList<String> pathInfoArrayFinal = new ArrayList<String>();
	
	public RunCode(String programName, String code){
		this.programName = programName;
		this.code = code;
	}
	
	// Saves the program in a specified location. Allows the user to select location.
	// This is a bit tricky. The save file needs to be saved in sentence case.
	public File writeToFile() throws FileNotFoundException, UnsupportedEncodingException {
		//chooser.setTitle("Save Program");
		String programNameCaps = programName.substring(0, 1).toUpperCase();
		String temp = programName.substring(1);
		programNameCaps = programNameCaps + temp;
		//chooser.setInitialFileName(programNameCaps + ".java");
		//File chosen = chooser.showSaveDialog(null);
		File chosen = new File("c:\\Users\\ferrer\\Desktop\\GitHub\\csci235spr17\\bin\\" + programNameCaps + ".java");
		PrintWriter out = new PrintWriter(chosen.getAbsolutePath());
		out.println(code);
		out.close();
		String pathName = returnPath(chosen.getPath());
		pathInfo = pathName;
		//String programNameAddJava = programName + ".java";
		File chosenPath = new File(pathName);
		return chosenPath;
	}
	
	// Our path must not include the file. We only need to know the directory in which we are working.
	// This method chops the file name off of the directory. 
	public String returnPath(String pathName){
		String programNameAddJava = programName + ".java";
		pathName = pathName.substring(0, ((pathName.length()-1) - (programNameAddJava.length()-1)));
		return pathName;
	}
	
	
	// Creates a manifest that must be added to the .jar file.
	// A manifest basically tells the .jar file how to run.
	public void createManifest() throws FileNotFoundException{
		// Come back and change these for loops!
		for(int i = 0; i < pathInfo.length(); i++){
			pathInfoArray.add(pathInfo.substring(i, i + 1));
		}
		
		for(String letter : pathInfoArray){
			if(letter.equals("\\")){
				pathInfoArrayFinal.add("/");
				//pathInfoArrayFinal.add("\\");
			} else {
				pathInfoArrayFinal.add(letter);
			}
		}
		
		
		String pathFinal = "";
		for(String letter : pathInfoArrayFinal){
			pathFinal = pathFinal + letter;
		}
		
		/////////////////////////////////////////
		String whereToStore = pathFinal + programName + ".mf";
		
		File manifesto = new File(whereToStore);
		
		PrintWriter out = new PrintWriter(manifesto.getAbsolutePath());
		out.println("Manifest-Version: 1.0");
		out.println("Class-Path: /home/root/lejos/lib/ev3classes.jar /home/root/lejos/lib/opencv-2411.jar /home/root/lejos/lib/dbusjava.jar /home/root/lejos/libjna/usr/share/java/jna.jar");
		out.println("Main-Class: " + programName);
		out.close();
	}
	
	
	/*private static void runProcess(String command) throws Exception {
		Process p = Runtime.getRuntime().exec(command);
		printLines(command + " stdout:", p.getInputStream());
		printLines(command + " stderr:", p.getErrorStream());
		p.waitFor();
		System.out.println(command + " exitValue() " + p.exitValue());
	}*/
	
	/*private static void printLines(String name, InputStream ins) throws Exception {
	        String line = null;
	        BufferedReader in = new BufferedReader(
	                new InputStreamReader(ins));
	        while ((line = in.readLine()) != null) {
	            System.out.println(name + " " + line);
	        }
	 }*/
	
	// Makes calls to the command line that will first compile the program and separate it into its classes.
	// It will then generate a manifest and add everything to a .jar file.
	public void run(){
		   try {
			   String path = "c:\\Program Files\\Java\\jdk1.8.0_131\\bin\\";
			   File fileDir = writeToFile();
			   Process pro = Runtime.getRuntime().exec(path + "javac -cp \".;c:\\Program Files\\leJOS EV3\\lib\\ev3\\ev3classes.jar;c:\\Users\\ferrer\\Desktop\\GitHub\\csci235spr17\\bin\" " + programName + ".java",
					   null, fileDir);
			   waitAndPrint(pro);
			   System.out.println("Done compiling");
			   
			   makeJar();
			   
			   //createManifest();
			   /*
			   // Curses, foiled again!
			   System.out.println("Starting jar...");
			   String jarCmd = path + "jar -cvfm " + programName + ".jar " + programName + ".mf " + programName + ".class " + programName + "$Condition.class " + programName + "$Mode.class edu\\hendrix\\modeselection\\*";
			   System.out.println("jarCmd: " + jarCmd);
			   Process jarPro = Runtime.getRuntime().exec(jarCmd, null, fileDir);
			   //waitAndPrint(jarPro);
			   System.out.println("Jar should be ready");
			   // TODO: when I try to make .jar file eveything goes crazy!!! HELp!!!
			   */
			   
			  // Process pro2 = Runtime.getRuntime().exec("jar cvf " + programName + ".jar " +  programName + ".mf ", null, theFile);
			   /*Runtime.getRuntime().exec("jar uf " + programName + ".jar " +  programName + ".class");
			   Runtime.getRuntime().exec("jar uf " + programName + ".jar " +  programName + "$Condition.class");
			   Runtime.getRuntime().exec("jar uf " + programName + ".jar " +  programName + "$Mode.class");*/		
			   
			   //Process pro3 = Runtime.getRuntime().exec("jar uf " + programName + ".jar " +  programName + ".class", null, theFile);
			   //Process pro4 = Runtime.getRuntime().exec("jar uf " + programName + ".jar " +  programName + "$Condition.class", null, theFile);
			   //Process pro5 = Runtime.getRuntime().exec("jar uf " + programName + ".jar " +  programName + "$Mode.class", null, theFile);
			  

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	}
	
	void waitAndPrint(Process pro) throws InterruptedException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(pro.getErrorStream()));
		reader.lines().forEach(s -> System.out.println(s));
		System.out.println(pro.waitFor());
	}
	
	// From Stack Overflow:
	// https://stackoverflow.com/questions/1281229/how-to-use-jaroutputstream-to-create-a-jar-file
	private void makeJar() throws FileNotFoundException, IOException {
		Manifest manifest = new Manifest();
		manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
		manifest.getMainAttributes().put(Attributes.Name.CLASS_PATH, "/home/root/lejos/lib/ev3classes.jar /home/root/lejos/lib/opencv-2411.jar /home/root/lejos/lib/dbusjava.jar /home/root/lejos/libjna/usr/share/java/jna.jar");
		manifest.getMainAttributes().put(Attributes.Name.MAIN_CLASS, programName);
		JarOutputStream target = new JarOutputStream(new FileOutputStream("output.jar"), manifest);
		add(new File(programName + ".class"), target);
		add(new File(programName + "$Condition.class "), target);
		add(new File(programName + "$Mode.class "), target);
		add(new File("edu" + File.separatorChar + "hendrix" + File.separatorChar + "modeselection"), target);
		target.close();
	}

	private static void add(File source, JarOutputStream target) throws IOException
	{
	  BufferedInputStream in = null;
	  try
	  {
	    if (source.isDirectory())
	    {
	      String name = source.getPath().replace("\\", "/");
	      if (!name.isEmpty())
	      {
	        if (!name.endsWith("/"))
	          name += "/";
	        JarEntry entry = new JarEntry(name);
	        entry.setTime(source.lastModified());
	        target.putNextEntry(entry);
	        target.closeEntry();
	      }
	      for (File nestedFile: source.listFiles())
	        add(nestedFile, target);
	      return;
	    }

	    JarEntry entry = new JarEntry(source.getPath().replace("\\", "/"));
	    entry.setTime(source.lastModified());
	    target.putNextEntry(entry);
	    in = new BufferedInputStream(new FileInputStream(source));

	    byte[] buffer = new byte[1024];
	    while (true)
	    {
	      int count = in.read(buffer);
	      if (count == -1)
	        break;
	      target.write(buffer, 0, count);
	    }
	    target.closeEntry();
	  }
	  finally
	  {
	    if (in != null)
	      in.close();
	  }
	}
}
