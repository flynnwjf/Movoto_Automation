package com.movoto.scripts.agent;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.utils.JSONParserForAutomationNG;

public class Reg_236_VerifythatContactAgent extends BaseTest{
	
	JSONParserForAutomationNG jsonParser;
	JSONObject jsonObj;

	public void init(String jsonPath) {
		try {

			jsonParser = new JSONParserForAutomationNG(jsonPath);
			jsonObj = jsonParser.getNode("ContactAgentHotLeadWorks");

		} catch (Exception exc) {
			System.out.println("Exception in demographics:init() ->" + exc.getMessage());
		}
	}

	@Test
	@Parameters("dataProviderPath")
	public void VerifyPropertiesAssignedToSchoolSectionWorks(String dataProviderPath) throws ParseException {
		init(dataProviderPath);
		if (jsonObj != null) {
			verifyThatContactAgentHotLeadWorksWell(jsonObj);
		}
		else
		   {
			   Assert.assertTrue(false,"Jason data not getting properly");
		   }

	}
	
	public void verifyThatContactAgentHotLeadWorksWell(JSONObject data) throws ParseException
	{
		try{
			library.wait(5);
			String email=data.get("Email").toString();
			String name=data.get("Name").toString();
			String phonenumber=data.get("Phone_Number").toString();
			JavascriptExecutor jse=(JavascriptExecutor)library.getDriver();
			if(library.getCurrentPlatform().equalsIgnoreCase("Android") || library.getCurrentPlatform().equalsIgnoreCase("IOS_WEB"))
		    {
				jse.executeScript("document.getElementsByClassName('text')[1].click();");
				library.wait(5);
				library.clear("HOMEPAGE.emailField");
				library.wait(5);
				library.typeDataInto(email, "HOMEPAGE.emailField");
				library.wait(5);
				library.clear("HOMEPAGE.nameFiled");
				library.wait(5);
				library.typeDataInto(name, "HOMEPAGE.nameFiled");
				library.wait(5);
				library.click("HOMEPAGE.phoneFiled");
				library.wait(5);
				library.clear("HOMEPAGE.phoneFiled");
				library.wait(5);
				library.typeDataInto(phonenumber, "HOMEPAGE.phoneFiled");
				library.wait(5);
				jse.executeScript("document.getElementsByClassName('btn orange responsive')[4].click()");
				library.wait(2);
				Assert.assertTrue(library.verifyPageContainsElement("HOMEPAGE.text"), "Thank you is not displayed");
				
		    }
			else
			{
				library.clear("HOMEPAGE.emailField");
				library.wait(2);
				library.typeDataInto(email, "HOMEPAGE.emailField");
				library.wait(2);
				library.clear("HOMEPAGE.nameFiled");
				library.wait(2);
				library.typeDataInto(name, "HOMEPAGE.nameFiled");
				library.wait(2);
				library.clear("HOMEPAGE.phoneFiled");
				library.wait(2);
				library.typeDataInto(phonenumber, "HOMEPAGE.phoneFiled");
				library.wait(2);
				library.click("HOMEPAGE.clickContact");
				library.wait(5);
				Assert.assertTrue(library.verifyPageContainsElement("HOMEPAGE.text"), "Thank you is not displayed");
			}
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
