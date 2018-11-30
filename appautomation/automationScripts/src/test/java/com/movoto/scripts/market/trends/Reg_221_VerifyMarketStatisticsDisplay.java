package com.movoto.scripts.market.trends;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.select.Evaluator.IsEmpty;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import com.movoto.scripts.BaseTest;
import com.movoto.utils.JSONParserForAutomationNG;

/*
 * Govind Kalria
 */
public class Reg_221_VerifyMarketStatisticsDisplay extends BaseTest {

	JSONParserForAutomationNG jsonParser;
	JSONObject jsonObj;
	WebDriver driver = null;

	@Test
	@Parameters("dataProviderPath")
	public void verifySearchInfo(String dataProviderPath) {
		try {
			jsonParser = new JSONParserForAutomationNG(dataProviderPath);
			jsonObj = jsonParser.getNode("marketStatistics");
		} catch (Exception exc) {
			System.out.println("Exception in BasicInfo:init() ->" + exc.getMessage());
		}

		if (jsonObj != null) {
			verifyMarketStatisticsDisplay();
		} else {
			Assert.assertFalse(false);
		}
	}

	public void verifyMarketStatisticsDisplay() {
		String response = getAPIResponse();
		library.wait(10);
		verifyDropDownCntAndName();
		verifyChartsData(response);
		verifyDownload();
		verifyFacebookShare();
	}
	
	public String getAPIResponse() {
		library.setRequestHeader("X-MData-Key", jsonObj.get("X-MData-Key").toString());
		String res = library.HTTPGet(jsonObj.get("CompareCityAPI").toString());
		return res;
	}
	
	private void verifyDropDownCntAndName() {
		library.scrollToElement("DROP_DOWN_CNT");
		int selectCnt = library.getElementCount("DROP_DOWN_CNT");
		String selectCntJson = jsonObj.get("selectCount").toString();
		Assert.assertTrue((selectCnt == Integer.parseInt(selectCntJson)), "Verified drop down count on the page Sucessfully");
		
		String selectUI1 = library.getTextFrom("SELECT_CITY").toString();
		String selectUI2 = library.getTextFrom("SELECT_PRICE").toString();
		String selectUI3 = library.getTextFrom("SELECT_PERIOD").toString();
		
		Assert.assertEquals(selectUI1, jsonObj.get("select_city").toString());
		Assert.assertEquals(selectUI2, jsonObj.get("select_price").toString());
		Assert.assertEquals(selectUI3, jsonObj.get("select_period").toString());
	}
	
	private void verifyChartsData(String response){
		driver = library.getDriver();
		Select dropdown = new Select(driver.findElement(By.id("mt-city-filter")));
		dropdown.selectByVisibleText(jsonObj.get("CityNameSelect").toString());
		library.wait(5);
		Assert.assertTrue(library.verifyPageContainsElement("CHART_PATH"));
		
		String finalFirstVal="0",finalSecondVal="0",finalDate=null;
		int markers = library.getElementCount("CHART_MARKERS");
		int j=markers;
		for(int i=0;i<markers;i++,j--){
			JSONArray comparableCityData=null,currentCityData=null;
			try {
				JSONParser parser = new JSONParser();
				comparableCityData = (JSONArray) parser.parse(library.getValueFromJson("$.comparableCityData", response).toString());
				currentCityData = (JSONArray) parser.parse(library.getValueFromJson("$.currentCityData", response).toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			library.wait(2);
			WebElement wClickElement = driver.findElement(By.xpath("//*[name()='svg']//*[name()='g' and @class='highcharts-markers highcharts-series-0 highcharts-tracker']//*[name()='path']["+(j)+"]"));
			WebElement ToolTip = driver.findElement(By.xpath("//*[name()='svg']//*[name()='g' and @class='highcharts-tooltip']//*[name()='text'][1]"));
			wClickElement.click();
			wClickElement.click();
			String toolData = ToolTip.getText();
			
			String oneDate[] = toolData.replaceAll(" ", "").split("/");
			String firstValue[] = toolData.replaceAll(" ", "").split((jsonObj.get("CityNameCurrent").toString()+":"))[1].split((jsonObj.get("CityNameSelect").toString()+":"));
			if(oneDate[1].length() == 1){
				finalDate = oneDate[0]+"/0"+oneDate[1];
			}else{
				finalDate = oneDate[0]+"/"+oneDate[1];
			}
			
			
			finalFirstVal = firstValue[0].replaceAll(",", "");
			if(firstValue.length > 1 && firstValue[1] != null){
				finalSecondVal = firstValue[1].replaceAll(",", "");
			}
			
			if(i < currentCityData.size()){
				String currentCityId = library.getValueFromJson("$.currentCityData["+i+"].cityId", response).toString();
				String currentMetric = library.getValueFromJson("$.currentCityData["+i+"].dataOfSpecificMetric", response).toString();
				String currentYears = library.getValueFromJson("$.currentCityData["+i+"].monthYear", response).toString();
			
				Assert.assertEquals(jsonObj.get("CityIdCurrent").toString(), currentCityId);
				Assert.assertEquals(finalFirstVal, currentMetric);
				Assert.assertEquals(finalDate, currentYears);
			}
			
			if(i < comparableCityData.size()){
				String comparableCityId = library.getValueFromJson("$.comparableCityData["+i+"].cityId", response).toString();
				String comparableMetric = library.getValueFromJson("$.comparableCityData["+i+"].dataOfSpecificMetric", response).toString();
				String comparableYears = library.getValueFromJson("$.comparableCityData["+i+"].monthYear", response).toString();
				
				Assert.assertEquals(jsonObj.get("CityIdSelect").toString(), comparableCityId);
				Assert.assertEquals(finalSecondVal, comparableMetric);
				Assert.assertEquals(finalDate, comparableYears);
			}
		}
	}
	
	private void verifyFacebookShare(){
		library.wait(5);
		if(library.getBrowserName().equalsIgnoreCase("Safari")){
			// Click or Javascript click not working in Safari.
			// As per wentao suggested, skipped validation step of facebook share ( Testlink step: 3). 
		}else{
			library.isJSEClicked("FB_ICON");
			library.wait(10);
			library.switchToWindow();
			Assert.assertTrue(library.verifyPageContainsElement("FB_SHARE"));
		}
	}
	
	private void verifyDownload(){
		library.wait(2);
		driver = library.getDriver();
		boolean jsClicked = library.isJSEClicked("DOWNLOAD_ICON");
		library.wait(5);
		boolean downloadCheck = false;
		if(library.getBrowserName().equalsIgnoreCase("Safari") && library.getCurrentPlatform().equalsIgnoreCase("Web")){
			downloadCheck = library.checkFileInFileSystem(jsonObj.get("FileDownLoadPathMac").toString());
			Assert.assertTrue(downloadCheck);
		}else if(library.getCurrentPlatformType().equalsIgnoreCase("IOS_WEB")){
			String chartURL = library.getUrl();
			Assert.assertEquals(chartURL, jsonObj.get("chartDownloadURL").toString());
			library.get((String) jsonObj.get("App-Url"));
			library.wait(5);
			Select dropdown = new Select(driver.findElement(By.id("mt-city-filter")));
			dropdown.selectByVisibleText(jsonObj.get("CityNameSelect").toString());
			library.wait(5);
			library.scrollToElement("DROP_DOWN_CNT");
		}else if(library.getCurrentPlatformType().equalsIgnoreCase("ANDROID_WEB")){
			Assert.assertTrue(jsClicked);
		}else{
			downloadCheck = library.checkFileInFileSystem(jsonObj.get("FileDownLoadPath").toString());
			Assert.assertTrue(downloadCheck);
		}
	}
}
