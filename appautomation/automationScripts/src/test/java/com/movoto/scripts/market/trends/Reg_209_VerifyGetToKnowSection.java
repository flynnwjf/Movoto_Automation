package com.movoto.scripts.market.trends;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.AgentTestDataProvider;
import com.movoto.scripts.data.DemographicsDataProvider;
import com.movoto.utils.JSONParserForAutomationNG;

import junit.framework.Assert;

public class Reg_209_VerifyGetToKnowSection extends BaseTest {

	JSONParserForAutomationNG jsonParser;
	JSONObject jsonObj;
	WebDriver wDriver;

	public void init(String jsonPath) {
		try {

			jsonParser = new JSONParserForAutomationNG(jsonPath);
			jsonObj = jsonParser.getNode("gettoknow");

		} catch (Exception exc) {
			System.out.println("Exception in demographics:init() ->" + exc.getMessage());
		}
	}

	@Test
	@Parameters("dataProviderPath")
	public void VerifyGettoKnowSectionWorks(String dataProviderPath) {
		init(dataProviderPath);
		Assert.assertTrue("Json data is null", jsonObj != null);
		VerifyGettoKnowSectionWorks(jsonObj);

	}

	public void VerifyGettoKnowSectionWorks(JSONObject data) {
        String response=scenarios.getResponseGetToKnowSectionApi(data);

		// Verify "Get to Know <City>" is displayed
		// Verify the first 4 articles' title and url are matched to the first 4
		// items of <GettoKnow_API> response
		scenarios.VerifyTitleandUrl(data,response);

		//Verify last article's title and url are matched to the 5th items of <GettoKnow_API> response
		scenarios.VerifyLastarticletitle(data,response);

		//Verify the 1st articles' title and url are matched to the 1st items of <GettoKnow_API> response
		scenarios.VerifyFirstarticletitle(data,response);
	}


	}
