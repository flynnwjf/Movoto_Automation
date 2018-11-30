package com.movoto.scripts.agent;

import java.util.Map;

import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.AgentTestDataProvider;

import junit.framework.Assert;

public class FC1NotificationTest extends BaseTest {
 
	@Test(dataProvider = "FCNotificationTestData", dataProviderClass = AgentTestDataProvider.class, priority = 1)
	public void loginTest(Map<String, String> data) {
		if (data != null) {
			scenarios.loginToAgentAppWithUsernameAndPassword(data.get("Username"), data.get("Password"));
			scenarios.verifyLoginSuccess();
			scenarios.goToNotificationPage();		
		}
	}	
	
	@Test(dataProvider = "Notification", dataProviderClass = AgentTestDataProvider.class,priority = 2)
	public void FC1notificationVerification(Map<String, Object> data) {
			scenarios.verifyforFC1notification(data);		
		}

}

