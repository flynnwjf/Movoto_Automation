package com.movoto.scripts.agent;

import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.MapSearchDataProvider;

/*
 *  @author Puneet.Bohra
 *  Reg-201 -> Verify the more filter function work correctly
 */

public class Reg_201_FilterFunctionality extends BaseTest {

	@Test(dataProvider = "FilterFunctionality", dataProviderClass = MapSearchDataProvider.class, priority = 1)
	public void verifyFilterFunctionality(Map<String, Object> data) {
		boolean flag = false;
		if (data != null) {
			flag = scenarios.checkPrecondiotionsForTC201(data); // Precondition
			if (flag) {
				scenarios.verifyFilterFunctionality(data);
			} else {
				Assert.assertTrue(false,"Please check data for precondition!!!");
			}
		}
	}
}