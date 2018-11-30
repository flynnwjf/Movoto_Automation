package com.movoto.scripts.agent;

import java.util.Map;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.AgentTestDataProvider;

public class ClientSearchFromClientListPage extends BaseTest {

	@Test(dataProvider = "LoginTestData", dataProviderClass = AgentTestDataProvider.class, priority = 1)
	public void loginTest(Map<String, String> data) {
		if (data != null) {

			scenarios.loginToAgentAppWithUsernameAndPassword(data.get("Username"), data.get("Password"));
			scenarios.verifyLoginSuccess();
			library.wait(10);
		}
	}

	// AgApp5-653
	@Test(dependsOnMethods = "loginTest", priority = 2)
	public void verifySearchTextAfterTappingOnX() {

		library.click("HOMEPAGE.searchbutton");
		library.typeDataInto("XYZ", "HOMEPAGE.typeintosearch");
		library.clear("HOMEPAGE.typeintosearch");
		String searchText = library.getTextFrom("HOMEPAGE.typeintosearch");
		if (searchText == "Search by Name or Email") {
			Reporter.log("success.");
		}
	}
	
	//AgApp5-316
	@Test(dependsOnMethods = "loginTest", priority = 3)
	public void verifySearchWithSpace(){
		library.typeDataInto("    ", "HOMEPAGE.typeintosearch");
		String searchResultText = library.getTextFrom("SEARCH.result");
		if(searchResultText == "No Results Found"){
			Reporter.log("success.");
		}
		library.click("SEARCHPAGE.cancel");
	}
	
	//AgApp5-30	
	@Test(dependsOnMethods = "loginTest", priority = 4)	
	public void verifySearchPageOnNotificationManagerPage(){
		scenarios.navigateToNotificationsListPage();
		library.wait(3);
		library.click("HOMEPAGE.searchbutton");
		library.verifyPageContainsElement("HOMEPAGE.typeintosearch");
		library.click("SEARCHPAGE.cancel");
			
		}  
	
	//AgApp5-3320,AgApp5-3321
	@Test(dataProvider = "ClientEmailAndPhoneNumberTestData", dataProviderClass = AgentTestDataProvider.class,dependsOnMethods = "loginTest", priority = 5)
	public void verifyClientSearchByEmailAddress(Map<String, String> map){
		
	   scenarios.searchByClientEmailIdAndVerify(map);
	   library.click("SEARCHPAGE.cancel");
	   //scenarios.searchByClientPhoneNumberAndVerify(map);
		
	} 
	
	//AgApp5-3324,AgApp5-3325
	@Test(dataProvider = "ClientEmailAndPhoneNumberTestData", dataProviderClass = AgentTestDataProvider.class,dependsOnMethods = "loginTest", priority = 6)
	public void verifyClientSearchByEmailAddressFromNotificationManagerPage(Map<String, String> map){
		
	   //scenarios.navigateToNotificationsListPage();
	   library.wait(3);
	   scenarios.searchByClientEmailIdAndVerify(map);
	   library.click("SEARCHPAGE.cancel");
	   //scenarios.searchByClientPhoneNumberAndVerify(map);
		
	}
	
	//AgApp5-3322,AgApp5-3323,AgApp5-29
	@Test(dataProvider = "ClientEmailAndPhoneNumberTestData", dataProviderClass = AgentTestDataProvider.class,dependsOnMethods = "loginTest", priority = 7)
	public void verifyClientSearchByEmailAddressFromClientDetailPage(Map<String, String> map){
		
		
		String clientName = map.get("clientName");
		String searchName = clientName.substring(0, 3);
		library.click("HOMEPAGE.searchbutton"); // click on search
		library.verifyPageContainsElement("HOMEPAGE.typeintosearch");
		library.typeDataInto(searchName, "HOMEPAGE.typeintosearch");
		library.wait(3);
		String resultLocator = "name->" + clientName;
		library.click(resultLocator);
		
		
	   scenarios.searchByClientEmailIdAndVerify(map);
	   library.click("SEARCHPAGE.cancel");
	   //scenarios.searchByClientPhoneNumberAndVerify(map);
		
	}
		
}
