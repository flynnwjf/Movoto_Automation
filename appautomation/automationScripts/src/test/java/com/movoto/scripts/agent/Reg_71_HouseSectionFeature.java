package com.movoto.scripts.agent;

import java.util.Map;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.DPPTestCaseDataProvider;

import net.minidev.json.JSONObject;

public class Reg_71_HouseSectionFeature extends BaseTest {

	@Test(dataProvider = "TestDataForReg_71", dataProviderClass = DPPTestCaseDataProvider.class)
	public void houseFeature(Map<String, String> data) throws ParseException {
		if(library.getCurrentPlatform().equals("Web"))
			scenarios.removeAddsPopUp();
		library.setRequestHeader(data.get("headerKey"), data.get("headerValue"));
		String response = library.HTTPGet(data.get("apiString"));
		scrollToElement(DriverLocator.HOMEPAGE_homefeaturetext);
		if(library.getCurrentPlatform().equals("Android") || library.getCurrentPlatform().equals("IOS_WEB")){
			library.wait(2);
			library.click("HOMEPAGE.homeFeatureHeader");
			library.wait(2);
		}
		scrollToElement(DriverLocator.HOMEPAGE_overview);
		library.wait(2);
		library.click("HOMEPAGE.Overview");
		String maintenanceFee = library.getTextFrom("HOMEPAGE.lotDescription");
//		String maintenanceFeeInApi = (String) library.getValueFromJson("$.features[?(@.name=='Overview')].value[?(@.name=='Other')].value[?(@.name=='Maintenance Fee Payment Schedule')].value",response);
		String maintenanceFeeInApi = (String) library.getValueFromJson("$.dpp.features[0].value[1].value[1].value",response);
		Assert.assertEquals(maintenanceFee, maintenanceFeeInApi);
		String builtYear = library.getTextFrom("HOMEPAGE.biultYear");
		String yearBuiltInApi = (String) library.getValueFromJson("$.dpp.features[0].value[0].value[0].value", response);
		Assert.assertEquals(builtYear, yearBuiltInApi);
//		scenarios.javascriptexecutorclick(DriverLocator.HOMEPAGE_overview);

		scrollToElement(DriverLocator.HOMEPAGE_interior);
		library.click("HOMEPAGE.interior");
		String masterBathDesc = library.getTextFrom("HOMEPAGE.flooring");
		String masterBathDescInApi = (String) library.getValueFromJson("$.dpp.features[1].value[3].value[0].value",
				response);
		Assert.assertEquals(masterBathDesc, masterBathDescInApi);
		String interiorFeature = library.getTextFrom("HOMEPAGE.windowTreatment");
		String interiorFeatureInApi = (String) library.getValueFromJson("$.dpp.features[1].value[4].value[0].value",
				response);
		Assert.assertEquals(interiorFeature, interiorFeatureInApi);
//		scenarios.javascriptexecutorclick(DriverLocator.HOMEPAGE_interior);

		scrollToElement(DriverLocator.HOMEPAGE_exterior);
		library.click("HOMEPAGE.exterior");
		String foundationDesc = library.getTextFrom("HOMEPAGE.roof");
		String foundationDescInApi = (String) library.getValueFromJson("$.dpp.features[2].value[0].value[0].value",
				response);
		Assert.assertEquals(foundationDesc, foundationDescInApi);
		String garageDesc = library.getTextFrom("HOMEPAGE.exteriorFeatures");
		String garageDescInApi = (String) library.getValueFromJson("$.dpp.features[2].value[2].value[3].value", response);
		Assert.assertEquals(garageDesc, garageDescInApi);
//		scenarios.javascriptexecutorclick(DriverLocator.HOMEPAGE_exterior);

		scrollToElement(DriverLocator.HOMEPAGE_amenities);
		library.click("HOMEPAGE.amenitiesAndUtilities");
		String fireplaceCount = library.getTextFrom("HOMEPAGE.heating");
		String fireplaceCountInApi = (String) library.getValueFromJson("$.dpp.features[3].value[0].value[0].value",
				response);
		Assert.assertEquals(fireplaceCount, fireplaceCountInApi);
		String poolArea = library.getTextFrom("HOMEPAGE.furnished");
		String poolAreaInApi = (String) library.getValueFromJson("$.dpp.features[3].value[3].value[3].value", response);
		Assert.assertEquals(poolArea, poolAreaInApi);
//		scenarios.javascriptexecutorclick(DriverLocator.HOMEPAGE_amenities);
		
		scrollToElement(DriverLocator.HOMEPAGE_location);
		library.click("HOMEPAGE.location");
		String locationOfProperty = library.getTextFrom("HOMEPAGE.waterfront");
		String locationOfPropertyInApi = (String) library.getValueFromJson("$.dpp.features[4].value[0].value[0].value",
				response);
		Assert.assertEquals(locationOfProperty, locationOfPropertyInApi);
		String directionOfProperty = library.getTextFrom("HOMEPAGE.directions");
		String directionOfPropertyInApi = (String) library.getValueFromJson("$.dpp.features[4].value[3].value[0].value",response);
		Assert.assertEquals(directionOfProperty, directionOfPropertyInApi);
//		scenarios.javascriptexecutorclick(DriverLocator.HOMEPAGE_location);

		if(library.getCurrentPlatform().equals("Android") || library.getCurrentPlatform().equals("IOS_WEB")){
			scrollToElement(DriverLocator.HOMEPAGE_homefeaturetext);
			library.click("HOMEPAGE.homeFeatureHeader");
			boolean ishidden = library.verifyPageNotContainsElement("HOMEPAGE.interior");
			Assert.assertTrue(ishidden);
		}
	}

	/*
	 * Test case specific Reusable scenarios
	 */
	public void scrollToElement(String path) {
		library.wait(2);
		JavascriptExecutor jse = (JavascriptExecutor) library.getDriver();
		WebElement element = library.getDriver().findElement(By.xpath(path));
		jse.executeScript("arguments[0].scrollIntoView();", element);
		Assert.assertTrue(element.isDisplayed());
		;
	}
}
