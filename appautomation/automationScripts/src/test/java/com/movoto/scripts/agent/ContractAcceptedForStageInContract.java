package com.movoto.scripts.agent;

import java.util.Map;

import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.AgentTestDataProvider;
//import com.movoto.scripts.salesforce.CommonScenariosSF;


// Puneet : - Modified priorities of all test cases 12/may/2016.
public class ContractAcceptedForStageInContract extends BaseTest {
	
	
	
	@Test(dataProvider = "LoginTestData2", dataProviderClass = AgentTestDataProvider.class, priority = 1)
	public void loginTest(Map<String, String> data) {
		if (data != null) {
			scenarios.loginToAgentAppWithUsernameAndPassword(data.get("Username"), data.get("Password"));
			
			scenarios.verifyLoginSuccess();	
		}
	}
	
	
	 // getting malformed URL exception
	@Test(dataProvider = "APILoginTest2", dataProviderClass = AgentTestDataProvider.class, priority = 2)
	public void updateToContractAcceptedStage(Map<String, Object> data) {
		if (data != null) {
            scenarios.updateToContractAcceptedStage1(data);
            

	 		}
}
}
