package com.movoto.scripts.agent;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.DPPTestCaseDataProvider;

public class Reg_88_MarketStatisticsSection extends BaseTest{
	
	@Test//(dataProvider = "TestDataForReg_73", dataProviderClass = DPPTestCaseDataProvider.class)
	public void marketStatistics(){
		
		System.out.println("Reg_88 is running.");
		if(library.getCurrentPlatform().equals("Web"))
			scenarios.removeAddsPopUp();
		
		verifyMarketStatisticsText();
		boolean isMedianPrice = library.verifyPageContainsElement("MARKETSECTION.medianPrice");
		String streetAddress = library.getTextFrom("HOMEPAGE.streetAddress");
		streetAddress = streetAddress.replace(",", " ").trim();
		String paragraphContent = library.getTextFrom("MARKETSECTION.paragraphContent");
		boolean doesItExist = paragraphContent.contains(streetAddress);
		String paragraphContent1 = library.getTextFrom("MARKETSECTION.paragraphContent1");
		boolean doesItExist1 = paragraphContent1.contains(streetAddress);
		Assert.assertTrue(doesItExist);
		Assert.assertTrue(doesItExist1);
		library.wait(2);
		
	}
	
	//Verification of Header Text
	  public void verifyMarketStatisticsText(){
	      JavascriptExecutor js = (JavascriptExecutor) library.getDriver();
	      js.executeScript("window.scrollBy(0,300)", "");
	      int step=50;
	      boolean flag=false;
	      while(step<=150) {
	    	  js.executeScript("window.scrollBy(0,"+(step)+")", "");
	          library.wait(5);
	          step+=10;
	          if(library.verifyPageContainsElement("HOMEPAGE.marketStatisticsSection")){
	              flag=true;
	              break;
	          }
	      }
	      Assert.assertTrue(flag);
	      library.wait(20);
	  }
}
