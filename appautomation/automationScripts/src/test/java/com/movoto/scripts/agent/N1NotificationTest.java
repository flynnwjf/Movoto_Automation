package com.movoto.scripts.agent;

import java.util.Map;

import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.AgentTestDataProvider;

import junit.framework.Assert;

public class N1NotificationTest extends BaseTest {
 
	@Test(dataProvider = "NNotificationTestData", dataProviderClass = AgentTestDataProvider.class, priority = 1)
	public void loginTest(Map<String, String> data) {
		if (data != null) {
			scenarios.loginToAgentAppWithUsernameAndPassword(data.get("Username"), data.get("Password"));
			scenarios.verifyLoginSuccess();
			scenarios.goToNotificationPage();		
		}
	}	
	
	@Test(dataProvider = "Notification", dataProviderClass = AgentTestDataProvider.class,priority = 2)
	public void N1notificationVerification(Map<String, Object> data) {
			scenarios.verifyforN1notification(data);		
		}

}