package com.movoto.scripts.market.trends;

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
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.Assert;

import com.movoto.api.APIResponseProcessor;
import com.movoto.api.pojo.APIResponse;
import com.movoto.api.pojo.Address;
import com.movoto.scripts.BaseTest;
import com.movoto.utils.JSONParserForAutomationNG;

/*
 * Govind Kalria
 */
public class Reg_215_VerifyOpenHousesSectionWorksWell extends BaseTest {
	JSONParserForAutomationNG jsonParser;
	JSONObject jsonObj;
	
	@Test
	@Parameters("dataProviderPath")
	public void verifyNewListingSection(String dataProviderPath){
		try {
	    	jsonParser = new JSONParserForAutomationNG(dataProviderPath);
	    	jsonObj = jsonParser.getNode("openHousesSectionData");
	    } catch (Exception exc) {
	      System.out.println("Exception in BasicInfo:init() ->" + exc.getMessage());
	    }
		
		if (jsonObj != null) {
			scenarios.openHousesSection(jsonObj);
	    } else {
	    	Assert.assertFalse(false);
	    }
	}

  
 }
