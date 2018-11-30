package com.movoto.scripts.agent;

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

public class Reg_77_GetToKnowSection extends BaseTest {

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
        String response=getResponseGetToKnowSectionApi(data);
        scenarios.remove_PopUp();
		// Verify "Get to Know <City>" is displayed
		// Verify the first 4 articles' title and url are matched to the first 4
		// items of <GettoKnow_API> response
		scenarios.VerifyTitleandUrl_API(data,response);
	}
		@SuppressWarnings("unchecked")
		String getResponseGetToKnowSectionApi(JSONObject data)
		{
			String contentType = String.valueOf(data.get("ContentType"));
			String GettoKnow_API = String.valueOf(data.get("Gettoknow_API_DPP"));
			library.setContentType(contentType);
			Map<String, Object> apidata = new HashMap<>();
			apidata = (Map<String, Object>) data.get("Postdata_DPP");
			String response = library.HTTPPost(GettoKnow_API, apidata);
			return response;
		}	

	}