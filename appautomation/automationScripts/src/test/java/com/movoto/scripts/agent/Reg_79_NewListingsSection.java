package com.movoto.scripts.agent;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.xerces.util.SynchronizedSymbolTable;
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
import com.movoto.scripts.data.DPPTestCaseDataProvider;
import com.movoto.scripts.data.MapSearchDataProvider;
import com.movoto.utils.JSONParserForAutomationNG;
import com.sun.jna.Library;

public class Reg_79_NewListingsSection extends BaseTest {

	JSONParserForAutomationNG jsonParser;
	JSONObject jsonObj;
	WebDriver wDriver;

	@Test
	@Parameters("dataProviderPath")
	public void VerifyNewListingsSectionWorksWell(String dataProviderPath) throws ParseException {
		init(dataProviderPath);
		if (jsonObj != null) {
			VerifyNewListingsSectionWorksWell(jsonObj);

		} else {
			Assert.assertTrue(false, "Jason data not getting properly");
		}
	}

	public void VerifyNewListingsSectionWorksWell(JSONObject Data) throws ParseException {
		verifyNewListingDisplayed();
		String response = getApiHeaderAndGetResponse(Data, "NewListing_API");
		verifyNewListingPropertyWithApi(Data, 4, response);
		verifyNewListingUrl();

	}

	public void verifyNewListingDisplayed() {
		Assert.assertTrue(library.verifyPageContainsElement("HOMEPAGE.newlisting"), "New Listings is not displayed");
		;// Verify "New Listings in" is displayed
	}

	public void verifyNewListingPropertyWithApi(JSONObject Data, int numberofcard, String response)
			throws ParseException {

		APIResponseProcessor apiResponseProcessor = new APIResponseProcessor();
		APIResponse apiResponse = apiResponseProcessor.processResponse(response);
		Address address = null;
		HashMap<String, JSONObject> listingsMap = apiResponse.getListings();

		if (library.getCurrentPlatform().equalsIgnoreCase("Android")) {
			numberofcard = 1;
			library.scrollToElement("HOMEPAGE.newlisting");
			library.wait(5);
		}
		// int uielementcount=library.getElementCount("NEWLISTSECTION.card");

		for (int i = 0; i < numberofcard; i++) {
			String propertyCardStreetAddressUI = library.getTextFrom(
					"xpath->(.//*[@id='newListingPanel']//span[@itemprop='streetAddress'])["+ (i+1) +"]");
			boolean isItemFound = false;
			String propertyCardStreetAddressAPI = null;
			for (int k = 0; k < listingsMap.size(); k++) {
				apiResponse = apiResponseProcessor.getAddress((JSONObject) listingsMap.get(k + ""));
				address = apiResponse.getAddress();
				propertyCardStreetAddressAPI = address.getAddressInfo();
				apiResponse.getListPrice();
				if (propertyCardStreetAddressAPI.equalsIgnoreCase(propertyCardStreetAddressUI)) {
					isItemFound = true;

					String price = library.getTextFrom("xpath->(.//*[@id='newListingPanel']//span[@class='price'])[" +(i+1)+"]");
					price = price.replaceAll(",", "");
					price = price.substring(1);
					int propertyCardPropertypriceUI = Integer.parseInt(price);
					int propertyCardPropertypriceAPI = apiResponse.getListPrice();
					Assert.assertTrue(propertyCardPropertypriceUI == propertyCardPropertypriceAPI);

					String Bed = library.getTextFrom("xpath->(.//*[@id='newListingPanel']//div[@class='top-base-info']/span[2])["+ (i+1) +"]");
					int propertyCardPropertyBedUI = Integer.parseInt(Bed);
					int propertyCardPropertyBedAPI = apiResponse.getBedrooms();
					Assert.assertEquals(propertyCardPropertyBedUI, propertyCardPropertyBedAPI);
					String BathUI = library.getTextFrom("xpath->(.//*[@id='newListingPanel']//div[@class='top-base-info']/span[3])["+ (i+1) +"]");
					if (!BathUI.contains(".")) {

						double BathAPI = Double.parseDouble(apiResponse.getBathroomsTotal());
						Assert.assertEquals(Double.parseDouble(BathUI), BathAPI);
					} else {

						Float BathAPI = Float.parseFloat(apiResponse.getBathroomsTotal());
						Assert.assertEquals(Float.parseFloat(BathUI), BathAPI);
					}
					propertyCardStreetAddressUI = library.getTextFrom("xpath->(.//*[@id='newListingPanel']//span[@itemprop='streetAddress'])["+ (i+1) +"]");
					propertyCardStreetAddressAPI = address.getAddressInfo();
					Assert.assertEquals(propertyCardStreetAddressUI, propertyCardStreetAddressAPI);

					String propertyCardCityUI = library.getTextFrom("xpath->(.//*[@id='newListingPanel']//span[@class='address-city'])["+ (i+1) +"]");
					String propertyCardCityAPI = address.getCity();
					Assert.assertEquals(propertyCardCityUI, propertyCardCityAPI);

					String cardurlUI = library.getAttributeOfElement("href","xpath->(.//*[@id='newListingPanel']//a[@class='imgmask'])["+ (i+1) +"]");
					String cardurlAPI = apiResponse.getListingUrl();
					Assert.assertTrue(cardurlUI.contains(cardurlAPI), "Card Url matching with API response");

				}
			}
			if (isItemFound == false) {
				Assert.assertFalse(false, "UI value not matching with API");
				break;
			}

		}
	}

	public void init(String jsonPath) {
		try {
			jsonParser = new JSONParserForAutomationNG(jsonPath);
			jsonObj = jsonParser.getNode("NewListingsSection");
		} catch (Exception exc) {
			System.out.println("Exception in BasicInfo:init() ->" + exc.getMessage());
		}
	}

	public String getApiHeaderAndGetResponse(JSONObject Data, String apiName) {
		library.setRequestHeader("X-MData-Key", Data.get("X-MData-Key").toString());
		String responseBasicInfo = library.HTTPGet(Data.get(apiName).toString());
		return responseBasicInfo;
	}

	public void verifyNewListingUrl() {
		library.wait(5);
		Assert.assertTrue(library.verifyPageContainsElement("HOMEPAGE.textDisplay"), "New listings is not displayed");
		String url = library.getAttributeOfElement("href", "HOMEPAGE.textDisplay");
		String urlPart = (String) jsonObj.get("NewListUrl");
		Assert.assertTrue(url.contains(urlPart), "Url is not matching");

	}

}