package com.movoto.fixtures;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * @author Bhagavan.Kailar
 *
 *         Interface to list all the UI Functions
 */

public interface DriverFixtures {

	/**
	 * @param target
	 *            locator reference key or a locator in the format
	 *            <strong>$locatorType->locatorValue</strong>
	 * @return execution status
	 */
	public boolean click(String target);
	
	public void clickHref(String target);

	/**
	 * @param data
	 *            Text to be entered
	 * @param target
	 *            locator reference key or a locator in the format
	 *            <strong>$locatorType->locatorValue</strong>
	 * @return execution status
	 */
	public boolean typeDataInto(String data, String target);
	//This method uses javascript to key in the text into web elements
	public boolean typeDataIntoWithJavaScript(String data, String target);

	/**
	 * @param target
	 *            locator reference key or a locator in the format
	 *            <strong>$locatorType->locatorValue</strong>
	 * @return execution status
	 * @return
	 */
	public boolean verifyPageContainsElement(String target);

	/**
	 * @param target
	 *            locator reference key or a locator in the format
	 *            <strong>$locatorType->locatorValue</strong>
	 * @return execution status
	 */
	public boolean clear(String target);

	public boolean select(String target, String data);

	public boolean quit();

	public String captureScreen(String outputDir);

	public WebDriver getDriver();

	public boolean open(String application);

	public String getTextFrom(String target);

	public boolean verifyIfAlertExists();

	public boolean waitForElement(String target);

	public boolean setImplicitWaitTime(int seconds);

	public boolean handleAlert(String action);

	public String getPageTitle();

	public boolean scrollTo(String target);
	
	public boolean tapOnElement(String target);
	
	public boolean tapOnLocation(int x, int y);
	
	public boolean isElementEnabled(String target, boolean assertFlag);
	
	public boolean swipeFromTo(int startx, int starty, int endx, int endy);
	
	public boolean navigateBack();
	
	public int getElementCount(String target);
	
	public String getElementCenterCoordinates(String target);
	
	public boolean pressAndReleaseAnElement(String target);
	
	public boolean pressAndReleaseLocation(int x,int y);
	
	public String getAttributeOfElement(String attribute, String target);
	
	public String getWindowSize();
	
	public boolean verifyPageNotContainsElement(String target);
	
	public boolean isElementDisabled(String target);
	
	public boolean newFixture();
	
	public boolean switchToWindow();
	
	public boolean isCheckBoxChecked(String target);
	
	public String getUrl();

	public WebElement findElement(String locator);
	
	public void get(String url);
	
	public boolean isJSEClicked(String target);
	
	public void refresh();
	
	public boolean scrollToElement(String target) ;
	
	public List<WebElement> findElements(String locator);

	public void mouseHoverJScript(WebElement element);

	public boolean checkFileInFileSystem(String filepath);
	
	public void hideKeyboard();
	
}
