package com.movoto.scripts.agent;

import java.util.Map;

import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.MapSearchDataProvider;
import com.movoto.utils.JSONParserForAutomationNG;

import junit.framework.Assert;

public class Reg_197_Save_Search_Functionality extends BaseTest {
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
	public void verifySaveSearchFunctionality(String dataProviderPath) throws InterruptedException {
		init(dataProviderPath);
		if (jsonObj != null) {
			scenarios.DeleteTrash(jsonObj); // Preconditions
			scenarios.verifySaveSearchFunctionality(jsonObj);
		}
		else{
			  Assert.assertFalse(false);
		  }
	}
}