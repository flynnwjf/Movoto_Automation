package com.movoto.scripts;

import org.testng.ITestContext;

import com.movoto.fixtures.FixtureLibrary;

public class CommonScenariosManager {

	public static CommonScenarios getCommonScenarios(ITestContext context, FixtureLibrary library) {
		
		CommonScenarios scenarios = null;
		String name = library.getCurrentPlatform();
		String type = library.getCurrentPlatformType();
		if (name.equalsIgnoreCase("IOS")) {
			scenarios = new CommonScenariosIOS(library);
		} else if (name.equalsIgnoreCase("WEB") || (type.contains("_WEB")) )  {
			scenarios = new CommonScenariosWeb(library);
		} else {
			scenarios = new CommonScenarios(library);
		}
		return scenarios;

	}

}