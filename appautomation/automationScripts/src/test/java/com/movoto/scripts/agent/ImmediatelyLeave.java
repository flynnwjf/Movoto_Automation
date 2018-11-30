package com.movoto.scripts.agent;

import java.util.Map;

import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.AgentTestDataProvider;

public class ImmediatelyLeave extends BaseTest {
	
	@Test(dataProvider = "LoginTestData", dataProviderClass = AgentTestDataProvider.class, priority = 1)
	public void loginTest(Map<String, String> data) {
		if (data != null) {

			scenarios.loginToAgentAppWithUsernameAndPassword(data.get("Username"), data.get("Password"));
			scenarios.verifyLoginSuccess();
			library.wait(10);
		}
	}
	
  @Test(dependsOnMethods = { "loginTest" }, priority = 2) // AgApp5-1702,1703,1704,1712,1705.
  public void verifyImmediatelyLeave() {
	  
	  scenarios.verifyImmediatelyLeaveSteps();
	 
  }
}
