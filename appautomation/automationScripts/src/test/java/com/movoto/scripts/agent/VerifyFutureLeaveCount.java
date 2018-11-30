package com.movoto.scripts.agent;

import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.AgentTestDataProvider;
import com.movoto.scripts.data.TransactionDataProvider;

import org.testng.annotations.BeforeTest;

import java.util.Map;

import org.testng.Reporter;
import org.testng.annotations.AfterTest;

public class VerifyFutureLeaveCount extends BaseTest {

	@Test(dataProvider = "LoginTestData", dataProviderClass = AgentTestDataProvider.class, priority = 1)
	public void loginTest(Map<String, String> data) {
		if (data != null) {

			scenarios.loginToAgentAppWithUsernameAndPassword(data.get("Username"), data.get("Password"));
			scenarios.verifyLoginSuccess();
		}
	}

	@Test(dependsOnMethods = { "loginTest" }, priority = 2) //2886 and 1719
	public void verifyTapOnCancel() {
		scenarios.navigateToFutureLeavePage();
		scenarios.verifyTapOnCancelSteps();
		
	}

	@Test(dependsOnMethods = { "verifyTapOnCancel" }, priority = 3)  //2892
	public void verifyGestureFromFutureLeavePage() {
		scenarios.navigateToFutureLeavePage();
		scenarios.openMenu();
		library.verifyPageContainsElement("MENU.goonleavebutton");
	}

	@Test(dependsOnMethods = { "verifyGestureFromFutureLeavePage" }, priority = 4) //2889,2890,2891
	public void verifyDeleteButtonIfTapOnX() {
		scenarios.navigateToFutureLeavePage();
		boolean leaveExists = library.verifyPageContainsElement("FLEAVE.cancelleavebutton");
		if (leaveExists) {
			library.click("FLEAVE.cancelleavebutton");
			library.wait(3);
			library.verifyPageContainsElement("FLEAVE.deleteleavebutton");
			library.isElementEnabled("FLEAVE.createbutton", true);
			library.click("FLEAVE.startdatefield");
			library.verifyPageContainsElement("FLEAVE.cancelleavebutton");
		} else {
			Reporter.log("Leave Not Present!!");

		}

	}
}
