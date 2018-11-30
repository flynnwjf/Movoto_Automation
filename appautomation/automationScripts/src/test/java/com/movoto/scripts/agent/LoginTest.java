package com.movoto.scripts.agent;

import org.testng.annotations.Test;

import com.movoto.fixtures.FixtureLibrary;
import com.movoto.scripts.BaseTest;
import com.movoto.scripts.CommonScenarios;
import com.movoto.scripts.data.AgentTestDataProvider;

import java.util.Map;

public class LoginTest extends BaseTest {
	
	@Test(dataProvider = "VerifyLoginTestData", dataProviderClass = AgentTestDataProvider.class, priority = 1)
	public void loginTest(Map<String, String> data) {
		if (data != null) {

			scenarios.loginWithUsernameAndPasswordAndExpectError(data.get("userName"), data.get("password"),
					data.get("alertMessage"));
		}
	}

}
