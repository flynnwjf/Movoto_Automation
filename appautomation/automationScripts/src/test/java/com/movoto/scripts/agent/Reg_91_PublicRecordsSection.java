package com.movoto.scripts.agent;

import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.DPPTestCaseDataProvider;

import junit.framework.Assert;

public class Reg_91_PublicRecordsSection extends BaseTest{
	
	@Test(dataProvider = "TestDataForReg_91", dataProviderClass = DPPTestCaseDataProvider.class)
	public void publicRecordsSection(Map<String, String> data){
		System.out.println("Reg_91 is running...");
		if(library.getCurrentPlatform().equals("Web"))
			scenarios.removeAddsPopUp();
		
		library.setRequestHeader(data.get("headerKey"), data.get("headerValue"));
		String response = library.HTTPGet(data.get("apiString"));
		
		scrollToAndVerifyText(DriverLocator.HOMEPAGE_publicRecord);
		library.wait(2);
		if(library.getCurrentPlatform().equals("Android") || library.getCurrentPlatform().equals("IOS_WEB"))
			library.click("HOMEPAGE.publicRecords");
		
		boolean isbasicInfo = library.verifyPageContainsElement("HOMEPAGE.basicInfo");
		Assert.assertTrue(isbasicInfo);
		boolean isHouseFeatures = library.verifyPageContainsElement("HOMEPAGE.houseFeatures");
		Assert.assertTrue(isHouseFeatures);
		scrollToAndVerifyText(DriverLocator.HOMEPAGE_basicInfo);
		library.wait(2);
		library.click("HOMEPAGE.basicInfo");
		
		String areaOfBuilding = library.getTextFrom("BASICINFO.buildingArea");
		areaOfBuilding = areaOfBuilding.replace(",", "");
		areaOfBuilding = areaOfBuilding.split(" ")[0];
		int buildingArea = Integer.parseInt(areaOfBuilding);
		Double areaOfBuildingInApi = (Double) library.getValueFromJson("$.buildingArea", response);
		double area = areaOfBuildingInApi;
		int area11 = (int)area;
		Assert.assertEquals(buildingArea, area11);
		
		String soldDateInApi = (String)library.getValueFromJson("$.lastSaleDate", response);
		soldDateInApi = soldDateInApi.split(" ")[0];
		String soldDateValue = 	soldDateInApi.split("-")[2]+ "/" +soldDateInApi.split("-")[1]+ "/" +soldDateInApi.split("-")[0];
		boolean isSoldDateCorrect = library.verifyPageContainsElement("xpath->.//*[@id='publicRecord_1']//span[contains(text(),'Last Sale Date')]/../span[contains(text(),'"+soldDateValue+"')]");
		library.wait(2);
		
		scrollToAndVerifyText(DriverLocator.HOMEPAGE_houseFeature);
		library.click("HOMEPAGE.houseFeatures");
		library.wait(2);
		
		String garageTypeInUI =library.getTextFrom("BASICINFO.garageType");
		String garageTypeInApi = (String) library.getValueFromJson("$.garageType", response);
		Assert.assertEquals(garageTypeInUI, garageTypeInApi);
		
		String pooltypeInUI = library.getTextFrom("BASICINFO.poolType");
		String poolTypeInApi = (String)library.getValueFromJson("$.pool", response);
		Assert.assertEquals(pooltypeInUI, poolTypeInApi);
		scenarios.javascriptexecutorclick("HOMEPAGE.houseFeatures");
		/*scrollToAndVerifyText(DriverLocator.HOMEPAGE_publicRecord);
		library.click("HOMEPAGE.publicRecords");
		Assert.assertTrue(library.verifyPageNotContainsElement("HOMEPAGE.houseFeatures"));	*/
	}
	
		//Scroll to and verify Text
		public void scrollToAndVerifyText(String locator){
			library.wait(3);
			JavascriptExecutor jse = (JavascriptExecutor) library.getDriver();
			WebElement element = library.getDriver().findElement(By.xpath(locator));
			jse.executeScript("arguments[0].scrollIntoView();", element);
			element.isDisplayed();
			
		}
}
