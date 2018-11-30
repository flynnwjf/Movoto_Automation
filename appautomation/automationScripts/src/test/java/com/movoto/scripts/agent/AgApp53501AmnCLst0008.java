package com.movoto.scripts.agent;

import java.util.Map;

import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.AgentTestDataProvider;

public class AgApp53501AmnCLst0008 extends BaseTest {

	@Test(dataProvider = "LoginTestData6", dataProviderClass = AgentTestDataProvider.class)
	public void loginTest(Map<String, String> data) {
		if (data != null) {

			scenarios.loginToAgentAppWithUsernameAndPassword(data.get("Username"), data.get("Password"));
			scenarios.verifyLoginSuccess();
			library.wait(10);

		}
	}

	@Test(dataProvider = "APILoginTest", dataProviderClass = AgentTestDataProvider.class, dependsOnMethods = {
			"loginTest" })
	public void StageLastUpdatedDate(Map<String, Object> data) {
		scenarios.verifyStageLastUpdatedDate(data);
	}

}
