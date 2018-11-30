package com.movoto.scripts.data;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

import com.movoto.fixtures.FixtureLibrary;

public class SalesForceDataProvider {

	@DataProvider(name = "UpdateSFActivityTestData")
	public static Object[][] UpdateSFActivityDataProvider(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String testName = context.getName();
		String path = "";
		if (context.getName().equalsIgnoreCase("Urgency_Test_Web")) {
			path = "data/" + testName + ".xlsx";
		} else {
			path = "data/SF_Data.xlsx";
		}
		
		library.openExcelSheet(path, "SalesForce", "read");
		String userName = library.getFromExcelRowAndColumn(1, "UserName");
		String password = library.getFromExcelRowAndColumn(1, "Password");
		String clientEmail = library.getFromExcelRowAndColumn(1, "ClientEmail");
		String activity = library.getFromExcelRowAndColumn(1, "Activity");
		String clientName = library.getFromExcelRowAndColumn(1, "ClientName");
		library.closeExcelSheet(path, "SalesForce", "read");

		Map<String, String> map = new HashMap<>();
		
		map.put("Username", userName);
		map.put("Password", password);
		map.put("ClientEmail", clientEmail);	
		map.put("Activity", activity);
		map.put("ClientName", clientName);

		Object[][] obj = { { map } };

		return obj;
	}
	
	@DataProvider(name = "VerifyActivityInAppTestData")
	public static Object[][] VerifyActivityInAppDataProvider(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String testName = context.getName();
		String path = "";
		if (context.getName().equalsIgnoreCase("Urgency_Test_Web")) {
			path = "data/" + testName + ".xlsx";
		} else {
			path = "data/SF_Data.xlsx";
		}
		library.openExcelSheet(path, "Login", "read");
		String userName = library.getFromExcelRowAndColumn(1, "UserName");
		String password = library.getFromExcelRowAndColumn(1, "Password");
		String clientName = library.getFromExcelRowAndColumn(1, "ClientName");
		library.closeExcelSheet(path, "Login", "read");
		
		Map<String, String> map = new HashMap<>();
		
		map.put("Username", userName);
		map.put("Password", password);
		map.put("ClientName", clientName);
		

		Object[][] obj = { { map } };

		return obj;
	}
	
	@DataProvider(name = "VerifyActivityInSfTestData")
	public static Object[][] UpdateAgentActivityDataProvider(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String testName = context.getName();
		String path = "";
		if (context.getName().equalsIgnoreCase("Urgency_Test_Web")) {
			path = "data/" + testName + ".xlsx";
		} else {
			path = "data/SF_Data.xlsx";
		}
		
		library.openExcelSheet(path, "SalesForce", "read");
		String userName = library.getFromExcelRowAndColumn(2, "UserName");
		String password = library.getFromExcelRowAndColumn(2, "Password");
		String clientEmail = library.getFromExcelRowAndColumn(2, "ClientEmail");
		String activity = library.getFromExcelRowAndColumn(2, "Activity");
		String clientName = library.getFromExcelRowAndColumn(2, "ClientName");
		library.closeExcelSheet(path, "SalesForce", "read");

		Map<String, String> map = new HashMap<>();
		
		map.put("Username", userName);
		map.put("Password", password);
		map.put("ClientEmail", clientEmail);	
		map.put("Activity", activity);
		map.put("ClientName", clientName);

		Object[][] obj = { { map } };

		return obj;
	}
	
	@DataProvider(name = "UpdateAgentActivityTestData")
	public static Object[][] VerifyActivityInSfDataProvider(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String testName = context.getName();
		String path = "";
		if (context.getName().equalsIgnoreCase("Urgency_Test_Web")) {
			path = "data/" + testName + ".xlsx";
		} else {
			path = "data/SF_Data.xlsx";
		}
		library.openExcelSheet(path, "Login", "read");
		String userName = library.getFromExcelRowAndColumn(2, "UserName");
		String password = library.getFromExcelRowAndColumn(2, "Password");
		String clientName = library.getFromExcelRowAndColumn(2, "ClientName");
		String activity = library.getFromExcelRowAndColumn(2, "Activity");
		library.closeExcelSheet(path, "Login", "read");
		
		Map<String, String> map = new HashMap<>();
		
		map.put("Username", userName);
		map.put("Password", password);
		map.put("ClientName", clientName);
		map.put("Activity", activity);

		Object[][] obj = { { map } };

		return obj;
	}
	
	
	@DataProvider(name = "Salesforcenotification")
	public static Object[][] UpdateSalesforcenotification(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String testName = context.getName();
		String path = "data/Salesforce_Notification_Test.xlsx";;

		library.openExcelSheet(path, "Login", "read");
		String Username = library.getFromExcelRowAndColumn(1, "Username");
		String Password = library.getFromExcelRowAndColumn(1, "Password");
		String Agent1 = library.getFromExcelRowAndColumn(1, "Agent1");
		String TimeZone1 = library.getFromExcelRowAndColumn(1, "TimeZone1");
		String Agent2 = library.getFromExcelRowAndColumn(1, "Agent2");
		String TimeZone2 = library.getFromExcelRowAndColumn(1, "TimeZone2");
		String Agent3 = library.getFromExcelRowAndColumn(1, "Agent3");
		String TimeZone3 = library.getFromExcelRowAndColumn(1, "TimeZone3");
		String Agent4 = library.getFromExcelRowAndColumn(1, "Agent4");
		String TimeZone4 = library.getFromExcelRowAndColumn(1, "TimeZone4");
		
	

		library.closeExcelSheet(path, "Login", "read");

		Map<String, String> map = new HashMap<>();
		
		map.put("Username", Username);
		map.put("Password", Password);
		map.put("Agent1", Agent1);
		map.put("TimeZone1", TimeZone1);
		map.put("Agent2", Agent2);
		map.put("TimeZone2", TimeZone2);
		map.put("Agent3", Agent3);
		map.put("TimeZone3", TimeZone3);
		map.put("Agent4", Agent4);
		map.put("TimeZone4", TimeZone4);
		

		Object[][] obj = { { map } };

		return obj;
	}
	
	@DataProvider(name = "SalesforcenotificationIOS")
	public static Object[][] UpdateSalesforcenotificationIOS(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String testName = context.getName();
		String path = "data/Salesforce_Notification_Test_IOS.xlsx";;

		library.openExcelSheet(path, "Login", "read");
		String Username = library.getFromExcelRowAndColumn(1, "Username");
		String Password = library.getFromExcelRowAndColumn(1, "Password");
		String Agent1 = library.getFromExcelRowAndColumn(1, "Agent1");
		String TimeZone1 = library.getFromExcelRowAndColumn(1, "TimeZone1");
		String Agent2 = library.getFromExcelRowAndColumn(1, "Agent2");
		String TimeZone2 = library.getFromExcelRowAndColumn(1, "TimeZone2");
		String Agent3 = library.getFromExcelRowAndColumn(1, "Agent3");
		String TimeZone3 = library.getFromExcelRowAndColumn(1, "TimeZone3");
		String Agent4 = library.getFromExcelRowAndColumn(1, "Agent4");
		String TimeZone4 = library.getFromExcelRowAndColumn(1, "TimeZone4");
		
	

		library.closeExcelSheet(path, "Login", "read");

		Map<String, String> map = new HashMap<>();
		
		map.put("Username", Username);
		map.put("Password", Password);
		map.put("Agent1", Agent1);
		map.put("TimeZone1", TimeZone1);
		map.put("Agent2", Agent2);
		map.put("TimeZone2", TimeZone2);
		map.put("Agent3", Agent3);
		map.put("TimeZone3", TimeZone3);
		map.put("Agent4", Agent4);
		map.put("TimeZone4", TimeZone4);

		Object[][] obj = { { map } };

		return obj;
	}
	
	@DataProvider(name = "SalesforcenotificationleadIDAndroid")
	public static Object[][] SalesforcenotificationleadIDAndroid(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String testName = context.getName();
		String path = "data/SalesForceTest.xlsx";

		library.openExcelSheet(path, "EmailIdForSalesForce", "read");
		String LeadEmailID = library.getFromExcelRowAndColumn(1, "LeadEmailID");

		library.closeExcelSheet(path, "LeadCreation_Workflow", "read");

		Map<String, String> map = new HashMap<>();
		map.put("LeadEmailID", LeadEmailID);

		Object[][] obj = { { map } };

		return obj;
	}


@DataProvider(name = "SalesforcenotificationleadIDiOS")
public static Object[][] SalesforcenotificationleadIDiOS(ITestContext context, Method method) throws Exception {
	FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
	library.setCurrentTestMethod(method.getName());
	String testName = context.getName();
	String path = "data/SalesForceTestForAnotherAgent.xlsx";

	library.openExcelSheet(path, "EmailIdForSalesForce", "read");
	String LeadEmailID = library.getFromExcelRowAndColumn(1, "LeadEmailID");

	library.closeExcelSheet(path, "LeadCreation_Workflow", "read");

	Map<String, String> map = new HashMap<>();
	map.put("LeadEmailID", LeadEmailID);

	Object[][] obj = { { map } };

	return obj;
}

@DataProvider(name = "SalesForceTestForAndroidNSeries")
public static Object[][] SalesForceTestForAndroidNSeries(ITestContext context, Method method) throws Exception {
	FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
	library.setCurrentTestMethod(method.getName());
	String testName = context.getName();
	String path = "data/SalesForceTestForAndroidNSeries.xlsx";

	library.openExcelSheet(path, "EmailIdForSalesForce", "read");
	String LeadEmailID = library.getFromExcelRowAndColumn(1, "LeadEmailID");

	library.closeExcelSheet(path, "LeadCreation_Workflow", "read");

	Map<String, String> map = new HashMap<>();
	map.put("LeadEmailID", LeadEmailID);

	Object[][] obj = { { map } };

	return obj;
}

@DataProvider(name = "SalesForceTestForIOSNSeries")
public static Object[][] SalesForceTestForIOSNSeries(ITestContext context, Method method) throws Exception {
	FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
	library.setCurrentTestMethod(method.getName());
	String testName = context.getName();
	String path = "data/SalesForceTestForIOSNSeries.xlsx";

	library.openExcelSheet(path, "EmailIdForSalesForce", "read");
	String LeadEmailID = library.getFromExcelRowAndColumn(1, "LeadEmailID");

	library.closeExcelSheet(path, "LeadCreation_Workflow", "read");

	Map<String, String> map = new HashMap<>();
	map.put("LeadEmailID", LeadEmailID);

	Object[][] obj = { { map } };

	return obj;
}
}
