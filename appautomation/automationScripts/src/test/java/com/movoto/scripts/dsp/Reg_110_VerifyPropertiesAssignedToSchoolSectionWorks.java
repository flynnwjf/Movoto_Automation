package com.movoto.scripts.dsp;


import org.json.simple.JSONObject;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.movoto.scripts.BaseTest;
import com.movoto.utils.JSONParserForAutomationNG;

public class Reg_110_VerifyPropertiesAssignedToSchoolSectionWorks extends BaseTest {
	JSONParserForAutomationNG jsonParser;
	JSONObject jsonObj;
	WebDriver wDriver;
	
	public void init(String jsonPath) {
		try {

			jsonParser = new JSONParserForAutomationNG(jsonPath);
			jsonObj = jsonParser.getNode("assignedpropertiessection");
		} catch (Exception exc) {
			System.out.println("Exception in Assigned Properties Section:init() ->"+ exc.getMessage());
		}
	}

	@Test
	@Parameters("dataProviderPath")
	public void VerifyPropertiesAssignedToSchoolSectionWorks(
			String dataProviderPath) {
		init(dataProviderPath);
		if (jsonObj != null) {
			verifyPropertiesAssignedToSchoolSectionWorks(jsonObj);
		} else {
			Assert.assertTrue(false, "Please check data for provided for script");
		}
	}

	public void verifyPropertiesAssignedToSchoolSectionWorks(JSONObject data) {
		// Delete all the added favorites houses in favorites page for the test
		// account.
		scenarios.loginAndDeleteFavouriteProperty(data);

		// Verify "Properties Assigned to <School Name>" is displayed
		verifyPropertiesAssignedToSchoolIsDisplayed(data);

		// Verify Price/PropertyType /Bedrooms /Bathrooms /Address /City/CardURL
		// /photo is the same as <AssignedProperties_API> response
		verifyPricePropertyTypeBedroomsBathroomsAddressCityCardUrlPhoto(data);

		// Verify the last 2 properties' address is matched to the 5th/6th
		// property of <AssignedProperties_API> response
		verifyTheLastTwoProperties(data);

		// Verify the 1st property's address is matched to 1st property of
		// <AssignedProperties_API> response
		verifyPropertyAddressOfFirstPropertyUIMatchesPropertyAddressOfFirstPropertyAPI(data);

		// Verify login window pops up,
		loginFavIcon(data);

		// Verify the add favorite icon in the card turns red
		verifyTheAddFavoriteIconInTheCardTurnsRed();

		// Verify the detail info is hided
		verifyTheDetailInfoIsHided();

		// Verify the favorite house's address is same with 1st card on step1
		verifyTheFavoriteHouseAddressIsSameWithFirstCard(data);
	}

	// API functions
	public String getApiHeaderAndGetResponse(JSONObject Data, String apiName) {
		library.setRequestHeader("X-MData-Key", Data.get("X-MData-Key")
				.toString());
		String responseBasicInfo = library.HTTPGet(Data.get(apiName)
				.toString());
		return responseBasicInfo;
	}

	// Function to Verify Properties Assigned To School Is Displayed
	public void verifyPropertiesAssignedToSchoolIsDisplayed(JSONObject Data) {
		library.get((String) Data.get("App-Url"));
		library.wait(20);
		library.scrollToElement("SCHOOLPAGEPROPERTIESASSIGNED.propertyassignedtotext");
		library.wait(5);
		String response = getApiHeaderAndGetResponse(Data, "BasicInfo_API");
		library.wait(5);
		String sPropertiesSchoolName = library
				.getTextFrom("SCHOOLPAGEPROPERTIESASSIGNED.propertiesassignedheaderschoolname");
		String SchoolNameAPI = library.getValueFromJson("$.name", response)
				.toString();
		Assert.assertEquals(SchoolNameAPI, sPropertiesSchoolName);
	}

	// Common function for
	// 1. verifyPricePropertyTypeBedroomsBathroomsAddressCityCardUrlPhoto,
	// 2. verifyPropertyAddressOfFirstPropertyUIMatchesPropertyAddressOfFirstPropertyAPI
	public void verifyPropertyDetails() {
		if (library.getCurrentPlatform().equals("Android")||library.getCurrentPlatform().equals("IOS_WEB")) {
			JavascriptExecutor jse = (JavascriptExecutor) library.getDriver();
			WebElement element = library
					.findElement("SCHOOLPAGEPROPERTIESASSIGNED.propertyassignedtotext");
			jse.executeScript("arguments[0].scrollIntoView(true);", element);
			jse.executeScript("arguments[0].scrollIntoView(true);", element);
		} else
		{
			library.wait(5);
			library.scrollToElement("SCHOOLPAGEPROPERTIESASSIGNED.propertyassignedtotext");
		}
	}

	// Function to Verify Property details of First 3 properties
	// Android - Only first property details is taken care
	// Web - First 3 property details are taken care, which is ESLE part
	public void verifyPricePropertyTypeBedroomsBathroomsAddressCityCardUrlPhoto(
			JSONObject Data) {
		verifyPropertyDetails();
		String response = getApiHeaderAndGetResponse(Data, "AssignedProperties_API");
		if (library.getCurrentPlatform().equals("Android")||library.getCurrentPlatform().equals("IOS_WEB")) {
			propertiesCount(0, 1, 0, response);
		} else {
			propertiesCount(0, 3, 0, response);
		}
	}

	// Function to Verify Property details of Last 2 property details
	// Android - Not taken care
	// Web - Last 2 property details are taken care
	public void verifyTheLastTwoProperties(JSONObject Data) {
		// Verify the last 2 properties' address is matched to the 5th/6th
		// property of <AssignedProperties_API> response
		if (library.getCurrentPlatform().equals("Android")||library.getCurrentPlatform().equals("IOS_WEB")) {
			// do nothing
		}
		else
		{
			library.scrollToElement("SCHOOLPAGEPROPERTIESASSIGNED.propertyassignedtotext");
			library.waitForElement("SCHOOLPAGEPROPERTIESASSIGNED.iconrightangle");
			library.isJSEClicked("SCHOOLPAGEPROPERTIESASSIGNED.iconrightangle");
			String response = getApiHeaderAndGetResponse(Data, "AssignedProperties_API");
			propertiesCount(1, 2, 3, response);
		}
	}

	// Function to Verify Property details of first property address with API
	// address
	// Android - Taken care
	// Safari - Taken care
	// Web - Taken care
	public void verifyPropertyAddressOfFirstPropertyUIMatchesPropertyAddressOfFirstPropertyAPI(
			JSONObject Data) {
		verifyPropertyDetails();
		library.wait(5);
		String response = getApiHeaderAndGetResponse(Data, "AssignedProperties_API");
		propertyAssignedFirstPropertyAddressOfUiMatchedWithFirstPropertyAddressOfApi(
				1, 1, 0, response);
	}

	// Common function for
	// 1. Android -
	// verifyPricePropertyTypeBedroomsBathroomsAddressCityCardUrlPhoto (First
	// property)
	// 2. Web - verifyPricePropertyTypeBedroomsBathroomsAddressCityCardUrlPhoto
	// (First 3 properties)
	// 3. Web - verifyTheLastTwoProperties (Last 2 properties)
	public void propertiesCount(int pstartingcount, int pcountproperties,
			int apiresponse, String response) {
		for (int i = pstartingcount; i < pcountproperties; i++) {
			library.wait(2);

			// Price validation - UI and API
			/*String sPrice = library.findElement(
					"(.//*[@id='homeMatch']//li[@class='active']//div[@class='baseInfo'])["
							+ (i + 1) + "]/span[@class='price']").getText();*/
			
			
			// ***********************************************************************************************
			// (.//*[@id='homeMatch']//li[@class='active']//div[@class='info'])[1]//span[@class='price']
			
			String sPrice = library.findElement(
					"(.//*[@id='homeMatch']//li[@class='active']//div[@class='info'])["
							+ (i + 1) + "]//span[@class='price']").getText();
			
			//*************************************************************************************************
			
			
			String sPrice11 = sPrice.replace("$", "");
			String sPrice12 = sPrice11.replace(",", "");
			String apiPrice = library.getValueFromJson(
					"$.[" + (i + apiresponse) + "].listPrice", response)
					.toString();
			Assert.assertEquals(sPrice12, apiPrice);

			/*// Property Type validation - UI and API
			String sPropertyType = library.getAttributeOfElement("class",
					"(.//*[@id='homeMatch']//li[@class='active']//div[@class='baseInfo'])["
							+ (i + 1) + "]/span[@class='price']/i");
			if (sPropertyType.contains("icon-text-singlefamily")) {
				sPropertyType = sPropertyType.replaceAll(
						"icon-text-singlefamily", "Single Family");
			}

			if (sPropertyType.contains("icon-text-condo")) {
				sPropertyType = sPropertyType.replaceAll("icon-text-condo",
						"Condo");
			}

			String apiPropertyType = library.getValueFromJson(
					"$.[" + (i + apiresponse) + "].listingType.name", response)
					.toString();

			if (apiPropertyType.contains("SINGLE_FAMILY_HOUSE")) {
				apiPropertyType = apiPropertyType.replaceAll(
						"SINGLE_FAMILY_HOUSE", "Single Family");
			}
			Assert.assertEquals(sPropertyType.toLowerCase(),
					apiPropertyType.toLowerCase());*/

			// Bed Rooms validation - UI and API
			
			/*String sBedRooms = library.findElement(
					"(.//*[@id='homeMatch']//li[@class='active']//div[@class='baseInfo'])["
							+ (i + 1) + "]/span[2]").getText();*/
			
			//********************************************************************************************
			
			String sBedRooms = library.findElement(
					"(.//*[@id='homeMatch']//li[@class='active']//div[@class='info'])["
							+ (i + 1) + "]//span[2]").getText();
			
			//********************************************************************************************
			
			
			
			//System.out.println("UI Bedroom value :" + sBedRooms);
			if (sBedRooms.contains("—")) {
				sBedRooms = sBedRooms.replaceAll("—", "—");
				String apiBedRooms = (String) library.getValueFromJson("$.["
						+ (i + apiresponse) + "].bedrooms", response);
				Assert.assertEquals(null, apiBedRooms);
			} else if (!sBedRooms.contains(".")) {
				double BedRoomAPI = Double.parseDouble(library
						.getValueFromJson(
								"$.[" + (i + apiresponse) + "].bedrooms",
								response).toString());
				Assert.assertEquals(Double.parseDouble(sBedRooms), BedRoomAPI);
			} else {
				Float BedRoomAPI = Float.parseFloat(library.getValueFromJson(
						"$.[" + (i + apiresponse) + "].bedrooms", response)
						.toString());
				Assert.assertEquals(Float.parseFloat(sBedRooms), BedRoomAPI);
			}

			// Bath Rooms validation - UI and API
			/*String sBathRooms = library.findElement(
					"(.//*[@id='homeMatch']//li[@class='active']//div[@class='baseInfo'])["
							+ (i + 1) + "]/span[3]").getText();*/
			
			
			//********************************************************************************************
			
			String sBathRooms = library.findElement(
					"(.//*[@id='homeMatch']//li[@class='active']//div[@class='info'])["
							+ (i + 1) + "]//span[3]").getText();
			
			//********************************************************************************************
			
			if (sBathRooms.contains("—")) {
				sBathRooms = sBathRooms.replaceAll("—", "—");
				String apiBathRooms = (String) library.getValueFromJson("$.["
						+ (i + apiresponse) + "].bathroomsTotal", response);
				Assert.assertEquals(null, apiBathRooms);
			} else if (!sBathRooms.contains(".")) {
				double BathRooms = Double.parseDouble(library.getValueFromJson(
						"$.[" + (i + apiresponse) + "].bathroomsTotal",
						response).toString());
				Assert.assertEquals(Double.parseDouble(sBathRooms), BathRooms);
			} else {
				Float BathRooms = Float.parseFloat(library.getValueFromJson(
						"$.[" + (i + apiresponse) + "].bathroomsTotal",
						response).toString());
				Assert.assertEquals(Float.parseFloat(sBathRooms), BathRooms);
			}

			// Address validation - UI and API
			String sAddress = library.findElement(
					"(.//*[@id='homeMatch']//li[@class='active']//div[@class='baseInfo'])["
							+ (i + 1) + "]/div[@class='addresslink']//span[1]")
					.getText();
			String apiAddress = library.getValueFromJson(
					"$.[" + (i + apiresponse) + "].address.addressInfo",
					response).toString();

			if (apiAddress.contains("#APT ")) {
				apiAddress = apiAddress.replaceAll("#APT ", "#");
			}
			Assert.assertEquals(sAddress, apiAddress);

			// City validation - UI and API
			String sCity = library.findElement(
					"(.//*[@id='homeMatch']//li[@class='active']//div[@class='baseInfo'])["
							+ (i + 1) + "]/div[@class='addresslink']//span[2]")
					.getText();
			String apiCity = library.getValueFromJson(
					"$.[" + (i + apiresponse) + "].address.city", response)
					.toString();
			Assert.assertEquals(sCity, apiCity);

			// Card URL validation - UI and API
			String sCardURL = library.getAttributeOfElement("href",
					"xpath->(.//*[@id='homeMatch']//li[@class='active'])["
							+ (i + 1) + "]/div[@class='cardone cardbox']//a[@class='imgmask']");
			String apiCardURL = library.getValueFromJson(
					"$.[" + (i + apiresponse) + "].listingUrl", response)
					.toString();
			String sCardURL1 = sCardURL.replace(
					"http://spider.san-mateo.movoto.net:3024/", "");
			Assert.assertEquals(sCardURL1, apiCardURL);

			// Photo URL validation - UI and API
			String sPhotoURL = library.getAttributeOfElement("content",
					"xpath->(.//*[@id='homeMatch']//li[@class='active'])["
							+ (i + 1)
							+ "]/div[@class='cardone cardbox']//img/../meta");
			String apiPhotoURL = library.getValueFromJson(
					"$.[" + (i + apiresponse) + "].thumbnails.STANDARD.url",
					response).toString();
			Assert.assertEquals(sPhotoURL, apiPhotoURL);
		}
	}

	// Function - When click on Favorite icon on First property of Property Assigned
	public void loginFavIcon(JSONObject data) {
		// Click the 1st card's add favorite icon - Verify login window pops up
		library.waitForElement("SCHOOLPAGEPROPERTIESASSIGNED.propertyassignedtotext");
		library.scrollToElement("SCHOOLPAGEPROPERTIESASSIGNED.propertyassignedtotext");
		if (library.getCurrentPlatform().equals("Android")||library.getCurrentPlatform().equals("IOS_WEB")) {
		
			// Do nothing
		}
		else
		{
			library.waitForElement("SCHOOLPAGEPROPERTIESASSIGNED.iconrightangle");
			library.isJSEClicked("SCHOOLPAGEPROPERTIESASSIGNED.iconrightangle");
		
		}
		library.refresh();
		library.wait(2);
		library.scrollToElement("SCHOOLPAGEPROPERTIESASSIGNED.propertyassignedtotext");
		// Clicking on Favorite icon
		library.wait(2);
		library.scrollToElement("SCHOOLPAGEPROPERTIESASSIGNED.propertyassignedtotext");
		library.wait(5);
		library.findElement("SCHOOLPAGEPROPERTIESASSIGNED.favoriteicon")
				.click();
		library.wait(5);
		library.waitForElement("SCHOOLPAGEPROPERTIESASSIGNED.loginUserName");
		// Get the data from JSON
		String userName = (String) data.get("Username");
		String password = (String) data.get("Password");
		library.typeDataInto(userName,
				"SCHOOLPAGEPROPERTIESASSIGNED.loginUserName");
		library.typeDataInto(password,
				"SCHOOLPAGEPROPERTIESASSIGNED.loginPassword");
		library.click("SCHOOLPAGEPROPERTIESASSIGNED.submitLogin");
		library.wait(10);
		}
	

	// Function to Navigate to Favorite homes after login
	public void navigateFaviHome(JSONObject data) {
		scenarios.NavigateToFavouriteHomes(data);
	}

	// Function to Verify The Add Favorite Icon In The Card Turns Red
	public void verifyTheAddFavoriteIconInTheCardTurnsRed() {
		library.wait(10);
		library.scrollToElement("SCHOOLPAGEPROPERTIESASSIGNED.propertyassignedtotext");
		library.wait(5);
		String classAttribute = library.getAttributeOfElement("class", "SCHOOLPAGEPROPERTIESASSIGNED.favoriteiconred");
		classAttribute = classAttribute.trim();
		boolean isClassAttributeActive = classAttribute.endsWith("active");
		Assert.assertTrue(isClassAttributeActive,
				"Favorite icon colour is not red");
	}

	// Function to The Detail Info Is Hided
	public void verifyTheDetailInfoIsHided() {
		// Click UP button on this section - Verify the detail info is hided
		library.wait(5);
		library.waitForElement("SCHOOLPAGEPROPERTIESASSIGNED.propertyassignedtotext");
		library.click("SCHOOLPAGEPROPERTIESASSIGNED.propertyassignedtotext");
		boolean ishidden = library
				.verifyPageNotContainsElement("SCHOOLPAGEPROPERTIESASSIGNED.firstaddressprice");
		Assert.assertTrue(ishidden);
	}

	// Function to Verify The Favorite House Address Is Same With First Card
	public void verifyTheFavoriteHouseAddressIsSameWithFirstCard(JSONObject Data) {
		// Function to Navigate to Favorite House
		navigateFaviHome(Data);
		library.wait(15);
		String UIfavHouseAddress = library
				.getTextFrom("SCHOOLPAGEPROPERTIESASSIGNED.favoriteiconredaddress");
		library.get((String) Data.get("App-Url"));
		library.wait(5);
		library.scrollToElement("SCHOOLPAGEPROPERTIESASSIGNED.propertyassignedtotext");
		library.wait(5);
		library.waitForElement("SCHOOLPAGEPROPERTIESASSIGNED.uifirstaddress");
		library.wait(5);
		String sAddress = library
				.getTextFrom("SCHOOLPAGEPROPERTIESASSIGNED.uifirstaddress");
		library.wait(5);
		boolean isAddressSame = UIfavHouseAddress.contains(sAddress);
		Assert.assertTrue(isAddressSame);
	}

	// Function for First Property Address Of Ui Matched With First Property
	// Address Of Api
	public void propertyAssignedFirstPropertyAddressOfUiMatchedWithFirstPropertyAddressOfApi(
			int pstartingcount, int pcountproperties, int apivaluesetting,
			String response) {
		
		if (library.getCurrentPlatform().equals("Android")||library.getCurrentPlatform().equals("IOS_WEB")) {
			// Do nothing
		} else {
			library.findElement("SCHOOLPAGEPROPERTIESASSIGNED.iconrightangle").click();
		}
		
		for (int j = pstartingcount; j <= pcountproperties; j++) {
			library.wait(2);
			String sAddress = library
					.findElement("(.//*[@id='homeMatch']//li[@class='active']//div[@class='baseInfo'])["
							+ j + "]/div[@class='addresslink']//span[1]").getText();
			library.wait(1);
			String apiAddress = library
					.getValueFromJson(
							"$.[" + (j - 1 + apivaluesetting)
									+ "].address.addressInfo", response)
					.toString();
			library.wait(1);
			if (apiAddress.contains("#APT ")) {
				apiAddress = apiAddress.replaceAll("#APT ", "#");
			}
			Assert.assertEquals(sAddress, apiAddress);
		}
	}

}
