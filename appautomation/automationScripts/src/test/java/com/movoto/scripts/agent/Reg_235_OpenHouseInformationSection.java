package com.movoto.scripts.agent;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.DPPTestCaseDataProvider;

public class Reg_235_OpenHouseInformationSection extends BaseTest{
	
	@Test(dataProvider = "TestDataForReg_235", dataProviderClass = DPPTestCaseDataProvider.class)
	public void openHouseInfoVerification(Map<String, String> data){
		System.out.println("Reg-235 is running");
		String urlOfFirstProperties = library.getAttributeOfElement("href", "HOMEPAGE.firstpropertyImage");
		library.wait(2);
		/*if(library.getCurrentPlatform().equals("Web"))
			Assert.assertTrue(library.verifyPageContainsElement("DPPSEARCHPAGE.morefilter"));*/
		library.getDriver().get(urlOfFirstProperties);
		scenarios.removeAddsPopUp();
//		Assert.assertTrue(library.verifyPageContainsElement("HOMEPAGE.photoView"));
		Assert.assertTrue(library.verifyPageContainsElement("HOMEPAGE.mapViewbtn"));
		Assert.assertTrue(library.verifyPageContainsElement("HOMEPAGE.shareSocialLink"));
		
		//Get response from api
		urlOfFirstProperties = "home" + urlOfFirstProperties.split("home")[1];
		library.setRequestHeader(data.get("headerKey"),data.get("headerValue"));
		String response = library.HTTPGet(data.get("apiString") + urlOfFirstProperties);
		library.wait(1);
		
		//Date verifivcation with API response
		String dateOnIU = library.getTextFrom("HOMEPAGE.dateOnUI");
		dateOnIU = dateOnIU.split(" ")[2];
		String dateInAPI =(String) library.getValueFromJson("$.dpp.currentOpenHouses[0].date", response);
		dateInAPI = dateInAPI.split("-")[1] + "/" + dateInAPI.split("-")[2];
		Assert.assertEquals(dateOnIU, dateInAPI);
		
		//Time verification with API response $.[]currentOpenHouses[0].date
		String timeOnIU = library.getTextFrom("HOMEPAGE.timeOnUI");
		String[] startAndEndTime = timeOnIU.split("-");
		
		String[] startTimeString = startAndEndTime[0].split(":");
		String startTime = startTimeString[0].trim();
		int numericValueOfStartTime = Integer.parseInt(startTime);
		if(startTimeString[1].trim().contains("pm") && numericValueOfStartTime != 12)
			numericValueOfStartTime = numericValueOfStartTime + 12;
		
		String endTimeString = startAndEndTime[1].trim();
		String endTime = endTimeString.split(":")[0].trim();
		int numericValueOfEndTime = Integer.parseInt(endTime);
		if(endTimeString.split(":")[1].trim().contains("pm") && numericValueOfEndTime != 12)
			numericValueOfEndTime = numericValueOfEndTime + 12;
		
		String startTimeInAPI =(String) library.getValueFromJson("$.dpp.currentOpenHouses[0].startTime", response);
		Assert.assertTrue(Integer.parseInt(startTimeInAPI.split(":")[0].trim()) == numericValueOfStartTime);
		String endTimeInAPI =(String) library.getValueFromJson("$.dpp.currentOpenHouses[0].endTime", response);
		Assert.assertTrue(Integer.parseInt(endTimeInAPI.split(":")[0].trim()) == numericValueOfEndTime);
	}

}
