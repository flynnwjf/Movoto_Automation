package com.movoto.scripts;

import java.lang.reflect.Method;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import com.movoto.context.ConfigurationManager;
import com.movoto.fixtures.FixtureLibrary;

public class BaseTest {

	protected FixtureLibrary library;
	protected CommonScenarios scenarios;
	protected ITestContext thiscontext;

	@BeforeTest
	public void setup(ITestContext context) {
		this.thiscontext = context;
		Reporter.log("Context: " + context.getName(), true);
		library = new ConfigurationManager().createContext(context.getName());
		context.setAttribute("LIBRARY", library);		
	}

	@BeforeMethod
	public void beforeMehtod(Method method) {
		library.setCurrentTestMethod(method.getName());
		Reporter.log("Method: " + method.getName(), true);
		scenarios = CommonScenariosManager.getCommonScenarios(thiscontext, library);
	}
	
	@AfterMethod
	public void getScreenshot(){

	}

	@AfterTest
	public void cleanUp() {		
		if (library != null) {
			library.quit();
		}
	}
	
}
