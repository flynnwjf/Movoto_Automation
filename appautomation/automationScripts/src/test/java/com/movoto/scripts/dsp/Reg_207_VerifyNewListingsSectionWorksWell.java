package com.movoto.scripts.dsp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.TreeMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.Assertion;
import com.movoto.scripts.BaseTest;
//import com.movoto.scripts.agent.PropertyData;
import com.movoto.utils.JSONParserForAutomationNG;

@SuppressWarnings("unused")
public class Reg_207_VerifyNewListingsSectionWorksWell extends BaseTest {
	 JSONParserForAutomationNG jsonParser;
	  JSONObject jsonObj;
	  WebDriver wDriver;  
	  
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
	
	/*public void  VerifyNewListingsSectionWorksWell(JSONObject Data) throws ParseException 
	  {
		//*****Preconditions********
    	// Step-1 before test step starts, not login
    	//Step-2  delete all the added favorites houses in favorites page
		library.wait(5);
	    scenarios.loginAndDeleteFavouriteProperty(Data);
	   library.get(Data.get("AppUrl").toString());
	   library.wait(5);
	   library.scrollToElement("REVIEW.schoolName");
	   library.wait(5);
	   library.scrollToElement("NEWLISTING.header");
	   library.wait(5);
	 //Verify "New Listings in <City>" is displayed
	   //Step-1    Verify "New Listings in <City>" is displayed
   	   scenarios.verifyCityinNewListings(Data);
	  		
    	library.wait(5);
    	//Verify Price/PropertyType /Bedrooms /Bathrooms /Address /City /CardURL /photo is the same as <NewListingsNearby_API> response for the first 4 properties
    	String response=scenarios.getNewListingResponseFromApi( jsonObj,"NewListing_API");
    	scenarios.verifyNewListingPropertyWithApi(Data,4, response);
    	//Verify Reflash icon is displayed
   		scenarios.clickonLeftIconTillVisibleNewListing();
		//Click on Reflash Icon
   		scenarios.verifyAndClickRefreshIconNewListing();
        //Verify the 1st property's address is matched to 1st property of <NewListingsNearby_API> response
	    scenarios.verifyNewListingPropertyWithApi(Data,1,response);
	    //Click on 1st card add favourite icon and verify login window popup
	    scenarios.selectFirstFavouriteCardNewList(jsonObj);	    
	    //Verify the add favorite icon in the card turns red
	    scenarios.verifyAddfavoriteIconintheCardturnsredNewListing();
	    // 	Verify the favorite house's address is same with 1st card on step1
		scenarios.verifyFirstFavouriteUrl(jsonObj);
	    //logout application
		scenarios.logOutForConsumerWeb(Data);
	  }*/
	
}
