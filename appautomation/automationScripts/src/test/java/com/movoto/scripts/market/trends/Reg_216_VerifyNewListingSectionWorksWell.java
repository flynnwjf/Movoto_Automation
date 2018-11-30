package com.movoto.scripts.market.trends;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.utils.JSONParserForAutomationNG;
/*
 * Govind Kalria
 */
public class Reg_216_VerifyNewListingSectionWorksWell extends BaseTest {
	JSONParserForAutomationNG jsonParser;
	JSONObject jsonObj;
	 @Test
	    @Parameters("dataProviderPath")
	 
	   public void VerifyNewListingsSectionWorksWell(String dataProviderPath) throws ParseException {
	     init(dataProviderPath);   
	   if (jsonObj != null) 
	   {		
		   scenarios.VerifyNewListingsSectionWorksWell(jsonObj);   
		
	   }
	   else
	   {
		   Assert.assertTrue(false,"Jason data not getting properly");
	   }
	  }
	  public void init(String jsonPath)
	  {
	   try
	   {
	   jsonParser=new JSONParserForAutomationNG(jsonPath);
	   jsonObj=jsonParser.getNode("NewListingsSection");
	   }catch(Exception exc)
	   {
	    System.out.println("Exception in BasicInfo:init() ->"+exc.getMessage());
	   }
	  }
	
	/*
	@Test
	@Parameters("dataProviderPath")
	public void verifyNewListingSection(String dataProviderPath) throws ParseException{
		try {
	    	jsonParser = new JSONParserForAutomationNG(dataProviderPath);
	    	jsonObj = jsonParser.getNode("newListingSectionData");
	    } catch (Exception exc) {
	      System.out.println("Exception in BasicInfo:init() ->" + exc.getMessage());
	    }
		
		if (jsonObj != null) {
			 scenarios.VerifyNewListingsSectionWorksWell(jsonObj);
	    } else {
	      Assert.assertFalse(false);
	    }
	}*/

   /* public void newListingSection() throws ParseException{
    	
    	//*****Preconditions********
    	// Step-1 before test step starts, not login
    	//Step-2  delete all the added favorites houses in favorites page

    	scenarios.loginAndDeleteFavouriteProperty(jsonObj);
    	library.get((String) jsonObj.get("App-Url"));
    	String response = scenarios.getNewListingResponseFromApi( jsonObj,"NewListing_API");
    	library.wait(7);
    	library.scrollToElement("NEWLISTING.text");
    	library.wait(5);
    	//Step-1Verify "New Listings in <City>" is displayed
    	scenarios.verifyCityinNewListings(jsonObj);
    	//Step-1Verify Verify StatusTag/Price/PropertyType /Bedrooms /Bathrooms /Address /City /CardURL /photo is the same as <NewListingsNearby_API> response for the first 4 properties
    	scenarios.verifyNewListingPropertyWithApi(jsonObj, 4, response);
    	//Step-2Verify Reflash icon is displayed
    	scenarios.clickonLeftIconTillVisibleNewListing();		
    	scenarios.verifyAndClickRefreshIconNewListing();
  		//Step-3 Verify the 1st property's url is matched to 1st property of <NewListingsNearby_API> response
  		scenarios.verifyNewListingPropertyWithApi(jsonObj, 1, response);
  		// Step-4 Select the first card
  		scenarios.selectFirstFavouriteCardNewList(jsonObj);
  		// Step-5 and step-7 Verify the add favorite icon in the card turns red and Verify the detail info is hided
  		scenarios.verifyAddfavoriteIconintheCardturnsredNewListing();
  		// Step-6 Verify the favorite house's url is same with 1st card on step1
  		scenarios.verifyFirstFavouriteUrl(jsonObj);
  		//logoout
    	scenarios.logOutForConsumerWeb(jsonObj);
    }             */    
}
