package com.movoto.scripts.agent;

import java.util.Map;

import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.AgentTestDataProvider;
import com.movoto.scripts.data.SalesForceDataProvider;

import bsh.ParseException;
import junit.framework.Assert;

public class N3Notification_Test_IOS extends BaseTest {
 
	@Test(dataProvider = "NNotificationTestDataIOS", dataProviderClass = AgentTestDataProvider.class, priority = 1)
	public void loginTest(Map<String, String> data) {
		if (data != null) {
			scenarios.loginToAgentAppWithUsernameAndPassword(data.get("Username"), data.get("Password"));
			scenarios.verifyLoginSuccess();
			//scenarios.goToNotificationPage();		
		}
	}	
	
	@Test(dataProvider = "Notification", dataProviderClass = AgentTestDataProvider.class, priority= 2)
	public void N3notificationVerification(Map<String, Object> data) {
			scenarios.verifyforN3notification(data);		
		}	
	
	@Test(dataProvider = "Salesforcenotification", dataProviderClass = SalesForceDataProvider.class, priority = 3)
	public void getsalesforceTime(Map<String, Object> data) throws ParseException, java.text.ParseException {
			scenarios.timeinapp(data);	
			
		}
}
