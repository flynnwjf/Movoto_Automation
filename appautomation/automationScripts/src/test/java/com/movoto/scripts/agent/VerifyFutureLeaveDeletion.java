package com.movoto.scripts.agent;

import java.util.Map;

import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.AgentTestDataProvider;

public class VerifyFutureLeaveDeletion extends BaseTest{
 
	 @Test(dataProvider = "LoginTestData", dataProviderClass = AgentTestDataProvider.class, priority = 1)
		public void loginTest(Map<String, String> data) {
			if (data != null) {
				scenarios.loginToAgentAppWithUsernameAndPassword(data.get("Username"), data.get("Password"));
				scenarios.verifyLoginSuccess();
			}
		}
	
	//AgApp-1747,1745
	@Test(dependsOnMethods="loginTest", priority=2)
	public void verifyDeleteFutureLeave() {
		scenarios.verifydeletionOfExistingLeave();
	}
	
	//AgApp-1748
	@Test(dependsOnMethods="verifyDeleteFutureLeave", priority=3)
	public void verifyLeaveCreationPge(){
		scenarios.navigateToFutureLeavePage();
		library.click("FLEAVE.createbutton");
		library.wait(2);
		library.verifyPageContainsElement("FLEAVE.schedulebutton");
	}
	
}
