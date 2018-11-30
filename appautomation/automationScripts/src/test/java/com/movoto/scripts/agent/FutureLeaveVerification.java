package com.movoto.scripts.agent;

import java.util.Map;

import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.AgentTestDataProvider;

public class FutureLeaveVerification extends BaseTest {

	@Test(dataProvider = "LoginTestData", dataProviderClass = AgentTestDataProvider.class, priority = 1)
	public void loginTest(Map<String, String> data) {
		if (data != null) {

			scenarios.loginToAgentAppWithUsernameAndPassword(data.get("Username"), data.get("Password"));
			scenarios.verifyLoginSuccess();
			//library.wait(10);
		}
	}

	@Test(dataProvider = "SecondFutureLeaveTestData", dataProviderClass = AgentTestDataProvider.class, dependsOnMethods = {
			"loginTest" }, priority = 2) //AgApp5-1735 AgApp5-1738,
	
	public void verifyFutureLeaveErrorMessage(Map<String, String> data) {

		if (data != null) {

			scenarios.enterSecondFutureLeaveAndExpectErrorMessage(data);
			scenarios.navigateToClientListPage();

		}

	}

}
