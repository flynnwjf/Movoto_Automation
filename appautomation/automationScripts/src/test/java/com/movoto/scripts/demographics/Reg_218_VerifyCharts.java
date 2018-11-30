package com.movoto.scripts.demographics;

import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;

public class Reg_218_VerifyCharts extends BaseTest {

	
	
	
	@Test
	public void verifyCharts() {

		if (library.getCurrentPlatform().equalsIgnoreCase("Android") || library.getCurrentPlatform().equalsIgnoreCase("IOS_WEB")) {
			scenarios.verifyChartWithApiResponse();
			scenarios.verifyUpandDownIcon();
		} else {
			scenarios.verifyChartWithApiResponse();
			scenarios.verifyUpandDownIcon();
//			scenarios.verifyGraph();
		}

	}

}