package com.movoto.scripts.agent;

import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.DPPTestCaseDataProvider;
/*
 *  @author Gopal.Prasad
 *  Reg-73 -> Verify that assigned school section works well both on map view and normal view
 */

public class Reg_73_AssignedSchoolsSection extends BaseTest{

	@Test(dataProvider = "TestDataForReg_73", dataProviderClass = DPPTestCaseDataProvider.class)
	public void assignedSchoolSection(Map<String, String> data){
		if(library.getCurrentPlatform().equals("Web"))
			scenarios.removeAddsPopUp();
		
		//Set Header and get Response
		String response = setHeaderAndGetResponse(data);	
		
		//Verification of Assigned School Text On AndroidWeb and IOSSafari
		verifyAssignedSchoolText();
		
		//Verification of Schools' name, Ratings, Type, Review, Distance, Grade
		verifySchoolNameUrlAndRating(response);
		
		//Verification of Schools' type, review, grade and distance on WindowsChrome, MacSafari and IE
		if(library.getCurrentPlatform().equals("Web"))
			verificationOfSchoolsTypeGradeReviewDistance(response);
	
		//Verification of Contains of Schools' URL
//		verifyContainsOfSchoolsURL(data);
				
		//Verification of PopOver Content and disappearing of PopOver after clicking close icon
		verifyPopoverContentAndClosing(data);
		
		//Verification of opening google map after clicking on View Assigned Schools On Map
		verifyOpeingGoogleMap();
			
		//Verification of hiding Assigned School Section after clicking on Up arrow
		verifyHidingOfAssignedSchoolSection();	
	}
	
	/*
	 * Test Case specific reusable scenarios for all platform like MacSafari, WindowsChrome, IE, AndroidWeb and IOSSafari
	 */
	
	//Set Header and get Response
	public String setHeaderAndGetResponse(Map<String, String> data){
		library.setRequestHeader(data.get("headerKey"), data.get("headerValue"));
		String response = library.HTTPGet(data.get("apiString"));
		return response;	
	}
	
	//Verification of Schools' Rating, Name and URL on all platform
	public void verifySchoolNameUrlAndRating(String response){
		if(library.getCurrentPlatform().equals("Android") || library.getCurrentPlatform().equals("IOS_WEB"))
			library.click("HOMEPAGE.assignedSchoolHeader");
		int numberOfSchools = library.getElementCount("HOMEPAGE.assignschoolrowcount");
		for(int i = 0; i < numberOfSchools; i++){
			library.wait(5);
			String rating = library.getTextFrom("xpath->.//*[@id='assignedSchoolInfo']/div[1]//tbody/tr["+(i+1)+"]/td[1]/span");
			int ratingValue = Integer.parseInt(rating);
			Integer ratingInApi=(Integer)library.getValueFromJson("$.assignedSchools["+i+"].rating", response);
			int ratingValueInApi = (int)ratingInApi;
			Assert.assertTrue(ratingValue == ratingValueInApi);

			String schoolName = library.getTextFrom("xpath->.//*[@id='assignedSchoolInfo']//tbody/tr["+(i+1)+"]/td[2]/a");
			String schoolNameInApi=(String)library.getValueFromJson("$.assignedSchools["+i+"].schoolName", response);
			Assert.assertEquals(schoolName,schoolNameInApi);
					
			String schoolUrl = library.getAttributeOfElement("href", "xpath->//*[@class='school-card']/tbody/tr["+(i+1)+"]/td[2]/a");
			String schoolUrlInApi = (String)library.getValueFromJson("$.assignedSchools[" + i + "].dspUrl", response);
			Assert.assertTrue(schoolUrl.contains(schoolUrlInApi));
		}
	}
	
	//Verification of Assigned School Header Text
	public void verifyAssignedSchoolText(){
		JavascriptExecutor jse = (JavascriptExecutor) library.getDriver();
		WebElement element = library.getDriver().findElement(By.xpath(DriverLocator.HOMEPAGE_assignedschoolheader));
		jse.executeScript("arguments[0].scrollIntoView();", element);
		Assert.assertTrue(library.verifyPageContainsElement("HOMEPAGE.assignedSchoolHeader"));
		
	}
	
	//Verification of Schools' type, review, grade and distance on WindowsChrome, MacSafari and IE
	public void verificationOfSchoolsTypeGradeReviewDistance(String response){
		int numberOfSchools = library.getElementCount("HOMEPAGE.assignschoolrowcount");
		for(int i = 0; i < numberOfSchools; i++){

			String schoolType= library.getTextFrom("xpath->.//*[@id='assignedSchoolInfo']/div[1]/table/tbody/tr["+(i+1)+"]/td[3]/span");
			String schoolTypeInApi=(String)library.getValueFromJson("$.assignedSchools["+i+"].type", response);
			Assert.assertTrue(schoolType.equalsIgnoreCase(schoolTypeInApi));

			String grade= library.getTextFrom("xpath->.//*[@id='assignedSchoolInfo']/div[1]/table/tbody/tr["+(i+1)+"]/td[4]/span");
			String gradeInApi=(String)library.getValueFromJson("$.assignedSchools["+i+"].level", response);
			Assert.assertEquals(grade,gradeInApi);

			String reviews= library.getTextFrom("xpath->.//*[@id='assignedSchoolInfo']/div[1]/table/tbody/tr["+(i+1)+"]/td[5]/span");
			int reviewValue = Integer.parseInt(reviews);
			Integer reviewsInApi=(Integer)library.getValueFromJson("$.assignedSchools["+i+"].reviewCount", response);
			int reviewValueInApi = (int)reviewsInApi;
			Assert.assertTrue(reviewValue == reviewValueInApi);

			//Distance value is not matching every time with API response
			String distance= library.getTextFrom("xpath->.//*[@id='assignedSchoolInfo']/div[1]/table/tbody/tr["+(i+1)+"]/td[6]/span");
			Double distanceInApI=(Double)library.getValueFromJson("$.assignedSchools["+i+"].distance", response);
			double distanceNumericValueInApi = (double)distanceInApI;
			String distanceStringValueInApI = Double.toString(distanceNumericValueInApi);
			Assert.assertTrue(distanceStringValueInApI.contains(distance.substring(0, 2)));		          
		}
	}
	
	//Verification of contains of Schools' URL
	public void verifyContainsOfSchoolsURL(Map<String, String> data){
		library.wait(1);
		String urlOfSanDieguitoUnionHigh = library.getAttributeOfElement("href","SCHOOL.sanDieguitoUnionHigh");
		String urlOfSolanaBeachElementarySchoolDistrict = library.getAttributeOfElement("href","SCHOOL.solanaBeachElementarySchoolDistrict");
		Assert.assertTrue(urlOfSanDieguitoUnionHigh.contains(data.get("containsOfUrl1")));
		Assert.assertTrue(urlOfSolanaBeachElementarySchoolDistrict.contains(data.get("containsOfUrl")));
		
	}
	
	//Verification of PopOver Content and disappearing of PopOver after clicking close icon
	public void verifyPopoverContentAndClosing(Map<String, String> data){
		library.wait(1);
		library.click("HOMEPAGE.providedByGreatSchools");
		library.wait(1);
		String popOverContains = library.getTextFrom("POPOVER.popoverContent").trim();
		Assert.assertTrue(popOverContains.equals(data.get("popOverContent")));
		library.wait(1);
		library.click("POPOVER.closeIcon");
		library.wait(2);
		Assert.assertTrue(library.verifyPageNotContainsElement("POPOVER.closeIcon"));
		
	}
	
	//Verification of opening google map after clicking on View Assigned Schools On Map
	public void verifyOpeingGoogleMap(){
		library.click("HOMEPAGE.openSchoolLink");
		library.wait(2);
		if(library.getCurrentPlatform().equals("Android") || library.getCurrentPlatform().equals("IOS_WEB"))
			Assert.assertTrue(library.verifyPageContainsElement("MAPPAGE.googleLogo"));
		else
			Assert.assertTrue(library.verifyPageContainsElement("MAPPAGE.zoomInButton"));
		
		library.click("MAPPAGE.closeMap");
		library.wait(2);
	}
	
	//Verification of hiding Assigned School Section after clicking on Up arrow
	public void verifyHidingOfAssignedSchoolSection(){
		verifyAssignedSchoolText();
		library.click("HOMEPAGE.assignedSchoolHeader");
		boolean isSectionHiden = library.verifyPageNotContainsElement("HOMEPAGE.VerifySchoolNameHeader");
		Assert.assertTrue(isSectionHiden);
	}

}
