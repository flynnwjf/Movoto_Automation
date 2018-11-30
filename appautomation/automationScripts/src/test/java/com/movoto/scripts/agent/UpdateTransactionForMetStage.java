package com.movoto.scripts.agent;

import java.util.Map;

import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.AgentTestDataProvider;

public class UpdateTransactionForMetStage extends BaseTest {
  

	@Test(dataProvider = "LoginTestData", dataProviderClass = AgentTestDataProvider.class, priority = 1)
	 public void loginTest(Map<String, String> data) {
	  if (data != null) {
	   scenarios.loginToAgentAppWithUsernameAndPassword(data.get("Username"), data.get("Password"));
	   scenarios.verifyLoginSuccess();
	  
	   
	  }
	 }
	 
	 
	/* @Test(dependsOnMethods = "loginTest", dataProvider = "SmokeTestDataForUrgency", dataProviderClass = AgentTestDataProvider.class,priority=2)
	  public void urgencyTest(Map<String, Object> data) {
	  
	  
	  Map<String, String> urgencyData = (Map<String, String>) data.get("urgencyData");
	  scenarios.verificationupdateUrgencyStageOne(urgencyData);
	  scenarios.verifyUrgencyForTalkedStage(data);
	  scenarios.updateUrgencyStageTwo(urgencyData);
 
	  }*/
	 
	 @Test(dependsOnMethods = {"loginTest"}, dataProvider = "SmokeTestForScheduledCallbackForWeb", dataProviderClass = AgentTestDataProvider.class,priority=2)
	  public void scheduledCallback(Map<String, Object> data) {
			 
			 Map<String, Object> transactionData = (Map<String, Object>) data.get("transactionData");
			 
			 String clientName = String.valueOf(transactionData.get("ClientName"));
			 String targetStage = String.valueOf(transactionData.get("updateTransactionStage"));
			 scenarios.updateToScheduledACallbackForMetStage(targetStage, clientName,data);
			 scenarios.openNotes();
			 scenarios.verifyNotesTime(data);
			 scenarios.verifyContractCancelStageTwo(transactionData);

	  		}
}
