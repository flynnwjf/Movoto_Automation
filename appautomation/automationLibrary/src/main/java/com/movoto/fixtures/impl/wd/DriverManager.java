package com.movoto.fixtures.impl.wd;

import java.io.File;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import com.movoto.data.TestDTO;
import com.movoto.data.TestProperties;
import com.movoto.utils.appium.AppiumManager;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

public class DriverManager {

	public static DriverManager getDriverManager() {
		return new DriverManager();
	}

	public String startServer(TestDTO dto) {
		AppiumManager manager = new AppiumManager();
		try {
			return manager.startAppium(dto);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public WebDriver getDriver(TestDTO dto, String port) {
		WebDriver driver = null;
		TestProperties prop = dto.getTestProperties();
		String browser = prop.getBrowserName();
		String application = prop.getApplicationPath();
		String platform = prop.getPlatformName();
		String deviceID = prop.getUDID();
		String versionID = prop.getVersionID();
		String deviceName = prop.getDeviceName();
		DesiredCapabilities capabilities=null;
		String url = "http://127.0.0.1:" + port + "/wd/hub";
		try 
		{		
			capabilities= new DesiredCapabilities();
		    capabilities.setCapability("deviceName", deviceName);
			capabilities.setCapability("udid", deviceID);
			capabilities.setCapability("platformName", platform);
			capabilities.setCapability("platformVersion", versionID);	
			capabilities.setCapability("appPackage", "com.ppdgsk.hotzone");
			capabilities.setCapability("appActivity", "md51384854994f54f5416fc33864cf2ee5a.SplashActivity");
			//capabilities.setCapability("automationName", "XCUITest");
			//capabilities.setCapability("bundleId",new File(simulatorProp.getProperty("bundleId")));

			File app = new File(application);
			if (app.exists()) 
			{		
				browser = "";
				capabilities.setCapability("app", app.getAbsolutePath());
				//Native APP on iOS
				if (platform.equalsIgnoreCase("iOS")) 
				{
					boolean autoAcceptAlertsDisabled = dto.getTestProperties().isAutoAcceptAlertsDisabled();
					if (autoAcceptAlertsDisabled) {
						capabilities.setCapability("autoAcceptAlerts", false);
					} 
					else {
						capabilities.setCapability("autoAcceptAlerts", true);
					}
					driver = new IOSDriver<WebElement>(new URL(url), capabilities);
				}
				//Native APP on Android
				else{
					driver = new AndroidDriver<WebElement>(new URL(url), capabilities);
				}
			}
			//Consumer web on Android
			else if(platform.equalsIgnoreCase("Android")) 
			{
				/**
				 * For APPIUM 1.4.13
				 */
				
				//capabilities.setCapability("app", browser);
		        //capabilities.setCapability("appPackage", "com.android.chrome");
		        //capabilities.setCapability("appActivity", "com.google.android.apps.chrome.Main");			
		        //MobileCapabilityType.PLATFORM_NAME
				
				/**
				 * For APPIUM 1.6.3
				 */
				capabilities.setCapability("browserName", browser);
				capabilities.setCapability("platformName", "Android");
				capabilities.setCapability("deviceName", "Android");
	        
				driver = new AutomationNGDriver(new URL(url), capabilities);							
			} 
			//Consumer Web on iOS
			else if(platform.equalsIgnoreCase("IOS_WEB"))
			{	
				//Appium-1.6.3 for iOS Safaru Platform
				capabilities.setCapability("deviceName", "iPhone7");
				capabilities.setCapability("automationName", "XCUITest");
				capabilities.setCapability("browserName","Safari");
				capabilities.setCapability("platformName", "iOS");
				capabilities.setCapability("platformVersion", "10.0");
				//File app = new File("/Users/gopalprasad/Library/Developer/Xcode/DerivedData/WebDriverAgent-gohxgnsiixvsdvggaddfqpypxuax/Build/Products/Debug-iphoneos/WebDriverAgentRunner-Runner.app");
				//capabilities.setCapability("app", app.getAbsolutePath());
				capabilities.setCapability("udid", deviceID);
				capabilities.setCapability("sendKeyStrategy", "grouped");
				capabilities.setCapability("realDeviceLogger", "/Users/gopalprasad/.nvm/versions/node/v7.3.0/lib/node_modules/deviceconsole");
						
				driver = new IOSDriverNG(new URL(url), capabilities);
			}
			//Consumer Web on PC
			else if (platform.equalsIgnoreCase("Web")) 
			{
				driver = getWebDriver(dto);
			} 

			driver.manage().timeouts().implicitlyWait(30L, TimeUnit.SECONDS);
			
			if (!browser.equals("")) {
				driver.get(application);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return driver;
	}

	private WebDriver getWebDriver(TestDTO dto) {
		WebDriver driver = null;
		DesiredCapabilities capabilities =null;
		switch (dto.getTestProperties().getBrowserName()) {
		case "Firefox":
			File profileDir = new File("profiles/firefox_40");
			if (profileDir.exists() && profileDir.isDirectory()) {
				FirefoxProfile profile = new FirefoxProfile(profileDir);
				driver = new FirefoxDriver(profile);
			} else {
				driver = new FirefoxDriver();
			}
			driver.manage().timeouts().implicitlyWait(30L, TimeUnit.SECONDS);
			driver.manage().window().maximize();
			break;
		case "Chrome":
			String driverPath = dto.getTestProperties().getDriverPath();
			if (driverPath != null) {
				System.setProperty("webdriver.chrome.driver", driverPath);
				capabilities= DesiredCapabilities.chrome();
				ChromeOptions options = new ChromeOptions();
				options.addArguments("test-type");
				driver = new ChromeDriver(capabilities);
				driver.manage().timeouts().implicitlyWait(30L, TimeUnit.SECONDS);
				driver.manage().window().maximize();
			}
			break;
		case "IExplore":
			
			String driverPath1 = dto.getTestProperties().getDriverPath();
		      if (driverPath1 != null) {
		    		        
		        
		         String path="drivers/IEDriverServer.exe";
		           System.setProperty("webdriver.ie.driver", path);
		         capabilities = DesiredCapabilities.internetExplorer();
		         capabilities.setCapability(CapabilityType.BROWSER_NAME, "IE");
		         capabilities.setCapability(InternetExplorerDriver.
		           INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
		         try {
		       driver = new InternetExplorerDriver(capabilities);
		      } catch (Exception e) {
		       // TODO Auto-generated catch block
		       e.printStackTrace();
		      }
		       
		      }
		  
			break;
		case "Safari":
		{

			capabilities = DesiredCapabilities.safari();
			capabilities.setBrowserName(dto.getTestProperties().getBrowserName().toLowerCase());
			capabilities.setVersion(dto.getTestProperties().getVersionID());
			capabilities.setVersion(dto.getTestProperties().getPlatformName().toUpperCase());
			capabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
            SafariOptions.fromCapabilities(capabilities);
			driver = new SafariDriver(capabilities);
			driver.manage().deleteAllCookies();
			
		}
			break;
		default:
			break;
		}
		return driver;
	}
}
