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

public class Reg_200_FilterFunctionality_And extends BaseTest {

	@Test(dataProvider = "FilterFunctionality", dataProviderClass = MapSearchDataProvider.class, priority = 1)
	public void verifyFilterFunctionality(Map<String, Object> data) {

		if (data != null) {
			WebDriver driver = library.getDriver();
			library.click("xpath->.//*[@id='body']/div[2]/div[2]/ul/li[4]/a");
			System.out.println(library.getTextFrom("xpath->.//*[@id='applyFilters']/span"));
			Integer defaultResultCount = Integer.parseInt(library.getTextFrom("xpath->.//*[@id='applyFilters']/span"));

			// start of verifyDefaultValuesOnSearchPage
			List<WebElement> minBedRadioButton = driver.findElements(By.name("minBed"));
			List<WebElement> minBathRadioButton = driver.findElements(By.name("minBath"));
			Select minPriceSelect = new Select(driver.findElement(By.name("minPrice")));
			Select maxPriceSelect = new Select(driver.findElement(By.name("maxPrice")));
			WebElement minPriceSelectedOption = minPriceSelect.getFirstSelectedOption();
			WebElement maxPriceSelectedOption = maxPriceSelect.getFirstSelectedOption();
			System.out.println(minPriceSelectedOption.getText());
			System.out.println(maxPriceSelectedOption.getText());
			Assert.assertTrue(minPriceSelectedOption.getText().equals("No Min"));
			Assert.assertTrue(maxPriceSelectedOption.getText().equals("No Max"));
			Assert.assertTrue(minBedRadioButton.get(0).isSelected());
			Assert.assertTrue(minBathRadioButton.get(0).isSelected());
			// end of verifyDefaultValuesOnSearchPage

			// start of selectMinValueForPriceAndVerify
			minPriceSelect.selectByVisibleText("$100K");
			library.wait(5);
			library.click("xpath->.//*[@id='applyFilters']");
			library.click("xpath->.//*[@id='body']/div[4]/div[2]/div/a/i");
			Boolean minPriceFlag = true;
			library.wait(10);
			for (int i = 1; i <= 50; i++) {
				System.out.println(library.getTextFrom("xpath->(//span[@class='price'])[" + i + "]"));
				String res = library.getTextFrom("xpath->(//span[@class='price'])[" + i + "]");
				String resWithoutComma = res.replace(",", "");
				String resWithout$ = resWithoutComma.replace("$", "");
				int priceInInt = Integer.parseInt(resWithout$);
				if (priceInInt < 100) {
					minPriceFlag = false;
					break;
				}
			}
			Assert.assertTrue(minPriceFlag);
			library.click("xpath->.//*[@id='body']/div[2]/div[2]/ul/li[4]/a");
			minPriceSelect.selectByVisibleText("No Min");
			library.wait(5);
			Integer defaultResultCountAfterMinReset = Integer
					.parseInt(library.getTextFrom("xpath->.//*[@id='applyFilters']/span"));
			Assert.assertEquals(defaultResultCount, defaultResultCountAfterMinReset);
			// end of selectMinValueForPriceAndVerify

			// start of selectMaxPriceValueAndVerify
			maxPriceSelect.selectByVisibleText("$10M");
			library.wait(5);
			library.click("xpath->.//*[@id='applyFilters']");
			Boolean maxPriceFlag = true;
			library.wait(10);
			for (int i = 1; i <= 50; i++) {
				System.out.println(library.getTextFrom("xpath->(//span[@class='price'])[" + i + "]"));
				String res = library.getTextFrom("xpath->(//span[@class='price'])[" + i + "]");
				String resWithoutComma = res.replace(",", "");
				String resWithout$ = resWithoutComma.replace("$", "");
				int priceInInt = Integer.parseInt(resWithout$);
				if (priceInInt > 10000000) {
					maxPriceFlag = false;
					break;
				}
			}
			Assert.assertTrue(maxPriceFlag);
			library.click("xpath->.//*[@id='body']/div[2]/div[2]/ul/li[4]/a");
			maxPriceSelect.selectByVisibleText("No Max");
			library.wait(5);
			Integer defaultResultCountAfterMaxReset = Integer
					.parseInt(library.getTextFrom("xpath->.//*[@id='applyFilters']/span"));
			Assert.assertEquals(defaultResultCount, defaultResultCountAfterMaxReset);
			// end of selectMaxPriceValueAndVerify

			
			// start of selectMinValueForBedAndVerify	
			library.click("xpath->.//*[@id='bedsRadio']/div/label[2]");
			library.wait(2);
			library.click("xpath->.//*[@id='applyFilters']");
			//library.click("xpath->.//*[@id='body']/div[4]/div[2]/div/a/i");
			library.wait(10);
			Boolean minBd=true;
			for (int i = 1; i <= 50; i++) {
				System.out.println(library.getTextFrom("xpath->(//div[@class='baseInfo']/span[2])[" + i + "]"));
				String res = library.getTextFrom("xpath->(//div[@class='baseInfo']/span[2])[" + i + "]");
				
				int noOfBed = Integer.parseInt(res);
				if (noOfBed < 1) {
					minBd = false;
					break;
				}
			}
			Assert.assertTrue(minBd);
			library.click("xpath->.//*[@id='body']/div[2]/div[2]/ul/li[4]/a");
			library.click("xpath->.//*[@id='bedsRadio']/div/label[1]");
			library.wait(5);
			Integer defaultResultCountAfterMinBd = Integer
					.parseInt(library.getTextFrom("xpath->.//*[@id='applyFilters']/span"));
			Assert.assertEquals(defaultResultCount, defaultResultCountAfterMinBd);
			// end of selectMinValueForBedAndVerify	
			
			// start of selectMinValueForBathAndVerify
			library.click("xpath->.//*[@id='bathsRadio']/div/label[2]");
			library.wait(2);
			library.click("xpath->.//*[@id='applyFilters']");
			//library.click("xpath->.//*[@id='body']/div[4]/div[2]/div/a/i");
			library.wait(10);
			Boolean minBath=true;
			for (int i = 1; i <= 50; i++) {
				System.out.println(library.getTextFrom("xpath->(//div[@class='baseInfo']/span[3])[" + i + "]"));
				String res = library.getTextFrom("xpath->(//div[@class='baseInfo']/span[3])[" + i + "]");
				float noOfBath = Float.parseFloat(res);
				//int noOfBath = Integer.parseInt(res);
				if (noOfBath < 1) {
					minBath = false;
					break;
				}
			}
			Assert.assertTrue(minBath);
			library.click("xpath->.//*[@id='body']/div[2]/div[2]/ul/li[4]/a");
			library.click("xpath->.//*[@id='bathsRadio']/div/label[1]");
			library.wait(5);
			Integer defaultResultCountAfterMinBath = Integer
					.parseInt(library.getTextFrom("xpath->.//*[@id='applyFilters']/span"));
			Assert.assertEquals(defaultResultCount, defaultResultCountAfterMinBath);
			// end of selectMinValueForBathAndVerify
			/*
			 * boolean isPriceDefaultValueIsCorrect =
			 * priceNoMinDefaultValue.contains("No Min"); boolean
			 * isPriceDefaultValueIsCorrectForMax =
			 * priceNoMaxDefaultValue.contains("No Max"); boolean
			 * isBedDefaultValueIsCorrect = bedDefaultValue.contains("Any");
			 * boolean isBathsDefaultValueIsCorrect =
			 * bathDefaultValue.contains("Any");
			 * 
			 * Assert.assertTrue(isPriceDefaultValueIsCorrect);
			 * Assert.assertTrue(isPriceDefaultValueIsCorrectForMax);
			 * Assert.assertTrue(isBedDefaultValueIsCorrect);
			 * Assert.assertTrue(isBathsDefaultValueIsCorrect);
			 */
			// scenarios.verifyDefaultValuesOnSearchPage();
			
	
			// scenarios.selectMinValueForPriceAndVerify();
			// scenarios.selectMaxPriceValueAndVerify();
			 //scenarios.selectMinValueForBedAndVerify();
			// scenarios.selectMinValueForBathAndVerify();

		}
	}
}
