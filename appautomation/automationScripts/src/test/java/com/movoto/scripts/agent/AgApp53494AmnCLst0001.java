package com.movoto.scripts.agent;

import java.util.Map;

import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.AgentTestDataProvider;

public class AgApp53494AmnCLst0001 extends BaseTest {

	@Test(dataProvider = "LoginTestData6", dataProviderClass = AgentTestDataProvider.class)
	public void loginTest(Map<String, String> data) {
		if (data != null) {

			scenarios.loginToAgentAppWithUsernameAndPassword(data.get("Username"), data.get("Password"));
			scenarios.verifyLoginSuccess();
			library.wait(10);

		}
	}

	@Test(dataProvider = "APILoginTest", dataProviderClass = AgentTestDataProvider.class, dependsOnMethods = {
			"loginTest" }, priority = 2)
	public void LeadsNameTest(Map<String, Object> data) {

		scenarios.verifyLeadsName(data);
		scenarios.verifyTotalLeadCounts(data);
		
	}

}
