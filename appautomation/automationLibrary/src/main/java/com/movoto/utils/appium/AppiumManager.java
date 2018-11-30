package com.movoto.utils.appium;

import com.movoto.data.TestDTO;

/**
 * Appium Manager - This class contains method to start and stops Appium server  
 */

public class AppiumManager {

	CommandPrompt cp = new CommandPrompt();
	AvailabelPorts ap = new AvailabelPorts();
	
	/**
	 * Method 1. Start Appium with default arguments
	 */
	
	public void startDefaultAppium() throws Exception
	{
		cp.runCommand("appium --session-override");
		Thread.sleep(5000);
	}
	
	/**
	 * Method 2. Start Appium with auto generated ports : appium port, chrome port, and bootstap port
	 */
	
	public String startAppium(TestDTO dto) throws Exception
	{
		String port = ap.getPort();		
		String params = " --full-reset --session-override";
		
		if(dto.getTestProperties().isFullResetDisabled()){
			params = " --no-reset --session-override";
		}
		
		String command = "node /Applications/Appium.app/Contents/Resources/node_modules/appium/lib/server/main.js --address 127.0.0.1 --port " + port + params;
		System.out.println(command);
		
		String output = cp.runCommand(command);
		System.out.println(output);
		
		if(output.contains("not"))
		{
			System.setProperty("PATH", "");
			System.out.println("\nAppium is not installed");
			System.exit(0);
		}
		return port;
	}
	
	/**
	 * Method 3. Start Appium with modified arguments : appium port, chrome port, and bootstap port as user pass port number
	 * @param appium port
	 * @param chrome port
	 * @param bootstrap port
	 */
	
	public void startAppium(String port, String chromePort, String bootstrapPort) throws Exception
	{
		String command = "appium --session-override -p " + port + " --chromedriver-port " + chromePort + " -bp " + bootstrapPort;
		System.out.println(command);
		
		String output = cp.runCommand(command);
		System.out.println(output);
	}
	

}
