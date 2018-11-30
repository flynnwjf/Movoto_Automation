package com.movoto.utils.appium;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Command Prompt - This class contains method to run Windows and Mac commands  
 */

public class CommandPrompt {
	
	Process p;
	ProcessBuilder builder;
	
	/**
	 * This method run command on Windows and Mac
	 * @param command to run  
	 */
	
	public String runCommand(String command) throws InterruptedException, IOException
	{
		//command = command.replace("adb", "/Users/automationng/Library/Android/sdk/platform-tools/adb");
		//command = command.replace("appium", "/usr/local/bin/appium");
		//command = command.replace("node", "/usr/local/bin/node");
		String os = System.getProperty("os.name");
		System.out.println(os);
		
		//Build CMD Process
		if(os.contains("Windows")) //Windows
		{
			builder = new ProcessBuilder("cmd.exe","/c", command);
			builder.redirectErrorStream(true);
			Thread.sleep(1000);
			p = builder.start();
		}
		else //Mac
			p = Runtime.getRuntime().exec(command);
		
		//Get standard output
		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		//Returns the input stream connected to the normal output of the subprocess. 
		//The stream obtains data piped from the standard output of the process represented by this Process object. 
		String line = "";
		String allLine = "";
		int i = 1;
		while((line = r.readLine()) != null){
			allLine = allLine + "" + line + "\n";
			if(line.contains("Console LogLevel: debug"))
				break;
			i++;
		}
		return allLine;
	}
	
}
