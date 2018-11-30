package com.movoto.scripts.agent;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.MapSearchDataProvider;

public class Reg_200_FilterFunctionality extends BaseTest {

	@Test(dataProvider = "FilterFunctionality", dataProviderClass = MapSearchDataProvider.class, priority = 1)
	public void verifyFilterFunctionality(Map<String, Object> data) {
         
		boolean flag = false;
		if (data != null) {
			flag = scenarios.checkPrecondiotionsForTC200(data); // For Precondition
			if (flag) {
				String DefaultBathValue = (String) data.get("DefaultBathValue");
		        String DefaultBedValue = (String)data.get("DefaultBedValue");
				String minPriceByDefaultOption = (String) data.get("MinPriceByDefaultOption");
				String maxPriceByDefaultOption = (String) data.get("MaxPriceByDefaultOption");
				String minPriceData = (String) data.get("MinPrice");
				String removeSpecialChaFromMinPriceData = minPriceData.replace("$", "");
				String removeSpecialChaFromMinPrice = removeSpecialChaFromMinPriceData.replaceAll("K", "000");
				int minPriceInInt = Integer.parseInt(removeSpecialChaFromMinPrice);
				String maxPrice = (String) data.get("MaxPrice");
				String removeSpecialChaFromMaxPrice = maxPrice.replace("$", "");
				String removeSpecialChaFromMax = removeSpecialChaFromMaxPrice.replaceAll("K", "000");
				int maxPriceInInt = Integer.parseInt(removeSpecialChaFromMax);
				String defaultBedValue = (String) data.get("DefaultBedValue");
				String defaultBathValue = (String) data.get("DefaultBathValue");
				String minBedValue = (String) data.get("MinBedValue");
				String minBathValue = (String) data.get("MinBathValue");
				
				if (library.getCurrentPlatform().equalsIgnoreCase("Android") || library.getCurrentPlatform().equals("IOS_WEB")) {
					WebDriver driver = library.getDriver();
					library.waitForElement("SEARCHPAGE.filtersButton");
					library.click("SEARCHPAGE.filtersButton");
					System.out.println(library.getTextFrom("MOREFILTERS.applyButtonCount"));
					Integer defaultResultCount = Integer
							.parseInt(library.getTextFrom("MOREFILTERS.applyButtonCount"));

					// start of verifyDefaultValuesOnSearchPage
					//Used driver.findElements because because of library.findElements under construction.
					List<WebElement> minBedRadioButton = driver.findElements(By.name("minBed"));
					List<WebElement> minBathRadioButton = driver.findElements(By.name("minBath"));
					Select minPriceSelect = new Select(driver.findElement(By.name("minPrice")));
					Select maxPriceSelect = new Select(driver.findElement(By.name("maxPrice")));
					WebElement minPriceSelectedOption = minPriceSelect.getFirstSelectedOption();
					WebElement maxPriceSelectedOption = maxPriceSelect.getFirstSelectedOption();
					System.out.println(minPriceSelectedOption.getText());
					System.out.println(maxPriceSelectedOption.getText());
					Assert.assertTrue(minPriceSelectedOption.getText().equals(minPriceByDefaultOption));
					Assert.assertTrue(maxPriceSelectedOption.getText().equals(maxPriceByDefaultOption));
					String bathByfaultValue = library.getTextFrom("SEARCHPAGE.bedByDefaultValue");
					Assert.assertEquals(bathByfaultValue,DefaultBathValue);
					String bedBydefaultValue = library.getTextFrom("SEARCHPAGE.bathByDefaultValue");
					Assert.assertEquals(bedBydefaultValue,DefaultBedValue);
					// end of verifyDefaultValuesOnSearchPage

					// start of selectMinValueForPriceAndVerify
					minPriceSelect.selectByVisibleText(minPriceData);
					library.wait(5);
					library.click("MOREFILTERS.applyButton");
					library.wait(3);
					boolean isAlertBoxPresent = library.verifyPageContainsElement("PROPERTY.selectnoinalertbox");
					if (isAlertBoxPresent) {

						try {
							library.waitForElement("PROPERTY.selectnoinalertbox");
							library.click("PROPERTY.selectnoinalertbox");
							library.wait(4);
						} catch (Exception e) {

						}
					}
					scenarios.verifyMinPrice(data);
					library.click("SEARCHPAGE.filtersButton");
					minPriceSelect.selectByVisibleText(minPriceByDefaultOption);
					//Page transition thats why used hard wait.
					library.wait(10);
					Integer defaultResultCountAfterMinReset = Integer
							.parseInt(library.getTextFrom("MOREFILTERS.applyButtonCount"));
					Assert.assertEquals(defaultResultCount, defaultResultCountAfterMinReset);
					// end of selectMinValueForPriceAndVerify

					// start of selectMaxPriceValueAndVerify
					maxPriceSelect.selectByVisibleText(maxPrice);
					library.wait(5);
					library.click("MOREFILTERS.applyButton");
					scenarios.verifyMaxPrice(data);
					
					library.click("SEARCHPAGE.filtersButton");
					maxPriceSelect.selectByVisibleText(maxPriceByDefaultOption);
					library.wait(5);
					Integer defaultResultCountAfterMaxReset = Integer
							.parseInt(library.getTextFrom("MOREFILTERS.applyButtonCount"));
					Assert.assertEquals(defaultResultCount, defaultResultCountAfterMaxReset);
					// end of selectMaxPriceValueAndVerify
					// start of selectMinValueForBedAndVerify
					library.click("MOREFILTERS.radioButtonForBedForLabel");
					library.wait(2);
					library.click("MOREFILTERS.applyButtonCount");
					try {
						driver.findElement(By.xpath("SEARCHPAGE.alertPopup")).click();
					} catch (Exception e) {

					}
					//Page transition thats why used hard wait
					library.wait(10);
					scenarios.verifyMinValueForBed(data);
					library.click("SEARCHPAGE.filtersButton");
					library.wait(5);
					library.click("MOREFILTERS.radioButtonForBed");
					library.wait(5);
					Integer defaultResultCountAfterMinBd = Integer
							.parseInt(library.getTextFrom("MOREFILTERS.applyButtonCount"));
					Assert.assertEquals(defaultResultCount, defaultResultCountAfterMinBd);
					// end of selectMinValueForBedAndVerify

					// start of selectMinValueForBathAndVerify
					library.click("MOREFILTERS.bath");
					library.wait(2);
					library.click("MOREFILTERS.applyButton");
					try {
						//used Hard coded xpath due to issue with library find Elements.
						driver.findElement(By.xpath(".//*[@id='body']/div[4]/div[2]/div/a/i")).click();
					} catch (Exception e) {

					}
					
					library.wait(10);
					scenarios.verifyMinValueForBath(data);
					//library.click("SEARCHPAGE.backToFilterButton");
					library.click("SEARCHPAGE.filtersButton");
					library.waitForElement("MOREFILTERS.radioButtonForBath");
					library.wait(3);
					library.click("MOREFILTERS.radioButtonForBath");
					library.waitForElement("MOREFILTERS.applyButtonCount");
					library.wait(10);
					Integer defaultResultCountAfterMinBath = Integer
							.parseInt(library.getTextFrom("MOREFILTERS.applyButtonCount"));
					Assert.assertEquals(defaultResultCount, defaultResultCountAfterMinBath);
				} else {
					scenarios.verifyDefaultValuesOnSearchPage(data);
					scenarios.selectMinValueForPriceAndVerify(data);
					scenarios.selectMaxPriceValueAndVerify(data);
					scenarios.selectMinValueForBedAndVerify(data);
					scenarios.selectMinValueForBathAndVerify(data);
				}
			} else {
				Assert.assertTrue(false, "Preconditions doesn't match.");
			} // Preconditions ends here
		}
	}
}