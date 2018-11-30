package com.movoto.scripts.dsp;

import java.util.List;
import java.util.ListIterator;

import org.apache.poi.hssf.record.ScenarioProtectRecord;
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

import com.movoto.scripts.BaseTest;
import com.movoto.utils.JSONParserForAutomationNG;

public class Reg_208_VerifyOpenHouseSectionWorks extends BaseTest {
	JSONParserForAutomationNG jsonParser;
	JSONObject jsonObj;
	
	@Test
	@Parameters("dataProviderPath")
	public void verifyNewListingSection(String dataProviderPath){
		try {
	    	jsonParser = new JSONParserForAutomationNG(dataProviderPath);
	    	jsonObj = jsonParser.getNode("openhousesection");
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
