package com.movoto.scripts.agent;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.AgentTestDataProvider;

public class LeavesFlow extends BaseTest {

	@Test(dataProvider = "LoginTestData3", dataProviderClass = AgentTestDataProvider.class, priority = 1)
	public void loginTest(Map<String, String> data) {
		if (data != null) {
			scenarios.loginToAgentAppWithUsernameAndPassword(data.get("Username"), data.get("Password"));
			scenarios.verifyLoginSuccess();
			library.wait(5);
		}
	}

	// Go on immediate leave and Add future leave
	@Test(dataProviderClass = AgentTestDataProvider.class, dataProvider = "APIgetLeave", priority = 2)
	public void addFutureLeave(Map<String, Object> data) {

		scenarios.setFutureDateUsingCalander(data);
		library.wait(2);
		int leaveCount = scenarios.goToLeavePageCountLeave();
		scenarios.setTokenAndUserId(data);
		String response = scenarios.getResponse((String) data.get("ContactsUrl"));
		scenarios.verifyLeavesWithApiResponse(response, leaveCount);

	}

	// Delete future leave
	@Test(dataProvider = "APIgetLeave", dataProviderClass = AgentTestDataProvider.class, priority = 3)
	public void deleteFutureLeave(Map<String, Object> data) {
		int getLeaveCountBeforeDeletion = scenarios.goToLeavePageCountLeaves();
		scenarios.deleteLeaves(1);
		library.wait(2);
		int leaveCount = scenarios.goToLeavePageCountLeaves();
		scenarios.setTokenAndUserId(data);
		String response = scenarios.getResponse((String) data.get("ContactsUrl"));
		scenarios.verifyLeavesWithApiResponse(response, leaveCount);
	}


}
