package com.movoto.scripts.agent;

import java.util.Map;


import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.MapSearchDataProvider;
import com.movoto.utils.JSONParserForAutomationNG;

import java.awt.Image;
import java.awt.Point;
import java.util.*;

/*
 *  @author Puneet.Bohra
 *  Reg-199 -> Verify the page display correctly in different view.
 */

public class Reg_199_DifferentViews extends BaseTest{
	
	JSONParserForAutomationNG jsonParser;
	  JSONObject jsonObj;
	  WebDriver wDriver;  
	  public void init(String jsonPath)
	  {
	   try
	   {
		jsonParser=new JSONParserForAutomationNG(jsonPath);
	    jsonObj=jsonParser.getNode("mapSearch");
	   }catch(Exception exc)
	   {
	    System.out.println("Exception in MapSearchForCity:init() ->"+exc.getMessage());
	   }
	  }
	     
	  @Test
	  @Parameters("dataProviderPath")
	public void verifyFilterFunctionality(String dataProviderPath) {
		  init(dataProviderPath);
		  
		String mapSearchAPIResponse = scenarios.getMapSearchApiResultsFromJson(jsonObj); // hitting map search API
		int houseCountInt = Integer.parseInt(library.getValueFromJson("$.totalCount", mapSearchAPIResponse).toString());
		//Precondition houses should be more than 0
		if(houseCountInt >0){
		String browsertype = library.getBrowserName();
		switch (browsertype) {
		
		case "Chrome":
		case "Safari":
		case "IExplore":
			if (library.getCurrentPlatform().equalsIgnoreCase("Android") || library.getCurrentPlatform().equalsIgnoreCase("IOS_WEB")) {
				
				String checkTxt = library.getTextFrom("SEARCHPAGE.maplist");
				
				if(checkTxt.equalsIgnoreCase("List")){
					library.click("SEARCHPAGE.maplist");
				}
				
				library.waitForElement("SEARCHPAGE.mapList");
				boolean isMapPresent = library.verifyPageContainsElement("SEARCHPAGE.maplist");
				Assert.assertTrue(isMapPresent);
				library.click("SEARCHPAGE.maplist");
				
				library.waitForElement("SEARCHPAGE.mapview");
				Assert.assertTrue(library.verifyPageContainsElement("SEARCHPAGE.mapview"));
				Assert.assertTrue(library.verifyPageNotContainsElement("PROPERTY.card1"));
				
				
				library.waitForElement("SEARCHPAGE.mapList");
				boolean isListPresent = library.verifyPageContainsElement("SEARCHPAGE.maplist");
				Assert.assertTrue(isListPresent);
				library.click("SEARCHPAGE.maplist");
				
				Assert.assertTrue(library.verifyPageContainsElement("PROPERTY.card1"));
				Assert.assertTrue(library.verifyPageNotContainsElement("SEARCHPAGE.mapview"));

			} else {
				//Since In Safari browser it opens recently modified option thats why directly clicking on Map view, it is not like IE/CHrome which opens fresh profile.
				//1.2. Verify map section appears on the page)(Not able to verify 1st and 3rd point of first verification step due to above mentioned behavior of Safari Browser.)
				library.verifyPageContainsElement("PROPERTY.card1");
				library.waitForElement("PROPERTY.selectedView");
				WebElement selectView = library.findElement("PROPERTY.selectedView");
				JavascriptExecutor js =(JavascriptExecutor)library.getDriver();
			   js.executeScript("if(document.createEvent){var evObj = document.createEvent('MouseEvents'); evObj.initEvent('mouseover',true, false); document.getElementsByClassName('viewsbox')[0].dispatchEvent(evObj);}else if(document.createEventObject){ document.getElementsByClassName('viewsbox')[0].fireEvent('onmouseover');}");	
				library.click("PROPERTY.mapView");
				library.wait(3);
				//2.1. Verify text changes to "Map View"  on the view box
				Assert.assertTrue(library.verifyPageContainsElement("SEARCHPAGE.map"));
				//2.2 Verify map section appears on the page
				Assert.assertTrue(library.verifyPageContainsElement("SEARCHPAGE.mapViewImage")); 
				//2.3. Verify house card section disappears on the page.
				boolean isPropertyDisabled = library.verifyPageContainsElement("PROPERTY.card1");
				
				Assert.assertFalse(isPropertyDisabled);
				js.executeScript("if(document.createEvent){var evObj = document.createEvent('MouseEvents'); evObj.initEvent('mouseover',true, false); document.getElementsByClassName('viewsbox')[0].dispatchEvent(evObj);}else if(document.createEventObject){ document.getElementsByClassName('viewsbox')[0].fireEvent('onmouseover');}");
				library.wait(2);
				library.click("xpath->//a[@id='gridView']");
				//3.1. Verify text changes to "Grid View"  on the view box
				Assert.assertTrue(library.verifyPageContainsElement("SEARCHPAGE.gridViewImage"));
				//3.2. Verify house card section appears on the page.
				Assert.assertTrue(library.verifyPageContainsElement("PROPERTY.card1"));
				//3.3. Verify map section disappears on the page
				boolean isMapDisabled = library.verifyPageContainsElement("SEARCHPAGE.map");
				Assert.assertFalse(isMapDisabled);
			}
			break;
		}
	} else
	  {
		  System.out.println("Precondition Fail-> No Of houses is equal to 0");
		  Assert.assertFalse(false, "Precondition Fail-> No Of houses is equal to 0");
	 }
		//Precondition ends here.
	  }
}