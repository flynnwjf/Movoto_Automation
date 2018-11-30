package com.movoto.scripts.agent;

import java.util.Map;

import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.MapSearchDataProvider;
import com.movoto.utils.JSONParserForAutomationNG;

/*
 *  @author Puneet.Bohra
 *  Reg-202 -> Verify the sorting function work correctly.
 */

public class Reg_202_SortingFunctoinality extends BaseTest{
	
  @Test(dataProvider = "MapSearchForCity", dataProviderClass = MapSearchDataProvider.class, priority = 1)
 
  public void verifySortingFunctionality(Map<String, Object> data) {
	  boolean flag = false;
			if (data != null)
			{
				library.wait(10);
				flag = scenarios.checkPreconditionsForTc202();
				if(flag){			
				scenarios.verifyHouseCardsDisplay(data);
				scenarios.selectPriceLowAndVerifiHouseCards(data);
				scenarios.selectSquareBigAndVerifyHouseCards(data);
                } 
			}
		}		
}