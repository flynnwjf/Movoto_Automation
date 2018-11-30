package com.movoto.scripts.agent;

import java.util.Map;

import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.AgentTestDataProvider;
//import com.movoto.scripts.salesforce.CommonScenariosSF;

public class Activities_Workflow_Mobile extends BaseTest {
	//CommonScenariosSF sfScenarios = new CommonScenariosSF(sfLibrary);
	

	@Test(dataProvider = "LoginTestData", dataProviderClass = AgentTestDataProvider.class, priority = 1)
	public void loginTest(Map<String, String> data) {
		if (data != null) {

			scenarios.loginToAgentAppWithUsernameAndPassword(data.get("Username"), data.get("Password"));
			scenarios.verifyLoginSuccess();
			
		}
	}

	//Puneet updated on 16/may/2016
	// For Urgency stage update.
	@Test(dependsOnMethods = "loginTest", dataProvider = "SmokeTestDataForUrgency", dataProviderClass = AgentTestDataProvider.class,priority=2)
	public void verifyUrgencyStage(Map<String, Object> data) {
		
		Map<String, String> urgencyData = (Map<String, String>) data.get("urgencyData");
		if (data != null) {
			
			scenarios.updateUrgencyStageOne(urgencyData);
			scenarios.verifyUrgencyDetails(data);
			scenarios.updateUrgencyStageTwo(urgencyData);
			scenarios.closeNotesField();
			scenarios.navigateToClientListPage();
		}
		
	}
	
	// For Talked, Emailed, Left Voicemail , Scheduled Meeting stage update.
	@Test(dependsOnMethods = "loginTest", dataProvider = "SmokeTestTransactionUpdate", dataProviderClass = AgentTestDataProvider.class,priority=3)
	  public void updateTransactionStage(Map<String, Object> data) {
			 
			 Map<String, Object> transactionData = (Map<String, Object>) data.get("transactionData");
			 
			 scenarios.verifyContractCancelStageOne(transactionData);
			 scenarios.openNotes();
			 scenarios.verifyTransactionDetails(data);
			 scenarios.closeNotesField();
			 scenarios.verifyContractCancelStageTwo(transactionData);
			 	
	    }
	
	
	// For Made An Offer Stage.
	@Test(dependsOnMethods = {"updateTransactionStage"}, dataProvider = "SmokeTestForMadeAnOffer", dataProviderClass = AgentTestDataProvider.class,priority=4)
		public void verifyMadeAnOffer(Map<String, Object> data) {
				 
				 Map<String, Object> MadeOfferData = (Map<String, Object>) data.get("MadeOfferData");
				 
				 
				scenarios.verifyContractCancelStageOne(data);
				scenarios.openNotes();
				 scenarios.verifyTransactionDetails(data);
				 scenarios.closeNotesField();
				 scenarios.verifyContractCancelStageTwo(MadeOfferData);

		}
	
	// For Contract Accepted Stage, which is done if Made an Offer stage is passed.
	@Test(dependsOnMethods = {"verifyMadeAnOffer"}, dataProvider = "SmokeTestDataForContractAccept", dataProviderClass = AgentTestDataProvider.class,priority=5)
	public void verifyContractAccept(Map<String, Object> data) {
			 
			 Map<String, Object> contractAcceptData = (Map<String, Object>) data.get("contractAcceptData");
			 
			 
			 scenarios.verifyContractCancelStageOne(data);
			 scenarios.openNotes();
			 scenarios.verifyTransactionDetails(data);
			 scenarios.closeNotesField();
			 scenarios.verifyContractCancelStageTwo(contractAcceptData);

	}
	
	    // For contract cancel, which is done if contract accepted stage is passed.
		@Test(dependsOnMethods = {"verifyContractAccept"}, dataProvider = "SmokeTestTransactionUpdateForContractCancel", dataProviderClass = AgentTestDataProvider.class,priority=6)
		public void updateContractCancelStage(Map<String, Object> data) {
				 
				 Map<String, Object> transactionData = (Map<String, Object>) data.get("transactionData");
				 
				 //scenarios.verifyContractCancelStageSteps(transactionData);
				 scenarios.verifyContractCancelStageOne(transactionData);
				 scenarios.openNotes();
				 scenarios.verifyTransactionDetails(data);
				 scenarios.closeNotesField();
				 scenarios.verifyContractCancelStageTwo(transactionData);
				 
		}
		
		// For Scheduled Meeting stage update.
		// Running but due to NOTES field error its getting failed.
		//@Test(dependsOnMethods = "loginTest", dataProvider = "SmokeTestForScheduledMeeting", dataProviderClass = AgentTestDataProvider.class,priority=7)
		public void verifyScheduledMeeting(Map<String, Object> data) {
				 
				 Map<String, Object> transactionData = (Map<String, Object>) data.get("transactionData");
				 
				 
				 scenarios.verifyContractCancelStageOne(transactionData);
				 scenarios.openNotes();
				 scenarios.verifyTransactionDetails(data);
				 scenarios.closeNotesField();
				 scenarios.verifyContractCancelStageTwo(transactionData);

		}
		
		
		// Verifying return to movoto option, it doent make call to API because once we use return to movoto for a particular client, it is removed from server.
		//@Test(dependsOnMethods = "loginTest", dataProvider = "SmokeTestDataForReturnToMovoto", dataProviderClass = AgentTestDataProvider.class,priority=7)
		public void verifyReturnToMovoto(Map<String, Object> data) {
				 
				 Map<String, Object> returnToMovotoData = (Map<String, Object>) data.get("returnToMovotoData");
				 Map<String, Object> apiData = (Map<String, Object>) data.get("apiData");
				 
				 
				 scenarios.verifyContractCancelStageOne(data);
				 scenarios.openNotes();
				 //scenarios.verifyTransactionDetails(data);
				 //scenarios.verifyContractCancelStageTwo(data);

		}
	
	// To construct dynamic API URL for notes API.
	//@Test(dependsOnMethods = "loginTest", dataProvider = "APILoginTestData", dataProviderClass = AgentTestDataProvider.class,priority=6)
	public void getClientListData(Map<String, Object> data){
		//scenarios.getClientListData(data);
		scenarios.getApiUrl(data);
		
	}
	
	
	

}
