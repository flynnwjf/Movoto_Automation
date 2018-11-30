package com.movoto.scripts.agent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.utils.JSONParserForAutomationNG;

public class Reg_111_HotLead extends BaseTest {
	JSONParserForAutomationNG jsonParser;
	JSONObject jsonObj;
	WebDriver wDriver;  


	@Test
	@Parameters("dataProviderPath")
	public void verifyMarketStatistics(String dataProviderPath) throws ParseException {
		try {

			jsonParser = new JSONParserForAutomationNG(dataProviderPath);
			jsonObj = jsonParser.getNode("HotLead");

		} catch (Exception exc) {
			System.out.println("Exception in HotLead() ->"
					+ exc.getMessage());
		}
		if (jsonObj != null) {
			verifyMarketStatistics(jsonObj);
		}
		else{
			Assert.assertTrue(false, "Please check data for provided for script");
		}
	}


	public void verifyMarketStatistics(JSONObject Data) throws ParseException {

		// Preconditions Not Login before executing the test steps
		
		if (library.getCurrentBrowser().equalsIgnoreCase("Safari")) {
			library.wait(5);
			scenarios.remove_PopUp();
			library.wait(5);
			logOutForHotLead(Data);
			library.wait(5);
			scenarios.remove_PopUp();
			library.wait(5);
			library.get((String) Data.get("App-Url"));
			library.wait(5);
		}

		String response = setHeaderAndGetResponse(Data, "BasicInfo_API");

		// Step - 1
		// Step - 1.1.1
		// Verify Price is the same with API response
		String priceAPI = String.valueOf(library.getValueFromJson("$.dpp.listPrice", response));

		if (library.getCurrentPlatform().equals("Android")|| library.getCurrentPlatform().equals("IOS_WEB")) {
			String price=library.getAttributeOfElement("content", "HOMEPAGE.priceElement");
			Assert.assertEquals(priceAPI, price);
		}else
		{
			String priceWithComma = library.getTextFrom("HOMEPAGE.priceElement");
			System.out.println("UI - Price : " + priceWithComma );
			String price = priceWithComma.replace(",", "");
			price = price.replace("$", "");
			Assert.assertEquals(priceAPI, price.trim());
		}
		// Step - 1.1.2
		// Verify Estimate value is the same with API response
		/*if (library.getCurrentPlatform().equals("Android")|| library.getCurrentPlatform().equals("IOS_WEB")) {
			library.wait(5);
			library.scrollToElement("HOMEPAGE.homeDescriptionA");
			library.wait(5);
			String estimateUIA = library.getTextFrom("HOMEPAGE.estimateA");
			library.wait(5);
			String estimatePriceUIA = estimateUIA.replaceAll("[Estimate ,$]", "");
			String estimateAPI =setHeaderAndGetResponse(jsonObj,"EstimateValue_API");
			Assert.assertEquals(estimateAPI, estimatePriceUIA);
		}else{
			String estimateUI = library.getTextFrom("HOMEPAGE.estimateElement");
			String estimatePriceUI = estimateUI.replaceAll("[Estimate ,$]", "");
			String estimateAPI =setHeaderAndGetResponse(jsonObj,"EstimateValue_API");
			Assert.assertEquals(estimateAPI, estimatePriceUI);
		}*/

		// Step - 1.1.3
		// Mortgage is same with principle & interests of mortgage calculator
		if (library.getCurrentPlatform().equals("Android")|| library.getCurrentPlatform().equals("IOS_WEB")) {
			library.wait(5);
			String MortgageUIFullData = library.getTextFrom("HOMEPAGE.mortgage");
			library.wait(5);
			String MortagageuicalculatorPI = library.getTextFrom("HOMEPAGE.pieChartContainer");
			MortagageuicalculatorPI = MortagageuicalculatorPI.replace(" ", "");
			Assert.assertTrue(MortgageUIFullData.contains(MortagageuicalculatorPI));
		}else{
			String MortgageUIFullData = library.getTextFrom("HOMEPAGE.agentFiledmortgageUIFullData");
			library.wait(5);
			String MortagageuicalculatorPI = library.getTextFrom("HOMEPAGE.pieChartContainer");
			library.wait(5);
			MortagageuicalculatorPI = MortagageuicalculatorPI.replace(" ", "");
			library.wait(5);
			Assert.assertTrue(MortgageUIFullData.contains(MortagageuicalculatorPI));
		}
		
		// Step - 1.2
		// Verify default message starts with "I am interested in [streetNumber]"([streetNumber] is from api response)
		if (library.getCurrentPlatform().equals("Android")|| library.getCurrentPlatform().equals("IOS_WEB")) {
			// Vivian - For checking “I’m interesting***”, skip it
			// Mail dated - 24th Jan 2017
		}else{
			String messageContent = library.getTextFrom("HOMEPAGE.msgContent");
			Assert.assertTrue(messageContent.trim().startsWith(Data.get("Message Content").toString()));
			String stringOfZip = (String) library.getValueFromJson("$.dpp.mlsAddress.streetNumber", response);
			Assert.assertTrue(messageContent.contains(stringOfZip));
		}
		// Step - 1.3
		// Verify AgentPhoto/"Movoto [City_FromAPI] Agent"/Stars is displayed
		// Verify AgentPhoto is displayed
		if (library.getCurrentPlatform().equals("Android")|| library.getCurrentPlatform().equals("IOS_WEB")) {
			library.wait(5);
			library.scrollToElement("HOMEPAGE.chatImage");
			library.wait(5);
			library.click("HOMEPAGE.chatImageClick");
			library.verifyPageContainsElement("HOMEPAGE.agentPhoto");
			
			String info = library.getTextFrom("HOMEPAGE.cityAndMovotoText");
			library.wait(5);
			String stringOfCity = (String) library.getValueFromJson("$.dpp.address.city", response);
			library.wait(5);
			Assert.assertTrue(info.contains(stringOfCity));
			library.wait(5);
			Assert.assertTrue(library.verifyPageContainsElement("HOMEPAGE.agentStars"));
		}else 
		{
			Assert.assertTrue(library.verifyPageContainsElement("HOMEPAGE.agentPhoto"));
			// Verify Movoto [City_FromAPI] Agent
			String agentFieldContactMovotoText = library.getTextFrom("HOMEPAGE.agentFieldContactMovotoText");
			String agentFieldCityText = library.getTextFrom("HOMEPAGE.agentFieldCityText");
			String contactMovotoTextPlusCityText = (agentFieldContactMovotoText + " " + agentFieldCityText);
			String stringOfCity = (String) library.getValueFromJson("$.dpp.address.city", response);
			Assert.assertTrue(contactMovotoTextPlusCityText.contains(stringOfCity));

			// Verify Stars is displayed  HOMEPAGE.agentFieldStars
			Assert.assertTrue(library.verifyPageContainsElement("HOMEPAGE.agentFieldStars"));
		}
		// Step - 1.4
		// Verify disclaimer is <CommonDisclaimer>  HOMEPAGE.commonDisclaimer
			String commonDisclaimerWebPage = library.getTextFrom("HOMEPAGE.commonDisclaimer");
			Assert.assertEquals(Data.get("Common Disclaimer").toString(), commonDisclaimerWebPage.trim());

		// Step - 2
		// Input <Email><Name><Phone> to the box and click "Contact [City_fromAPI] Agent" button
		// Thank you is present
		inputEmailNamePhone(Data);

		// Step - 3
		// Click "Log in" button and login with account the case's summary mention
		// Verify people icon as appears on the menu.
		loginFromPopUp(Data);
	}

	//Set Header and get Response
	public String setHeaderAndGetResponse(JSONObject data, String apiUrl){
		String response=null;
		JSONObject resp=null;
		JSONParser jParser=new JSONParser();
		
		String contentType = String.valueOf(data.get("ContentType"));
		String API = String.valueOf(data.get(apiUrl).toString());
		library.setRequestHeader("X-MData-Key", data.get("X-MData-Key").toString());
		library.setRequestHeader("Accept-Encoding","application/gzip");
		library.setRequestHeader("Content=Length","36");
		library.setRequestHeader("Connection","Keep-Alive");
		library.setRequestHeader("User-Agent","Apache-HttpClient/4.1.1");
		library.setRequestHeader("Host","service.ng.movoto.net");
		library.setRequestHeader("Cache-Control", "no-cache");
		library.setContentType(contentType);
		
		if(apiUrl == "BasicInfo_API"){
			response = library.HTTPGet(API);
		}else{
			response = library.HTTPPost(API, (String)data.get("PostBody"));
			try{
				resp=(JSONObject)jParser.parse(response);
				response=Long.toString((long)resp.get((String)data.get("PostBody")));
			}catch(ParseException pe){
				System.out.println("ParseException in Reg_111_HoteLead:getResponseHotLeadEstimateApi===>>>"+pe.getMessage());
			}
		}
		return response;
	}

	// Preconditions Not Login before executing the test steps
	public void logOutForHotLead(JSONObject data) {
		if(library.getCurrentBrowser().equalsIgnoreCase("Safari")){
			library.get((String) data.get("LogoutUrl"));
		}
	}

	public void inputEmailNamePhone(JSONObject data){
		if (library.getCurrentPlatform().equals("Android")|| library.getCurrentPlatform().equals("IOS_WEB")) {
			// Do nothing
		}else{
			library.scrollToElement("HOMEPAGE.agentPhoto");
			library.wait(2);
		}
		library.clear("HOMEPAGE.HotleadEmail");
		library.wait(1);
		library.typeDataIntoWithJavaScript(data.get("Mail_ID").toString(), "HOMEPAGE.HotleadEmail");
		library.wait(1);
		library.clear("HOMEPAGE.hotLeadName");
		library.typeDataInto(data.get("Name Of Visitor").toString(), "HOMEPAGE.hotLeadName");
		library.clear("HOMEPAGE.hotLeadNumber");
		library.wait(1);
		library.typeDataInto(data.get("Contact Number").toString(), "HOMEPAGE.hotLeadNumber");
		library.wait(2);
		if (library.getCurrentPlatform().equals("Android")|| library.getCurrentPlatform().equals("IOS_WEB")) {
			library.wait(2);
			library.click("HOMEPAGE.agentContactButtonA");
//			library.wait(1);
			library.waitForElement("HOMEPAGE.thankyouLogin");
		}else{
			library.typeDataInto(Keys.chord(Keys.TAB), "HOMEPAGE.msgContent");
			library.wait(10);
			WebElement we = library.findElement("HOMEPAGE.agentContactButton");
			library.wait(10);
			we.sendKeys(Keys.chord(Keys.ENTER));
			library.wait(10);
		}
		// Thank you is present
		Assert.assertTrue(library.verifyPageContainsElement("HOMEPAGE.thankyouLogin"));
		library.wait(2);
	}

	public void loginFromPopUp(JSONObject data){
		if (library.getCurrentPlatform().equals("Android")|| library.getCurrentPlatform().equals("IOS_WEB")) {
			library.click("HOMEPAGE.link");
			library.wait(10);
			library.click("HOMEPAGE.menubutton");
			library.wait(5);
			library.click("HOMEPAGE.loginLinkA");
			library.wait(5);
		}else{
			library.wait(2);
			library.click("HOMEPAGE.agentThankYouLogin");
			library.wait(2);
		}
		scenarios.LoginCredential(data);
		
		if (library.getCurrentPlatform().equals("Android")|| library.getCurrentPlatform().equals("IOS_WEB")) {
			library.wait(5);
			library.click("HOMEPAGE.menubutton");
			library.wait(2);
			// Verify people icon as appears on the menu.   
			Assert.assertTrue(library.verifyPageContainsElement("LOGINPAGE.userIcon"));
		} else {
			scenarios.removeAddsPopUp();
			library.wait(5);
			// Verify people icon as appears on the menu.   
			Assert.assertTrue(library.verifyPageContainsElement("HOMEPAGE.agentUserIcon"));
		}
	}
}

