package com.movoto.scripts.agent;

import java.util.Map;

import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.AgentTestDataProvider;

public class UpdateUrgencyForTalkedStage extends BaseTest {
 
	
	@Test(dataProvider = "LoginTestData", dataProviderClass = AgentTestDataProvider.class, priority = 1)
	public void loginTest(Map<String, String> data) {
		if (data != null) {
			scenarios.loginToAgentAppWithUsernameAndPassword(data.get("Username"), data.get("Password"));
			scenarios.verifyLoginSuccess();
			scenarios.loginVerification(data);
			  
		}
	}
	
	
	@Test(dependsOnMethods = "loginTest", dataProvider = "SmokeTestDataForUrgency", dataProviderClass = AgentTestDataProvider.class,priority=2)
	 public void urgencyTest(Map<String, Object> data) {
		
		
		Map<String, String> urgencyData = (Map<String, String>) data.get("urgencyData");
		scenarios.verificationupdateUrgencyStageOne(urgencyData); // search and select client and click on client.
		scenarios.verifyUrgencyForTalkedStage(data); // update urgency and validating time.
		scenarios.updateUrgencyStageTwo(urgencyData);
		
				
	  
	 }
}
