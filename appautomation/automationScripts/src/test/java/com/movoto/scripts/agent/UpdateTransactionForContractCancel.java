package com.movoto.scripts.agent;

import java.util.Map;

import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.AgentTestDataProvider;

public class UpdateTransactionForContractCancel extends BaseTest {
 
	@Test(dataProvider = "LoginTestData2", dataProviderClass = AgentTestDataProvider.class, priority = 1)
	 public void loginTest(Map<String, String> data) {
	  if (data != null) {
	   scenarios.loginToAgentAppWithUsernameAndPassword(data.get("Username"), data.get("Password"));
	   scenarios.verifyLoginSuccess();
	  }
	 }
	 
	@Test(dependsOnMethods = {"loginTest"}, dataProvider = "SmokeTestTransactionUpdateForContractCancelForWebForExtendedSmoke", dataProviderClass = AgentTestDataProvider.class,priority=4)
	  public void madeAnOffer(Map<String, Object> data) {
			 
			Map<String, String> transactionData = (Map<String, String>) data.get("transactionData");
	
			String clientName = transactionData.get("ClientName");
			String stage = transactionData.get("updateTransactionStage");
	
			 scenarios.updateToStageForContractCancel(stage,clientName);
			 scenarios.openNotes();
			 scenarios.verifyNotesDataForContractCancel(data);
			 scenarios.openProperties();
			 scenarios.verifyPropertyDetailsForContractCancel();
			 
			 scenarios.navigateToClientListPage();
	}
}
