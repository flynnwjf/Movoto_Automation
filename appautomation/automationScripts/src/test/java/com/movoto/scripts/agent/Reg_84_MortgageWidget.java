package com.movoto.scripts.agent;

import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.AgentTestDataProvider;
import com.movoto.scripts.data.DPPTestCaseDataProvider;

public class Reg_84_MortgageWidget extends BaseTest {

	@Test(dataProvider = "TestDataForReg_84", dataProviderClass = DPPTestCaseDataProvider.class)
	public void MortgaeTest(Map<String, String> data) {
		System.out.println("Reg_84 is running");
		scrollTo("//div[@id='mortgageWidget']//thead//tr[1]/th[1]");
		library.wait(3);
		Assert.assertEquals(library.getTextFrom("MORTGAGE.Lenderxpath"), data.get("lender"));
		Assert.assertEquals(library.getTextFrom("MORTGAGE.APRxpath"), data.get("aprValue"));
		Assert.assertEquals(library.getTextFrom("MORTGAGE.EstPayment"), data.get("estimatedPayment"));
		Assert.assertEquals(library.getTextFrom("MORTGAGE.Rate"), data.get("rateValue"));
		String linkname = library.getAttributeOfElement("href", "MORTGAGE.SeeMoreRatesURLxpath");
		System.out.println(linkname);
		Assert.assertTrue(linkname.contains(data.get("urlContent")));
	}

	public void scrollTo(String locator) {
		JavascriptExecutor jse = (JavascriptExecutor) library.getDriver();
		WebElement element = library.getDriver().findElement(By.xpath(locator));
		jse.executeScript("arguments[0].scrollIntoView();", element);
		library.verifyPageContainsElement("HOMEPAGE.publicRecords");
	}
}