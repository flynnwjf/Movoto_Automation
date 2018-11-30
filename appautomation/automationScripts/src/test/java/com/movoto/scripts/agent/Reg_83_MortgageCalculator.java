package com.movoto.scripts.agent;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.DPPTestCaseDataProvider;

import org.testng.Assert;

public class Reg_83_MortgageCalculator extends BaseTest {

	@Test(dataProvider = "TestDataForReg_83", dataProviderClass = DPPTestCaseDataProvider.class)
	public void MortagageTest(Map<String, String> data) {
		scenarios.removeAddsPopUp();
		String response = setHeaderAndGetResponse(data);
		library.wait(2);
		String priceAndroid=null;
		String priceWeb=null;
		// Verify default home price in the calculator is the same with API response
		String priceAPI = String.valueOf(library.getValueFromJson("$.dpp.listPrice", response));
		if (library.getCurrentPlatform().equals("Android")|| library.getCurrentPlatform().equals("IOS_WEB")) {
			priceAndroid = library.getAttributeOfElement("content", "MORTGAGE.priceElementAnd");
			Assert.assertEquals(priceAPI, priceAndroid);
		}else
		{
			priceWeb = library.getTextFrom("MORTGAGE.homepriceCalc");
			priceWeb = priceWeb.replace(",", "");
			priceWeb = priceWeb.replace("$", "");
			Assert.assertEquals(priceAPI, priceWeb.trim());
		}

		// Verify "Mortgage Calculator" text is displayed
		Assert.assertTrue(library.verifyPageContainsElement("MORTGAGE.MortgageCalculatorText"));
		
		if(library.getCurrentPlatform().equals("Android") || library.getCurrentPlatform().equals("IOS_WEB")){
			library.scrollToElement("MORTGAGE.MortgageCalculatorText");
			library.wait(10);
			library.click("MORTGAGE.customizeSettings");
		} else {
			library.scrollToElement("MORTGAGE.MortgageCalculatorText");
			library.wait(2);
		}

		// Verify default Down Payment is "20%"
		String downpaymentText=library.getAttributeOfElement("value", "MORTGAE.downpayment");
		String downpaymentText1=data.get("defaultDownPaymentText");
		Assert.assertEquals(downpaymentText, downpaymentText1);
		
		// Verify default Interest is "3.5%"
		String defaultInteresttext=library.getAttributeOfElement("value", "MORTGAGE.interest");
		library.wait(2);
		String defaultInterest1 = data.get("interestInPerc");
		library.wait(2);
		System.out.println(data.get("interestInPerc"));
		Assert.assertEquals(defaultInteresttext, defaultInterest1);
		
		// Step - 2
		// Click "More (Taxes, Insurance & HOA)"
		if(library.getCurrentPlatform().equals("Android") || library.getCurrentPlatform().equals("IOS_WEB") || library.getCurrentBrowser().contains("Safari")  ){
			library.wait(2);
			library.scrollToElement("MORTGAGE.interest");
			library.wait(2);
		}
		try{
			library.click("MORTGAGE.morelinkxpath");
		}catch (NoSuchElementException e) {
			System.out.println("Element Not Found");
		}
		
		
		// Verify Loan Payment is "30 years fixed"
		String loanPymentText = library.getTextFrom("MORTGAGE.LoanPayment");
		Assert.assertEquals(loanPymentText, data.get("loanPymentText"));
		
		// Verify "Property Tax/Home Insurance/HOA Dues/Estimated Credit" is displayed
		WebElement propertyTax = library.findElement("MORTGAGE.PropertyTax");
		Assert.assertEquals(true, propertyTax.isDisplayed());
		library.wait(2);

		WebElement homeInsurance = library.findElement("MORTGAGE.HomeInsurance");
		Assert.assertEquals(true, homeInsurance.isDisplayed());
		
		library.scrollToElement("MORTGAGE.PropertyTax");
		library.wait(2);

		WebElement homeDues = library.findElement("MORTGAGE.HomeDues");
		Assert.assertEquals(true, homeDues.isDisplayed());
		library.scrollToElement("MORTGAGE.PropertyTax");
		library.wait(2);

		WebElement estimatedCredit = library.findElement("MORTGAGE.EstimatedCredit");
		Assert.assertEquals(true, estimatedCredit.isDisplayed());
		library.scrollToElement("MORTGAGE.PropertyTax");
		library.wait(2);
		
		// Step - 3
		// Click "Less (Taxes, Insurance & HOA)"
		library.scrollToElement("MORTGAGE.PropertyTax");
		library.click("MORTGAGE.lesslinkxpath");
		
		//Verify "Property Tax/Home Insurance/HOA Dues/Estimated Credit" is hided
		library.wait(2);
		Assert.assertFalse(library.verifyPageContainsElement("MORTGAGE.PropertyTax"));
		Assert.assertFalse(library.verifyPageContainsElement("MORTGAGE.HomeInsurance"));
		Assert.assertFalse(library.verifyPageContainsElement("MORTGAGE.HomeDues"));
		Assert.assertFalse(library.verifyPageContainsElement("MORTGAGE.EstimatedCredit"));
		library.wait(2);
		
		// Step - 4
		// Edit and add a "0" to Home Price edit box
		int initialMortgagePerMonths = mortgageCalculator();
		library.scrollToElement("MORTGAGE.MortgageCalculatorText");
		library.wait(2);
		library.clear("MORTGAGE.HomePriceTextField");
		library.wait(2);
		library.typeDataInto("0", "MORTGAGE.HomePriceTextField");
		library.wait(5);
		library.clear("MORTGAGE.HomePriceTextField");
		library.typeDataInto(data.get("homePrice"), "MORTGAGE.HomePriceTextField");
		int mortgagePerMonths = mortgageCalculator();
		// Verify value of mortgage per month is bigger than initial value
		Assert.assertTrue(initialMortgagePerMonths < mortgagePerMonths);
		
		// Step - 5
		// Change the price to the property's price
		library.wait(5);
		library.clear("MORTGAGE.HomePriceTextField");
		library.wait(5);
		if (library.getCurrentPlatform().equals("Android")|| library.getCurrentPlatform().equals("IOS_WEB")) {
			library.typeDataInto(priceAndroid, "MORTGAGE.HomePriceTextField");
			System.out.println("Price Android entered " + priceAndroid);
		}else
		{
			library.typeDataInto(priceWeb, "MORTGAGE.HomePriceTextField");
			System.out.println("Price web entered " + priceWeb);
		}

		library.wait(5);
		int mortgagePerMonth2s = mortgageCalculator();
		library.wait(5);
		// Verify value of mortgage per month is the same as initial value
		Assert.assertTrue(initialMortgagePerMonths == mortgagePerMonth2s);
		
		// Step - 6
		// Change the Down Payment to "50%"
		library.click("MORTGAGE.DwnPaymentRateField");
		library.wait(2);
		library.typeDataInto(Keys.chord(Keys.CONTROL, "a"), "MORTGAGE.DwnPaymentRateField");
		library.wait(2);
		library.typeDataInto(Keys.chord(Keys.DELETE), "MORTGAGE.DwnPaymentRateField");
		library.wait(2);
		library.typeDataInto(data.get("downPaymentText"), "MORTGAGE.downpaymentpercenttextboxxpath");
		library.wait(2);
		int mortgagePerMonth3s = mortgageCalculator();
		//Verify value of mortgage per month is smaller than initial value
		Assert.assertTrue(initialMortgagePerMonths > mortgagePerMonth3s);
		
		// Step - 7
		// Change the Down Payment to "20%"
		library.wait(2);
		library.typeDataInto(Keys.chord(Keys.CONTROL, "a"), "MORTGAGE.DwnPaymentRateField");
		library.wait(2);
		library.typeDataInto(Keys.chord(Keys.DELETE), "MORTGAGE.DwnPaymentRateField");
		library.wait(2);
		library.typeDataInto(data.get("defaultDownPaymentText"), "MORTGAGE.downpaymentpercenttextboxxpath");
		library.wait(2);
		int mortgagePerMonth4s = mortgageCalculator();
		// Verify value of mortgage per month is the same as initial value
		Assert.assertTrue(initialMortgagePerMonths == mortgagePerMonth4s);
		
		// Step - 8
		// Change the Interest to "10%"
		library.wait(2);
		//library.click("MORTGAGE.InterestRateToggle");
		library.scrollToElement("MORTGAGE.DwnPaymentRateField");
		library.wait(2);
		library.click("MORTGAGE.InterestRateText");
		library.wait(2);
		library.typeDataInto(Keys.chord(Keys.CONTROL, "a"), "MORTGAGE.InterestRateText");
		library.wait(2);
		library.typeDataInto(Keys.chord(Keys.DELETE), "MORTGAGE.InterestRateText");
		library.wait(2);
		library.typeDataInto(Keys.chord(Keys.BACK_SPACE), "MORTGAGE.InterestRateText");
		library.wait(2);
		library.typeDataInto(data.get("interestRate"), "MORTGAGE.InterestRateTextField");
		library.wait(2);
		int mortgagePerMonth5s = mortgageCalculator();
		// Verify value of mortgage per month is bigger than initial value
		Assert.assertTrue(initialMortgagePerMonths < mortgagePerMonth5s);
		
		// Step - 9
		// Change the Interest to "3.5%"
		library.wait(2);
		library.typeDataInto(Keys.chord(Keys.CONTROL, "a"), "MORTGAGE.InterestRateText");
		library.wait(2);
		library.typeDataInto(Keys.chord(Keys.DELETE), "MORTGAGE.InterestRateText");
		library.wait(2);
		library.typeDataInto(Keys.chord(Keys.BACK_SPACE), "MORTGAGE.InterestRateText");
		library.wait(2);
		library.typeDataInto(data.get("interestInPerc").trim(), "MORTGAGE.InterestRateTextField");
		library.wait(2);
		int mortgagePerMonth6s = mortgageCalculator();
		library.wait(2);
		// Verify value of mortgage per month is the same as initial value
		Assert.assertTrue(initialMortgagePerMonths == mortgagePerMonth6s);
		
		// Step - 10
		// Click UP button on this section
		library.wait(2);
		library.click("MORTGAGE.mortgageCalculatorField");
		// Verify the detail info is hided
		Assert.assertFalse(library.verifyPageContainsElement("MORTGAGE.interest"));
	}
	
	//Set Header and get Response
	public String setHeaderAndGetResponse(Map<String, String> data){
		library.setRequestHeader(data.get("headerKey"), data.get("headerValue"));
		String response = library.HTTPGet(data.get("apiString"));
		return response;	
		}
		
	public int mortgageCalculator() {
		library.wait(4);
		String taxestext = library
				.getTextFrom("MORTGAGE.piCharttaxes");
		String[] parts = taxestext.split(" ");
		String taxes = parts[1];
		taxes = taxes.replace(",", "");
		String HomeInsurancetext = library
				.getTextFrom("MORTGAGE.piChartHomeInsurance");
		String[] parts1 = HomeInsurancetext.split(" ");
		String HomeInsurance = parts1[1];
		HomeInsurance = HomeInsurance.replace(",", "");
		String PersonalInteresttext = library
				.getTextFrom("MORTGAGE.piChartPrincipleInterest");
		String[] parts3 = PersonalInteresttext.split(" ");
		String PersonalInterest = parts3[1];
		PersonalInterest = PersonalInterest.replace(",", "");
		int Incomepermonth = Integer.parseInt(taxes) + Integer.parseInt(HomeInsurance)
				+ Integer.parseInt(PersonalInterest);
		return Incomepermonth;
	}
	
	public void dblClickMoreOrLessLink(String linkName){
		WebElement we =   library.findElement(linkName);
		Actions action = new Actions(library.getDriver());
		action
			.moveToElement(we)
			.doubleClick(we)
			.build()
			.perform();
		
	}
}

