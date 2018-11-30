package com.movoto.fixtures.impl.wd;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import com.movoto.data.DataManager;
import com.movoto.data.ObjectRepository;
import com.movoto.data.TestDTO;
import com.movoto.data.impl.Locator;
import com.movoto.data.impl.ORManager;
import com.movoto.fixtures.DriverFixtures;

import io.appium.java_client.android.AndroidDriver;

/**
 * @author Bhagavan.Kailar
 * 
 *         The getUiFunctions method in the class, based on the configuration
 *         data, reads the Object Repository file and populates the Locator Map.
 * 
 *         Also based on the PLATFORM creates the Driver instance specific to
 *         the given platform
 * 
 *         Object Repository file - any text file (Properties file, JSON, Excel
 *         etc.)
 */
public class DriverFixtureManager {

	
	public static DriverFixtureManager getWDManager() {
		return new DriverFixtureManager();
	}

	public DriverFixtures getDriverFixtures(TestDTO dto) {
		DriverFixtures functions = null;
		String port= "4723";
		WebDriver driver = null;
		if (dto != null) {
			String platform = dto.getTestProperties().getPlatformType();
			switch (platform) {
			case "ANDROID_NATIVE":
				//port = DriverManager.getDriverManager().startServer(dto);
				driver = DriverManager.getDriverManager().getDriver(dto, port);
				functions = new AndroidDriverFixturesImpl(driver, dto);
				break;
			case "ANDROID_WEB":
				port = DriverManager.getDriverManager().startServer(dto);
				driver = DriverManager.getDriverManager().getDriver(dto, port);
				functions = new AndroidDriverFixturesImpl(driver, dto);
				break;
			case "WEB_FIREFOX":
				driver = DriverManager.getDriverManager().getDriver(dto, "");
				functions = new CommonDriverFixturesImpl(driver, dto);
				break;
			case "IOS_NATIVE":
				port = DriverManager.getDriverManager().startServer(dto);
				driver  = DriverManager.getDriverManager().getDriver(dto, port);
				functions = new IOSDriverFixturesImpl(driver, dto);
				break;
			case "IOS_WEB":
				port = DriverManager.getDriverManager().startServer(dto);
				driver = DriverManager.getDriverManager().getDriver(dto, port);
				functions = new IOSDriverFixturesImpl(driver, dto);
				break;
			case "WEB_SAFARI":
				driver = DriverManager.getDriverManager().getDriver(dto, port);
				break;
				
			case "WEB_IExplore":
			    driver = DriverManager.getDriverManager().getDriver(dto, port);
			    functions = new CommonDriverFixturesImpl(driver, dto);
			    break;
			    
			default:
				functions = null;
				break;
			}
		}
		DataManager.getData().setDriver(driver);
		return functions;
	}

	/**
	 * @return AndroidDriver
	 */
	private static synchronized AndroidDriver<WebElement> getNativeDriver() {

		AndroidDriver<WebElement> driver = null;
		try {
			File app = new File("C:/apps/Movoto.apk");
			DesiredCapabilities capabilities = new DesiredCapabilities();
			// capabilities.setCapability("device", "Android");
			// mandatory capabilities
			capabilities.setCapability("deviceName", "2912969d");
			capabilities.setCapability("udid", "2912969d");
			capabilities.setCapability("platformName", "Android");
			capabilities.setCapability("platformVersion", "4.2.2");

			capabilities.setCapability("app", app.getAbsolutePath());
			driver = new AndroidDriver<WebElement>(new URL("http://127.0.0.1:4730/wd/hub"), capabilities);
			driver.manage().timeouts().implicitlyWait(30L, TimeUnit.SECONDS);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return driver;
	}

	private static WebDriver getFirefoxDriver() {
		WebDriver driver = null;
		try {
			driver = new FirefoxDriver(DesiredCapabilities.firefox());
			driver.manage().window().maximize();
			driver.get("http://www.movoto.com");
			driver.manage().timeouts().implicitlyWait(30L, TimeUnit.SECONDS);
			return driver;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return driver;
	}

	private static synchronized AndroidDriver<WebElement> getAndroidWebDriver() {
		AndroidDriver<WebElement> driver = null;
		try {
			DesiredCapabilities capabilities = new DesiredCapabilities();
			// capabilities.setCapability("device", "Android");
			// mandatory capabilities
			capabilities.setCapability("deviceName", "37a5bab9");
			capabilities.setCapability("udid", "37a5bab9");
			capabilities.setCapability("platformName", "Android");
			capabilities.setCapability("platformVersion", "5.1.1");
			capabilities.setCapability("browserName", "Chrome");
			driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
			driver.manage().timeouts().implicitlyWait(30L, TimeUnit.SECONDS);
			driver.get("http://www.movoto.com");
		} catch (Exception e) {
			// TODO: handle exception
		}
		return driver;
	}

}
