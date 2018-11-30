package com.movoto.scripts.agent;

import java.util.Map;

import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.AgentTestDataProvider;

public class UpdateEmailedforFollowUpStage extends BaseTest{


	@Test(dataProvider = "LoginTestData2", dataProviderClass = AgentTestDataProvider.class, priority = 1)
	public void loginTest(Map<String, String> data) {
		if (data != null) {
			scenarios.loginToAgentAppWithUsernameAndPassword(data.get("Username"), data.get("Password"));
			
			scenarios.verifyLoginSuccess();	
		}
	}	
	
	@Test(dataProvider = "APILoginTest2", dataProviderClass = AgentTestDataProvider.class, priority = 2)
	public void updateEmailed(Map<String, Object> data) {
		if (data != null) {
			scenarios.updateLeadStageToTalked(data);	
		}
	}

}
