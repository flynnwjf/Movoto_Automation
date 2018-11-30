package com.movoto.scripts.buyer;

import org.testng.annotations.Test;
import org.testng.ITestContext;
import com.movoto.context.ConfigurationManager;
import com.movoto.fixtures.FixtureLibrary;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

public class MovotoTest {

	private FixtureLibrary fixtures;

	
	@BeforeTest
	public void setup(ITestContext context) {
		Reporter.log("Platform: "+context.getName(), true);
		fixtures = new ConfigurationManager().createContext(context.getName());
	}

	@Test
	public void movotoTest() {
		searchForLocation("Scottsdale");
		Assert.assertTrue(verifyResultsExist(), "Search results not found");
	}

	
	@AfterTest
	public void tearDown() {
		closeApplication();
	}
	
	
	private void searchForLocation(String location) {
			fixtures.clear("HOMEPAGE.searchfield");
			fixtures.typeDataInto(location, "HOMEPAGE.searchfield");
			fixtures.click("HOMEPAGE.searchbutton");
			Reporter.log("Searched for location: "+location, true);
	}
	
	private boolean verifyResultsExist(){
		Reporter.log("Verify Search Results Exist", true);
		return fixtures.verifyPageContainsElement("RESULTS.propertylist");
	}
	
	private void closeApplication(){
		Reporter.log("Quit Application", true);
		if (fixtures!=null) {
			fixtures.quit();
		}
		
	}
}
