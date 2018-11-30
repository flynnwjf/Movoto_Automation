package com.movoto.utils.appium;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import io.appium.java_client.android.AndroidDriver;

public class AppiumAndroidManager {
	protected AndroidDriver driver;
	protected int numOfDevices;
	protected String deviceId;
	protected String deviceName;
	protected String osVersion;
	protected String port;
	protected int deviceCount;

	AppiumManager appiumMgr = new AppiumManager();
	static Map<String, String> devices = new HashMap<String, String>();
	static DeviceConfiguration deviceConf = new DeviceConfiguration();

	public AppiumAndroidManager() {
		try {
			devices = deviceConf.getDivces();
			deviceCount = devices.size() / 3;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public AppiumAndroidManager(int i) {
		int deviceNumber = (i + 1);
		this.deviceId = devices.get("deviceID" + deviceNumber);
		this.deviceName = devices.get("deviceName" + deviceNumber);
		this.osVersion = devices.get("osVersion" + deviceNumber);
	}

	public AndroidDriver createDriver() {
		try {
			DesiredCapabilities capabilities = DesiredCapabilities.android();
			capabilities.setCapability("deviceName", deviceName);
			capabilities.setCapability("platformName", "android");
			capabilities.setCapability(CapabilityType.VERSION, osVersion);
			capabilities.setCapability(CapabilityType.BROWSER_NAME, "chrome");
			capabilities.setCapability("udid", deviceId);
			return new AndroidDriver(new URL("http://127.0.0.1:" + port + "/wd/hub"), capabilities);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public AndroidDriver createDriver(String appPath) {
		try {
			try {
				Thread.sleep(5000);
			} catch (Exception e) {
				// TODO: handle exception
			}
			File app = new File(appPath);
			if (app.exists()) {
				DesiredCapabilities capabilities = DesiredCapabilities.android();
				capabilities.setCapability("deviceName", deviceName);
				capabilities.setCapability("platformName", "Android");
				capabilities.setCapability("platformVersion", osVersion);
				capabilities.setCapability("app", app.getAbsolutePath());
				capabilities.setCapability("udid", deviceId);
				AndroidDriver<WebElement> driver = new  AndroidDriver<WebElement>(new URL("http://127.0.0.1:" + port + "/wd/hub"), capabilities);
				System.out.println(driver);
				return driver;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void destroyDriver() {
		driver.quit();
		try {
			deviceConf.stopADB();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getDeviceCount() {
		return deviceCount;
	}

	public static Map<String, String> getDevices() {
		return devices;
	}

	public static void main(String[] args) {
		AppiumAndroidManager manager = new AppiumAndroidManager();
		int count = manager.getDeviceCount();
		Map<String, String> devices = manager.getDevices();
		for (int i = 0; i < count; i++) {
			AppiumAndroidManager manager2 = new AppiumAndroidManager(i);
			AndroidDriver driver = manager2.createDriver("app/salesforce.apk");
			if (driver != null) {
				System.out.println(driver.currentActivity());
			}
		}
	}
	
}
