package com.movoto.scripts.agent;

import java.util.Map;

import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.AgentTestDataProvider;

public class VerifyFutureLeaveCreationWithValidDate extends BaseTest{
	@Test(dataProvider = "LoginTestData", dataProviderClass = AgentTestDataProvider.class, priority = 1)
	public void loginTest(Map<String, String> data) {
		if (data != null) {
			scenarios.loginToAgentAppWithUsernameAndPassword(data.get("Username"), data.get("Password"));
			scenarios.verifyLoginSuccess();
		}
	}
	
	//AgApp-2894
		@Test(dataProvider="FutureLeaveTestData",dataProviderClass = AgentTestDataProvider.class, dependsOnMethods="loginTest", priority=2)
		public void verifyFutureLeaveCreationWithDate(Map<String, String> data){
			scenarios.navigateToFutureLeavePage();
			boolean isLeavePresent = library.verifyPageContainsElement("LEAVEPAGE.deletebuttonimage");
			
			if(isLeavePresent == false){
				library.click("FLEAVE.schedulebutton");
			}
			
			boolean leaveExists = library.verifyPageContainsElement("FLEAVE.cancelleavebutton");
			if (leaveExists) {
				library.click("FLEAVE.cancelleavebutton");
				library.click("FLEAVE.deleteleavebutton");
				library.wait(5);
				library.click("FLEAVE.createbutton");
			}

			String startDate = data.get("fromDate");
			String endDate = data.get("toDate");

			scenarios.chooseStartAndEndLeaveDates(startDate, endDate);
			library.click("FLEAVE.schedulebutton");
			leaveExists = library.verifyPageContainsElement("FLEAVE.cancelleavebutton");
			
		}
}
