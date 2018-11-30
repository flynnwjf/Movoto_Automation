package com.movoto.fixtures.impl.wd;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DeviceManager {
	private static Set<String> deviceList = new HashSet<>();
	
	public static synchronized boolean addDevice(String device){
		return deviceList.add(device);
	}
	
	public static synchronized boolean isDeviceInUse(String device){
		return deviceList.contains(device);
	}
	
	public static synchronized boolean removeDevice(String device){
		return deviceList.remove(device);
	}
}
