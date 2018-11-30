package com.movoto.fixtures.impl.wd;

import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.Select;
import com.movoto.data.ObjectRepository;
import com.movoto.data.TestDTO;
import com.movoto.data.impl.Locator;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;

/**
 * @author Bhagavan.Kailar
 * 
 *         The class inherits all the generic functions from UIFunctionsImpl,
 *         only android platform specific implementations are to be done here
 */
public class AndroidDriverFixturesImpl extends CommonDriverFixturesImpl {

	public AndroidDriverFixturesImpl(WebDriver driver, TestDTO dto) {
		this.driver =  (AndroidDriver<WebElement>)driver;
		this.dto = dto;
		this.locators = dto.getObjectRepository();
	}

	protected WebElement getElement(Locator locator) {
		try{
		switch (locator.getType()) {
		case "xpath":
			return driver.findElement(By.xpath(locator.getValue()));
		case "id":
			return driver.findElement(By.id(locator.getValue()));
		case "text":
			return driver.findElement(By.linkText(locator.getValue()));
		case "name":
			return driver.findElement(By.name(locator.getValue()));
		default:
			return driver.findElement(By.xpath(locator.getValue()));
		}
		}catch (Exception e) {
			System.out.println("Exception in CommonDriverFixturesImpl:getElement(Locator locator)->"+e.getMessage());
		}
		return null;
	}

	@Override
	public boolean select(String target, String data) {
		WebElement e = getElement(getLocator(target));
		((AndroidDriver<WebElement>) driver).scrollToExact(data);
		e.click();
		return true;
	}

	@Override
	public WebDriver getDriver() {
		// TODO Auto-generated method stub
		return ((AndroidDriver<WebElement>) driver);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean scrollTo(String text) {
		((AndroidDriver<WebElement>) driver).scrollToExact(text);
		return true;
	}
	
	@Override
	public boolean tapOnElement(String target) {
		WebElement e = getElement(getLocator(target));
	
		/*int x= e.getLocation().x;
		int y = e.getLocation().y;
		int width = e.getSize().width;
		int height = e.getSize().height;
		
		x = x + (width/2);
		y = y + (height/2);
		
		System.out.println("X: "+x+" Y: "+y+" isEnabled: "+e.isEnabled());*/
		
		((AndroidDriver<WebElement>)getDriver()).tap(1, e, 500);
		
		//TouchAction action = new TouchAction((AndroidDriver<WebElement>)getDriver()).tap(e).perform();
		return true;
	}
	
	@Override
	public boolean tapOnLocation(int x, int y) {
		System.out.println("X: "+x+" Y: "+y);
		TouchAction action = new TouchAction((AndroidDriver<WebElement>)getDriver()).tap(x, y).perform();
		return true;
	}
	
	@Override
	public boolean swipeFromTo(int startx, int starty, int endx, int endy) {
		AndroidDriver<WebElement> driver = (AndroidDriver<WebElement>) this.driver;
		driver.swipe(startx, starty, endx, endy, 800);
		return true;
	}
	
	@Override
	public boolean navigateBack() {
		AndroidDriver<WebElement> driver = (AndroidDriver<WebElement>) this.driver;
		driver.pressKeyCode(AndroidKeyCode.BACK);
		return true;
	}
	
	@Override
	public boolean pressAndReleaseAnElement(String target) {
		WebElement e = getElement(getLocator(target));
		
		int x= e.getLocation().x;
		int y = e.getLocation().y;
		int width = e.getSize().width;
		int height = e.getSize().height;
		System.out.println("X: "+x+" Y: "+y+" Width x Height: "+width+" x "+height+" isEnabled: "+e.isEnabled());
		
		x = x + (width/2);
		y = y + (height/2);
		
		
		TouchAction action = new TouchAction((AndroidDriver<WebElement>)getDriver()).press(x, y).release().perform();
	
		return true;
	}
	
	@Override
	public boolean pressAndReleaseLocation(int x, int y) {
		
		TouchAction action = new TouchAction((AndroidDriver<WebElement>)getDriver()).press(x, y).release().perform();
		return true;
	}
	
	@Override
	public boolean click(String target) {
		getElement(getLocator(target)).click();
		return true;
	}
	
	@Override
	public boolean newFixture() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public void hideKeyboard(){
		AndroidDriver<WebElement> driver = (AndroidDriver<WebElement>) this.driver;
		driver.hideKeyboard();
	}
	
}
