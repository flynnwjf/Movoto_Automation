package com.movoto.scripts;

import org.testng.Assert;

import com.movoto.fixtures.FixtureLibrary;

public class SendFeedBackScenarios {
	
	private CommonScenarios scenarios;
	private FixtureLibrary library;

	public SendFeedBackScenarios(CommonScenarios scenarios, FixtureLibrary library) {
		this.scenarios = scenarios;
		this.library = library;
	}

	public void openSendFeedBackMessageCenter() {
		scenarios.openMenu();
		library.click("MENU.sendfeedbackbutton");
		String messageCenter = "name->Message Center";
		library.verifyPageContainsElement(messageCenter);
	}
	
	public void openSendFeedBackPopUp() {
		scenarios.openMenu();
		library.click("MENU.sendfeedbackbutton");
		library.verifyPageContainsElement("SENDFB.cancel");
	}

	public void verifyEmailInSendFeedBackWith(String email) {
		String loc = "name->"+email;
		library.verifyPageContainsElement(loc);
		library.clear("SENDFB.email");
		library.typeDataInto(email, "SENDFB.email");
	}
	
	public void closeSendFeedBackPopUp(){
		Assert.assertTrue(library.verifyPageContainsElement("SENDFB.cancel"), "Send feedback popup not found!");
		library.click("SENDFB.cancel");
		library.verifyPageNotContainsElement("SENDFB.cancel");
	}
	
	public void sendFeedBack(String email, String message){
		library.clear("SENDFB.email");
		library.typeDataInto(email, "SENDFB.email");
		
		library.clear("SENDFB.message");
		library.typeDataInto(email, "SENDFB.message");
		
		Assert.assertTrue(library.isElementEnabled("SENDFB.send", true), "Send button not enabled!");
		library.click("SENDFB.send");
	}
	
	public void verifyErrorMessageExists(String error){
		String errorLoc = "name->"+error;
		String ok = "name->OK";
		
		library.verifyPageContainsElement(errorLoc);
		library.click(ok);
	}
	
	public void verifySuccessAndClose(){
		String successLoc = "name->Thanks!";
		String closeLoc = "name->Close";
		
		library.verifyPageContainsElement(successLoc);
		library.click(closeLoc);
		library.verifyPageContainsElement(closeLoc);
	}
	
	public void verifySuccessAndOpenMessageCenter(){
		String successLoc = "name->Thanks!";
		String mcLoc = "name->View Messages";
		String messageCenter = "name->Message Center";
		
		library.verifyPageContainsElement(successLoc);
		library.click(mcLoc);
		library.verifyPageContainsElement(messageCenter);
	}
}
