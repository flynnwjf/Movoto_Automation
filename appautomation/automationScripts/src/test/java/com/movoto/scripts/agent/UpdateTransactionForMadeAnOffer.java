package com.movoto.scripts.agent;

import java.util.Map;

import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.AgentTestDataProvider;

public class UpdateTransactionForMadeAnOffer extends BaseTest {
	
	
	@Test(dataProvider = "LoginTestData2", dataProviderClass = AgentTestDataProvider.class, priority = 1)
	 public void loginTest(Map<String, String> data) {
	  if (data != null) {
	   scenarios.loginToAgentAppWithUsernameAndPassword(data.get("Username"), data.get("Password"));
	   scenarios.verifyLoginSuccess();
	  }
	 }
	 
  
	@Test(dependsOnMethods = {"loginTest"}, dataProvider = "MadeAnOfferDataProviderForExtendedSmoke", dataProviderClass = AgentTestDataProvider.class,priority=2)
	  public void madeAnOffer(Map<String, Object> data) {
			 
	Map<String, String> MadeOfferData = (Map<String, String>) data.get("MadeOfferData");
			 
			scenarios.updateToMakeANOffer(MadeOfferData);
			 //scenarios.verifyContractCancelStageOne(data);
			 scenarios.openNotes();
			 scenarios.verifyNotesData(data);
			 //scenarios.verifyTransactionDetails(data);
			 scenarios.openProperties();
			 scenarios.verifyPropertyDetails(MadeOfferData);
			 //scenarios.verifyContractCancelStageTwo();
			 scenarios.navigateToClientListPage();

	}
	
	
}
