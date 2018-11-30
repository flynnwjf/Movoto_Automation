package com.movoto.scripts.demographics;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.movoto.api.APIResponseProcessor;
import com.movoto.api.pojo.APIResponse;
import com.movoto.api.pojo.Address;
import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.AgentTestDataProvider;
import com.movoto.scripts.data.DemographicsDataProvider;
import com.movoto.utils.JSONParserForAutomationNG;


public class Reg_229_VerifyOpenHousesSectionWorksWell extends BaseTest {

	JSONParserForAutomationNG jsonParser;
	JSONObject jsonObj;
	WebDriver wDriver;

	@Test
	@Parameters("dataProviderPath")
	public void VerifyPropertiesAssignedToSchoolSectionWorks(String dataProviderPath) throws ParseException {
		try {
			jsonParser = new JSONParserForAutomationNG(dataProviderPath);
			jsonObj = jsonParser.getNode("openhousesection");
		} catch (Exception exc) {
			System.out.println("Exception in demographics:init() ->" + exc.getMessage());
		}

		if (jsonObj != null) {
			scenarios.openHousesSection(jsonObj);
		}
	}

	
}
