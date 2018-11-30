package com.movoto.scripts.agent;

import org.testng.annotations.Test;

import com.movoto.fixtures.FixtureLibrary;
import com.movoto.scripts.BaseTest;
import com.movoto.scripts.CommonScenarios;
import com.movoto.scripts.data.AgentTestDataProvider;

import org.testng.annotations.BeforeTest;

import java.util.Map;

import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.AfterTest;

public class NotificationLocation extends BaseTest {
	
	@Test(dataProvider = "LoginTestData", dataProviderClass = AgentTestDataProvider.class, priority = 1)
	public void loginTest(Map<String, String> data) {
		if (data != null) {

			scenarios.loginToAgentAppWithUsernameAndPassword(data.get("Username"), data.get("Password"));
			scenarios.verifyLoginSuccess();
			scenarios.navigateToNotificationsListPage();
			library.verifyPageContainsElement("NOTIFICATION.blanklist");
		}
	}
}
