package com.movoto.scripts.agent;

import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.AgentTestDataProvider;

import org.testng.annotations.BeforeTest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;

public class VerifyFutureLeavePage extends BaseTest {
	
	@Test(dataProvider = "LoginTestData", dataProviderClass = AgentTestDataProvider.class, priority = 1)
	public void loginTest(Map<String, String> data) {
		if (data != null) {
			scenarios.loginToAgentAppWithUsernameAndPassword(data.get("Username"), data.get("Password"));
			scenarios.verifyLoginSuccess();
		}
	}
 
  
  //AgApp-1723
//  @Test(dependsOnMethods="loginTest", priority=2)
  public void verifyFutureLeave() {
	 //library.tapOnElement("id->com.movoto.agentfc:id/greenInfoBtn");
	  //scenarios.setFutureDate(data);
	  library.wait(5);
		scenarios.openMenu();
		library.click("MENU.goonleavebutton");
		library.click("MENU.futureleavebutton");
		library.wait(3);
		
		if(library.verifyPageContainsElement("LEAVESCHEDULEPAGE.scheduleleave"))
			library.click("LEAVESCHEDULEPAGE.scheduleleave");
		
//		library.verifyPageContainsElement("FLEAVE.crossimage", true);
		library.verifyPageContainsElement("FLEAVE.createbutton");
		  
  }
  
  //AgApp-2887
//  @Test(dependsOnMethods="verifyFutureLeave", priority=3)
  public void verifyHamburgerTable(){
	  scenarios.openMenu();
	  library.verifyPageContainsElement("MENU.goonleavebutton");
	  scenarios.closeMenu();
  }
  
  //AgApp-68
  @Test(dependsOnMethods ="verifyHamburgerTable", priority=4)
  public void verifyLeaveSchedulePage(){
	  scenarios.verifydeletionOfExistingLeave();
  }
  
  //AgApp-2888
//  @Test(dependsOnMethods="verifyLeaveSchedulePage", priority=5, dataProvider="SearchAndVerifyClient", dataProviderClass = AgentTestDataProvider.class)
  public void verifyClientSearchPage(Map<String,String> data){
	  if(library.verifyPageContainsElement("SCHEDULELEAVE.cancel"))
		  library.click("SCHEDULELEAVE.cancel");
	  
	  scenarios.searchForClientAndVerify(data);
	 
  }
   
}
