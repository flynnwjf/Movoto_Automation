package com.movoto.scripts.agent;

import java.util.Map;

import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.AgentTestDataProvider;

import junit.framework.Assert;

public class FC05Notification_Test_IOS extends BaseTest {
 
	@Test(dataProvider = "FCNotificationTestDataIOS", dataProviderClass = AgentTestDataProvider.class, priority = 1)
	public void loginTest(Map<String, String> data) {
		if (data != null) {
			scenarios.loginToAgentAppWithUsernameAndPassword(data.get("Username"), data.get("Password"));
			scenarios.verifyLoginSuccess();
			scenarios.goToNotificationPage();		
		}
	}	
	
	@Test(priority = 2)
	public void FC05notificationVerification() {
			scenarios.verifyforFC05notification();		
		}

}
