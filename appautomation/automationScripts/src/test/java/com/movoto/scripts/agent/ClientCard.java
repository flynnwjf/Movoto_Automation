package com.movoto.scripts.agent;

import java.util.Map;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.AgentTestDataProvider;

public class ClientCard extends BaseTest {

	@Test(dataProvider = "LoginTestData", dataProviderClass = AgentTestDataProvider.class, priority = 1)
	public void loginTest(Map<String, String> data) {
		if (data != null) {

			scenarios.loginToAgentAppWithUsernameAndPassword(data.get("Username"), data.get("Password"));
			scenarios.verifyLoginSuccess();
		}
	}
    
	//AgApp5-437,AgApp5-1696,AgApp5-1697,AgApp5-3347,AgApp5-444,AgApp5-1910,AgApp5-325,AgApp5-66
	@Test(dataProvider = "ClientNameTestData", dataProviderClass = AgentTestDataProvider.class, dependsOnMethods = "loginTest", priority = 2)
	public void verifyClientCard(Map<String, String> map) {
		
		scenarios.verifyClientCardSteps(map);
		Reporter.log("437 is passed", true);

	}
}
