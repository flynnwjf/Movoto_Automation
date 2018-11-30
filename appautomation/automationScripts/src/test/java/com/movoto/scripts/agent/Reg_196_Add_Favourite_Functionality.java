package com.movoto.scripts.agent;

import java.util.Map;

import org.json.simple.JSONObject;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.MapSearchDataProvider;
import com.movoto.utils.JSONParserForAutomationNG;

/*
 *  @author Puneet.Bohra
 *  Reg-196:Verify add favorite function work correctly
 */

public class Reg_196_Add_Favourite_Functionality extends BaseTest {
	JSONParserForAutomationNG jsonParser;
	JSONObject jsonObj;
	WebDriver wDriver;

	public void init(String jsonPath) {
		try {

			jsonParser = new JSONParserForAutomationNG(jsonPath);
			jsonObj = jsonParser.getNode("mapSearch");

		} catch (Exception exc) {
			System.out.println("Exception in MapSearchForCity:init() ->" + exc.getMessage());
		}
}

	@Test
	@Parameters("dataProviderPath")
	public void verifyAddFavouriteFunctionality(String dataProviderPath) {
		init(dataProviderPath);
		if (jsonObj != null) {
			scenarios.verifyAddFavouriteFunctionality(jsonObj);
		}
		else{
			  Assert.assertFalse(false, "Please check data for provided for script");
		 }
	}
}
