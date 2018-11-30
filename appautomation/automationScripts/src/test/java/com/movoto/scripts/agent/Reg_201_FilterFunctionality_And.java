package com.movoto.scripts.agent;

import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.MapSearchDataProvider;

public class Reg_201_FilterFunctionality_And extends BaseTest {

	@Test(dataProvider = "FilterFunctionality", dataProviderClass = MapSearchDataProvider.class, priority = 1)
	public void verifyFilterFunctionality(Map<String, Object> data) {

		if (data != null) {
			WebDriver driver = library.getDriver();
			System.out.println(library.getTextFrom("xpath->.//*[@id='applyFilters']/span"));
			
			String mapSearchAPIResponse = scenarios.getMapSearchApiResultForFilterFunctionality(data); // hitting map search API with headers and json body.	
	    	String getTotalPropertyCount = String.valueOf(library.getValueFromJson("$.totalCount", mapSearchAPIResponse));
			library.click("xpath->.//*[@id='body']/div[2]/div[2]/ul/li[4]/a");
			Integer defaultResultCount = Integer.parseInt(library.getTextFrom("xpath->.//*[@id='applyFilters']/span"));
			Select minHouseSizeSelect = new Select(driver.findElement(By.name("minHouseSize")));
			WebElement minHouseSizeSelectedOption = minHouseSizeSelect.getFirstSelectedOption();
			Assert.assertTrue(minHouseSizeSelectedOption.getText().equals("No Min"));
			minHouseSizeSelect.selectByVisibleText("500");
			library.wait(5);
			library.click("xpath->.//*[@id='applyFilters']");
			library.click("xpath->.//*[@id='body']/div[4]/div[2]/div/a/i");
			Boolean minHouseSizeFlag = true;
			library.wait(10);
			for (int i = 1; i <= 48; i++) {
				System.out.println(library.getTextFrom("xpath->(//div[@class='baseInfo']/span[4])[" + i + "]"));
				String res = library.getTextFrom("xpath->(//div[@class='baseInfo']/span[4])[" + i + "]");
				String resWithoutComma = res.replace(",", "");
				int sqFeetInInt = Integer.parseInt(resWithoutComma);
				if (sqFeetInInt < 500) {
					minHouseSizeFlag = false;
					break;
				}
			}
			Assert.assertTrue(minHouseSizeFlag);
			library.click("xpath->.//*[@id='body']/div[2]/div[2]/ul/li[4]/a");
			Integer resultCountBeforeSelect = Integer
					.parseInt(library.getTextFrom("xpath->.//*[@id='applyFilters']/span"));
			minHouseSizeSelect.selectByVisibleText("No Min");
			library.wait(5);
			Integer defaultResultCountAfterMinSqFeet = Integer
					.parseInt(library.getTextFrom("xpath->.//*[@id='applyFilters']/span"));
			
			Assert.assertEquals(defaultResultCount, defaultResultCountAfterMinSqFeet);
			
			library.click("id->cancelFilters");
			library.wait(2);
			String countOnPage =library.getTextFrom("xpath->.//*[@class='l count']");
			String[]  countOnPageArr = countOnPage.split(" ");
			Integer countOnPageValue = Integer.parseInt(countOnPageArr[1]);
			Assert.assertEquals(countOnPageValue, resultCountBeforeSelect);
			library.click("xpath->.//*[@id='body']/div[2]/div[2]/ul/li[4]/a");
			library.click("id->resetFilters");
			library.wait(5);
			Integer resultCountAfterReset = Integer
					.parseInt(library.getTextFrom("xpath->.//*[@id='applyFilters']/span"));
			Assert.assertEquals(defaultResultCount, resultCountAfterReset);
			
	

		}
	}
}
