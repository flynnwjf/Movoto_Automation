package com.movoto.scripts.agent;

import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.AgentTestDataProvider;
import com.movoto.scripts.data.TransactionDataProvider;

import java.util.Map;

public class ContractCancel extends BaseTest {

	@Test(dataProvider = "LoginTestData", dataProviderClass = AgentTestDataProvider.class, priority = 1)
	public void loginTest(Map<String, String> data) {
		if (data != null) {

			scenarios.loginToAgentAppWithUsernameAndPassword(data.get("Username"), data.get("Password"));
			scenarios.verifyLoginSuccess();
		}
	}

	@Test(dataProvider = "ContractCancelledStageTestData", dataProviderClass = TransactionDataProvider.class, dependsOnMethods = {
			"loginTest" }, priority = 2)
	public void verifyContractCancelStage(Map<String, Object> data) {
		scenarios.verifyContractCancelStageSteps(data);

	}
}