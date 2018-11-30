package com.movoto.scripts.agent;

import java.util.Map;

import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.AgentTestDataProvider;

public class ImmediatelyLeaveWithFutureLeave extends BaseTest {

	@Test(dataProvider = "LoginTestData", dataProviderClass = AgentTestDataProvider.class, priority = 1)
	public void loginTest(Map<String, String> data) {
		if (data != null) {

			scenarios.loginToAgentAppWithUsernameAndPassword(data.get("Username"), data.get("Password"));
			scenarios.verifyLoginSuccess();
		}
	}

	//AgApp5-2899
	//For Web - AgApp5-1702,AgApp5-1703,AgApp5-1704,AgApp5-2899,AgApp5-3012,AgApp5-3012
	@Test(dataProvider = "FutureLeaveTestData", dataProviderClass = AgentTestDataProvider.class,dependsOnMethods = { "loginTest" }, priority = 2)
	public void verifyImmediatelyLeaveWithFutureLeave(Map<String, String> data) {
		
		scenarios.verifyImmediatelyLeaveWithFutureLeaveSteps(data);
    	
	}
}
