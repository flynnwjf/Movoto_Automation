package com.movoto.scripts.agent;

import java.text.ParseException;
import java.util.Map;

import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.AgentTestDataProvider;

public class ClientDetailsTestingWithApiResponse extends BaseTest {

	// CommonScenariosSF sfScenarios = new CommonScenariosSF(sfLibrary);

	@Test(dataProvider = "LoginTestData", dataProviderClass = AgentTestDataProvider.class)
	public void loginTest(Map<String, String> data) {
		if (data != null) {
			scenarios.loginToAgentAppWithUsernameAndPassword(data.get("Username"), data.get("Password"));
			scenarios.verifyLoginSuccess();
		}
	}
	
	// Verify lead details of talked stage
	@Test(dependsOnMethods = "loginTest", dataProvider = "ClientDetailsAndApi", dataProviderClass = AgentTestDataProvider.class)
	public void leadDetailsTesting(Map<String, Object> data) {

		scenarios.goToClientDetailsPage(data);
		// verification
		scenarios.verifyClientDetailsWithApi(data);
	}

	// Verify multiple opportunities of Offered
	@Test(dependsOnMethods = "loginTest", dataProvider = "ClientDetailsAndApi", dataProviderClass = AgentTestDataProvider.class)
	public void leadOpportunitiesOfOffered(Map<String, Object> data) {

		scenarios.goToTransactionDetails(data);
		// 	verifycation
		scenarios.verifyOpportunitiesWithApi(data);
		library.wait(3);
	}

	// Verify opportunities of met stage
	@Test(dependsOnMethods = "loginTest", dataProvider = "ClientDetailsAndApi", dataProviderClass = AgentTestDataProvider.class)
	public void leadOpportunitiesOfMetStage(Map<String, Object> data) {

		scenarios.searchAndSelectClient((String) data.get("clientName3"));
		// verifycation
		scenarios.verifyOpportunitiesOfMetStage(data);
		library.wait(3);
	}

	// Verify notes of talked stage
	@Test(dependsOnMethods = "loginTest", dataProvider = "ClientDetailsAndApi", dataProviderClass = AgentTestDataProvider.class)
	public void notesOftalkedStage(Map<String, Object> data) {
		scenarios.searchAndSelectClient((String) data.get("clientName"));
		scenarios.navigateToNotes();
		scenarios.setTokenAndUserId(data);
		scenarios.verifyNotesForTalkedStage(data);
	}

	// verify Properties of talked Stage
	@Test(dependsOnMethods = "loginTest", dataProvider = "ClientDetailsAndApi", dataProviderClass = AgentTestDataProvider.class)
	public void propertiesOfTalkedStage(Map<String, Object> data) throws ParseException {
		scenarios.searchAndSelectClient((String) data.get("clientName"));
		scenarios.verifyPropertiesOftalkedStage(data);
	}
}
