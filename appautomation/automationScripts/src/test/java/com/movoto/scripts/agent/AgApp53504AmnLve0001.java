package com.movoto.scripts.agent;

import java.util.Map;

import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.AgentTestDataProvider;

public class AgApp53504AmnLve0001 extends BaseTest {

	@Test(dataProvider = "LoginTestData1", dataProviderClass = AgentTestDataProvider.class, priority=1)
	public void loginTest(Map<String, String> data) {
		if (data != null) {

			scenarios.loginToAgentAppWithUsernameAndPassword(data.get("Username"), data.get("Password"));
			scenarios.verifyLoginSuccess();
			library.wait(10);

		}
	}
	
	@Test(dataProviderClass = AgentTestDataProvider.class, dataProvider = "APIgetLeavefunctional",priority=2)
	public void goOnImmediateLeave(Map<String, Object> data)
	{
		scenarios.goOnImmediateLeave(data);
	}
	
}
