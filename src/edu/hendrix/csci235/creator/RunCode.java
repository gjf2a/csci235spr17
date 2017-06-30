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
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import javafx.stage.FileChooser;

//import javafx.stage.FileChooser;

public class RunCode {
	private String programName, code;
	//FileChooser chooser = new FileChooser();
	
	public String pathInfo;
	public ArrayList<String> pathInfoArray = new ArrayList<String>();
	public ArrayList<String> pathInfoArrayFinal = new ArrayList<String>();
	
	// Read to set up for Eclipse on Mac: https://stackoverflow.com/questions/7501678/set-environment-variables-on-mac-os-x-lion
	public String EV3_HOME = "c:\\Program Files\\leJOS EV3\\";
	public String MODE_SELECTION_CLASSES = "c:\\Users\\ferrer\\Desktop\\GitHub\\csci235spr17\\bin";
	public String JAVA_COMPILER = "c:\\Program Files\\Java\\jdk1.8.0_131\\bin\\javac";
	
	public RunCode(String programName, String code){
		this.programName = programName.replaceAll(" ", "_");
		this.code = code;
		
		String ev3Home = System.getenv("EV3_HOME");
		if (ev3Home != null) {EV3_HOME = ev3Home;}
		System.out.println("ev3home: " + ev3Home);
		
		String modeSelection = System.getenv("MODE_SELECTION_CLASSES");
		if (modeSelection != null) {MODE_SELECTION_CLASSES = modeSelection;}
		System.out.println("modeselection: " + modeSelection);
		
		String javaHome = System.getenv("JAVA_COMPILER");
		if (javaHome != null) {JAVA_COMPILER = javaHome;}
		System.out.println("java: " + javaHome);
		
		//System.out.println(programName);
	}
	
	
	// Saves the program in a specified location. Allows the user to select location.
	// This is a bit tricky. The save file needs to be saved in sentence case.
	public File writeToFile() throws FileNotFoundException, UnsupportedEncodingException {
		FileChooser save = new FileChooser();
		save.setTitle("Save Program");
		String programNameCaps = programName.substring(0, 1).toUpperCase();
		String temp = programName.substring(1);
		programNameCaps = programNameCaps + temp;
		save.setInitialFileName(programNameCaps + ".java");
		File chosen = save.showSaveDialog(null);
		File chosen2 = new File(chosen.getPath());
		//System.out.println(chosen2.toString());
		PrintWriter out = new PrintWriter(chosen2.getAbsolutePath());
		out.println(code);
		out.close();
		String pathName = returnPath(chosen2.getPath());
		pathInfo = pathName;
		//String programNameAddJava = programName + ".java";
		File chosenPath = new File(pathName);
		//System.out.println(pathName);
		return chosenPath;
	}
	
	// Our path must not include the file. We only need to know the directory in which we are working.
	// This method chops the file name off of the directory. 
	public String returnPath(String pathName){
		String programNameAddJava = programName + ".java";
		pathName = pathName.substring(0, ((pathName.length()-1) - (programNameAddJava.length()-1)));
		//pathName = pathName + File.separatorChar;
		return pathName;
	}
	
	// Makes calls to the command line that will first compile the program and separate it into its classes.
	// It will then generate a manifest and add everything to a .jar file.
	public void run(){
		   try {
			   File fileDir = writeToFile();
			   //System.out.println(fileDir.getAbsolutePath().toString());
			   Process pro = Runtime.getRuntime().exec(JAVA_COMPILER + " -cp \"." + File.pathSeparatorChar + EV3_HOME + "lib" + File.separatorChar + "ev3" + File.separatorChar + "ev3classes.jar" + File.pathSeparatorChar + MODE_SELECTION_CLASSES + " " + programName + ".java",
					   null, fileDir);
			   waitAndPrint(pro);
			   System.out.println("Done compiling");
			   
			   makeJar();
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
		printWhere();
		
		Manifest manifest = new Manifest();
		manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
		manifest.getMainAttributes().put(Attributes.Name.CLASS_PATH, "/home/root/lejos/lib/ev3classes.jar /home/root/lejos/lib/opencv-2411.jar /home/root/lejos/lib/dbusjava.jar /home/root/lejos/libjna/usr/share/java/jna.jar");
		manifest.getMainAttributes().put(Attributes.Name.MAIN_CLASS, programName);
		JarOutputStream target = new JarOutputStream(new FileOutputStream(programName + ".jar"), manifest);
		add(new File("bin" + File.separatorChar + programName + ".class"), target);
		add(new File("bin" + File.separatorChar + programName + "$Condition.class"), target);
		add(new File("bin" + File.separatorChar + programName + "$Mode.class"), target);
		add(new File("bin" + File.separatorChar + "edu" + File.separatorChar + "hendrix" + File.separatorChar + "modeselection"), target);
		target.close();
		
		
	}
	
	public static boolean isJarExist(String jarName)
    {	
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        if (classLoader instanceof URLClassLoader)
        {
            URLClassLoader classLoader2 = (URLClassLoader) classLoader;
            URL [] urls = classLoader2.getURLs();
            for (URL url : urls)
            {
                File file = new File(url.getFile());
                if (file.getPath().endsWith(jarName))
                {
                    System.out.println(jarName + " exist");
                    return true;
                }
            }
            System.out.println(jarName + " not exist");
            return false;
        }
		return false;
    }
	
	private static void printWhere() {
		File f = new File(".");
		System.out.println("pwd:" + f.getAbsolutePath());
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
	        JarEntry entry = new JarEntry(name.replace("bin/", ""));
	        entry.setTime(source.lastModified());
	        target.putNextEntry(entry);
	        target.closeEntry();
	      }
	      for (File nestedFile: source.listFiles())
	        add(nestedFile, target);
	      return;
	    }

	    System.out.println("source: " + source.getPath());
	    JarEntry entry = new JarEntry(source.getPath().replace("\\", "/").replace("bin/", ""));
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
