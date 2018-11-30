package com.movoto.utils.appium;

import java.util.HashMap;
import java.util.Map;

/**
 * DeviceConfiguration - This class contains methods to start adb server to get connected devices and their information.   
 */

public class DeviceConfiguration {

	CommandPrompt cmd = new CommandPrompt();
	Map<String, String> devices = new HashMap<String, String>();
	
	/**
	 * Start ADB Server
	 */
	
	public void startADB() throws Exception {
		String output = cmd.runCommand("adb start-server");
		String[] lines = output.split("\n");
		if(lines.length == 1)
			System.out.println("adb service already started");
		else if(lines[1].equalsIgnoreCase("* daemon started successfully *"))
			System.out.println("adb service started");
		else if(lines[0].contains("internal or external command")){
			System.out.println("adb path not set in system varibale");
			System.exit(0);
		}
	}
	
	/**
	 * Stop ADB Server
	 */
	
	public void stopADB() throws Exception {
		cmd.runCommand("adb kill-server");
	}
	
	/**
	 * Return connected devices
	 * @return hashmap of connected devices information
	 */
	
	public Map<String, String> getDivces() throws Exception	{
		
		startADB();
		String output = cmd.runCommand("adb devices");
		String[] lines = output.split("\n");

		//No Device Connected
		if(lines.length <= 1){
			System.out.println("No Device Connected");
			stopADB();
			System.exit(0);
		}
		
		for(int i = 1; i < lines.length; i++){
			lines[i] = lines[i].replaceAll("\\s+", "");
			
			//Condition 1: Contains "device"
			if(lines[i].contains("device")){
				lines[i] = lines[i].replaceAll("device", "");
				
				String deviceID = lines[i];
				String model = cmd.runCommand("adb -s " + deviceID + " shell getprop ro.product.model").replaceAll("\\s+", "");
				String brand = cmd.runCommand("adb -s " + deviceID + " shell getprop ro.product.brand").replaceAll("\\s+", "");
				String osVersion = cmd.runCommand("adb -s " + deviceID + " shell getprop ro.build.version.release").replaceAll("\\s+", "");
				String deviceName = brand + " " + model;
				
				devices.put("deviceID" + i, deviceID);
				devices.put("deviceName" + i, deviceName);
				devices.put("osVersion" + i, osVersion);
				
				System.out.println("Following device is connected");
				System.out.println(deviceID+" "+deviceName+" "+osVersion+"\n");			
			}
			//Condition 2: Contains "unauthorized"
			else if(lines[i].contains("unauthorized")){
				lines[i] = lines[i].replaceAll("unauthorized", "");
				String deviceID = lines[i];		
				
				System.out.println("Following device is unauthorized");
				System.out.println(deviceID + "\n");
			}
			//Condition 3: Contains "offline"
			else if(lines[i].contains("offline")){
				lines[i] = lines[i].replaceAll("offline", "");
				String deviceID = lines[i];	
				
				System.out.println("Following device is offline");
				System.out.println(deviceID+"\n");
			}
		}
		return devices;
	}
	

}
