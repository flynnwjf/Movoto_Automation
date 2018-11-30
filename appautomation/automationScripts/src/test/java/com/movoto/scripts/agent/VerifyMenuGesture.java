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

public class VerifyMenuGesture extends BaseTest {

	@Test(dataProvider = "LoginTestData", dataProviderClass = AgentTestDataProvider.class, priority = 1)
	public void loginTest(Map<String, String> data) {
		if (data != null) {
			scenarios.loginToAgentAppWithUsernameAndPassword(data.get("Username"), data.get("Password"));
			scenarios.verifyLoginSuccess();
		}
	}

	@Test(dependsOnMethods = { "loginTest" })
	public void verifyMenuGesture() {

		library.wait(3);
		scenarios.openMenu();
		library.wait(2);
		library.verifyPageContainsElement("MENU.notificationpagelink");
		scenarios.closeMenu();
		library.verifyPageContainsElement("CLIENTLIST.hamburger");
	}

	@Test(dataProvider = "GestureClientTestData", dataProviderClass = AgentTestDataProvider.class, dependsOnMethods = {
			"verifyMenuGesture" })
	public void verifyMenuGestureFromOtherFunction(Map<String, Object> data) {

		if (data != null) {
			String clientName = String.valueOf(data.get("clientName"));
			scenarios.navigateToNotificationsListPage();
			verifyMenuGesture();
			scenarios.navigateToClientListPage();
			scenarios.selectClient(clientName);
		}

	}
}
