package com.movoto.scripts.agent;

import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.AgentTestDataProvider;

import java.util.Map;

public class UrgencyTest extends BaseTest{

	@Test(dataProvider = "LoginTestData", dataProviderClass = AgentTestDataProvider.class, priority = 1)
	public void loginTest(Map<String, String> data) {
		if (data != null) {

			scenarios.loginToAgentAppWithUsernameAndPassword(data.get("Username"), data.get("Password"));
			scenarios.verifyLoginSuccess();
		}
	}

	@Test(dataProvider = "UrgencyStageTestData", dataProviderClass = AgentTestDataProvider.class, dependsOnMethods = {
			"loginTest" }, priority = 2)
	public void verifyUrgencyStage(Map<String, String> data) {
		if (data != null) {
			scenarios.updateUrgencyStage(data);
			scenarios.navigateToClientListPage();
		}
	}
}
