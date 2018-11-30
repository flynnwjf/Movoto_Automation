package com.movoto.scripts;

import com.movoto.fixtures.FixtureLibrary;

public class ForgotPasswordScenarios {
	private CommonScenarios scenarios;
	private FixtureLibrary library;

	public ForgotPasswordScenarios(CommonScenarios scenarios, FixtureLibrary library) {
		this.scenarios = scenarios;
		this.library = library;
	}
	
	public void openForgotPasswordPage(){
		library.click("name->Forgot Password?");
		library.verifyPageContainsElement("name->Send");
	}
	
	public void enterEmailAndSend(String email){
		library.clear("FPWD.email");
		library.typeDataInto(email+"\n", "FPWD.email");
		library.click("name->Send");
	}
	
	public void verifySuccess(){
		String successMessage = "Email sent. Please check your email for your password.";
		String okButton = "name->Back to Login";
		library.verifyPageContainsElement("name->"+successMessage);
		library.click(okButton);
	}
	
	public void verifyErrorMessageExists(String error){
		String errorLoc = "name->"+error;
		String okButton = "name->OK";
		library.verifyPageContainsElement(errorLoc);
		library.click(okButton);
	}
}
