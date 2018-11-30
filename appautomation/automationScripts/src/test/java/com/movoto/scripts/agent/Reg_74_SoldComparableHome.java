package com.movoto.scripts.agent;

import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.DPPTestCaseDataProvider;

public class Reg_74_SoldComparableHome extends BaseTest {

	@Test(dataProvider = "TestDataForReg_74", dataProviderClass = DPPTestCaseDataProvider.class)
	public void homeSectionContains(Map<String, String> data) {
		scenarios.removeAddsPopUp();
		library.setRequestHeader(data.get("headerKey"), data.get("headerValue"));
		String response = library.HTTPGet(data.get("apiString"));
		
		String addressFromPropertyHeader = library.getTextFrom("HOMEPAGE.addressOnDppTitle");
		scrollToElementAndVerify(DriverLocator.HOMEPAGE_comparableHomeText);
		library.click("HOMEPAGE.recentySold");
		int elementsOnMap = library.getElementCount("HOMEPAGE.elementOnMapPage");
		Assert.assertTrue(elementsOnMap == 1 || elementsOnMap > 1 );
		Assert.assertTrue(library.verifyPageContainsElement("HOMEPAGE.comparableButtonNearbySold"));
		
		//Verification of first property is matched with opened property
		Assert.assertTrue(library.verifyPageContainsElement("HOMEPAGE.youAreHereText"));
		String addressFromCSP = library.getTextFrom("HOMEPAGE.firstPropertyAddress");
		Assert.assertTrue(addressFromPropertyHeader.contains(addressFromCSP));
		
		//Verification of Compare Similar Homes properties' data with api response
		verifyHomesPropertiesInfo(response, "nearbySoldTab");
	}

	/*
	 * Test case specific reusable methods
	 */

	public void scrollToElementAndVerify(String path) {
		library.wait(2);
		JavascriptExecutor jse = (JavascriptExecutor) library.getDriver();
		WebElement element = library.getDriver().findElement(By.xpath(path));
		jse.executeScript("arguments[0].scrollIntoView();", element);
		Assert.assertTrue(element.isDisplayed());
		library.wait(2);
	}
	
	public void verifyHomesPropertiesInfo(String response, String sectionId){
		int numberOfProerties = library.getElementCount("HOMEPAGE.numOfNearbySoldTab");
		for(int i = 1; i <= numberOfProerties; i++){
			String addressOnUI = library.getTextFrom("xpath->.//*[@id='"+sectionId+"']//div[@class='gird-property-item']["+i+"]//div[@class='address singleline']");
			String addressUrlOnUI = library.getAttributeOfElement("href","xpath->.//*[@id='"+sectionId+"']//div[@class='gird-property-item']["+i+"]//a");
			
			String priceStringOnUI = library.getTextFrom("xpath->.//*[@id='"+sectionId+"']//div[@class='gird-property-item']["+i+"]//ul/li[2]");
//			if (priceStringOnUI.equals(""))
			priceStringOnUI = priceStringOnUI.replace("$", "");
			priceStringOnUI = priceStringOnUI.replace(",", "");
			int priceOnUI = Integer.parseInt(priceStringOnUI);
			
			String dateOnMovoto = library.getTextFrom("xpath->.//*[@id='"+sectionId+"']//div[@class='gird-property-item']["+i+"]//ul/li[3]");
			
			String bedStringOnUI = library.getTextFrom("xpath->.//*[@id='"+sectionId+"']//div[@class='gird-property-item']["+i+"]//ul/li[4]");
			bedStringOnUI = bedStringOnUI.split(" ")[0];
			int numericValueOfBeds = Integer.parseInt(bedStringOnUI);
			
			String bathStringOnUI = library.getTextFrom("xpath->.//*[@id='"+sectionId+"']//div[@class='gird-property-item']["+i+"]//ul/li[5]");
			bathStringOnUI = bathStringOnUI.split(" ")[0];
			double bathsOnUI = Double.parseDouble(bathStringOnUI);
			
			String pricePerSqftStringOnUI = library.getTextFrom("xpath->.//*[@id='"+sectionId+"']//div[@class='gird-property-item']["+i+"]//ul/li[7]");
			pricePerSqftStringOnUI = pricePerSqftStringOnUI.split("/")[0];
			pricePerSqftStringOnUI = pricePerSqftStringOnUI.replace("$", "");
			
			String sqftAreaStringOnUI = library.getTextFrom("xpath->.//*[@id='"+sectionId+"']//div[@class='gird-property-item']["+i+"]//ul/li[6]");
			double sqftArea = 0;
			if(!sqftAreaStringOnUI.equals("—")){
				sqftAreaStringOnUI = sqftAreaStringOnUI.split(" ")[0];
				sqftAreaStringOnUI = sqftAreaStringOnUI.replace(",", "");
				sqftArea = Double.parseDouble(sqftAreaStringOnUI);
			}
			
			String sqftLotStringOnUI = library.getTextFrom("xpath->.//*[@id='"+sectionId+"']//div[@class='gird-property-item']["+i+"]//ul/li[8]");
			String builtYearStringOnUI = library.getTextFrom("xpath->.//*[@id='"+sectionId+"']//div[@class='gird-property-item']["+i+"]//ul/li[9]");
			
			int j = 0;
			while(!(library.getValueFromJson("$.dpp.["+j+"].address.addressInfo", response) == null)){
				String addressInApi = (String) library.getValueFromJson("$.["+j+"].address.addressInfo", response);
				if(addressOnUI.trim().equals(addressInApi)){
					//Address Verification
					Assert.assertTrue(addressOnUI.contains(addressInApi));
					
					//Verification of Url
					String addressUrlInApi = (String)library.getValueFromJson("$.dpp.["+j+"].listingUrl", response);
					Assert.assertTrue(addressUrlOnUI.contains(addressUrlInApi));
					
					//Price Verification
					Integer priceInApi = (Integer)library.getValueFromJson("$.dpp.["+j+"].closePrice", response);
					int price = (int)priceInApi;
					Assert.assertTrue(priceOnUI == price);
					
					//Date verification
					String dateInApi = (String) library.getValueFromJson("$.dpp.["+j+"].soldDate", response);
					dateInApi = dateInApi.split(" ")[0];
					dateInApi = dateInApi.split("-")[1] + "/" + dateInApi.split("-")[2] + "/" + dateInApi.split("-")[0];
					Assert.assertTrue(dateInApi.equals(dateOnMovoto));
					
					//bed verification
					Integer bedsInApi = (Integer) library.getValueFromJson("$.dpp.["+j+"].bedrooms", response);
					int beds = (int)bedsInApi;
					Assert.assertTrue(numericValueOfBeds == beds);
					
					//Bath verification
					Double bathInApi = (Double)library.getValueFromJson("$.dpp.["+j+"].bathroomsTotal", response);
					double baths = (double)bathInApi;
					Assert.assertTrue(bathsOnUI == baths);
					
					//sqft area verification
					Double sqftAeraInApi =(Double) library.getValueFromJson("$.dpp.["+j+"].sqftTotal", response);
					if(sqftAeraInApi == null)
						Assert.assertTrue(sqftAreaStringOnUI.contains("—"));
					else{
						double sqftAreaValueInApi = sqftAeraInApi;
						Assert.assertTrue(sqftAreaValueInApi == sqftArea);
					}
					
					//Per sqft price verification
					// In API response Per sqft price is not available.
									
					//SqftLot verification
					// In API response SqftLot is not available.
					String sqftLotInApi = (String) library.getValueFromJson("$.dpp.["+j+"].sqftLot", response);
					if(!(sqftLotInApi == null)){
						Assert.assertTrue(sqftLotStringOnUI.contains(sqftLotInApi));
					}
					else{
						Assert.assertTrue(sqftLotStringOnUI.contains("—"));
					}	
					
					//Built year verification 
					//At least on build year should be there so that we can check the code
					String yearBuilt = library.getValueFromJson("$.dpp.["+j+"].yearBuilt", response).toString();
					if(!(yearBuilt == null)){
						Assert.assertTrue(builtYearStringOnUI.contains(yearBuilt));
					}
					else{
						Assert.assertTrue(builtYearStringOnUI.contains("—"));
					}
					break;
				}
				j++;
			}
		}
	}
}