package com.movoto.scripts.agent;

import org.testng.annotations.Test;
import org.testng.Assert;
import com.movoto.scripts.BaseTest;

public class AgentAppTest extends BaseTest {

	@Test
	public void testLogin() {
		loginWithEmailAndPassword("fwang+33@parallel6.com", "111111");	
		Assert.assertTrue(library.verifyPageContainsElement("Dashboard.patienticon"));
	}
	
	private void loginWithEmailAndPassword(String email, String password){
		if(library.findElement("LOGIN.emailtext").isDisplayed()){
			library.typeDataInto(email, "LOGIN.emailfield");
			library.hideKeyboard();
			library.click("LOGIN.nextbutton");
			library.typeDataInto(password, "LOGIN.passwordfield");
		}
		else{
			library.typeDataInto(password, "LOGIN.passwordfield");
		}
		library.wait(10);
	}

}