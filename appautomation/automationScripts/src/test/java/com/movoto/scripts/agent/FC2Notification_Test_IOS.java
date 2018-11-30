package com.movoto.scripts.agent;

import java.util.Map;

import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.AgentTestDataProvider;

import junit.framework.Assert;

public class FC2Notification_Test_IOS extends BaseTest {
 
	@Test(dataProvider = "FCNotificationTestDataIOS", dataProviderClass = AgentTestDataProvider.class, priority = 1)
	public void loginTest(Map<String, String> data) {
		if (data != null) {
			scenarios.loginToAgentAppWithUsernameAndPassword(data.get("Username"), data.get("Password"));
			scenarios.verifyLoginSuccess();
			//scenarios.goToNotificationPage();		
		}
	}	
	
	 @Test(dataProvider = "Notification", dataProviderClass = AgentTestDataProvider.class,priority = 2)
	 public void FC2notificationVerification(Map<String, Object> data) {
	   scenarios.verifyforFC2notification(data);  		
		}

}
