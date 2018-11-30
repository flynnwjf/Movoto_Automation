package com.movoto.scripts.agent;

import java.util.Map;

import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.AgentTestDataProvider;

public class updateToReturnToMovoto extends BaseTest{


	@Test(dataProvider = "LoginTestData2", dataProviderClass = AgentTestDataProvider.class, priority = 1)
	public void loginTest(Map<String, String> data) {
		if (data != null) {
			scenarios.loginToAgentAppWithUsernameAndPassword(data.get("Username"), data.get("Password"));
			
			scenarios.verifyLoginSuccess();	
		}
	}	
	
	@Test(dataProvider = "Emailcreated", dataProviderClass = AgentTestDataProvider.class, priority =2)
	public void updateReturnToMovoto1(Map<String, String> data) {
			scenarios.updateToReturnToMovoto1(data);	

	}
}
	
