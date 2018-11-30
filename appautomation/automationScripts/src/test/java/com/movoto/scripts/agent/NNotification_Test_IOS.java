package com.movoto.scripts.agent;

import java.util.Map;

import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.AgentTestDataProvider;

import junit.framework.Assert;

public class NNotification_Test_IOS extends BaseTest {
 
	@Test(dataProvider = "NNotificationTestDataIOS", dataProviderClass = AgentTestDataProvider.class, priority = 1)
	public void loginTest(Map<String, String> data) {
		if (data != null) {
			scenarios.loginToAgentAppWithUsernameAndPassword(data.get("Username"), data.get("Password"));
			scenarios.verifyLoginSuccess();
			scenarios.goToNotificationPage();		
		}
	}	
	
	@Test(priority = 2)
	public void N1notificationVerification() {
			scenarios.verifyN1notification();		
		}
	
	@Test(priority = 3)
	public void N2notificationVerification() {
			scenarios.verifyN2notification();			
		}
	
	@Test(priority = 4)
	public void N3notificationVerification() {
			scenarios.verifyN3notification();			
		}
	
}