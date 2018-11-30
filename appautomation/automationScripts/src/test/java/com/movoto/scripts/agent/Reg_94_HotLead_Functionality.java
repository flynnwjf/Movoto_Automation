package com.movoto.scripts.agent;

import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.MapSearchDataProvider;
import com.movoto.utils.JSONParserForAutomationNG;

public class Reg_94_HotLead_Functionality extends BaseTest {
	JSONParserForAutomationNG jsonParser;
	JSONObject jsonObj;
	WebDriver wDriver;

	public void init(String jsonPath) {
		try {
			jsonParser = new JSONParserForAutomationNG(jsonPath);
			jsonObj = jsonParser.getNode("hotLead");
		} catch (Exception exc) {
			System.out.println("Exception in MapSearchForCity:init() ->" + exc.getMessage());
		}
	}

	@Test
	@Parameters("dataProviderPath")
	public void verifyFilterFunctionality(String dataProviderPath) throws ParseException {
		init(dataProviderPath);
		if (jsonObj != null) {
			String mapSearchAPIResponse = scenarios.getApiResultsForHotlead(jsonObj);
			library.wait(20);
			scenarios.hotLeadFunctionality(mapSearchAPIResponse, jsonObj);
		} else {
			Assert.assertFalse(false);
		}
	}
}