package com.movoto.fixtures.impl.fixturelib;

import java.util.List;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.jayway.restassured.response.Response;
import com.movoto.data.TestDTO;
import com.movoto.fixtures.DriverFixtures;
import com.movoto.fixtures.FixtureLibrary;
import com.movoto.fixtures.UtilityFixtures;
import com.movoto.fixtures.WebServicesFixtures;
import com.movoto.fixtures.impl.util.UtilityFixturesImpl;
import com.movoto.fixtures.impl.wd.DriverFixtureManager;
import com.movoto.fixtures.impl.ws.WebServiceFixtureManager;


public class FixtureLibraryImpl implements FixtureLibrary {

	  private DriverFixtures uiLibrary;
	  private UtilityFixtures utilLibrary;
	  private WebServicesFixtures wsLibrary;

	  public FixtureLibraryImpl(TestDTO dto) {
		  uiLibrary = DriverFixtureManager.getWDManager().getDriverFixtures(dto);
		  utilLibrary = new UtilityFixturesImpl(dto);
		  wsLibrary = WebServiceFixtureManager.getWebServiceManager().getWebServicesFixtures(dto);	
	  }

	  @Override
	  public Map<String, String> loadObjectRepository(String filePath) {
	      return utilLibrary.loadObjectRepository(filePath);
	  }

	  @Override
	  public boolean click(String target) {
	      return uiLibrary.click(target);
	  }

	  @Override
	  public boolean typeDataInto(String data, String target) {
	      return uiLibrary.typeDataInto(data, target);
	  }

	  @Override
	  public boolean verifyPageContainsElement(String target) {
	      return uiLibrary.verifyPageContainsElement(target);
	  }

	  @Override
	  public boolean clear(String target) {
	      return uiLibrary.clear(target);
	  }

	  @Override
	  public boolean quit() {
	      return uiLibrary.quit();
	  }

	  @Override
	  public void openExcelSheet(String path, String sheet, String mode) {
	      utilLibrary.openExcelSheet(path, sheet, mode);
	  }

	  @Override
	  public String getFromExcelRowAndColumn(int row, String header) {
	      return utilLibrary.getFromExcelRowAndColumn(row, header);
	  }

	  @Override
	  public void closeExcelSheet(String path, String sheet, String mode) {
	      utilLibrary.closeExcelSheet(path, sheet, mode);
	  }

	  @Override
	  public void writeToExcel(String data, int row, int column) {
	      utilLibrary.writeToExcel(data, row, column);
	  }

	  @Override
	  public int getExcelRowNumberForKey(String key) {
	      return utilLibrary.getExcelRowNumberForKey(key);
	  }

	  @Override
	  public int getExcelRowCount() {
	      return utilLibrary.getExcelRowCount();
	  }

	  @Override
	  public boolean deleteRowForKey(String key) {
	      return utilLibrary.deleteRowForKey(key);
	  }

  @Override
  public Object getValueFromJson(String jsonPath, String json) {
    return utilLibrary.getValueFromJson(jsonPath, json);
  }

  @Override
  public boolean select(String target, String data) {
    return uiLibrary.select(target, data);
  }

  @Override
  public String captureScreen(String outputDir) {
    return uiLibrary.captureScreen(outputDir);
  }

  @Override
  public WebDriver getDriver() {
    return uiLibrary.getDriver();
  }

  @Override
  public String HTTPGet(String URL) {
    // TODO Auto-generated method stub
    return wsLibrary.HTTPGet(URL);
  }

  @Override
  public String HTTPPost(String URL, Map<String, Object> data) {
    // TODO Auto-generated method stub
    return wsLibrary.HTTPPost(URL, data);
  }

  @Override
  public String HTTPDelete(String URL) {
    // TODO Auto-generated method stub
    return wsLibrary.HTTPDelete(URL);
  }

  @Override
  public String HTTPut(String URL, Map<String, Object> data) {
    // TODO Auto-generated method stub
    return wsLibrary.HTTPut(URL, data);
  }

  @Override
  public void setRequestHeader(String key, String value) {
    // TODO Auto-generated method stub
    wsLibrary.setRequestHeader(key, value);
  }

  @Override
  public String getResponseHeaderValueForKey(String key) {
    // TODO Auto-generated method stub
    return wsLibrary.getResponseHeaderValueForKey(key);
  }

  @Override
  public Response getCurrentResponse() {
    // TODO Auto-generated method stub
    return wsLibrary.getCurrentResponse();
  }

  @Override
  public void setContentType(String contentType) {
    // TODO Auto-generated method stub
    wsLibrary.setContentType(contentType);
  }

  @Override
  public boolean connectToDatabase(String db) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public Object executeQuery(String query) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean closeDatabaseConnection() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean open(String application) {
    // TODO Auto-generated method stub
    return uiLibrary.open(application);
  }

  @Override
  public String getTextFrom(String target) {
    // TODO Auto-generated method stub
    return uiLibrary.getTextFrom(target);
  }

  @Override
  public boolean verifyIfAlertExists() {
    // TODO Auto-generated method stub
    return uiLibrary.verifyIfAlertExists();
  }

  @Override
  public boolean waitForElement(String target) {
    // TODO Auto-generated method stub
    return uiLibrary.waitForElement(target);
  }

  @Override
  public boolean setImplicitWaitTime(int seconds) {
    // TODO Auto-generated method stub
    return uiLibrary.setImplicitWaitTime(seconds);
  }

  @Override
  public boolean handleAlert(String action) {
    // TODO Auto-generated method stub
    return uiLibrary.handleAlert(action);
  }

  @Override
  public String getPageTitle() {
    // TODO Auto-generated method stub
    return uiLibrary.getPageTitle();
  }

  @Override
  public boolean scrollTo(String target) {
    // TODO Auto-generated method stub
    return uiLibrary.scrollTo(target);
  }

  @Override
  public boolean wait(int seconds) {
    return utilLibrary.wait(seconds);
  }

  @Override
  public boolean tapOnElement(String target) {
    return uiLibrary.tapOnElement(target);
  }

  @Override
  public boolean tapOnLocation(int x, int y) {
    return uiLibrary.tapOnLocation(x, y);
  }

  @Override
  public boolean isElementEnabled(String target, boolean assertFlag) {
    return uiLibrary.isElementEnabled(target, assertFlag);
  }

  @Override
  public boolean swipeFromTo(int startx, int starty, int endx, int endy) {
    return uiLibrary.swipeFromTo(startx, starty, endx, endy);
  }

  @Override
  public boolean navigateBack() {
    return uiLibrary.navigateBack();
  }

  @Override
  public int getElementCount(String target) {
    return uiLibrary.getElementCount(target);
  }

  @Override
  public String getElementCenterCoordinates(String target) {
    return uiLibrary.getElementCenterCoordinates(target);
  }

  @Override
  public void setCurrentTestMethod(String name) {
    utilLibrary.setCurrentTestMethod(name);
  }

  @Override
  public String getCurrentPlatform() {
    return utilLibrary.getCurrentPlatform();
  }

  @Override
  public boolean pressAndReleaseAnElement(String target) {
    return uiLibrary.pressAndReleaseAnElement(target);
  }

  @Override
  public boolean pressAndReleaseLocation(int x, int y) {
    return uiLibrary.pressAndReleaseLocation(x, y);
  }

  @Override
  public String getCurrentDate() {
    return utilLibrary.getCurrentDate();
  }

  @Override
  public String getAttributeOfElement(String attribute, String target) {
    return uiLibrary.getAttributeOfElement(attribute, target);
  }

  @Override
  public String getWindowSize() {
    return uiLibrary.getWindowSize();
  }

  @Override
  public boolean verifyPageNotContainsElement(String target) {
    return uiLibrary.verifyPageNotContainsElement(target);
  }

  @Override
  public boolean isElementDisabled(String target) {
    return uiLibrary.isElementDisabled(target);
  }

  @Override
  public boolean newFixture() {
    return uiLibrary.newFixture();
  }
  
  @Override
  public void hideKeyboard(){
	  uiLibrary.hideKeyboard();
  }

  @Override
  public boolean switchToWindow() {

    return uiLibrary.switchToWindow();
  }

  @Override
  public boolean isCheckBoxChecked(String target) {

    return uiLibrary.isCheckBoxChecked(target);
  }

  @Override
  public String getUrl() {
    // TODO Auto-generated method stub
    return uiLibrary.getUrl();
  }

  @Override
  public String getCurrentBrowser() {
    // TODO Auto-generated method stub
    return utilLibrary.getCurrentBrowser();
  }

  @Override
  public String getBrowserName() {
    // TODO Auto-generated method stub
    return utilLibrary.getBrowserName();
  }

  @Override
  public String getCurrentOS() {
    // TODO Auto-generated method stub
    return utilLibrary.getCurrentOS();
  }

  @Override
  public WebElement findElement(String locator) {
    return (uiLibrary.findElement(locator));

  }

  @Override
  public void get(String url) {
    uiLibrary.get(url);

  }

  @Override
  public boolean isJSEClicked(String target) {
    return uiLibrary.isJSEClicked(target);
  }

  @Override
  public void refresh() {
    uiLibrary.refresh();
  }

  @Override
  public boolean scrollToElement(String target) {
    return uiLibrary.scrollToElement(target);
  }

  @Override
  public List<WebElement> findElements(String locator) {
    return (uiLibrary.findElements(locator));
  }

  @Override
  public boolean typeDataIntoWithJavaScript(String data, String target) {
    // TODO Auto-generated method stub
    uiLibrary.typeDataIntoWithJavaScript(data, target);
    return false;
  }

  @Override
  public void clickHref(String target) {
    uiLibrary.clickHref(target);
  }

  @Override
  public String getCurrentPlatformType() {
    // TODO Auto-generated method stub
    return utilLibrary.getCurrentPlatformType();
  }


  /**
   * Checks if a string is a valid path. Null safe.
   * 
   * Calling examples: path("c:/test"); //returns true path("c:/te:t"); //returns false
   * path("c:/te?t"); //returns false path("c/te*t"); //returns false path("good.txt"); //returns
   * true path("not|good.txt"); //returns false path("not:good.txt"); //returns false
   */
  @Override
  public boolean checkFileInFileSystem(String filepath) {

	  return utilLibrary.checkFileInFileSystem(filepath);

  }

  @Override
  public void mouseHoverJScript(WebElement element) {
    uiLibrary.mouseHoverJScript(element);

  }

  @Override
  public String HTTPPost(String URL, String body) {
   // TODO Auto-generated method stub
   return wsLibrary.HTTPPost(URL, body);
  }

}

