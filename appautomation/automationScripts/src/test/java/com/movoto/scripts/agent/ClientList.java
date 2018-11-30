package com.movoto.scripts.agent;

import java.util.Map;
import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.AgentTestDataProvider;

public class ClientList extends BaseTest {

	// com.movoto.scripts.salesforce.CommonScenariosSF sfscenarios = new
	// com.movoto.scripts.salesforce.CommonScenariosSF(library);

	@Test(dataProvider = "LoginTestData6", dataProviderClass = AgentTestDataProvider.class)
	public void loginTest(Map<String, String> data) {
		if (data != null) {

			scenarios.loginToAgentAppWithUsernameAndPassword(data.get("Username"), data.get("Password"));
			scenarios.verifyLoginSuccess();
			library.wait(10);

		}
	}

	@Test(dataProvider = "APILoginTest", dataProviderClass = AgentTestDataProvider.class, dependsOnMethods = {
			"loginTest" }, priority = 2)
	public void LeadsNameTest(Map<String, Object> data) {
		scenarios.verifyLeadsName(data);
	}

	@Test(dataProvider = "APILoginTest", dataProviderClass = AgentTestDataProvider.class, dependsOnMethods = {
			"loginTest" }, priority = 3)
	public void StageNameTest(Map<String, Object> data) {
		scenarios.verifyClientListExistwithLeads(data);
		scenarios.verifyStageName(data);
	}

	@Test(dataProvider = "APILoginTest", dataProviderClass = AgentTestDataProvider.class, dependsOnMethods = {
			"loginTest" }, priority = 4)
	public void StageLastUpdatedDateTest(Map<String, Object> data) {
		scenarios.verifyClientListExistwithLeads(data);
		//scenarios.verifyStageLastUpdatedDate(data);
	}

	@Test(dataProvider = "APILoginTest", dataProviderClass = AgentTestDataProvider.class, dependsOnMethods = {
			"loginTest" }, priority = 5)
	public void EmailFieldNameTest(Map<String, Object> data) {
		scenarios.verifyClientListExistwithLeads(data);
		scenarios.verifyEmailFieldName(data);
	}

	@Test(dataProvider = "APILoginTest", dataProviderClass = AgentTestDataProvider.class, dependsOnMethods = {
			"loginTest" }, priority = 6)
	public void PrimaryPhoneFieldTest(Map<String, Object> data) {
		scenarios.verifyClientListExistwithLeads(data);
		scenarios.verifyPrimaryPhoneField(data);
	}

	@Test(dataProvider = "APILoginTest", dataProviderClass = AgentTestDataProvider.class, dependsOnMethods = {
			"loginTest" }, priority = 8)
	public void LastVisitedDateTest(Map<String, Object> data) {
		scenarios.verifyClientListExistwithLeads(data);
		scenarios.verifyLastVisitedDate(data);
	}

//	@Test(dataProvider = "APIGetContents", dataProviderClass = AgentTestDataProvider.class, dependsOnMethods = {
//			"loginTest" }, priority = 9)
//	public void SelectSortorderTest(Map<String, Object> data) {
//		//scenarios.verifyClientListExistwithLeads(data);
//		scenarios.verfyselectSortorder(data);
//	}

	@Test(dataProvider = "APILoginTest", dataProviderClass = AgentTestDataProvider.class, dependsOnMethods = {
			"loginTest" }, priority = 7)
	public void ClientFilterTest(Map<String, Object> data) {
		scenarios.verifyClientFilter(data);
	}

    @Test(dataProvider = "APILoginTest", dataProviderClass = AgentTestDataProvider.class, dependsOnMethods = {
	"loginTest" }, priority = 10)
    public void SendEmailTest(Map<String, Object> data) {
   scenarios.verifySendEmail(data);
   }
   
}
