   package com.movoto.scripts.demographics;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.jayway.jsonpath.JsonPath;
import com.jayway.restassured.response.Response;
import com.movoto.data.TestDTO;
import com.movoto.scripts.BaseTest;
import com.movoto.utils.JSONParserForAutomationNG;

import groovyjarjarantlr.collections.List;
import junit.framework.Assert;

public class Reg_228_VerifySearchFunctionWorksWell extends BaseTest{

	JSONParserForAutomationNG jsonParser;
	JSONObject jsonObj;
	WebDriver wDriver;

	public void init(String jsonPath) {

		  library.wait(10);
		try {

			jsonParser = new JSONParserForAutomationNG(jsonPath);
			jsonObj = jsonParser.getNode("verifySearchFunction");

		} catch (Exception exc) {
			System.out.println("Exception in demographics:init() ->" + exc.getMessage());
		}
	}
	

	@Test
	@Parameters("dataProviderPath")
	public void VerifyPsearchfunctionWorks(String dataProviderPath) throws ParseException {
		  library.wait(10);
		init(dataProviderPath);
		if (jsonObj != null) {
			verifySearchworks(jsonObj);
		}

	}
	

 public void verifySearchworks(JSONObject data) throws ParseException
 {
            //Verify text and URL in pop up window is same with demographics API
            scenarios.verifytextandURL(data);

            //Verify City and zipcode displays in the popup box.
            scenarios.verifyzipcodeandCity(data);

            //verify pop up box disappears.
            scenarios.verifypopupdisappears();
 }
}