package com.movoto.scripts.agent;

import java.util.Map;

import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.AgentTestDataProvider;

import junit.framework.Assert;

public class FCNotificationTest extends BaseTest {
 
	@Test(dataProvider = "FCNotificationTestData", dataProviderClass = AgentTestDataProvider.class, priority = 1)
	public void loginTest(Map<String, String> data) {
		if (data != null) {
			scenarios.loginToAgentAppWithUsernameAndPassword(data.get("Username"), data.get("Password"));
			scenarios.verifyLoginSuccess();
			scenarios.goToNotificationPage();		
		}
	}	
	
	@Test(priority = 2)
	public void FC1notificationVerification() {
			scenarios.verifyFC1notification();		
		}
	
	@Test(priority = 3)
	public void FC2notificationVerification() {
			scenarios.verifyFC2notification();			
		}
	
	@Test(priority = 4)
	public void FC3notificationVerification() {
			scenarios.verifyFC3notification();			
		}
	
}
