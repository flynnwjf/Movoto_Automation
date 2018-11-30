package com.movoto.scripts.agent;

import java.util.Map;

import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.SendFeedBackScenarios;
import com.movoto.scripts.data.AgentTestDataProvider;

public class SendFeedbackTest extends BaseTest {

	private SendFeedBackScenarios sfScenarios;

	
	@Test//Map<String, String> data(dataProvider = "LoginTestData", dataProviderClass = AgentTestDataProvider.class, priority = 1)
	public void loginTest() {
//		if (data != null) {
			scenarios.loginToAgentAppWithUsernameAndPassword("agentautomationtest1@movoto.com", "passw0rd");
			scenarios.verifyLoginSuccess();
			sfScenarios  = new SendFeedBackScenarios(scenarios, library);
//		}
	}

	@Test(dependsOnMethods = { "loginTest" })
	public void sendFeedBackTest() {
		sfScenarios.openSendFeedBackPopUp();
		sfScenarios.verifyEmailInSendFeedBackWith("agentautomationtest1@movoto.com");
		sfScenarios.sendFeedBack("agent", "isus");
		sfScenarios.verifyErrorMessageExists("Invalid Email Address");
		sfScenarios.sendFeedBack("agentautomationtest1@movoto.com", "Agent Automation");
		sfScenarios.verifySuccessAndClose();
		sfScenarios.openSendFeedBackMessageCenter();
	}
}
