package com.movoto.fixtures.impl.wd;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.movoto.data.ObjectRepository;
import com.movoto.data.TestDTO;
import com.movoto.data.impl.Locator;
import com.movoto.fixtures.DriverFixtures;

/**
 * @author Bhagavan.Kailar
 *
 *         Abstract class that implements all the generic UI functions for all
 *         the platforms
 */

public class CommonDriverFixturesImpl implements DriverFixtures {

	protected WebDriver driver;
	protected ObjectRepository locators;
	protected TestDTO dto;

	public CommonDriverFixturesImpl() {
		// TODO Auto-generated constructor stub
	}

	public CommonDriverFixturesImpl(WebDriver driver, TestDTO dto) {
		this.driver = driver;
		this.dto = dto;
		this.locators = dto.getObjectRepository();
	}

	@Override
	public String captureScreen(String outputDir) {
		String path = null;

		return path;
	}

	@Override
	public boolean clear(String target) {
		JavascriptExecutor jse=null;
		try
		{
		getElement(getLocator(target)).clear();
		}catch(Exception exc)
		{
			jse=(JavascriptExecutor)getDriver();
			jse.executeScript("arguments[0].value=''",getElement(getLocator(target)));
		}
		return true;
	}

	@Override
	public boolean click(String target) {
		getElement(getLocator(target)).click();
		return true;
	}
	@Override
	public void clickHref(String target) {
		try
		{
		
		}catch(Exception exc)
		{
			System.out.println("Exception in CommonDriverFxituresImpl.clickHref(String target)->"+exc.getMessage());
		}
		
	}
	
	@Override
	public WebDriver getDriver() {
		return this.driver;
	}

	/*protected WebElement getElement(Locator locator) {
		WebDriverWait wait = new WebDriverWait(getDriver(), 30L);
		switch (locator.getType()) {
		case "xpath":
			return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator.getValue())));
		case "id":
			return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(locator.getValue())));
		default:
			return driver.findElement(By.xpath(locator.getValue()));
		}
	}*/
	
	protected WebElement getElement(Locator locator) 
	 {
	  WebDriverWait wait = new WebDriverWait(getDriver(), 30L);
	  try
	  {
	  switch (locator.getType()) {
	 // case "xpath":
	  // return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator.getValue())));
	  case "id":
		  return driver.findElement(By.id(locator.getValue()));
	  // return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(locator.getValue())));
	  default:
	   return driver.findElement(By.xpath(locator.getValue()));
	  }
	  }catch(Exception exe)
	  {
	   System.out.println("Exception in CommonDriverFixturesImpl:getElement(Locator locator)->"+exe.getMessage());
	  }
	  return null;
	 }
	
	protected Locator getLocator(String target) 
	 {
	  Locator loc = null;
	  String[] locatorStr = null;
	  String type=null,value=null;
	  try
	  {
	    if ((loc = locators.get(target)) != null) 
	    {
	     return loc;
	    }
	    if(target!=null && target.length()>0)
	    {
	     if(target.contains("->"))
	     {
	       locatorStr = target.split("->");
	       type = locatorStr[0];
	       value = locatorStr[1];
	     }
	     else
	     {
	           type="xpath";
	           value=target;
	     }
	     loc = new Locator();
	     loc.setName("Dynamic");
	     loc.setType(type);
	     loc.setValue(value);
	    }
	   }catch (Exception e) 
	   {
	    System.out.println("Exception in CommonDriverFixturesImpl:getLocator(String target)->"+e.getMessage());
	   }
	  return loc;
	 }
	

	/*protected Locator getLocator(String target) {
		Locator loc = null;
		if ((loc = locators.get(target)) != null) {
			return loc;
		}
		String[] locatorStr = target.split("->");
		if (locatorStr != null && locatorStr.length == 2) {
			String type = locatorStr[0];
			String value = locatorStr[1];

			loc = new Locator();
			loc.setName("Dynamic");
			loc.setType(type);
			loc.setValue(value);
		} else {
			try {
				throw new Throwable("Invalid Locator");
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return loc;
	}*/

	@Override
	public String getPageTitle() {
		return getDriver().getTitle();
	}

	@Override
	public String getTextFrom(String target) {
		return getElement(getLocator(target)).getText();
	}

	@Override
	public boolean handleAlert(String action) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 10);
			Alert alert = wait.until(ExpectedConditions.alertIsPresent());
			switch (action) {
			case "ACCEPT":
				alert.accept();
				break;
			case "DISMISS":
				alert.dismiss();
				break;
			default:
				alert.accept();
				break;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public boolean verifyPageContainsElement(String target) {
	/*	try {
			return getElement(getLocator(target)).isDisplayed();
		} catch (Exception e) {
			return false;
		}*/
		
		WebElement element=null;
		  try {
		   element=getElement(getLocator(target));
		   if(element!=null)
		   {
		    return element.isDisplayed();
		   }
		  } catch (Exception e) {
		   System.out.println("Exception is verifyPageContainsElement:"+e.getMessage());
		  }
		  return false;
	}
	
	@Override
	public boolean open(String application) {
		getDriver().get(application);
		return true;
	}

	@Override
	public boolean quit() {
		getDriver().quit();
		try {
			String deviceID = dto.getTestProperties().getUDID();
			if (DeviceManager.isDeviceInUse(deviceID)) {
				DeviceManager.removeDevice(deviceID);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return true;
	}

	@Override
	public boolean scrollTo(String target) {
		((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView(true);",
				getElement(getLocator(target)));
		try {
			Thread.sleep(500);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return true;
	}

	@Override
	public boolean select(String target, String data) {
		WebElement e = getElement(getLocator(target));
		Select select = new Select(e);
		select.selectByValue(data);
		return true;
	}

	@Override
	public boolean setImplicitWaitTime(int seconds) {
		
		driver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
		return true;
	}

	@Override
	public boolean typeDataInto(String data, String target) {
		try
		{
			
		getElement(getLocator(target)).sendKeys(data);
	//	JavascriptExecutor jse=(JavascriptExecutor)getDriver();
		//jse.executeScript("arguments[0].value='"+data+"'", getElement(getLocator(target)));
		}catch(Exception exc)
		{
			System.out.println("Exception in CommonDriverFixturesImpl->typeDataInto(String data,String target"+exc.getMessage());
		}
		return true;
	}
	@Override
	public boolean typeDataIntoWithJavaScript(String data, String target) {
		try
		{	
	     JavascriptExecutor jse=(JavascriptExecutor)getDriver();
		 jse.executeScript("arguments[0].value='"+data+"'", getElement(getLocator(target)));
		}catch(Exception exc)
		{
			System.out.println("Exception in CommonDriverFixturesImpl->typeDataIntoWithJavaScript(String data,String target"+exc.getMessage());
		}
		return true;
	}

	
	@Override
	public boolean verifyIfAlertExists() {
		try {
			Alert alert = getDriver().switchTo().alert();
			if (alert != null) {
				getDriver().switchTo().defaultContent();
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	@Override
	public boolean waitForElement(String target) {
		boolean found = false;

		try {
			found = new FluentWait<WebDriver>(getDriver()).withTimeout(30L, TimeUnit.SECONDS)
					.pollingEvery(500L, TimeUnit.MILLISECONDS).ignoring(NoSuchElementException.class)
					.until(new ExpectedCondition<Boolean>() {
						public Boolean apply(WebDriver d) {
							return getElement(getLocator(target)).isDisplayed();
						}
					});
		} catch (Exception e) {
			// TODO: handle exception
		}
		return found;
	}
	
	@Override
	public boolean tapOnElement(String target) {
		return true;
	}


	@Override
	public boolean tapOnLocation(int x, int y) {
		return true;
	}

	@Override
	public boolean isElementEnabled(String target, boolean assertFlag) {
		WebElement e = getElement(getLocator(target));
		return e.isEnabled();
	}

	@Override
	public boolean swipeFromTo(int startx, int starty, int endx, int endy) {
		return true;
	}

	@Override
	public boolean navigateBack() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int getElementCount(String target) {
		Locator locator = getLocator(target);
		WebDriverWait wait = new WebDriverWait(getDriver(), 30L);
		switch (locator.getType()) {
		case "xpath":
			return getDriver().findElements(By.xpath(locator.getValue())).size();
		case "id":
			return getDriver().findElements(By.id(locator.getValue())).size();
		default:
			return 0;
		}
	}
	
	@Override
	public String getElementCenterCoordinates(String target) {
		WebElement e = getElement(getLocator(target));
		int x = e.getLocation().x;
		int y = e.getLocation().y;
		int width = e.getSize().width;
		int height = e.getSize().height;
		
		x = x + (width/2);
		y = y + (height/2);
		return x+","+y;
	}

	@Override
	public boolean pressAndReleaseAnElement(String target) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pressAndReleaseLocation(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public String getAttributeOfElement(String attribute, String target) {
		WebElement e = getElement(getLocator(target));
		return e.getAttribute(attribute);
	}

	@Override
	public String getWindowSize() {
		int width =  getDriver().manage().window().getSize().width;
		int height = getDriver().manage().window().getSize().height;
		return width+","+height;
	}
	
	@Override
	public boolean verifyPageNotContainsElement(String target) {
		try {
			WebElement e = getElement(getLocator(target));
			if(e!=null){
				return !e.isDisplayed();
			}
		} catch (Exception e) {
			return true;
		}
		return true;
	}
	
	@Override
	public boolean isElementDisabled(String target) {
		
		WebElement e = getElement(getLocator(target));
		if(e!=null)
		return(!e.isEnabled());
		
		return false;
	}
	
	@Override
	public boolean newFixture() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void hideKeyboard(){
		
	}
	
	@Override
	 public boolean switchToWindow() {
	  System.out.println("test");
	  String parentWindowHandler = driver.getWindowHandle(); // Store your parent window
	   System.out.println(parentWindowHandler);
	  String subWindowHandler = null;

	  Set<String> handles = driver.getWindowHandles();// get all window handles
	  Iterator<String> iterator = handles.iterator();
	  while (iterator.hasNext()){
	      subWindowHandler = iterator.next();
	      System.out.println(subWindowHandler);
	  }
	  driver.switchTo().window(subWindowHandler); // switch to popup window
	                                              // perform operations on popup
	  return true;
	 }
	
	@Override
	public boolean isCheckBoxChecked(String target){
		WebElement e = getElement(getLocator(target));
		boolean isChecked = e.isSelected();
		return isChecked;
	}
	
	@Override
	public String getUrl() {
		return driver.getCurrentUrl();
		//return currentUrl;
	}
	
	@Override
	 public WebElement findElement(String locator) {
		return(getElement(getLocator(locator)));
	 }

	@Override
	public void get(String url) {
		driver.get(url);
	}

	@Override
	public boolean isJSEClicked(String target) {
		
		try {
			   WebElement element=getElement(getLocator(target));
			   JavascriptExecutor jse=(JavascriptExecutor)driver;
			   jse.executeScript("arguments[0].click()",element);
			   
			  } catch (Exception e) {
			   
			   System.out.println("Exception in CommonDriverFixturesImpl:isClicked(target)->"+e.getMessage());
			   return false;
			  }
			  return true;
	}

	@Override
	public void refresh() {
		driver.navigate().refresh();
		
	}

	@Override
	public boolean scrollToElement(String target) {
		
		 try {
			   WebElement element=getElement(getLocator(target));
			   JavascriptExecutor jse = (JavascriptExecutor)getDriver(); 
			   jse.executeScript("arguments[0].scrollIntoView();", element);
			  
			  } catch (Exception e) {
			   return false;
			   }
			   return true;

	}

	@Override
	public List<WebElement> findElements(String locator) {
		Locator loc=getLocator(locator);
		switch(loc.getType())
		{
			case "id":
			return driver.findElements(By.id(loc.getValue()));
			
	        default:
	    	return driver.findElements(By.xpath(loc.getValue()));
				
		}
	
		
	}

	
	
	@Override
	 //Method to replace Robot Click.
	 public void mouseHoverJScript(WebElement element) {
	     
	 try {
	  if (element.isDisplayed()) { 
	   String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover',true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject){ arguments[0].fireEvent('onmouseover');}";
	   JavascriptExecutor js = (JavascriptExecutor) getDriver();
	   js.executeScript(mouseOverScript,element);
	  } 
	  else {
	   System.out.println("Element was not visible to hover " + "\n");
	  }
	 } catch (StaleElementReferenceException e) {
	  System.out.println("Element with " + element
	    + "is not attached to the page document"
	    + e.getStackTrace());
	
	 }
	}

	@Override
	  public boolean checkFileInFileSystem(String filepath) {

	    try {
	      if (filepath == null)
	        throw new FileNotFoundException();
	      File file = Paths.get(filepath).toFile();
	      return (file.exists() && file.isFile() && !file.isDirectory()) ? true : false;
	    } catch (FileNotFoundException | InvalidPathException | NullPointerException e) {
	      e.printStackTrace();
	      return false;
	    }

	  }
}
