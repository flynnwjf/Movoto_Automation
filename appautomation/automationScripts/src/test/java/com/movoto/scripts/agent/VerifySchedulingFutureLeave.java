package com.movoto.scripts.agent;

import java.util.Map;

import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.AgentTestDataProvider;

import org.testng.annotations.BeforeTest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;

import com.movoto.scripts.data.AgentTestDataProvider;

public class VerifySchedulingFutureLeave extends BaseTest {
	@Test(dataProvider = "LoginTestData", dataProviderClass = AgentTestDataProvider.class, priority = 1)
	public void loginTest(Map<String, String> data) {
		if (data != null) {
			scenarios.loginToAgentAppWithUsernameAndPassword(data.get("Username"), data.get("Password"));
			scenarios.verifyLoginSuccess();
		}
	}

	// AgApp-3295,1709,2878
	@Test(dependsOnMethods = "loginTest", priority = 2)
	public void verifyImmediatelyButtonAfterTapingReturnEarly() {

		scenarios.verifyImmediateLeaveStatus();

	}

	// AgApp-719,720,(1723,68
	@Test(dataProvider = "DateProvider", dataProviderClass = AgentTestDataProvider.class, dependsOnMethods = "loginTest", priority = 3)
	public void verifyImmediateLeaveCreation(Map<String, String> data) {
		scenarios.verifyFutureLeaveCreationCriterias(data);
	}
	
}
