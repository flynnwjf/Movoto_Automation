package com.movoto.scripts.agent;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.utils.JSONParserForAutomationNG;
import com.sun.jna.Library;

import freemarker.template.SimpleDate;

public class Reg_238_PropertyHistorySection extends BaseTest {

	JSONParserForAutomationNG jsonParser;
	JSONObject jsonObj;
	WebDriver wdriver;

	@Test
	@Parameters("dataProviderPath")
	public void propertySectionHistory(String dataProviderPath) {

		try {
			jsonParser = new JSONParserForAutomationNG(dataProviderPath);
			jsonObj = jsonParser.getNode("PropertyHistorySection");
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (jsonObj == null)
			Assert.assertTrue(false, "Json Datafile is empty or with error!!!");
		else {
			verifyPropertyHistorySection();
		}
	}

	public void verifyPropertyHistorySection() {
		scenarios.removeAddsPopUp();
		String response = setHeaderAndGetResponse();

		Assert.assertTrue(scrollAndVerifyText("//span[contains(text(),'Property History')]"));

		for (int i = 1; i <= 2; i++) {

			String statusValue = library.getTextFrom(
					"xpath->(.//*[@id='priceHistoryField']//div[@class='row innerrow'])[" + i + "]/div[1]/div[1]");
			String statusInResponse = (String) library.getValueFromJson("$.histories[" + (2 - i) + "].status",
					response);
			if (statusValue.equals("Listed"))
				Assert.assertTrue(statusInResponse.equals("Active"));
			else
				Assert.assertEquals(statusValue, statusInResponse);

			String dateValuez = library.getTextFrom(
					"xpath->(.//*[@id='priceHistoryField']//div[@class='row innerrow'])[" + i + "]/div[2]");
			String dateInResponse = ((String) library.getValueFromJson("$.histories[" + (2 - i) + "]dateTime",
					response)).replaceAll("-", "/");
			DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
			String apiDateFormate = null;
			try {
				Date startDate = (Date) formatter.parse(dateInResponse);
				apiDateFormate = new SimpleDateFormat("MMM").format(startDate) + " "
						+ new SimpleDateFormat("d").format(startDate) + ", "
						+ new SimpleDateFormat("YYYY").format(startDate);
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Assert.assertEquals(apiDateFormate, dateValuez);
			if (library.getCurrentPlatform().equals("Web")) {
				if (library
						.getTextFrom(
								"xpath->(.//*[@id='priceHistoryField']//div[@class='row innerrow'])[" + i + "]/div[4]")
						.trim().equals("—")) {
					String changeValue = (String) library.getValueFromJson("$.histories[" + (2 - i) + "].change",
							response);
					Assert.assertTrue(changeValue == null);
				} else {
					System.out.println(library.getTextFrom(
							"xpath->(.//*[@id='priceHistoryField']//div[@class='row innerrow'])[" + i + "]/div[4]")
							.trim());
					Double changeValue = Double.parseDouble(library.getTextFrom(
							"xpath->(.//*[@id='priceHistoryField']//div[@class='row innerrow'])[" + i + "]/div[4]")
							.replace("%", "").toString());
					double value = (double) changeValue;
					Double changeValueInResponse = Double.parseDouble(
							library.getValueFromJson("$.histories[" + (2 - i) + "].change", response).toString()) * 100;
					double valueInApi = (double) changeValueInResponse;
					Assert.assertTrue(value - valueInApi < 0.01 || value - valueInApi > -0.01);
				}
			}

			String priceUI = library
					.getTextFrom("xpath->(.//*[@id='priceHistoryField']//div[@class='row innerrow'])[" + i + "]/div[3]")
					.replace("$", "").replaceAll(",", "").toString();
			Double priceValue = Double.parseDouble(priceUI);
			Double priceInResponse = (Double) library.getValueFromJson("$.histories[" + (2 - i) + "].price", response);
			Assert.assertEquals(priceInResponse, priceValue);

		}

	}

	public boolean scrollAndVerifyText(String elementPath) {

		JavascriptExecutor jse = (JavascriptExecutor) library.getDriver();
		WebElement ele = library.getDriver().findElement(By.xpath(elementPath));
		jse.executeScript("arguments[0].scrollIntoView(true);", ele);// arguments[0].scrollIntoView(true);
		library.wait(2);
		return library.verifyPageContainsElement("xpath->" + elementPath);

	}

	public String setHeaderAndGetResponse() {
		String response = null;

		library.setRequestHeader(jsonObj.get("Header_Key").toString(), jsonObj.get("Header_Value").toString());
		response = (String) library.HTTPGet(jsonObj.get("API").toString());

		return response;
	}

}
