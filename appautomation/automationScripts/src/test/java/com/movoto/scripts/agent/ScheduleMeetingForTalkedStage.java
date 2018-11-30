package com.movoto.scripts.agent;

import java.util.Map;

import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.AgentTestDataProvider;

public class ScheduleMeetingForTalkedStage extends BaseTest {
	@Test(dataProvider = "LoginTestData", dataProviderClass = AgentTestDataProvider.class)
	public void loginTest(Map<String, String> data) {
		if (data != null) {
			scenarios.loginToAgentAppWithUsernameAndPassword(data.get("Username"), data.get("Password"));
			scenarios.verifyLoginSuccess();
		}
	}

	// Verify schedule a meeting for talked stage
	@Test(dependsOnMethods = "loginTest", dataProviderClass = AgentTestDataProvider.class, dataProvider = "DataForScheduleMeeting")
	public void scheduleMeetingforTalkedStage(Map<String, Object> data) {
		scenarios.searchAndSelectClient(data.get("clientName").toString());
		scenarios.verifyMeetingTimeAndNotes(data);	
	}
}
