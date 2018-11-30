package com.movoto.scripts.agent;

import java.util.Map;

import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.AgentTestDataProvider;
//import com.movoto.scripts.salesforce.CommonScenariosSF;


// Puneet : - Modified priorities of all test cases 12/may/2016.
public class Activities_workflow extends BaseTest {
	
	
	
	@Test(dataProvider = "LoginTestData", dataProviderClass = AgentTestDataProvider.class, priority = 1)
	public void loginTest(Map<String, String> data) {
		if (data != null) {
			scenarios.loginToAgentAppWithUsernameAndPassword(data.get("Username"), data.get("Password"));
			scenarios.verifyLoginSuccess();
			
		}
	}	
	
	@Test(dependsOnMethods = "loginTest", dataProvider = "SmokeTestDataForUrgency", dataProviderClass = AgentTestDataProvider.class,priority=2)
	 public void urgencyTest(Map<String, Object> data) {
		
		Map<String, String> urgencyData = (Map<String, String>) data.get("urgencyData");
	  
		scenarios.updateUrgencyStageOne(urgencyData);
		
		scenarios.verifyUrgencyDetails(data);
		scenarios.updateUrgencyStageTwo(urgencyData);
		
				
	  
	 }
	
	@Test(dataProvider = "SmokeTestTransactionUpdateForWeb", dataProviderClass = AgentTestDataProvider.class,priority=3)
	public void transactionUpdate(Map<String, Object> data) {
			 
			 Map<String, Object> transactionData = (Map<String, Object>) data.get("transactionData");
			 
			 //scenarios.verifyContractCancelStageSteps(transactionData);
			 scenarios.verifyContractCancelStageOne(transactionData);
			 scenarios.openNotes();
			 scenarios.verifyTransactionDetails(data);
			 scenarios.verifyContractCancelStageTwo(transactionData);
			 	
	 }
	
	@Test(dependsOnMethods = {"transactionUpdate"}, dataProvider = "SmokeTestForMadeAnOfferForWeb", dataProviderClass = AgentTestDataProvider.class,priority=4)
	  public void madeAnOffer(Map<String, Object> data) {
			 
			 Map<String, Object> MadeOfferData = (Map<String, Object>) data.get("MadeOfferData");
			 
			 
			 scenarios.verifyContractCancelStageOne(data);
			 scenarios.openNotes();
			 scenarios.verifyTransactionDetails(data);
			 scenarios.verifyContractCancelStageTwo(MadeOfferData);

	}
	 
	 // getting malformed URL exception
	 @Test(dependsOnMethods = {"madeAnOffer"}, dataProvider = "SmokeTestDataForContractAcceptForWeb", dataProviderClass = AgentTestDataProvider.class,priority=5)
	 public void contractAccept(Map<String, Object> data) {
	 		 
	 		 Map<String, Object> contractAcceptData = (Map<String, Object>) data.get("contractAcceptData");
	 		 
	 		 
	 		 scenarios.verifyContractCancelStageOne(data);
	 		 scenarios.openNotes();
	 		 scenarios.verifyTransactionDetails(data);
	 		 scenarios.verifyContractCancelStageTwo(contractAcceptData);

	 		}

 @Test(dependsOnMethods = {"contractAccept"}, dataProvider = "SmokeTestTransactionUpdateForContractCancelForWeb", dataProviderClass = AgentTestDataProvider.class,priority=6)
  public void contractCancel(Map<String, Object> data) {
		 
		 Map<String, Object> transactionData = (Map<String, Object>) data.get("transactionData");
		 
		 //scenarios.verifyContractCancelStageSteps(transactionData);
		 scenarios.verifyContractCancelStageOne(transactionData);
		 scenarios.openNotes();
		 scenarios.verifyTransactionDetails(data);
		 scenarios.verifyContractCancelStageTwo(transactionData);
		 	
    	}


 @Test(dependsOnMethods = {"contractCancel"}, dataProvider = "SmokeTestForScheduledMeetingForWeb", dataProviderClass = AgentTestDataProvider.class,priority=7)
  public void scheduledMeeting(Map<String, Object> data) {
		 
		 Map<String, Object> transactionData = (Map<String, Object>) data.get("transactionData");
		 
		 
		 scenarios.verifyContractCancelStageOne(transactionData);
		 scenarios.openNotes();
		 scenarios.verifyTransactionDetails(data);
		 scenarios.verifyContractCancelStageTwo(transactionData);

  		}
  
  @Test(dependsOnMethods = {"scheduledMeeting"}, dataProvider = "SmokeTestForScheduledCallbackForWeb", dataProviderClass = AgentTestDataProvider.class,priority=8)
  public void scheduledCallback(Map<String, Object> data) {
		 
		 Map<String, Object> transactionData = (Map<String, Object>) data.get("transactionData");
		 
		 
		 scenarios.verifyContractCancelStageOne(transactionData);
		 scenarios.openNotes();
		 scenarios.verifyTransactionDetails(data);
		 scenarios.verifyContractCancelStageTwo(transactionData);

  }

//Puneet dated :- 12/may/2016
// Verifying return to movoto option, it doent make call to API because once we use return to movoto for a particular client, it is removed from server.
//@Test(dependsOnMethods = "loginTest", dataProvider = "SmokeTestDataForReturnToMovoto", dataProviderClass = AgentTestDataProvider.class,priority=9)
public void returnToMovoto(Map<String, Object> data) {
		 
		 Map<String, Object> returnToMovotoData = (Map<String, Object>) data.get("returnToMovotoData");
		 Map<String, Object> apiData = (Map<String, Object>) data.get("apiData");
		 
		 
		 scenarios.verifyContractCancelStageOne(data);
		 //scenarios.openNotes();
		 //scenarios.verifyTransactionDetails(data);
		 //scenarios.verifyContractCancelStageTwo(data);

}





}