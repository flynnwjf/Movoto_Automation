package com.movoto.scripts.agent;

import java.util.Map;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.AgentTestDataProvider;

public class ClientManagerHeaderTest extends BaseTest {

	@Test(dataProvider = "LoginTestData", dataProviderClass = AgentTestDataProvider.class)
	public void loginTest(Map<String, String> data) {
		if (data != null) {
			scenarios.loginToAgentAppWithUsernameAndPassword(data.get("Username"), data.get("Password"));
			scenarios.verifyLoginSuccess();
		}
	}

	// AgApp5-441,616
	@Test(dependsOnMethods = "loginTest", dataProviderClass = AgentTestDataProvider.class, dataProvider = "SearchAndVerifyClient")
	public void verifyPrevAndNextButtonFunctionality(Map<String, String> data) {
		scenarios.verifyPreviousAndNextButton(data);
		Reporter.log("616 is pass", true);
	}

	// AgApp5-1900
	@Test(dependsOnMethods = "verifyPrevAndNextButtonFunctionality", dataProvider = "SearchAndVerifyClient", dataProviderClass = AgentTestDataProvider.class)
	public void verifyUrgencyOnContactDetailPage(Map<String, String> data) {
		scenarios.verifyContactDetailsPage(data);
		Reporter.log("1900 is pass", true);
	}

	// AgApp5-529,1895,1896,31
//	@Test(dependsOnMethods = "verifyUrgencyOnContactDetailPage")
	public void verifyHamburgerMenu() {
		scenarios.openMenu();
		library.verifyPageContainsElement("MENU.goonleavebutton");
		scenarios.closeMenu();
		library.verifyPageNotContainsElement("MENU.goonleavebutton");
	}

	// AgApp5-442,440,306,28,509
//	@Test(dataProvider = "SearchAndVerifyClient", dataProviderClass = AgentTestDataProvider.class, dependsOnMethods = "verifyHamburgerMenu")
	public void verifyClientSearchPage(Map<String, String> data) {
		String clientName = data.get("clientFullName");
		scenarios.tapSearchIconOnHeaderAndVerify();
		library.verifyPageContainsElement("name->" + clientName);

	}

	// AgApp5-307,309,312
//	@Test(dependsOnMethods = "verifyClientSearchPage")
	public void verifySearchHeaderOnNotificationPage() {
		scenarios.navigateToNotificationsListPage();
		scenarios.tapSearchIconOnHeaderAndVerify();
		library.verifyPageContainsElement("NOTIFICATIONPAGE.notification");

	}

}
