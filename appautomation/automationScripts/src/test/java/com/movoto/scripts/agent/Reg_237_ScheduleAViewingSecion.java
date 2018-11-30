package com.movoto.scripts.agent;

import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.DPPTestCaseDataProvider;

public class Reg_237_ScheduleAViewingSecion extends BaseTest{
	
	@Test(dataProvider = "TestDataForReg_237", dataProviderClass = DPPTestCaseDataProvider.class)
	public void schecduleAView(Map<String, String> data){
		System.out.println("Reg-237 is running");
		scenarios.removeAddsPopUp();
		scrollToElementAndVerify(".//*[@id='inlineHotleadField']/div/div");
		library.wait(2);
		if(library.getCurrentPlatform().equals("Web") || library.getCurrentPlatform().equals("Android"))
			library.clear("HOMEPAGE.emailInScheduleViewing");
		
		library.clear("HOMEPAGE.nameInScheduleViewing");
		library.clear("HOMEPAGE.contactNumInScheduleViewing");
		library.wait(2);
		if(library.getCurrentPlatform().equals("Web") || library.getCurrentPlatform().equals("Android"))
			library.typeDataInto(data.get("email"),"HOMEPAGE.emailInScheduleViewing");
		
		library.typeDataInto(data.get("name"),"HOMEPAGE.nameInScheduleViewing");
		library.typeDataInto(data.get("contactNumber"),"HOMEPAGE.contactNumInScheduleViewing");
		
		if(library.getCurrentPlatform().equals("Android") || library.getCurrentPlatform().equals("IOS_WEB")){
			library.wait(1);
			library.navigateBack();
		}
		
		library.wait(2);
		library.click("HOMEPAGE.goButtonInScheduleViewing");
		library.wait(4);
//		Assert.assertTrue(library.verifyPageContainsElement("POPUP.msgInThankYouPopup"));
		Assert.assertTrue(library.verifyPageContainsElement("POPUP.thankYou"));
		
		
	}
	
	public void scrollToElementAndVerify(String path) {
		library.wait(2);
		JavascriptExecutor jse = (JavascriptExecutor) library.getDriver();
		WebElement element = library.getDriver().findElement(By.xpath(path));
		jse.executeScript("arguments[0].scrollIntoView();", element);
		Assert.assertTrue(element.isDisplayed());
		library.wait(2);
	}

}
