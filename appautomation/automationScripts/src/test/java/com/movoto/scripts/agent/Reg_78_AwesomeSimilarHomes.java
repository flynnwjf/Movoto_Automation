package com.movoto.scripts.agent;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.DPPTestCaseDataProvider;

public class Reg_78_AwesomeSimilarHomes extends BaseTest {

	@Test(dataProvider = "TestDataForReg_78", dataProviderClass = DPPTestCaseDataProvider.class)
	public void awesomeSimilarHomes(Map<String, String> data) {
		// fetch responce from API
		String responce = getApiResponce(data);
		verifyAwesomeSimilarHomes(data);
		verifyAwesomeSimilarHomesAPI(responce);
	}

	/*
	 * Test case specific Reusable methods
	 */
	public void verifyAwesomeSimilarHomes(Map<String, String> data) {
		WebDriver driver = library.getDriver();
		if (library.getCurrentPlatform().equalsIgnoreCase("Web"))
			scenarios.removeAddsPopUp();

		// check 'Awesome Similar Homes' exists in page
		if (library.getCurrentPlatform().equalsIgnoreCase("Android")
				|| library.getCurrentPlatform().equalsIgnoreCase("IOS_WEB")) {
			scrollToElement(DriverLocator.HOMEPAGE_awesomesimilarhomesheader);
			Assert.assertTrue(library.verifyPageContainsElement("HOMEPAGE.awesomeSimilarHomes"));
		} else {
			Assert.assertTrue(library.verifyPageContainsElement("HOMEPAGE.awesomeSimilarHomes"));
			scrollToElement(DriverLocator.HOMEPAGE_awesomesimilarhomesheader);
		}

	}

	public void verifyAwesomeSimilarHomesAPI(String response) {
		if (library.getCurrentPlatform().equalsIgnoreCase("Android") || library.getCurrentPlatform().equalsIgnoreCase("IOS_WEB")) {
			library.click("HOMEPAGE.awesomeSimilarHomes");
			library.wait(3);
		}
		
		// match UI details with API response
		int numberOfAwesomeProperties = library.getElementCount("xpath->.//*[@id='awesomehomesPanel']/div");
		for (int i = 1; i <= numberOfAwesomeProperties; i++) {
			String addr = library.getTextFrom("xpath->(.//*[@id='awesomehomesPanel']//span[@itemprop='streetAddress'])[" + i + "]");
			addr = addr.replace("#E", "#APT E");
			String city = library.getTextFrom("xpath->(.//*[@id='awesomehomesPanel']//span[@itemprop='addressLocality'])[" + i + "]");
			String bd = library.getTextFrom("xpath->(.//*[@id='awesomehomesPanel']//div[@class='top-base-info']/span[2])[" + i + "]");
			String ba = library.getTextFrom("xpath->(.//*[@id='awesomehomesPanel']//div[@class='top-base-info']/span[3])[" + i + "]");
			String sqftwithComma = library.getTextFrom("xpath->(.//*[@id='awesomehomesPanel']//div[@class='top-base-info']/span[4])[" + i + "]");
			String sqft = sqftwithComma.replace(",", "");
			String cardURL = library.getAttributeOfElement("href","xpath->(.//*[@id='awesomehomesFields']//a[@class='imgmask'])[" + i + "]");

			int j = 0;
			while (!((String) library.getValueFromJson("$.[" + j + "].address.addressInfo", response) == null)) {
				if (addr.contains((String) library.getValueFromJson("$.[" + j + "].address.addressInfo", response))) {
					String addrAPI = String
							.valueOf(library.getValueFromJson("$.[" + j + "].address.addressInfo", response));
					boolean isAddressMatched = addrAPI.equals(addr);
					String cityAPI = String.valueOf(library.getValueFromJson("$.[" + j + "].address.city", response));
					String bdAPI = String.valueOf(library.getValueFromJson("$.[" + j + "].bedrooms", response));
					String baAPI = String.valueOf(library.getValueFromJson("$.[" + j + "].bathroomsTotal", response));
					String sqftAPI = String.valueOf(library.getValueFromJson("$.[" + j + "].sqftTotal", response));
					String cardURLAPI = String.valueOf(library.getValueFromJson("$.[" + j + "].listingUrl", response));

					Assert.assertEquals(addr, addrAPI, "UI Address does not match with API Address");
					Assert.assertEquals(city, cityAPI, "UI City does not match with API City");
					Assert.assertEquals(Double.parseDouble(bd), Double.parseDouble(bdAPI),"UI Bed does not match with API Bed");
					Assert.assertEquals(Double.parseDouble(ba), Double.parseDouble(baAPI),"UI Bathroom does not match with API Bathroom");
					Assert.assertEquals(Double.parseDouble(sqft), Double.parseDouble(sqftAPI),"UI Sqft does not match with API Sqft");
					// Assert.assertTrue(library.verifyPageContainsElement("xpath->.//*[@id='awesomehomesPanel']/div[" + j + "]//div[@class='mlsbox']/img"));
					Assert.assertTrue(cardURL.contains(cardURLAPI), "UI cardURL does not match with API cardURL");

					break;
				}
				j++;
			}
		}
	}

	public void scrollToElement(String locatorPath) {
		library.wait(4);
		JavascriptExecutor jse = (JavascriptExecutor) library.getDriver();
		WebElement element = library.getDriver().findElement(By.xpath(locatorPath));
		jse.executeScript("arguments[0].scrollIntoView();", element);
		library.wait(2);
	}

	public String getApiResponce(Map<String, String> data) {
		// fetch responce from API
		library.setRequestHeader(data.get("headerKey"), data.get("headerValue"));
		String shshs = data.get("apiString");
		String response = library.HTTPGet(data.get("apiString"));
		return response;
	}

}