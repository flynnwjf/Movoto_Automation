package com.movoto.fixtures.impl.wd;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import com.movoto.data.ObjectRepository;
import com.movoto.data.TestDTO;
import com.movoto.data.impl.Locator;
import com.movoto.fixtures.DriverFixtures;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

/**
 * @author Bhagavan.Kailar
 * 
 *         The class inherits all the generic functions from UIFunctionsImpl,
 *         only iOS platform specific implementations are to be done here
 */

public class IOSDriverFixturesImpl extends CommonDriverFixturesImpl {


	public IOSDriverFixturesImpl(WebDriver driver, TestDTO dto) {
		this.driver = (IOSDriver<WebElement>) driver;
		this.dto = dto;
		this.locators = dto.getObjectRepository();
		
	}

	protected WebElement getElement(Locator locator) {
		try{
		switch (locator.getType()) {
		case "ios":
			return ((IOSDriver<WebElement>)driver).findElementByIosUIAutomation(locator.getValue());
		case "xpath":
			return driver.findElement(By.xpath(locator.getValue()));
		case "name":
			return driver.findElement(By.name(locator.getValue()));
		default:
			return driver.findElement(By.xpath(locator.getValue()));
		}
		}catch(Exception e)
		{
			System.out.println("Exception in IOSDriverFixturesImpl:getElement(Locator locator)->"+e.getMessage());
		}
		return null;
	}

	@Override
	public boolean scrollTo(String target) {
//		driver.scrollTo(target);
		return true;
	}

	@Override
	public boolean click(String target) {
		getElement(getLocator(target)).click();
		return true;
	}

	@Override
	public boolean swipeFromTo(int startx, int starty, int endx, int endy) {
		TouchAction action = new TouchAction((IOSDriver<WebElement>) driver);
		int dx = endx - startx;
		int dy = endy - starty;
		action.longPress(startx, starty).moveTo(dx, dy).release().perform();
		return true;
	}
	
	@Override
	public WebDriver getDriver() {
		return ((IOSDriver<WebElement>) driver);
	}
}
