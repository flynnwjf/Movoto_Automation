package com.movoto.scripts.agent;

import java.util.Map;

import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.AgentTestDataProvider;

public class ActivitySelectionListForMetStage extends BaseTest {
 
	
	@Test(dataProvider = "LoginTestData", dataProviderClass = AgentTestDataProvider.class, priority = 1)
	public void loginTest(Map<String, String> data) {
		if (data != null) {
			scenarios.loginToAgentAppWithUsernameAndPassword(data.get("Username"), data.get("Password"));
			scenarios.verifyLoginSuccess();
			
		}
	}
	
	
	@Test(dependsOnMethods = "loginTest", dataProvider = "SmokeTestTransactionUpdateForWeb", dataProviderClass = AgentTestDataProvider.class,priority=2)
	public void transactionUpdate(Map<String, Object> data) {
			 
			 Map<String, Object> transactionData = (Map<String, Object>) data.get("transactionData");
			 
			 String clientName = (String) transactionData.get("ClientName");
			 
			scenarios.searchAndSelectClient(clientName);// search and select client.
			
			scenarios.verifyActivityListSages();// click on update button and verify whether it is showing expected stage.
		
	 }
}
