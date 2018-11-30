package com.movoto.scripts.agent;

import org.testng.annotations.Test;

import com.movoto.fixtures.FixtureLibrary;
import com.movoto.scripts.BaseTest;
import com.movoto.scripts.CommonScenarios;
import com.movoto.scripts.data.AgentTestDataProvider;

import org.testng.annotations.BeforeTest;

import java.util.Map;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.AfterTest;

public class SearchClientNameAndVerify extends BaseTest {

	@Test(dataProvider = "LoginTestData", dataProviderClass = AgentTestDataProvider.class, priority = 1)
	public void loginTest(Map<String, String> data) {
		if (data != null) {
			scenarios.loginToAgentAppWithUsernameAndPassword(data.get("Username"), data.get("Password"));
			scenarios.verifyLoginSuccess();
		}
	}

	// AgApp5-40,AgApp5-39,AgApp5-317
	@Test(dataProvider = "ClientNameTestData", dataProviderClass = AgentTestDataProvider.class, dependsOnMethods = {
			"loginTest" }, priority = 2)
	public void searchClientNameAndVerify(Map<String, String> data) {
		if (data != null) {

			scenarios.searchForClientAndVerify(data);	
		}
	}

	@Test(dependsOnMethods = {"loginTest"},priority = 3)
		
		public void notificationPage(){
			scenarios.navigateToNotificationsListPage();	
		}
		
	
	//AgApp5-457,AgApp5-513,AgApp5-514
	@Test(dataProvider = "ClientNameTestData", dataProviderClass = AgentTestDataProvider.class, dependsOnMethods = {
			"loginTest" }, priority = 4)
	
	
	public void searchClientNameAndVerifyOnNotificationManagerPage(Map<String, String> data) {

		if (data != null) {

			scenarios.searchForClientAndVerify(data);
		}
	}
	
}
