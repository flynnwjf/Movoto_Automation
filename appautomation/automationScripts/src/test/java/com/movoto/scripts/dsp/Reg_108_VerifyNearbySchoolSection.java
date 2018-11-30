package com.movoto.scripts.dsp;

import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.utils.JSONParserForAutomationNG;

public class Reg_108_VerifyNearbySchoolSection extends BaseTest {
	JSONParserForAutomationNG jsonParser;
	  JSONObject jsonObj;
	  WebDriver wDriver;  
	  public void init(String jsonPath)
	  {
	   try
	   {
	   jsonParser=new JSONParserForAutomationNG(jsonPath);
	   jsonObj=jsonParser.getNode("NearbySchool");
	   }catch(Exception exc)
	   {
	    System.out.println("Exception in BasicInfo:init() ->"+exc.getMessage());
	   }
	  }
	  @Test
	    @Parameters("dataProviderPath")
	  	  public void VerifyNearbySchoolSection(String dataProviderPath) {
		     init(dataProviderPath);   
		   if (jsonObj != null) 
		   {
			   VerifyNearbySchoolSection(jsonObj);   
		   }
		   else{
				  Assert.assertFalse(false);
			  }
		  }
	  public void VerifyNearbySchoolSection(JSONObject Data)
	  {
		  System.out.println("Reg_108 Testcase start");
		  // Code for api response
		  library.wait(10);
		  String response =getResponseFromApi(Data, "BasicInfo_API");
		  //Verify "<District Name>(See Schools)" is displayed
		  VerifySchoolName(response);
		  // Verify total displayed schools' qty is 5 
		  TotalDisplayedSchoolQuantity();
		  //Verify total displayed schools' qty/RatingName/Type/Grade/Geviews/Distance is the same with <DistrictSchools_API> response
		  ValidateTotalSchoolWithApiResponse(Data);
		  //Verify total displayed schools' qty is 5 after  Click "Less Schools in <District Name>"
		  VerifySchoolQuantityAfterClickonLessSchoolsLink();
		  //  Verrify the detail info is hided after Click UP button on this section
		  VerifyDetailsInfoIsHided();
		}
	
	  public void VerifySchoolName(String response)
	  {
		 // Verify "<District Name>(See Schools)" is displayed
		  String SchoolNameAPI=library.getValueFromJson("$.districtName", response).toString();
			 String SchoolNameUI=library.getTextFrom("SHOOLPAGE.schoolnamedistirct");
			 Assert.assertTrue(SchoolNameUI.contains(SchoolNameAPI),"verifed school name");
	  }
	  public void TotalDisplayedSchoolQuantity()
	  {
		   //click on the school name link
		  // For andoroid schoolname not displayed.So we need to scroll down
		  
		        if(library.getCurrentPlatform().equals("Android")||library.getCurrentPlatform().equals("IOS_WEB"))
		        {
		        	scrollElement("SCHOOLPAGE.nearbyschool"); 
		        }
		        else
		        {
	    	      library.click("SCHOOLPAGE.schoolnamelink");
		        }
		      
		   	// Verify the page jump to Schools section	
			 boolean flag=library.verifyPageContainsElement("SCHOOLPAGE.schoollistpanel");
			 Assert.assertTrue(flag, "Verify the page jump to Schools section ");
		     if(!flag)
		     {
		    	 library.click("SCHOOLPAGE.schoolheader"); 
		     }
		     //Verify the School Quantity
		     int displaySchoolQuantity=library.getElementCount("SCHOOLPAGE.schoollistrowcount");
		     Assert.assertEquals(displaySchoolQuantity, 5);
		  
	  }
	  public void ValidateTotalSchoolWithApiResponse(JSONObject Data)
	  {        //Verify total displayed schools' qty/RatingName/Type/Grade/Geviews/Distance is the same with <DistrictSchools_API> response
		     boolean flag= library.verifyPageContainsElement("SCHOOLPAGE.allschoolmadisonmetropolitanlink");
		     Assert.assertTrue(flag,"Veiryfy the all school madison metro politan link");
		     library.isJSEClicked("SCHOOLPAGE.allschoolmadisonmetropolitanlink");
		   
		   
			String response =getResponseFromApi(Data, "DistrictSchool_API");
			//School count from the API
			int schoolquantityUI=library.getElementCount("xpath->//td[@class='school-card-link']/a");
			Integer quantity=(Integer)library.getValueFromJson("$.schoolCount", response);
   	        int schoolquantityAPI = (int)quantity;
   	         Assert.assertEquals(schoolquantityUI, schoolquantityAPI, "Verified school quantity");
		       for(int i=0;i<schoolquantityAPI;i++)
		       {
			
				String ratingUI = library.getTextFrom("xpath->(.//*[@id='nearbySchoolPanel']/..//td[1]/span)[" + (i + 1) + "]");
				if (!ratingUI.matches("\\d+")) {
					// We are not validating for rating NR .( in API its come
					// null value for NR rating)
					String ratingAPIs = (String) library.getValueFromJson("$.schools[" + i + "].rating", response);
					if (ratingAPIs == null) {
						String rating = "NR";

						Assert.assertEquals(ratingUI, rating);
					}
				} else {
					int ratingUiValue = Integer.parseInt(ratingUI);
					Integer ratingAPI = (Integer) library.getValueFromJson("$.schools[" + i + "].rating", response);
					int ratingValueInApi = (int) ratingAPI;
					Assert.assertEquals(ratingUiValue, ratingValueInApi);
					
				}
				// validate for (School Name)
				String schoolnameUI = library.getTextFrom("xpath->(//td[@class='school-card-link']/a)[" + (i + 1) + "]");
				String schoolnameAPI = (String) library.getValueFromJson("$.schools[" + i + "].name", response);
				Assert.assertEquals(schoolnameUI, schoolnameAPI);
				if(library.getCurrentPlatform().equals("Web"))
				{
				// validate for (School Type)
				String schooltypeUI = library.getTextFrom("xpath->(.//*[@id='nearbySchoolPanel']/..//td[3]/span)[" + (i + 1) + "]");
			    String schooltypeAPI = (String) library.getValueFromJson("$.schools[" + i + "].type", response);
				Assert.assertEquals(schooltypeUI .toLowerCase(), schooltypeAPI);
				
				//  validate for (School Grade)
				String schoolgradeUI = library.getTextFrom("xpath->(.//*[@id='nearbySchoolPanel']/..//td[4]/span)[" + (i + 1) + "]");
				String schoolgradeAPI = (String) library.getValueFromJson("$.schools[" + i + "].level", response);
				Assert.assertEquals(schoolgradeUI, schoolgradeAPI);
				}
		     
		     }
	  }
	  public void VerifySchoolQuantityAfterClickonLessSchoolsLink()
	  {
		// Verify total displayed schools' qty is 5 after Click "Less Schools in<District Name>"
		  scrollElement("SCHOOLPAGE.madisonmetropolitan");
		library.isJSEClicked("SCHOOLPAGE.madisonmetropolitanlink");
		int displaySchoolQuantitys = library.getElementCount("SCHOOLPAGE.schoollistrowcount");
		Assert.assertEquals(displaySchoolQuantitys, 5);
	  }
	  public void VerifyDetailsInfoIsHided()
	  {
		// Verify the detail info is hided after Click UP button on this section
		scrollElement("SCHOOLPAGE.schoolheader"); 
		library.isJSEClicked("SCHOOLPAGE.upbutton");
		boolean flag;
		if(library.getBrowserName().equals("Safari"))
		{
			 flag = library.getDriver().findElement(By.xpath(".//*[@id='nearbySchoolPanel']")).isDisplayed();
	
		}
		else
		{
			 flag=library.findElement("SCHOOLPAGE.schoollistpanel").isDisplayed();
		}
		
		
		Assert.assertFalse(flag, "Verify DetailsInfo is Hided");
	  }
	  public String getResponseFromApi(JSONObject Data, String ApiName) {
		  library.setRequestHeader("X-MData-Key", Data.get("X-MData-Key").toString());
		  String response = library.HTTPGet(Data.get(ApiName).toString());
		  return response;
		}
	  public void scrollElement(String element) 
		{   
			library.waitForElement(element);
			JavascriptExecutor jse = (JavascriptExecutor) library.getDriver();
			WebElement elements = library.findElement(element);
			jse.executeScript("arguments[0].scrollIntoView();", elements);
		}

}
