package com.movoto.scripts.agent;

import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.ForgotPasswordScenarios;

public class ForgotPasswordTest extends BaseTest {
  
	private ForgotPasswordScenarios fpScenarios;
	
	@Test
	public void forgotPasswordTest() {
		fpScenarios = new ForgotPasswordScenarios(scenarios, library);
		fpScenarios.openForgotPasswordPage();
		fpScenarios.enterEmailAndSend("abc@aooa.com");
		fpScenarios.verifyErrorMessageExists("User email does not exist.");
		fpScenarios.enterEmailAndSend("agentautomationtest3@movoto.com");
		fpScenarios.verifySuccess();
	}
}
