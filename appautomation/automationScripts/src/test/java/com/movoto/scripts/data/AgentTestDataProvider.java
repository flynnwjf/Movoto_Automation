package com.movoto.scripts.data;

import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

import com.movoto.fixtures.FixtureLibrary;

import groovy.json.JsonException;

public class AgentTestDataProvider {
	
	@DataProvider(name = "LoginTestData")
	public static Object[][] LoginDataProvider(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String testName = context.getName();
		String path = "";
		if (context.getName().equalsIgnoreCase("Urgency_Test_Web") || context.getName().equalsIgnoreCase("Activities_Workflow_Web") || context.getName().equalsIgnoreCase("Activities_Workflow_Android") || context.getName().equalsIgnoreCase("Activities_Workflow_IOS") || context.getName().equalsIgnoreCase("AmnAct0001_Update_Urgency_For_Talked_Stage_Web") || context.getName().equalsIgnoreCase("AmnAct0002_Activity_Selection_List_For_MetStage_Web") || context.getName().equalsIgnoreCase("AmnAct0002_Activity_Selection_List_For_MetStage_Android") || context.getName().equalsIgnoreCase("AmnAct0001_Update_Urgency_For_Talked_Stage_Android") || context.getName().equalsIgnoreCase("AmnAct0002_Activity_Selection_List_For_MetStage_IOS") || context.getName().equalsIgnoreCase("AmnAct0004_Update_Transaction_For_MetStage_Web")||context.getName().equalsIgnoreCase("AmnAct0001_Update_Urgency_For_Talked_Stage_IOS")||context.getName().equalsIgnoreCase("AmnAct0004_Update_Transaction_For_MetStage_Android") || context.getName().equalsIgnoreCase("AmnAct0004_Update_Transaction_For_MetStage_IOS") || context.getName().equalsIgnoreCase("AmnAct0006_UpdateTransactionForMadeAnOffer_Web")|| context.getName().equalsIgnoreCase("AmnAct0006_UpdateTransactionForMadeAnOffer_IOS")||context.getName().equalsIgnoreCase("AmnAct0006_UpdateTransactionForMadeAnOffer_Android")||context.getName().equalsIgnoreCase("AmnAct0008_UpdateTransactionForContractCancel_Web") || context.getName().equalsIgnoreCase("AmnAct0008_UpdateTransactionForContractCancel_Android")) {   path = "data/" + testName + ".xlsx";
	    } else {
	     path = "data/Common_Data.xlsx";
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
		map.put("TestName", "Login");

		Object[][] obj = { { map } };

		return obj;
	}
	
	@DataProvider(name = "LoginTestData1")
	public static Object[][] LoginDataProvider1(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String testName = context.getName();
		String path = "";
		if (context.getName().equalsIgnoreCase("Urgency_Test_Web") || context.getName().equalsIgnoreCase("Activities_Workflow_Web")) {			path = "data/" + testName + ".xlsx";
		} else {
			path = "data/Common_Data4.xlsx";
		}
		library.openExcelSheet(path, "LoginLeave", "read");
		String userName = library.getFromExcelRowAndColumn(1, "UserName");
		String password = library.getFromExcelRowAndColumn(1, "Password");
		String clientName = library.getFromExcelRowAndColumn(1, "ClientName");
		library.closeExcelSheet(path, "LoginLeave", "read");

		Map<String, String> map = new HashMap<>();
		map.put("Username", userName);
		map.put("Password", password);
		map.put("ClientName", clientName);
		map.put("TestName", "Login");

		Object[][] obj = { { map } };

		return obj;
	}
	
	
	@DataProvider(name = "LoginTestData6")
	public static Object[][] LoginDataProvider6(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String testName = context.getName();
		String path = "";
		if (context.getName().equalsIgnoreCase("Urgency_Test_Web") || context.getName().equalsIgnoreCase("Activities_Workflow_Web")) {			path = "data/" + testName + ".xlsx";
		} else {
			path = "data/Common_Data4.xlsx";
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
		map.put("TestName", "Login");

		Object[][] obj = { { map } };

		return obj;
	}
	
	@DataProvider(name = "FCNotificationTestData")
	public static Object[][] FC1NotificationDataProvider(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String path = "data/NotificationTest.xlsx";
        library.openExcelSheet(path, "Login", "read");
		String userName = library.getFromExcelRowAndColumn(1, "UserName");
		String password = library.getFromExcelRowAndColumn(1, "Password");
		String clientName = library.getFromExcelRowAndColumn(1, "ClientName");
		library.closeExcelSheet(path, "Login", "read");

		Map<String, String> map = new HashMap<>();
		map.put("Username", userName);
		map.put("Password", password);
		map.put("ClientName", clientName);
		map.put("TestName", "Login");

		Object[][] obj = { { map } };

		return obj;
	}
	
	@DataProvider(name="APIgetLeavefunctional")
	  public static Object[][] APIgetLeavefunctional(ITestContext context, Method method) throws Exception 
	{
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
	   library.setCurrentTestMethod(method.getName());
	   //String testName = context.getName();
	   String path = "data/LeavedateFunctional.xlsx";
	   library.openExcelSheet(path, "Future Leaves", "read");
	   String contentType = library.getFromExcelRowAndColumn(1, "ContentType");
	   String accessTokenURL = library.getFromExcelRowAndColumn(1, "AccessTokenURL");
	   String contactsUrl = library.getFromExcelRowAndColumn(1, "ContactsUrl");
	   String notificationFilePath = library.getFromExcelRowAndColumn(1, "Notification Data Path");
	   String notificationFileName = library.getFromExcelRowAndColumn(1, "Notification");
	   library.closeExcelSheet(path, "Future Leaves", "read");
	   Map<String, Object> map = new HashMap<>();
	   
	   synchronized (AgentTestDataProvider.class) 
	   {
	   String jsonFilePath = "data/notifications/LoginDataLeaveFunctional.json";
	   Map<String, Object> jsonData = getJsonAsMap(jsonFilePath);
	   map.put("ContentType", contentType);
	   map.put("AccessTokenURL", accessTokenURL);
	   map.put("ContactsUrl", contactsUrl);
	   map.put("NotificationData", jsonData);		    
	   map.put("LoginDataLeave", jsonData);
	   Object[][] obj = { { map } };
	   return obj;
	  }
	 }
	
	@DataProvider(name = "FCNotificationTestDataIOS")
	public static Object[][] FC1NotificationDataProviderIOS(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String path = "data/NotificationTest.xlsx";
        library.openExcelSheet(path, "Login1", "read");
		String userName = library.getFromExcelRowAndColumn(1, "UserName");
		String password = library.getFromExcelRowAndColumn(1, "Password");
		String clientName = library.getFromExcelRowAndColumn(1, "ClientName");
		library.closeExcelSheet(path, "Login1", "read");

		Map<String, String> map = new HashMap<>();
		map.put("Username", userName);
		map.put("Password", password);
		map.put("ClientName", clientName);
		map.put("TestName", "Login");

		Object[][] obj = { { map } };

		return obj;
	}
	
	@DataProvider(name = "NNotificationTestData")
	public static Object[][] NNotificationDataProvider(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String path = "data/NotificationTest.xlsx";
        library.openExcelSheet(path, "Login3", "read");
		String userName = library.getFromExcelRowAndColumn(1, "UserName");
		String password = library.getFromExcelRowAndColumn(1, "Password");
		String clientName = library.getFromExcelRowAndColumn(1, "ClientName");
		library.closeExcelSheet(path, "Login3", "read");

		Map<String, String> map = new HashMap<>();
		map.put("Username", userName);
		map.put("Password", password);
		map.put("ClientName", clientName);
		map.put("TestName", "Login");

		Object[][] obj = { { map } };

		return obj;
	}
	
	@DataProvider(name = "NNotificationTestDataIOS")
	public static Object[][] NNotificationIOSDataProvider(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String path = "data/NotificationTest.xlsx";
        library.openExcelSheet(path, "Login4", "read");
		String userName = library.getFromExcelRowAndColumn(1, "UserName");
		String password = library.getFromExcelRowAndColumn(1, "Password");
		String clientName = library.getFromExcelRowAndColumn(1, "ClientName");
		library.closeExcelSheet(path, "Login4", "read");

		Map<String, String> map = new HashMap<>();
		map.put("Username", userName);
		map.put("Password", password);
		map.put("ClientName", clientName);
		map.put("TestName", "Login");

		Object[][] obj = { { map } };

		return obj;
	}
	
	
	
	
	@DataProvider(name = "LoginTestData3")
	public static Object[][] LoginDataProvider3(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String testName = context.getName();
		String path = "";
		if (context.getName().equalsIgnoreCase("Urgency_Test_Web") || context.getName().equalsIgnoreCase("Activities_Workflow_Web")) {			path = "data/" + testName + ".xlsx";
		} else {
			path = "data/Common_Data3.xlsx";
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
		map.put("TestName", "Login");

		Object[][] obj = { { map } };

		return obj;
	}

	
	

	@DataProvider(name = "NotificationTestData", parallel = false)
	public static Object[][] NotificationProvider(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String testName = context.getName();
		String path = "data/" + testName + ".xlsx";
		library.openExcelSheet(path, "Notification", "read");
		String notificationFileName = library.getFromExcelRowAndColumn(1, "Notification");
		String notificationFilePath = library.getFromExcelRowAndColumn(1, "Notification Data Path");
		String xmdataKey = library.getFromExcelRowAndColumn(1, "XMData Key");
		String contentType = library.getFromExcelRowAndColumn(1, "Content Type");
		String accessTokenURL = library.getFromExcelRowAndColumn(1, "Access Token URL");
		String notificationURL = library.getFromExcelRowAndColumn(1, "Notification URL");
		library.closeExcelSheet(path, "Notification", "read");
		Map<String, Object> map = new HashMap<>();

		synchronized (AgentTestDataProvider.class) {
			String jsonFilePath = notificationFilePath + "/" + notificationFileName + ".json";
			Map<String, Object> jsonData = getJsonAsMap(jsonFilePath);
			map.put("XMDataKey", xmdataKey);
			map.put("ContentType", contentType);
			map.put("AccessTokenURL", accessTokenURL);
			map.put("NotificationURL", notificationURL);
			map.put("NotificationData", jsonData);
			Object[][] obj = { { map } };

			return obj;
		}

	}

	@DataProvider(name = "DRTestData")
	public static Object[][] DRDataProvider(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String testName = context.getName();
		String path = "data/" + testName + ".xlsx";
		library.openExcelSheet(path, "DR", "read");
		String clientName = library.getFromExcelRowAndColumn(1, "ClientName");
		library.closeExcelSheet("", "", "");

		Map<String, String> map = new HashMap<>();
		map.put("ClientName", clientName);
		Object[][] obj = { { map } };

		return obj;
	}

	@DataProvider(name = "MakeOfferTestData")
	public static Object[][] MakeOfferDataProvider(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String testName = context.getName();
		String path = "data/" + testName + ".xlsx";
		library.openExcelSheet(path, "Make Offer", "read");

		String clientName = library.getFromExcelRowAndColumn(1, "ClientName");
		String address = library.getFromExcelRowAndColumn(1, "Address");
		String offerDate = library.getFromExcelRowAndColumn(1, "Offer Date");
		String offerPrice = library.getFromExcelRowAndColumn(1, "Offer Price");
		String preStage = library.getFromExcelRowAndColumn(1, "Pre-Stage");
		String postStage = library.getFromExcelRowAndColumn(1, "Post-Stage");

		library.closeExcelSheet(path, "Make Offer", "read");

		Map<String, String> map = new HashMap<>();

		map.put("ClientName", clientName);
		map.put("Address", address);
		map.put("OfferDate", offerDate);
		map.put("OfferPrice", offerPrice);
		map.put("preStage", preStage);
		map.put("postStage", postStage);
		map.put("TestName", "Make Offer");

		Object[][] obj = { { map } };

		return obj;
	}

	@DataProvider(name = "AcceptContractTestData")
	public static Object[][] AcceptContractProvider(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String testName = context.getName();
		String path = "data/" + testName + ".xlsx";
		library.openExcelSheet(path, "Accept Contract", "read");

		String clientName = library.getFromExcelRowAndColumn(1, "ClientName");
		String contractDate = library.getFromExcelRowAndColumn(1, "Contract Date");
		String contractPrice = library.getFromExcelRowAndColumn(1, "Contract Price");
		String commission = library.getFromExcelRowAndColumn(1, "Commission");
		String contractFname = library.getFromExcelRowAndColumn(1, "Contract First Name");
		String contractLname = library.getFromExcelRowAndColumn(1, "Contract Last Name");
		String phone = library.getFromExcelRowAndColumn(1, "Phone");
		String email = library.getFromExcelRowAndColumn(1, "Email");
		String preStage = library.getFromExcelRowAndColumn(1, "Pre-Stage");
		String postStage = library.getFromExcelRowAndColumn(1, "Post-Stage");

		library.closeExcelSheet(path, "Accept Contract", "read");

		Map<String, String> map = new HashMap<>();

		map.put("ClientName", clientName);
		map.put("contractDate", contractDate);
		map.put("contractPrice", contractPrice);
		map.put("commission", commission);
		map.put("contractFname", contractFname);
		map.put("contractLname", contractLname);
		map.put("phone", phone);
		map.put("email", email);
		map.put("preStage", preStage);
		map.put("postStage", postStage);
		map.put("TestName", "Accept Contract");

		Object[][] obj = { { map } };

		return obj;
	}

	@DataProvider(name = "FutureLeaveTestData")
	public static Object[][] FutureLeaveProvider(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String testName = context.getName();
		String path = "data/" + testName + ".xlsx";
		library.openExcelSheet(path, "Future Leaves", "read");
		String clientName = library.getFromExcelRowAndColumn(1, "ClientName");
		String fromDate = library.getFromExcelRowAndColumn(1, "From Date");
		String toDate = library.getFromExcelRowAndColumn(1, "To Date");
		library.closeExcelSheet(path, "Future Leaves", "read");

		Map<String, String> map = new HashMap<>();
		map.put("ClientName", clientName);
		map.put("fromDate", fromDate);
		map.put("toDate", toDate);
		map.put("TestName", "Future Leaves");
		Object[][] obj = { { map } };

		return obj;
	}

	@DataProvider(name = "UrgencyStageTestData")
	public static Object[][] UrgencyStageProvider(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String testName = context.getName();
		String path = "data/" + testName + ".xlsx";
		library.openExcelSheet(path, "Urgency", "read");
		int rowCount = library.getExcelRowCount();
		Object[][] data = new Object[rowCount][1];
		for (int i = 0; i < rowCount - 1; i++) {
			int j = i + 1;
			String urgencyCode = library.getFromExcelRowAndColumn(j, "UrgencyCode");
			String urgencyName = library.getFromExcelRowAndColumn(j, "UrgencyName");
			String clientName = library.getFromExcelRowAndColumn(j, "ClientName");
			Map<String, String> map = new HashMap<>();
			map.put("urgencyCode", urgencyCode);
			map.put("urgencyName", urgencyName);
			map.put("clientName", clientName);
			try {
				data[i][0] = map;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		library.closeExcelSheet(path, "Urgency", "read");

		return data;
	}

	@DataProvider(name = "ClientNameTestData")
	public static Object[][] ClientNameProvider(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String testName = context.getName();
		String path = "data/" + testName + ".xlsx";
		library.openExcelSheet(path, "ClientName", "read");
		int rowCount = library.getExcelRowCount();
		Object[][] data = new Object[rowCount - 1][1];
		for (int i = 0; i < rowCount - 1; i++) {
			int j = i + 1;
			String clientName = library.getFromExcelRowAndColumn(j, "ClientName");
			String clientFullName = library.getFromExcelRowAndColumn(j, "ClientFullName");
			String primaryPhone = library.getFromExcelRowAndColumn(j, "PrimaryPhone");

			Map<String, String> map = new HashMap<>();
			map.put("clientName", clientName);
			map.put("clientFullName", clientFullName);
			map.put("primaryPhone", primaryPhone);

			try {
				data[i][0] = map;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		library.closeExcelSheet(path, "ClientName", "read");
		return data;
	}

	@DataProvider(name = "VerifyLoginTestData")
	public static Object[][] LoginTestDataProvider(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String testName = context.getName();
		String path = "data/" + testName + ".xlsx";
		library.openExcelSheet(path, "LoginTest", "read");
		int rowCount = library.getExcelRowCount();
		Object[][] data = new Object[rowCount][1];
		for (int i = 0; i < rowCount - 1; i++) {
			int j = i + 1;
			String userName = library.getFromExcelRowAndColumn(j, "Username");
			String password = library.getFromExcelRowAndColumn(j, "Password");
			String alertMessage = library.getFromExcelRowAndColumn(j, "Alertmessage");
			Map<String, String> map = new HashMap<>();
			map.put("userName", userName);
			map.put("password", password);
			map.put("alertMessage", alertMessage);
			try {
				data[i][0] = map;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		library.closeExcelSheet(path, "LoginTest", "read");
		return data;
	}

	@DataProvider(name = "N0TestData", parallel = false)
	public static Object[][] N0TestProvider(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String testName = context.getName();
		String path = "data/" + testName + ".xlsx";
		library.openExcelSheet(path, "Notification", "read");
		String notificationFileName = library.getFromExcelRowAndColumn(2, "Notification");
		String notificationFilePath = library.getFromExcelRowAndColumn(2, "Notification Data Path");
		String xmdataKey = library.getFromExcelRowAndColumn(2, "XMData Key");
		String contentType = library.getFromExcelRowAndColumn(2, "Content Type");
		String accessTokenURL = library.getFromExcelRowAndColumn(2, "Access Token URL");
		String notificationURL = library.getFromExcelRowAndColumn(2, "Notification URL");
		library.closeExcelSheet(path, "Notification", "read");
		Map<String, Object> map = new HashMap<>();

		synchronized (AgentTestDataProvider.class) {
			String jsonFilePath = notificationFilePath + "/" + notificationFileName + ".json";
			Map<String, Object> jsonData = getJsonAsMap(jsonFilePath);
			map.put("XMDataKey", xmdataKey);
			map.put("ContentType", contentType);
			map.put("AccessTokenURL", accessTokenURL);
			map.put("NotificationURL", notificationURL);
			map.put("NotificationData", jsonData);
			Object[][] obj = { { map } };

			return obj;
		}

	}

	@DataProvider(name = "FC5TestData", parallel = false)
	public static Object[][] FC5TestProvider(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String testName = context.getName();
		String path = "data/" + testName + ".xlsx";
		library.openExcelSheet(path, "Notification", "read");
		String notificationFileName = library.getFromExcelRowAndColumn(3, "Notification");
		String notificationFilePath = library.getFromExcelRowAndColumn(3, "Notification Data Path");
		String xmdataKey = library.getFromExcelRowAndColumn(3, "XMData Key");
		String contentType = library.getFromExcelRowAndColumn(3, "Content Type");
		String accessTokenURL = library.getFromExcelRowAndColumn(3, "Access Token URL");
		String notificationURL = library.getFromExcelRowAndColumn(3, "Notification URL");
		library.closeExcelSheet(path, "Notification", "read");
		Map<String, Object> map = new HashMap<>();

		synchronized (AgentTestDataProvider.class) {
			String jsonFilePath = notificationFilePath + "/" + notificationFileName + ".json";
			Map<String, Object> jsonData = getJsonAsMap(jsonFilePath);
			map.put("XMDataKey", xmdataKey);
			map.put("ContentType", contentType);
			map.put("AccessTokenURL", accessTokenURL);
			map.put("NotificationURL", notificationURL);
			map.put("NotificationData", jsonData);
			Object[][] obj = { { map } };

			return obj;
		}

	}

	private static Map<String, Object> getJsonAsMap(String jsonFilePath) {
		JSONParser parser = new JSONParser();
		try {

			Object obj = parser.parse(new FileReader(jsonFilePath));

			JSONObject jsonObject = (JSONObject) obj;
			System.out.println(jsonObject);
			Map<String, Object> retMap = new HashMap<>();

			if (jsonObject != null) {
				retMap = toMap(jsonObject);
				return retMap;
			}

			System.out.println(retMap);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Map<String, Object> toMap(JSONObject object) throws JsonException {
		Map<String, Object> map = new HashMap<>();

		Iterator<String> keysItr = object.keySet().iterator();
		while (keysItr.hasNext()) {
			String key = keysItr.next();
			Object value = object.get(key);

			if (value instanceof JSONArray) {
				value = toList((JSONArray) value);
			}

			else if (value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			map.put(key, value);
		}
		return map;
	}

	private static List<Object> toList(JSONArray array) throws JsonException {
		List<Object> list = new ArrayList<>();
		for (int i = 0; i < array.size(); i++) {
			Object value = array.get(i);
			if (value instanceof JSONArray) {
				value = toList((JSONArray) value);
			}

			else if (value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			list.add(value);
		}
		return list;
	}

	@DataProvider(name = "CancelContractAndMakeOfferTestData")
	public static Object[][] CancelContractAndMakeOfferTestDataProvider(ITestContext context, Method method)
			throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String testName = context.getName();
		String path = "data/" + testName + ".xlsx";
		library.openExcelSheet(path, "Make Offer", "read");

		String clientName = library.getFromExcelRowAndColumn(1, "ClientName");
		String address = library.getFromExcelRowAndColumn(1, "Address");
		String offerDate = library.getFromExcelRowAndColumn(1, "Offer Date");
		String offerPrice = library.getFromExcelRowAndColumn(1, "Offer Price");
		String preStage = library.getFromExcelRowAndColumn(1, "Pre-Stage");
		String postStage = library.getFromExcelRowAndColumn(1, "Post-Stage");

		library.closeExcelSheet(path, "Make Offer", "read");

		Map<String, String> map = new HashMap<>();

		map.put("ClientName", clientName);
		map.put("Address", address);
		map.put("OfferDate", offerDate);
		map.put("OfferPrice", offerPrice);
		map.put("preStage", preStage);
		map.put("postStage", postStage);
		map.put("TestName", "Make Offer");

		Object[][] obj = { { map } };

		return obj;
	}

	@DataProvider(name = "GestureClientTestData")
	public static Object[][] GestureClientNameProvider(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String testName = context.getName();
		String path = "data/" + testName + ".xlsx";
		library.openExcelSheet(path, "Gesture_Data", "read");
		int rowCount = library.getExcelRowCount();
		Object[][] data = new Object[rowCount][1];
		for (int i = 0; i < rowCount - 1; i++) {
			int j = i + 1;
			String clientName = library.getFromExcelRowAndColumn(j, "ClientName");

			Map<String, String> map = new HashMap<>();
			map.put("clientName", clientName);

			try {
				data[i][0] = map;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		library.closeExcelSheet(path, "Gesture_Data", "read");
		return data;
	}

	// Gopal
	@DataProvider(name = "SearchAndVerifyClient")
	public static Object[][] provideDataToSeachAndVerify(ITestContext context, Method method) {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		// String testName = context.getName();
		// String path = "data/"+testName+".xlsx";
		String path = "data/AgentAppTestData.xlsx";
		library.openExcelSheet(path, "ClientList", "read");
		int rowCount = library.getExcelRowCount();
		Object[][] data = new Object[rowCount - 1][1];
		for (int i = 0; i < rowCount - 1; i++) {
			int j = i + 1;
			String searchText = library.getFromExcelRowAndColumn(j, "Search Name");
			String clientName = library.getFromExcelRowAndColumn(j, "Client Name");

			Map<String, String> map = new HashMap<>();
			map.put("clientName", searchText);
			map.put("clientFullName", clientName);

			try {
				data[i][0] = map;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		library.closeExcelSheet(path, "ClientList", "read");
		return data;
	}

	@DataProvider(name = "DateProvider")
	public static Object[][] dateProvider(ITestContext context, Method method) {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String path = "data/AgentAppTestData.xlsx";
		library.openExcelSheet(path, "Future Leaves", "read");
		int rowCount = library.getExcelRowCount();
		Object[][] data = new Object[rowCount - 1][1];
		for (int i = 0; i < rowCount - 1; i++) {
			int j = i + 1;
			String fromDate = library.getFromExcelRowAndColumn(j, "From Date");
			String toDate = library.getFromExcelRowAndColumn(j, "To Date");

			Map<String, String> map = new HashMap<>();
			map.put("fromDate", fromDate);
			map.put("toDate", toDate);

			try {
				data[i][0] = map;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		library.closeExcelSheet(path, "Future Leaves", "read");
		return data;
	}

	// Created by Puneet
	@DataProvider(name = "ClientEmailAndPhoneNumberTestData")
	public static Object[][] ClientEmailAndPhoneNumberProvider(ITestContext context, Method method) throws Exception {

		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String testName = context.getName();
		String path = "data/" + testName + ".xlsx";
		library.openExcelSheet(path, "EmailAndPhoneNumber", "read");
		int rowCount = library.getExcelRowCount();
		Object[][] data = new Object[rowCount - 1][1];
		for (int i = 0; i < rowCount - 1; i++) {
			int j = i + 1;
			String clientEmail = library.getFromExcelRowAndColumn(j, "ClientEmail");
			String clientName = library.getFromExcelRowAndColumn(j, "ClientName");
			String clientPhoneNumber = library.getFromExcelRowAndColumn(j, "ClientPhoneNumber");
			Map<String, String> map = new HashMap<>();
			map.put("clientEmail", clientEmail);
			map.put("clientName", clientName);
			map.put("clientPhoneNumber", clientPhoneNumber);
			try {
				data[i][0] = map;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		library.closeExcelSheet(path, "EmailAndPhoneNumber", "read");
		return data;

	}

	// Created by Gopal
	@DataProvider(name = "contactInfoData")
	public Object[][] contactInfoDataProvider(ITestContext context, Method method) {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String path = "data/AgentAppTestData.xlsx";
		library.openExcelSheet(path, "Contact Info", "read");
		int rowCount = library.getExcelRowCount();
		Object[][] data = new Object[rowCount - 1][1];
		for (int i = 0; i < rowCount - 1; i++) {
			int j = i + 1;
			String clientName = library.getFromExcelRowAndColumn(j, "Client Name");
			String primaryPhone = library.getFromExcelRowAndColumn(j, "Primary Phone");
			String mobilePhone = library.getFromExcelRowAndColumn(j, "Mobile Phone");
			String homePhone = library.getFromExcelRowAndColumn(j, "Home Phone");
			String officePhone = library.getFromExcelRowAndColumn(j, "Office Phone");
			String otherPhone = library.getFromExcelRowAndColumn(j, "Other Phone");
			String email = library.getFromExcelRowAndColumn(j, "Email");

			Map<String, String> map = new HashMap<>();
			map.put("clientName", clientName);
			map.put("primaryPhone", primaryPhone);
			map.put("mobilePhone", mobilePhone);
			map.put("homePhone", homePhone);
			map.put("officePhone", officePhone);
			map.put("otherPhone", otherPhone);
			map.put("email", email);

			try {
				data[i][0] = map;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		library.closeExcelSheet(path, "ClientList", "read");
		return data;
	}
    
    //Created by Priyanka on 05/15/2016 to fetch data from ClientList excel sheet and LoginData json data.
    @DataProvider(name="APILoginTest")
    public static Object[][] APILoginTestDataProviders(ITestContext context, Method method) throws Exception {
        FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
        library.setCurrentTestMethod(method.getName());
        //String testName = context.getName();
        String path = "data/ClientList.xlsx";
        library.openExcelSheet(path, "Sheet1", "read");
        String contentType = library.getFromExcelRowAndColumn(1, "Content Type");
        String contactsUrl = library.getFromExcelRowAndColumn(1, "Contact URL");
        String accessTokenURL = library.getFromExcelRowAndColumn(1, "Access Token URL");
        //String body = library.getFromExcelRowAndColumn(1, "Body");
        
        library.closeExcelSheet(path, "Sheet1", "read");
        Map<String, Object> map = new HashMap<>();
        
        synchronized (AgentTestDataProvider.class) {
            String jsonFilePath = "data/notifications/LoginData.json";
            Map<String, Object> LoginData = getJsonAsMap(jsonFilePath);
            map.put("ContentType", contentType);
            map.put("AccessTokenURL", accessTokenURL);
            map.put("contactsUrl", contactsUrl);
            //map.put("Body",body);
            map.put("LoginData", LoginData);
            Object[][] obj = { { map } };
            return obj;
        }
    }
    
    
    //Created by Priyanka on 05/11/2016 to fetch data from ClientList excel sheet and LoginData json data.
    
    @DataProvider(name = "APIGetContents")
    public static Object[][] APILoginTestDataProvider(ITestContext context, Method method) throws Exception {
        FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
        library.setCurrentTestMethod(method.getName());
        // String testName = context.getName();
        String path = "data/ClientList.xlsx";
        library.openExcelSheet(path, "Sheet2", "read");
        int rowCount = library.getExcelRowCount();
        library.closeExcelSheet(path, "Sheet2", "read");
        Object[][] data = new Object[rowCount-1][1];
        library.openExcelSheet(path, "Sheet1", "read");
        String contentType = library.getFromExcelRowAndColumn(1, "Content Type");
        String contactsUrl = library.getFromExcelRowAndColumn(1, "Contact URL");
        String newestreferredurl = library.getFromExcelRowAndColumn(1, "Newest referred Sort URL");
        String oldestreferredurl = library.getFromExcelRowAndColumn(1, "Oldest referred Sort URL");
        String mosturgenturl = library.getFromExcelRowAndColumn(1, "Most urgent Sort URL");
        String accessTokenURL = library.getFromExcelRowAndColumn(1, "Access Token URL");
        String firstnameurl = library.getFromExcelRowAndColumn(1, "First Name Sort URL");
        
        library.closeExcelSheet(path, "Sheet1", "read");
        
        Map<String, Object> apiData = new HashMap<>();
        synchronized (AgentTestDataProvider.class) {
            
            String jsonFilePath = "data/notifications/LoginData.json";
            Map<String, Object> jsonData = getJsonAsMap(jsonFilePath);
            apiData.put("ContentType", contentType);
            apiData.put("AccessTokenURL", accessTokenURL);
            apiData.put("contactsUrl", contactsUrl);
            apiData.put("newestreferredurl", newestreferredurl);
            apiData.put("oldestreferredurl", oldestreferredurl);
            apiData.put("mosturgenturl", mosturgenturl);
            apiData.put("firstnameurl", firstnameurl);
            //apidata.put("sortUrl", sortUrl);
            apiData.put("LoginData", jsonData);
        }
        for (int i = 0; i < rowCount - 1; i++) {
            int j = i + 1;
            library.openExcelSheet(path, "Sheet2", "read");
            String Order = library.getFromExcelRowAndColumn(j, "Order");
            String SortURL = library.getFromExcelRowAndColumn(j, "Sort URL");
            //Integer.parseInt(variable);
            library.closeExcelSheet(path, "Sheet2", "read");
            Map<String, String> orderData = new HashMap<>();
            orderData.put("Order", Order);
            orderData.put("SortURL", SortURL);
            //orderData.put("variable", variable);
            Map<String, Object> map = new HashMap<>();
            map.put("apiData", apiData);
            map.put("orderData", orderData);
            try {
                data[i][0] = map;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        }
        library.closeExcelSheet(path, "Sheet2", "read");
        
        return data;
        
    }
 // Gopal
    @DataProvider(name = "APITesting")
    public static Object[][] APITestingDataProvider(ITestContext context, Method method) throws Exception {
        FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
        library.setCurrentTestMethod(method.getName());
        String path = "data/AgentAppTestData.xlsx";
        library.openExcelSheet(path, "APILoginTest", "read");
        String notificationFileName = library.getFromExcelRowAndColumn(1, "Notification");
        String clientLeadApi = library.getFromExcelRowAndColumn(1, "ClientLeadAPI");
        String notesApi = library.getFromExcelRowAndColumn(1, "Notes API");
        String propertiesApi = library.getFromExcelRowAndColumn(1, "Properties API");
        String clientName = library.getFromExcelRowAndColumn(1, "Client Name");
        String notificationFilePath = library.getFromExcelRowAndColumn(1, "Notification Data Path");
        String contentType = library.getFromExcelRowAndColumn(1, "Content Type");
        String body = library.getFromExcelRowAndColumn(1, "Body");
        String accessTokenURL = library.getFromExcelRowAndColumn(1, "Access Token URL");
        library.closeExcelSheet(path, "APILoginTest", "read");
        Map<String, Object> map = new HashMap<>();

        synchronized (AgentTestDataProvider.class) {
            String jsonFilePath = notificationFilePath + "/" + notificationFileName + ".json";
            Map<String, Object> jsonData = getJsonAsMap(jsonFilePath);
            map.put("ContentType", contentType);
            map.put("AccessTokenURL", accessTokenURL);
            map.put("Body", body);
            map.put("NotificationData", jsonData);
            map.put("clientName", clientName);
            map.put("clientLeadApi", clientLeadApi);
            map.put("notesApi", notesApi);
            map.put("propertiesApi", propertiesApi);
            Object[][] obj = { { map } };
            return obj;
        }
    }
 	//Puneet date: - 10/May/2016
 		@DataProvider(name = "SmokeTestTransactionUpdate")
 		public static Object[][] TransactionStageDataProvider(ITestContext context, Method method) throws Exception {
 			FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
 			library.setCurrentTestMethod(method.getName());
 			String testName = context.getName();
 			String path = "data/" + testName + ".xlsx";
 			library.openExcelSheet(path, "UpdateTransaction", "read");
 			int rowCount = library.getExcelRowCount();
 			library.closeExcelSheet(path, "UpdateTransaction", "read");
 			Object[][] data = new Object[rowCount-1][1];
 			for (int i = 0; i < rowCount-1; i++) {
 				int j = i + 1;
 				library.openExcelSheet(path, "UpdateTransaction", "read");
 				String clientName = library.getFromExcelRowAndColumn(j, "ClientName");
 				String presentStage = library.getFromExcelRowAndColumn(j, "PresentStage");
 				String postStage = library.getFromExcelRowAndColumn(j, "PostStage");
 				String updateTransactionStage = library.getFromExcelRowAndColumn(j, "UpdateTransactionStage");
 				library.closeExcelSheet(path, "UpdateTransaction", "read");
 				
 				Map<String, Object> map = new HashMap<>();
 				
 				
 				library.openExcelSheet(path, "apitestdata", "read");
 				String loginurl = library.getFromExcelRowAndColumn(1, "LoginUrl");
 				String leadDetailsUrl = library.getFromExcelRowAndColumn(1, "LeadDetailsUrl");
 				
 				String loginDataFileName = library.getFromExcelRowAndColumn(1, "Login File");
 				String loginDataFilePath = library.getFromExcelRowAndColumn(1, "Login Data Path");
 				String notesApiUrl = library.getFromExcelRowAndColumn(1, "Notes Api");
 				
 	               synchronized (AgentTestDataProvider.class) {
 					
 					Map<String, Object> apiData = new HashMap<>();
 					String jsonFilePath = loginDataFilePath + "/" + loginDataFileName + ".json";
 					Map<String, Object> jsonData = getJsonAsMap(jsonFilePath);
 					apiData.put("ContentType", "application/json");
 					apiData.put("AccessTokenURL", loginurl);
 					apiData.put("NotesApiUrl", notesApiUrl);
 					//map.put("Body",body);
 					apiData.put("LoginData", jsonData);
 					map.put("apiData", apiData);

 				}
 	               library.closeExcelSheet(path, "apitestdata", "read");
 	               
 	               Map<String, String> transactionData = new HashMap<>();

 	               transactionData.put("ClientName", clientName);
 	               transactionData.put("presentStage", presentStage);
 	               transactionData.put("updateTransactionStage", updateTransactionStage);
 	               transactionData.put("postStage", postStage);
 	               map.put("transactionData",transactionData);
 				try {
 					data[i][0] = map;
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			}

 			return data;

 		}	
 		
 		//Puneet date: - 11/May/2016
 		@DataProvider(name = "SmokeTestDataForReturnToMovoto")
 		public static Object[][] TransactionStageReturnToMovotoDataProvider(ITestContext context, Method method) throws Exception {
 			FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
 			library.setCurrentTestMethod(method.getName());
 			String testName = context.getName();
 			String path = "data/" + testName + ".xlsx";
 			library.openExcelSheet(path, "ReturnToMovoto", "read");
 			int rowCount = library.getExcelRowCount();
 			library.closeExcelSheet(path, "ReturnToMovoto", "read");
 			Object[][] data = new Object[rowCount-1][1];
 			for (int i = 0; i < rowCount-1; i++) {
 				int j = i + 1;
 				library.openExcelSheet(path, "ReturnToMovoto", "read");
 				String clientName = library.getFromExcelRowAndColumn(j, "ClientName");
 				String presentStage = library.getFromExcelRowAndColumn(j, "PresentStage");
 				String postStage = library.getFromExcelRowAndColumn(j, "PostStage");
 				String updateTransactionStage = library.getFromExcelRowAndColumn(j, "UpdateTransactionStage");
 				String returnReason = library.getFromExcelRowAndColumn(j, "Return Reason");
 				library.closeExcelSheet(path, "ReturnToMovoto", "read");
 				
 				Map<String, Object> map = new HashMap<>();
 				
 				
 				library.openExcelSheet(path, "apitestdata", "read");
 				String loginurl = library.getFromExcelRowAndColumn(1, "LoginUrl");
 				String leadDetailsUrl = library.getFromExcelRowAndColumn(1, "LeadDetailsUrl");
 				
 				String loginDataFileName = library.getFromExcelRowAndColumn(1, "Login File");
 				String loginDataFilePath = library.getFromExcelRowAndColumn(1, "Login Data Path");
 				String notesApiUrl = library.getFromExcelRowAndColumn(1, "Notes Api");
 				
 	               synchronized (AgentTestDataProvider.class) {
 					
 					Map<String, Object> apiData = new HashMap<>();
 					String jsonFilePath = loginDataFilePath + "/" + loginDataFileName + ".json";
 					Map<String, Object> jsonData = getJsonAsMap(jsonFilePath);
 					apiData.put("ContentType", "application/json");
 					apiData.put("AccessTokenURL", loginurl);
 					apiData.put("NotesApiUrl", notesApiUrl);
 					//map.put("Body",body);
 					apiData.put("LoginData", jsonData);
 					map.put("apiData", apiData);

 				}
 	               library.closeExcelSheet(path, "apitestdata", "read");
 	               
 	               Map<String, String> returnToMovotoData = new HashMap<>();

 	               returnToMovotoData.put("ClientName", clientName);
 	               returnToMovotoData.put("presentStage", presentStage);
 	               returnToMovotoData.put("updateTransactionStage", updateTransactionStage);
 	               returnToMovotoData.put("postStage", postStage);
 	               returnToMovotoData.put("returnReason", returnReason);
 	               map.put("returnToMovotoData",returnToMovotoData);
 				try {
 					data[i][0] = map;
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			}

 			return data;

 		}	
 		
 		//Puneet date: - 10/May/2016
 		@DataProvider(name = "SmokeTestForScheduledMeeting")
 		public static Object[][] ScheduledMeetingDataProvider(ITestContext context, Method method) throws Exception {
 			FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
 			library.setCurrentTestMethod(method.getName());
 			String testName = context.getName();
 			String path = "data/" + testName + ".xlsx";
 			library.openExcelSheet(path, "ScheduledMeeting", "read");
 			int rowCount = library.getExcelRowCount();
 			library.closeExcelSheet(path, "ScheduledMeeting", "read");
 			Object[][] data = new Object[rowCount-1][1];
 			for (int i = 0; i < rowCount-1; i++) {
 				int j = i + 1;
 				library.openExcelSheet(path, "ScheduledMeeting", "read");
 				String clientName = library.getFromExcelRowAndColumn(j, "ClientName");
 				String presentStage = library.getFromExcelRowAndColumn(j, "PresentStage");
 				String postStage = library.getFromExcelRowAndColumn(j, "PostStage");
 				String updateTransactionStage = library.getFromExcelRowAndColumn(j, "UpdateTransactionStage");
 				library.closeExcelSheet(path, "ScheduledMeeting", "read");
 				
 				Map<String, Object> map = new HashMap<>();
 				
 				
 				library.openExcelSheet(path, "apitestdata", "read");
 				String loginurl = library.getFromExcelRowAndColumn(1, "LoginUrl");
 				String leadDetailsUrl = library.getFromExcelRowAndColumn(1, "LeadDetailsUrl");
 				
 				String loginDataFileName = library.getFromExcelRowAndColumn(1, "Login File");
 				String loginDataFilePath = library.getFromExcelRowAndColumn(1, "Login Data Path");
 				String notesApiUrl = library.getFromExcelRowAndColumn(1, "Notes Api");
 				
 	               synchronized (AgentTestDataProvider.class) {
 					
 					Map<String, Object> apiData = new HashMap<>();
 					String jsonFilePath = loginDataFilePath + "/" + loginDataFileName + ".json";
 					Map<String, Object> jsonData = getJsonAsMap(jsonFilePath);
 					apiData.put("ContentType", "application/json");
 					apiData.put("AccessTokenURL", loginurl);
 					apiData.put("NotesApiUrl", notesApiUrl);
 					//map.put("Body",body);
 					apiData.put("LoginData", jsonData);
 					map.put("apiData", apiData);

 				}
 	               library.closeExcelSheet(path, "apitestdata", "read");
 	               
 	               Map<String, String> transactionData = new HashMap<>();

 	               transactionData.put("ClientName", clientName);
 	               transactionData.put("presentStage", presentStage);
 	               transactionData.put("updateTransactionStage", updateTransactionStage);
 	               transactionData.put("postStage", postStage);
 	               map.put("transactionData",transactionData);
 				try {
 					data[i][0] = map;
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			}

 			return data;

 		}	
 		
 		//Puneet date: - 11/May/2016
 		@DataProvider(name = "SmokeTestForMadeAnOffer")
 		public static Object[][] MadeAnOfferDataProvider(ITestContext context, Method method) throws Exception {
 			FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
 			library.setCurrentTestMethod(method.getName());
 			String testName = context.getName();
 			String path = "data/" + testName + ".xlsx";
 			library.openExcelSheet(path, "MakeOffer", "read");
 			int rowCount = library.getExcelRowCount();
 			library.closeExcelSheet(path, "MakeOffer", "read");
 			Object[][] data = new Object[rowCount-1][1];
 			for (int i = 0; i < rowCount-1; i++) {
 				int j = i + 1;
 				library.openExcelSheet(path, "MakeOffer", "read");
 				String clientName = library.getFromExcelRowAndColumn(j, "ClientName");
 				String address = library.getFromExcelRowAndColumn(j, "Address");
 				String offerDate = library.getFromExcelRowAndColumn(j, "Offer Date");
 				String offerPrice = library.getFromExcelRowAndColumn(j, "Offer Price");
 				String preStage = library.getFromExcelRowAndColumn(j, "Pre-Stage");
 				String postStage = library.getFromExcelRowAndColumn(j, "Post-Stage");
 				
 				library.closeExcelSheet(path, "MakeOffer", "read");
 				
 				library.openExcelSheet(path, "MadeAnOfferStage", "read");
 				String updateTransactionStage = library.getFromExcelRowAndColumn(j, "UpdateTransactionStage");
 				library.closeExcelSheet(path, "MadeAnOfferStage", "read");
 				
 				Map<String, Object> map = new HashMap<>();
 				
 				
 				library.openExcelSheet(path, "apitestdata", "read");
 				String loginurl = library.getFromExcelRowAndColumn(1, "LoginUrl");
 				String leadDetailsUrl = library.getFromExcelRowAndColumn(1, "LeadDetailsUrl");
 				
 				String loginDataFileName = library.getFromExcelRowAndColumn(1, "Login File");
 				String loginDataFilePath = library.getFromExcelRowAndColumn(1, "Login Data Path");
 				String notesApiUrl = library.getFromExcelRowAndColumn(1, "Notes Api");
 				
 	               synchronized (AgentTestDataProvider.class) {
 					
 					Map<String, Object> apiData = new HashMap<>();
 					String jsonFilePath = loginDataFilePath + "/" + loginDataFileName + ".json";
 					Map<String, Object> jsonData = getJsonAsMap(jsonFilePath);
 					apiData.put("ContentType", "application/json");
 					apiData.put("AccessTokenURL", loginurl);
 					apiData.put("NotesApiUrl", notesApiUrl);
 					//map.put("Body",body);
 					apiData.put("LoginData", jsonData);
 					map.put("apiData", apiData);

 				}
 	               library.closeExcelSheet(path, "apitestdata", "read");
 	               
 	               Map<String, String> madeAnOfferData = new HashMap<>();

 	               madeAnOfferData.put("ClientName", clientName);
 	               madeAnOfferData.put("Address", address);
 	               madeAnOfferData.put("OfferDate", offerDate);
 	               madeAnOfferData.put("OfferPrice", offerPrice);
 	               madeAnOfferData.put("preStage", preStage);
 	               madeAnOfferData.put("postStage", postStage);
 	               madeAnOfferData.put("updateTransactionStage", updateTransactionStage);
 	               map.put("MadeOfferData",madeAnOfferData);
 				try {
 					data[i][0] = map;
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			}

 			return data;

 		}
 		
 		//Puneet date: - 10/May/2016
 		@DataProvider(name = "SmokeTestDataForUrgency")
 		public static Object[][] SmokeUrgencyStageProvider(ITestContext context, Method method) throws Exception {
 			FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
 			library.setCurrentTestMethod(method.getName());
 			String testName = context.getName();
 			String path = "data/" + testName + ".xlsx";
 			library.openExcelSheet(path, "SmokeUrgencyTest", "read");
 			int rowCount = library.getExcelRowCount();
 			library.closeExcelSheet(path, "SmokeUrgencyTest", "read");
 			Object[][] data = new Object[rowCount-1][1];
 			for (int i = 0; i < rowCount - 1; i++) {
 				int j = i + 1;
 				
 				library.openExcelSheet(path, "SmokeUrgencyTest", "read");
 				
 				
 				String urgencyCode = library.getFromExcelRowAndColumn(j, "UrgencyCode");
 				String urgencyName = library.getFromExcelRowAndColumn(j, "UrgencyName");
 				String clientName = library.getFromExcelRowAndColumn(j, "ClientName");
 				library.closeExcelSheet(path, "SmokeUrgencyTest", "read");

 				library.openExcelSheet(path, "apitestdata", "read");
 				String loginurl = library.getFromExcelRowAndColumn(1, "LoginUrl");
 				String leadDetailsUrl = library.getFromExcelRowAndColumn(1, "LeadDetailsUrl");
 				
 				String loginDataFileName = library.getFromExcelRowAndColumn(1, "Login File");
 				String loginDataFilePath = library.getFromExcelRowAndColumn(1, "Login Data Path");
 				Map<String, Object> map = new HashMap<>();
 				
 				synchronized (AgentTestDataProvider.class) {
 					
 					Map<String, Object> apiData = new HashMap<>();
 					String jsonFilePath = loginDataFilePath + "/" + loginDataFileName + ".json";
 					Map<String, Object> jsonData = getJsonAsMap(jsonFilePath);
 					apiData.put("ContentType", "application/json");
 					apiData.put("AccessTokenURL", loginurl);
 					apiData.put("LeadDetailsUrl", leadDetailsUrl);
 					//map.put("Body",body);
 					apiData.put("LoginData", jsonData);
 					map.put("apiData", apiData);

 				}
 				
 				library.closeExcelSheet(path, "apitestdata", "read");
 				
 				Map<String, String> urgencyData = new HashMap<>();
 				
 				urgencyData.put("urgencyCode", urgencyCode);
 				urgencyData.put("urgencyName", urgencyName);
 				urgencyData.put("ClientName", clientName);
 				
 				map.put("urgencyData", urgencyData);
 				try {
 					data[i][0] = map;
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}

 			}

 			library.closeExcelSheet(path, "SmokeUrgencyTest", "read");

 			return data;
 		}
 		
 	// Updated By Puneet dated : - 23-may-16
 	 		@DataProvider(name = "SmokeTestTransactionUpdateForContractCancel")
 	 		public static Object[][] TransactionStageDataProviderForContractCancel(ITestContext context, Method method) throws Exception {
 	 			FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
 	 			library.setCurrentTestMethod(method.getName());
 	 			String testName = context.getName();
 	 			String path = "data/" + testName + ".xlsx";
 	 			library.openExcelSheet(path, "UpdateTransactionForCancel", "read");
 	 			int rowCount = library.getExcelRowCount();
 	 			library.closeExcelSheet(path, "UpdateTransactionForCancel", "read");
 	 			Object[][] data = new Object[rowCount-1][1];
 	 			for (int i = 0; i < rowCount-1; i++) {
 	 				int j = i + 1;
 	 				library.openExcelSheet(path, "UpdateTransactionForCancel", "read");
 	 				String clientName = library.getFromExcelRowAndColumn(j, "ClientName");
 	 				String presentStage = library.getFromExcelRowAndColumn(j, "PresentStage");
 	 				String postStage = library.getFromExcelRowAndColumn(j, "PostStage");
 	 				String updateTransactionStage = library.getFromExcelRowAndColumn(j, "UpdateTransactionStage");
 	 				library.closeExcelSheet(path, "UpdateTransactionForCancel", "read");
 	 				
 	 				Map<String, Object> map = new HashMap<>();
 	 				
 	 				
 	 				library.openExcelSheet(path, "apitestdata", "read");
 	 				String loginurl = library.getFromExcelRowAndColumn(1, "LoginUrl");
 	 				String leadDetailsUrl = library.getFromExcelRowAndColumn(1, "LeadDetailsUrl");
 	 				
 	 				String loginDataFileName = library.getFromExcelRowAndColumn(1, "Login File");
 	 				String loginDataFilePath = library.getFromExcelRowAndColumn(1, "Login Data Path");
 	 				String notesApiUrl = library.getFromExcelRowAndColumn(1, "Notes Api");
 	 				
 	 	               synchronized (AgentTestDataProvider.class) {
 	 					
 	 					Map<String, Object> apiData = new HashMap<>();
 	 					String jsonFilePath = loginDataFilePath + "/" + loginDataFileName + ".json";
 	 					Map<String, Object> jsonData = getJsonAsMap(jsonFilePath);
 	 					apiData.put("ContentType", "application/json");
 	 					apiData.put("AccessTokenURL", loginurl);
 	 					apiData.put("NotesApiUrl", notesApiUrl);
 	 					//map.put("Body",body);
 	 					apiData.put("LoginData", jsonData);
 	 					map.put("apiData", apiData);

 	 				}
 	 	               library.closeExcelSheet(path, "apitestdata", "read");
 	 	               
 	 	               Map<String, String> transactionData = new HashMap<>();

 	 	               transactionData.put("ClientName", clientName);
 	 	               transactionData.put("presentStage", presentStage);
 	 	               transactionData.put("updateTransactionStage", updateTransactionStage);
 	 	               transactionData.put("postStage", postStage);
 	 	               map.put("transactionData",transactionData);
 	 				try {
 	 					data[i][0] = map;
 	 				} catch (Exception e) {
 	 					// TODO Auto-generated catch block
 	 					e.printStackTrace();
 	 				}
 	 			}

 	 			return data;

 	 		}	
 	 		
 	 	//Created by akash
 	 		@DataProvider(name="APIgetLeave")
 			  public static Object[][] APILoginTestDataProvider1(ITestContext context, Method method) throws Exception 
 			{
 	 			FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
 	 		   library.setCurrentTestMethod(method.getName());
 	 		   //String testName = context.getName();
 	 		   String path = "data/Leavedate.xlsx";
 	 		   library.openExcelSheet(path, "Future Leaves", "read");
 	 		   String contentType = library.getFromExcelRowAndColumn(1, "ContentType");
 	 		   String accessTokenURL = library.getFromExcelRowAndColumn(1, "AccessTokenURL");
 	 		   String contactsUrl = library.getFromExcelRowAndColumn(1, "ContactsUrl");
 	 		   String notificationFilePath = library.getFromExcelRowAndColumn(1, "Notification Data Path");
 	 		   String notificationFileName = library.getFromExcelRowAndColumn(1, "Notification");
 	 		   library.closeExcelSheet(path, "Future Leaves", "read");
 	 		   Map<String, Object> map = new HashMap<>();
 	 		   
 	 		   synchronized (AgentTestDataProvider.class) 
 	 		   {
 	 		   String jsonFilePath = "data/notifications/LoginDataLeave.json";
 	 		   Map<String, Object> jsonData = getJsonAsMap(jsonFilePath);
 	 		   map.put("ContentType", contentType);
 	 		   map.put("AccessTokenURL", accessTokenURL);
 	 		   map.put("ContactsUrl", contactsUrl);
 	 		   map.put("NotificationData", jsonData);		    
 	 		   map.put("LoginDataLeave", jsonData);
 	 		   Object[][] obj = { { map } };
 	 		   return obj;
 	 		  }
 	 		 }

 	 	
 		
 		//Puneet date: - 11/May/2016
 		
 		@DataProvider(name = "SmokeTestDataForContractAccept")
 		public static Object[][] SmokeContarctAcceptStageProvider(ITestContext context, Method method) throws Exception {
 			FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
 			library.setCurrentTestMethod(method.getName());
 			String testName = context.getName();
 			String path = "data/" + testName + ".xlsx";
 			library.openExcelSheet(path, "Accept Contract", "read");
 			int rowCount = library.getExcelRowCount();
 			library.closeExcelSheet(path, "Accept Contract", "read");
 			Object[][] data = new Object[rowCount-1][1];
 			for (int i = 0; i < rowCount - 1; i++) {
 				int j = i + 1;
 				
 				library.openExcelSheet(path, "Accept Contract", "read");
 				
 				
 				String clientName = library.getFromExcelRowAndColumn(j, "ClientName");
 				String contractDate = library.getFromExcelRowAndColumn(j, "Contract Date");
 				String contractPrice = library.getFromExcelRowAndColumn(j, "Contract Price");
 				String commission = library.getFromExcelRowAndColumn(j, "Commission");
 				String contractFname = library.getFromExcelRowAndColumn(j, "Contract First Name");
 				String contractLname = library.getFromExcelRowAndColumn(j, "Contract Last Name");
 				String phone = library.getFromExcelRowAndColumn(j, "Phone");
 				String email = library.getFromExcelRowAndColumn(j, "Email");
 				String preStage = library.getFromExcelRowAndColumn(j, "Pre-Stage");
 				String postStage = library.getFromExcelRowAndColumn(j, "Post-Stage");
 				String updateTransactionStage = library.getFromExcelRowAndColumn(j, "UpdateTransactionStage");

 				library.closeExcelSheet(path, "Accept Contract", "read");

 				library.openExcelSheet(path, "apitestdata", "read");
 				String loginurl = library.getFromExcelRowAndColumn(1, "LoginUrl");
 				String leadDetailsUrl = library.getFromExcelRowAndColumn(1, "LeadDetailsUrl");
 				
 				String loginDataFileName = library.getFromExcelRowAndColumn(1, "Login File");
 				String loginDataFilePath = library.getFromExcelRowAndColumn(1, "Login Data Path");
 				
 				Map<String, Object> map = new HashMap<>();
 				
 				synchronized (AgentTestDataProvider.class) {
 					
 					Map<String, Object> apiData = new HashMap<>();
 					String jsonFilePath = loginDataFilePath + "/" + loginDataFileName + ".json";
 					Map<String, Object> jsonData = getJsonAsMap(jsonFilePath);
 					apiData.put("ContentType", "application/json");
 					apiData.put("AccessTokenURL", loginurl);
 					apiData.put("LeadDetailsUrl", leadDetailsUrl);
 					//map.put("Body",body);
 					apiData.put("LoginData", jsonData);
 					map.put("apiData", apiData);

 				}
 				
 				library.closeExcelSheet(path, "apitestdata", "read");
 				
 				Map<String, String> contractAcceptData = new HashMap<>();
 				
 				contractAcceptData.put("ClientName", clientName);
 				contractAcceptData.put("contractDate", contractDate);
 				contractAcceptData.put("contractPrice", contractPrice);
 				contractAcceptData.put("commission", commission);
 				contractAcceptData.put("contractFname", contractFname);
 				contractAcceptData.put("contractLname", contractLname);
 				contractAcceptData.put("phone", phone);
 				contractAcceptData.put("email", email);
 				contractAcceptData.put("preStage", preStage);
 				contractAcceptData.put("postStage", postStage);
 				contractAcceptData.put("updateTransactionStage",updateTransactionStage);
 				
 				map.put("contractAcceptData", contractAcceptData);
 				try {
 					data[i][0] = map;
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}

 			}

 			library.closeExcelSheet(path, "SmokeUrgencyTest", "read");

 			return data;
 		}
 		
 		//Puneet date: - 10/May/2016
 		@DataProvider(name = "SmokeTestTransactionUpdateForWeb")
 		public static Object[][] TransactionStageDataProviderForWeb(ITestContext context, Method method) throws Exception {
 			FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
 			library.setCurrentTestMethod(method.getName());
 			String testName = context.getName();
 			String path = "data/" + testName + ".xlsx";
 			library.openExcelSheet(path, "UpdateTransaction", "read");
 			int rowCount = library.getExcelRowCount();
 			library.closeExcelSheet(path, "UpdateTransaction", "read");
 			Object[][] data = new Object[rowCount-1][1];
 			for (int i = 0; i < rowCount-1; i++) {
 				int j = i + 1;
 				library.openExcelSheet(path, "UpdateTransaction", "read");
 				String clientName = library.getFromExcelRowAndColumn(j, "ClientName");
 				String presentStage = library.getFromExcelRowAndColumn(j, "PresentStage");
 				String postStage = library.getFromExcelRowAndColumn(j, "PostStage");
 				String updateTransactionStage = library.getFromExcelRowAndColumn(j, "UpdateTransactionStage");
 				library.closeExcelSheet(path, "UpdateTransaction", "read");
 				
 				Map<String, Object> map = new HashMap<>();
 				
 				
 				library.openExcelSheet(path, "apitestdata", "read");
 				String loginurl = library.getFromExcelRowAndColumn(1, "LoginUrl");
 				String leadDetailsUrl = library.getFromExcelRowAndColumn(1, "LeadDetailsUrl");
 				
 				String loginDataFileName = library.getFromExcelRowAndColumn(1, "Login File");
 				String loginDataFilePath = library.getFromExcelRowAndColumn(1, "Login Data Path");
 				//String notesApiUrl = library.getFromExcelRowAndColumn(1, "Notes Api");
 				
 	               synchronized (AgentTestDataProvider.class) {
 					
 					Map<String, Object> apiData = new HashMap<>();
 					String jsonFilePath = loginDataFilePath + "/" + loginDataFileName + ".json";
 					Map<String, Object> jsonData = getJsonAsMap(jsonFilePath);
 					apiData.put("ContentType", "application/json");
 					apiData.put("AccessTokenURL", loginurl);
 					apiData.put("LeadDetailsUrl", leadDetailsUrl);
 					//map.put("Body",body);
 					apiData.put("LoginData", jsonData);
 					map.put("apiData", apiData);

 				}
 	               library.closeExcelSheet(path, "apitestdata", "read");
 	               
 	               Map<String, String> transactionData = new HashMap<>();

 	               transactionData.put("ClientName", clientName);
 	               transactionData.put("presentStage", presentStage);
 	               transactionData.put("updateTransactionStage", updateTransactionStage);
 	               transactionData.put("postStage", postStage);
 	               map.put("transactionData",transactionData);
 				try {
 					data[i][0] = map;
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			}

 			return data;

 		}	

 		//25May2016
 		//Puneet date: - 10/May/2016
 		@DataProvider(name = "SmokeTestForScheduledMeetingForWeb")
 		public static Object[][] ScheduledMeetingDataProviderForWeb(ITestContext context, Method method) throws Exception {
 			FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
 			library.setCurrentTestMethod(method.getName());
 			String testName = context.getName();
 			String path = "data/" + testName + ".xlsx";
 			library.openExcelSheet(path, "ScheduledMeeting", "read");
 			int rowCount = library.getExcelRowCount();
 			library.closeExcelSheet(path, "ScheduledMeeting", "read");
 			Object[][] data = new Object[rowCount-1][1];
 			for (int i = 0; i < rowCount-1; i++) {
 				int j = i + 1;
 				library.openExcelSheet(path, "ScheduledMeeting", "read");
 				String clientName = library.getFromExcelRowAndColumn(j, "ClientName");
 				String presentStage = library.getFromExcelRowAndColumn(j, "PresentStage");
 				String postStage = library.getFromExcelRowAndColumn(j, "PostStage");
 				String updateTransactionStage = library.getFromExcelRowAndColumn(j, "UpdateTransactionStage");
 				library.closeExcelSheet(path, "ScheduledMeeting", "read");
 				
 				Map<String, Object> map = new HashMap<>();
 				
 				
 				library.openExcelSheet(path, "apitestdata", "read");
 				String loginurl = library.getFromExcelRowAndColumn(1, "LoginUrl");
 				String leadDetailsUrl = library.getFromExcelRowAndColumn(1, "LeadDetailsUrl");
 				
 				String loginDataFileName = library.getFromExcelRowAndColumn(1, "Login File");
 				String loginDataFilePath = library.getFromExcelRowAndColumn(1, "Login Data Path");
 				String notesApiUrl = library.getFromExcelRowAndColumn(1, "Notes Api");
 				
 	               synchronized (AgentTestDataProvider.class) {
 					
 					Map<String, Object> apiData = new HashMap<>();
 					String jsonFilePath = loginDataFilePath + "/" + loginDataFileName + ".json";
 					Map<String, Object> jsonData = getJsonAsMap(jsonFilePath);
 					apiData.put("ContentType", "application/json");
 				      apiData.put("AccessTokenURL", loginurl);
 				      apiData.put("LeadDetailsUrl", leadDetailsUrl);
 					//map.put("Body",body);
 					apiData.put("LoginData", jsonData);
 					map.put("apiData", apiData);

 				}
 	               library.closeExcelSheet(path, "apitestdata", "read");
 	               
 	               Map<String, String> transactionData = new HashMap<>();

 	               transactionData.put("ClientName", clientName);
 	               transactionData.put("presentStage", presentStage);
 	               transactionData.put("updateTransactionStage", updateTransactionStage);
 	               transactionData.put("postStage", postStage);
 	               map.put("transactionData",transactionData);
 				try {
 					data[i][0] = map;
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			}

 			return data;

 		}	
 		
 		
 		//Puneet date: - 25May16
 		@DataProvider(name = "SmokeTestForMadeAnOfferForWeb")
 		public static Object[][] MadeAnOfferDataProviderForWeb(ITestContext context, Method method) throws Exception {
 			FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
 			library.setCurrentTestMethod(method.getName());
 			String testName = context.getName();
 			String path = "data/" + testName + ".xlsx";
 			library.openExcelSheet(path, "MakeOffer", "read");
 			int rowCount = library.getExcelRowCount();
 			library.closeExcelSheet(path, "MakeOffer", "read");
 			Object[][] data = new Object[rowCount-1][1];
 			for (int i = 0; i < rowCount-1; i++) {
 				int j = i + 1;
 				library.openExcelSheet(path, "MakeOffer", "read");
 				String clientName = library.getFromExcelRowAndColumn(j, "ClientName");
 				String address = library.getFromExcelRowAndColumn(j, "Address");
 				String offerDate = library.getFromExcelRowAndColumn(j, "Offer Date");
 				String offerPrice = library.getFromExcelRowAndColumn(j, "Offer Price");
 				String preStage = library.getFromExcelRowAndColumn(j, "Pre-Stage");
 				String postStage = library.getFromExcelRowAndColumn(j, "Post-Stage");
 				
 				library.closeExcelSheet(path, "MakeOffer", "read");
 				
 				library.openExcelSheet(path, "MadeAnOfferStage", "read");
 				String updateTransactionStage = library.getFromExcelRowAndColumn(j, "UpdateTransactionStage");
 				library.closeExcelSheet(path, "MadeAnOfferStage", "read");
 				
 				Map<String, Object> map = new HashMap<>();
 				
 				
 				library.openExcelSheet(path, "apitestdata", "read");
 				String loginurl = library.getFromExcelRowAndColumn(1, "LoginUrl");
 				String leadDetailsUrl = library.getFromExcelRowAndColumn(1, "LeadDetailsUrl");
 				
 				String loginDataFileName = library.getFromExcelRowAndColumn(1, "Login File");
 				String loginDataFilePath = library.getFromExcelRowAndColumn(1, "Login Data Path");
 				//String notesApiUrl = library.getFromExcelRowAndColumn(1, "Notes Api");
 				
 	               synchronized (AgentTestDataProvider.class) {
 					
 					Map<String, Object> apiData = new HashMap<>();
 					String jsonFilePath = loginDataFilePath + "/" + loginDataFileName + ".json";
 					Map<String, Object> jsonData = getJsonAsMap(jsonFilePath);
 					apiData.put("ContentType", "application/json");
 				      apiData.put("AccessTokenURL", loginurl);
 				      apiData.put("LeadDetailsUrl", leadDetailsUrl);
 					//map.put("Body",body);
 					apiData.put("LoginData", jsonData);
 					map.put("apiData", apiData);

 				}
 	               library.closeExcelSheet(path, "apitestdata", "read");
 	               
 	               Map<String, String> madeAnOfferData = new HashMap<>();

 	               madeAnOfferData.put("ClientName", clientName);
 	               madeAnOfferData.put("Address", address);
 	               madeAnOfferData.put("OfferDate", offerDate);
 	               madeAnOfferData.put("OfferPrice", offerPrice);
 	               madeAnOfferData.put("preStage", preStage);
 	               madeAnOfferData.put("postStage", postStage);
 	               madeAnOfferData.put("updateTransactionStage", updateTransactionStage);
 	               map.put("MadeOfferData",madeAnOfferData);
 				try {
 					data[i][0] = map;
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			}

 			return data;

 		}
 		
 	// Updated By Puneet dated : - 25-may-16
 	 		@DataProvider(name = "SmokeTestTransactionUpdateForContractCancelForWeb")
 	 		public static Object[][] TransactionStageDataProviderForContractCancelForWeb(ITestContext context, Method method) throws Exception {
 	 			FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
 	 			library.setCurrentTestMethod(method.getName());
 	 			String testName = context.getName();
 	 			String path = "data/" + testName + ".xlsx";
 	 			library.openExcelSheet(path, "UpdateTransactionForCancel", "read");
 	 			int rowCount = library.getExcelRowCount();
 	 			library.closeExcelSheet(path, "UpdateTransactionForCancel", "read");
 	 			Object[][] data = new Object[rowCount-1][1];
 	 			for (int i = 0; i < rowCount-1; i++) {
 	 				int j = i + 1;
 	 				library.openExcelSheet(path, "UpdateTransactionForCancel", "read");
 	 				String clientName = library.getFromExcelRowAndColumn(j, "ClientName");
 	 				String presentStage = library.getFromExcelRowAndColumn(j, "PresentStage");
 	 				String postStage = library.getFromExcelRowAndColumn(j, "PostStage");
 	 				String updateTransactionStage = library.getFromExcelRowAndColumn(j, "UpdateTransactionStage");
 	 				library.closeExcelSheet(path, "UpdateTransactionForCancel", "read");
 	 				
 	 				Map<String, Object> map = new HashMap<>();
 	 				
 	 				
 	 				library.openExcelSheet(path, "apitestdata", "read");
 	 				String loginurl = library.getFromExcelRowAndColumn(1, "LoginUrl");
 	 				String leadDetailsUrl = library.getFromExcelRowAndColumn(1, "LeadDetailsUrl");
 	 				
 	 				String loginDataFileName = library.getFromExcelRowAndColumn(1, "Login File");
 	 				String loginDataFilePath = library.getFromExcelRowAndColumn(1, "Login Data Path");
 	 				String notesApiUrl = library.getFromExcelRowAndColumn(1, "Notes Api");
 	 				
 	 	               synchronized (AgentTestDataProvider.class) {
 	 					
 	 					Map<String, Object> apiData = new HashMap<>();
 	 					String jsonFilePath = loginDataFilePath + "/" + loginDataFileName + ".json";
 	 					Map<String, Object> jsonData = getJsonAsMap(jsonFilePath);
 	 					apiData.put("ContentType", "application/json");
 	 				      apiData.put("AccessTokenURL", loginurl);
 	 				      apiData.put("LeadDetailsUrl", leadDetailsUrl);

 	 					//map.put("Body",body);
 	 					apiData.put("LoginData", jsonData);
 	 					map.put("apiData", apiData);

 	 				}
 	 	               library.closeExcelSheet(path, "apitestdata", "read");
 	 	               
 	 	               Map<String, String> transactionData = new HashMap<>();

 	 	               transactionData.put("ClientName", clientName);
 	 	               transactionData.put("presentStage", presentStage);
 	 	               transactionData.put("updateTransactionStage", updateTransactionStage);
 	 	               transactionData.put("postStage", postStage);
 	 	               map.put("transactionData",transactionData);
 	 				try {
 	 					data[i][0] = map;
 	 				} catch (Exception e) {
 	 					// TODO Auto-generated catch block
 	 					e.printStackTrace();
 	 				}
 	 			}

 	 			return data;

 	 		}	
 	 	
 	 	//Puneet date: - 25/May/2016
 	 		
 	 		@DataProvider(name = "SmokeTestDataForContractAcceptForWeb")
 	 		public static Object[][] SmokeContarctAcceptStageProviderForWeb(ITestContext context, Method method) throws Exception {
 	 			FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
 	 			library.setCurrentTestMethod(method.getName());
 	 			String testName = context.getName();
 	 			String path = "data/" + testName + ".xlsx";
 	 			library.openExcelSheet(path, "Accept Contract", "read");
 	 			int rowCount = library.getExcelRowCount();
 	 			library.closeExcelSheet(path, "Accept Contract", "read");
 	 			Object[][] data = new Object[rowCount-1][1];
 	 			for (int i = 0; i < rowCount - 1; i++) {
 	 				int j = i + 1;
 	 				
 	 				library.openExcelSheet(path, "Accept Contract", "read");
 	 				
 	 				
 	 				String clientName = library.getFromExcelRowAndColumn(j, "ClientName");
 	 				String contractDate = library.getFromExcelRowAndColumn(j, "Contract Date");
 	 				String contractPrice = library.getFromExcelRowAndColumn(j, "Contract Price");
 	 				String commission = library.getFromExcelRowAndColumn(j, "Commission");
 	 				String contractFname = library.getFromExcelRowAndColumn(j, "Contract First Name");
 	 				String contractLname = library.getFromExcelRowAndColumn(j, "Contract Last Name");
 	 				String phone = library.getFromExcelRowAndColumn(j, "Phone");
 	 				String email = library.getFromExcelRowAndColumn(j, "Email");
 	 				String preStage = library.getFromExcelRowAndColumn(j, "Pre-Stage");
 	 				String postStage = library.getFromExcelRowAndColumn(j, "Post-Stage");
 	 				String updateTransactionStage = library.getFromExcelRowAndColumn(j, "UpdateTransactionStage");

 	 				library.closeExcelSheet(path, "Accept Contract", "read");

 	 				library.openExcelSheet(path, "apitestdata", "read");
 	 				String loginurl = library.getFromExcelRowAndColumn(1, "LoginUrl");
 	 				String leadDetailsUrl = library.getFromExcelRowAndColumn(1, "LeadDetailsUrl");
 	 				
 	 				String loginDataFileName = library.getFromExcelRowAndColumn(1, "Login File");
 	 				String loginDataFilePath = library.getFromExcelRowAndColumn(1, "Login Data Path");
 	 				String notesApiUrl = library.getFromExcelRowAndColumn(1, "Notes Api");
 	 				
 	 				Map<String, Object> map = new HashMap<>();
 	 				
 	 				synchronized (AgentTestDataProvider.class) {
 	 					
 	 					Map<String, Object> apiData = new HashMap<>();
 	 					String jsonFilePath = loginDataFilePath + "/" + loginDataFileName + ".json";
 	 					Map<String, Object> jsonData = getJsonAsMap(jsonFilePath);
 	 					apiData.put("ContentType", "application/json");
 	 		           apiData.put("AccessTokenURL", loginurl);
 	 		           apiData.put("LeadDetailsUrl", leadDetailsUrl); 					//map.put("Body",body);
 	 					apiData.put("LoginData", jsonData);
 	 					map.put("apiData", apiData);

 	 				}
 	 				
 	 				library.closeExcelSheet(path, "apitestdata", "read");
 	 				
 	 				Map<String, String> contractAcceptData = new HashMap<>();
 	 				
 	 				contractAcceptData.put("ClientName", clientName);
 	 				contractAcceptData.put("contractDate", contractDate);
 	 				contractAcceptData.put("contractPrice", contractPrice);
 	 				contractAcceptData.put("commission", commission);
 	 				contractAcceptData.put("contractFname", contractFname);
 	 				contractAcceptData.put("contractLname", contractLname);
 	 				contractAcceptData.put("phone", phone);
 	 				contractAcceptData.put("email", email);
 	 				contractAcceptData.put("preStage", preStage);
 	 				contractAcceptData.put("postStage", postStage);
 	 				contractAcceptData.put("updateTransactionStage",updateTransactionStage);
 	 				
 	 				map.put("contractAcceptData", contractAcceptData);
 	 				try {
 	 					data[i][0] = map;
 	 				} catch (Exception e) {
 	 					// TODO Auto-generated catch block
 	 					e.printStackTrace();
 	 				}

 	 			}

 	 			library.closeExcelSheet(path, "SmokeUrgencyTest", "read");

 	 			return data;
 	 		}
 	 		
 	 		
 	 	
 	 		//Puneet date: - 17/06/2016
 	 		// This is to provide data for lead creation in Salesforce like lead creation URL and JSON path.
 	 		@DataProvider(name = "LeadCreationData")
 	 		public static Object[][] LeadCreationDataProvider(ITestContext context, Method method) throws Exception {
 	 			FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
 	 			library.setCurrentTestMethod(method.getName());
 	 			String testName = context.getName();
 	 			String path = "data/" + testName + ".xlsx";
 	 			library.openExcelSheet(path, "LeadCreationData", "read");
 	 			int rowCount = library.getExcelRowCount();
 	 			library.closeExcelSheet(path, "LeadCreationData", "read");
 	 			Object[][] data = new Object[rowCount-1][1];
 	 			for (int i = 0; i < rowCount-1; i++) {
 	 				int j = i + 1;
 	 				library.openExcelSheet(path, "LeadCreationData", "read");
 	 				String leadCreationUrl = library.getFromExcelRowAndColumn(j, "LeadCreationUrl");
 	 				String leadDataPath = library.getFromExcelRowAndColumn(j, "LeadDataPath");
 	 				String jsonFileName = library.getFromExcelRowAndColumn(j, "JsonFileName");
 	 				
 	 				library.closeExcelSheet(path, "LeadCreationData", "read");
 	 		
 	 	               
 	 					
 	 	            	Map<String, Object> map = new HashMap<>();
 	 					Map<String, Object> leadCreationData = new HashMap<>();
 	 					String jsonFilePath = leadDataPath + "/" + jsonFileName + ".json";
 	 					Map<String, Object> jsonData = getJsonAsMap(jsonFilePath);
 	 					leadCreationData.put("JsonFileName", jsonFileName);
 	 					leadCreationData.put("LeadCreationUrl", leadCreationUrl);
 	 					leadCreationData.put("LeadDataPath", leadDataPath);
 	 					
 	 					map.put("JsonFile", jsonData);
 	 					map.put("leadCreationData", leadCreationData);

 	 				
 	 	               
 	 				try {
 	 					data[i][0] = map;
 	 				} catch (Exception e) {
 	 					// TODO Auto-generated catch block
 	 					e.printStackTrace();
 	 				}
 	 			}

 	 			return data;

 	 		}
 	 		
 	 	//Puneet date: - 10/May/2016
 	 		@DataProvider(name = "SmokeTestForScheduledCallbackForWeb")
 	 		public static Object[][] ScheduledCallBackForWeb(ITestContext context, Method method) throws Exception {
 	 			FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
 	 			library.setCurrentTestMethod(method.getName());
 	 			String testName = context.getName();
 	 			String path = "data/" + testName + ".xlsx";
 	 			library.openExcelSheet(path, "ScheduledCallBack", "read");
 	 			int rowCount = library.getExcelRowCount();
 	 			library.closeExcelSheet(path, "ScheduledCallBack", "read");
 	 			Object[][] data = new Object[rowCount-1][1];
 	 			for (int i = 0; i < rowCount-1; i++) {
 	 				int j = i + 1;
 	 				library.openExcelSheet(path, "ScheduledCallBack", "read");
 	 				String clientName = library.getFromExcelRowAndColumn(j, "ClientName");
 	 				String presentStage = library.getFromExcelRowAndColumn(j, "PresentStage");
 	 				String postStage = library.getFromExcelRowAndColumn(j, "PostStage");
 	 				String updateTransactionStage = library.getFromExcelRowAndColumn(j, "UpdateTransactionStage");
 	 				library.closeExcelSheet(path, "ScheduledCallBack", "read");
 	 				
 	 				Map<String, Object> map = new HashMap<>();
 	 				
 	 				
 	 				library.openExcelSheet(path, "apitestdata", "read");
 	 				String loginurl = library.getFromExcelRowAndColumn(1, "LoginUrl");
 	 				String leadDetailsUrl = library.getFromExcelRowAndColumn(1, "LeadDetailsUrl");
 	 				
 	 				String loginDataFileName = library.getFromExcelRowAndColumn(1, "Login File");
 	 				String loginDataFilePath = library.getFromExcelRowAndColumn(1, "Login Data Path");
 	 				String notesApiUrl = library.getFromExcelRowAndColumn(1, "Notes Api");
 	 				
 	 	               synchronized (AgentTestDataProvider.class) {
 	 					
 	 					Map<String, Object> apiData = new HashMap<>();
 	 					String jsonFilePath = loginDataFilePath + "/" + loginDataFileName + ".json";
 	 					Map<String, Object> jsonData = getJsonAsMap(jsonFilePath);
 	 					apiData.put("ContentType", "application/json");
 	 				      apiData.put("AccessTokenURL", loginurl);
 	 				      apiData.put("LeadDetailsUrl", leadDetailsUrl);
 	 					//map.put("Body",body);
 	 					apiData.put("LoginData", jsonData);
 	 					map.put("apiData", apiData);

 	 				}
 	 	               library.closeExcelSheet(path, "apitestdata", "read");
 	 	               
 	 	               Map<String, String> transactionData = new HashMap<>();

 	 	               transactionData.put("ClientName", clientName);
 	 	               transactionData.put("presentStage", presentStage);
 	 	               transactionData.put("updateTransactionStage", updateTransactionStage);
 	 	               transactionData.put("postStage", postStage);
 	 	               map.put("transactionData",transactionData);
 	 				try {
 	 					data[i][0] = map;
 	 				} catch (Exception e) {
 	 					// TODO Auto-generated catch block
 	 					e.printStackTrace();
 	 				}
 	 			}

 	 			return data;

 	 		}	


    @DataProvider(name = "SecondFutureLeaveTestData")
    public static Object[][] SecondFutureLeaveProvider(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		// String testName = context.getName();
		// String path = "data/" + testName + ".xlsx";
		String path = "data/AgentAppTestData.xlsx";
		library.openExcelSheet(path, "VerifyFutureLeaves", "read");

		int rowCount = library.getExcelRowCount();
		Object[][] data = new Object[rowCount - 1][1];
		for (int i = 0; i < rowCount - 1; i++) {
			int j = i + 1;

			String fromDate = library.getFromExcelRowAndColumn(j, "From Date");
			String toDate = library.getFromExcelRowAndColumn(j, "To Date");

			Map<String, String> map = new HashMap<>();

			map.put("fromDate", fromDate);
			map.put("toDate", toDate);
			try {
				data[i][0] = map;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		library.closeExcelSheet(path, "Verify Future Leaves", "read");
		return data;
	}

	static final String EXTENSION = ".xlsx";



	//Priyanka:23rd June
		@DataProvider(name = "SalesforceData")
	 		public static Object[][] SalesForceData(ITestContext context, Method method) throws Exception {
	 			FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
	 			library.setCurrentTestMethod(method.getName());
	 			String path = "data/SalesforceData.xlsx";
	 			library.openExcelSheet(path, "Login", "read");
	 			int rowCount = library.getExcelRowCount();
	 			library.closeExcelSheet(path, "Login", "read");
	 			Object[][] data = new Object[rowCount-1][1];
	 			for (int i = 0; i < rowCount-1; i++) {
	 				int j = i + 1;
	 				library.openExcelSheet(path, "Login", "read");
	 				String Username = library.getFromExcelRowAndColumn(j, "Username");
	 				String Password = library.getFromExcelRowAndColumn(j, "Password");
	 				library.closeExcelSheet(path, "Login", "read");
	 				
	 				Map<String, Object> map = new HashMap<>();
	 				
	 				
	 				library.openExcelSheet(path, "Sheet1", "read");
	 				String Agent1 = library.getFromExcelRowAndColumn(1, "Agent1");
	 				String TimeZone1 = library.getFromExcelRowAndColumn(1, "TimeZone1");
	 				String Agent2 = library.getFromExcelRowAndColumn(1, "Agent2");
	 				String TimeZone2 = library.getFromExcelRowAndColumn(1, "TimeZone2");
	 				String Agent3 = library.getFromExcelRowAndColumn(1, "Agent3");
	 				String TimeZone3 = library.getFromExcelRowAndColumn(1, "TimeZone3");
	 				String Agent4 = library.getFromExcelRowAndColumn(1, "Agent4");
	 				String TimeZone4 = library.getFromExcelRowAndColumn(1, "TimeZone4");
	 	               library.closeExcelSheet(path, "apitestdata", "read");
	 	               
	 	               Map<String, String> TimeZoneData = new HashMap<>();
	 	              TimeZoneData.put("Username", Username);
	 	             TimeZoneData.put("Password", Password);
	 	            TimeZoneData.put("Agent1", Agent1);
	 	           TimeZoneData.put("TimeZone1", TimeZone1);
	 	            TimeZoneData.put("Agent2", Agent2);
	 	           TimeZoneData.put("TimeZone2", TimeZone2); 
	 	            TimeZoneData.put("Agent3", Agent3);
	 	           TimeZoneData.put("TimeZone3", TimeZone3);
	 	            TimeZoneData.put("Agent4", Agent4);
	 	           TimeZoneData.put("TimeZone4", TimeZone4); 
	 	               map.put("TimeZoneData",TimeZoneData);
	 				try {
	 					data[i][0] = map;
	 				} catch (Exception e) {
	 					// TODO Auto-generated catch block
	 					e.printStackTrace();
	 				}
	 			}

	 			return data;

	 		}
		
		@DataProvider(name = "DownloadApp")
		 public static Object[][] DownloadDataProvider2(ITestContext context, Method method) throws Exception {
		  FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		  library.setCurrentTestMethod(method.getName());
		  String testName = context.getName();
		  String path = "";
		  if (context.getName().equalsIgnoreCase("Urgency_Test_Web") || context.getName().equalsIgnoreCase("Activities_Workflow_Web")) {   
		   path = "data/" + testName + ".xlsx";
		  } else {
		   path = "data/Download.xlsx";
		  }
		  library.openExcelSheet(path, "Login", "read");
		  String name = library.getFromExcelRowAndColumn(1, "name");
		  String pass = library.getFromExcelRowAndColumn(1, "pass");
		  
		  library.closeExcelSheet(path, "Login", "read");

		  Map<String, String> map = new HashMap<>();
		  map.put("name", name);
		  map.put("pass", pass);
		  map.put("TestName", "Login");

		  Object[][] obj = { { map } };

		  return obj;
		 }
		
		// Gopal
	    @DataProvider(name = "ClientDetailsAndApi")
	    public static Object[][] ApiTestingDataProvider(ITestContext context, Method method) throws Exception {
	        FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
	        library.setCurrentTestMethod(method.getName());
	        String path = "data/AgentAppTestData.xlsx";
	        library.openExcelSheet(path, "ClientDetailsWithApi", "read");
	        String notificationFileName = library.getFromExcelRowAndColumn(1, "Notification");
	        String clientLeadApi = library.getFromExcelRowAndColumn(1, "ClientLeadAPI");
	        String clientLeadApi2= library.getFromExcelRowAndColumn(2, "ClientLeadAPI");
	        String clientLeadApi3= library.getFromExcelRowAndColumn(3, "ClientLeadAPI");
	        String clientLeadApi4= library.getFromExcelRowAndColumn(4, "ClientLeadAPI");
	        String notesApi = library.getFromExcelRowAndColumn(1, "Notes API");
	        String propertiesApi = library.getFromExcelRowAndColumn(1, "Properties API");
	        String clientName = library.getFromExcelRowAndColumn(1, "Client Name");
	        String clientName2 = library.getFromExcelRowAndColumn(2, "Client Name");
	        String clientName3 = library.getFromExcelRowAndColumn(3, "Client Name");
	        String notificationFilePath = library.getFromExcelRowAndColumn(1, "Notification Data Path");
	        String contentType = library.getFromExcelRowAndColumn(1, "Content Type");
	        String body = library.getFromExcelRowAndColumn(1, "Body");
	        String accessTokenURL = library.getFromExcelRowAndColumn(1, "Access Token URL");
	        library.closeExcelSheet(path, "APILoginTest", "read");
	        Map<String, Object> map = new HashMap<>();

	        synchronized (AgentTestDataProvider.class) {
	            String jsonFilePath = notificationFilePath + "/" + notificationFileName + ".json";
	            Map<String, Object> jsonData = getJsonAsMap(jsonFilePath);
	            map.put("ContentType", contentType);
	            map.put("AccessTokenURL", accessTokenURL);
	            map.put("Body", body);
	            map.put("NotificationData", jsonData);
	            map.put("clientName", clientName);
	            map.put("clientLeadApi", clientLeadApi);
	            map.put("notesApi", notesApi);
	            map.put("propertiesApi", propertiesApi);
	            map.put("clientName2", clientName2);
	            map.put("clientLeadApi2", clientLeadApi2);
	            map.put("clientName3", clientName3);
	            map.put("clientLeadApi3", clientLeadApi3);
	            map.put("clientLeadApi4", clientLeadApi4);
	            Object[][] obj = { { map } };
	            return obj;
	        }
	    }
	    
	    //Gopal
	@DataProvider(name = "DataForScheduleMeeting")
	public static Object[][] dataforScheduleMeeting(ITestContext context, Method method) {

		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String path = "data/AgentAppTestData.xlsx";
		library.openExcelSheet(path, "ScheduleMeetingForTalked", "read");

		String clientName = library.getFromExcelRowAndColumn(1, "Client Name");
		String notificationFileName = library.getFromExcelRowAndColumn(1, "Notification");
		String notesApi = library.getFromExcelRowAndColumn(1, "Notes API");
		String notificationFilePath = library.getFromExcelRowAndColumn(1, "Notification Data Path");
		String contentType = library.getFromExcelRowAndColumn(1, "Content Type");
		String body = library.getFromExcelRowAndColumn(1, "Body");
		String accessTokenURL = library.getFromExcelRowAndColumn(1, "Access Token URL");
		library.closeExcelSheet(path, "ScheduleMeetingForTalked", "read");
		Map<String, Object> map = new HashMap<>();

		synchronized (AgentTestDataProvider.class) {
			String jsonFilePath = notificationFilePath + "/" + notificationFileName + ".json";
			Map<String, Object> jsonData = getJsonAsMap(jsonFilePath);
			map.put("NotificationData", jsonData);
			map.put("clientName", clientName);
			map.put("ContentType", contentType);
			map.put("AccessTokenURL", accessTokenURL);
			map.put("Body", body);
			map.put("clientName", clientName);
			map.put("notesApi", notesApi);
			Object[][] obj = { { map } };
			return obj;
		}

	    }
	    
	    @DataProvider(name = "LoginTestData2")
	    public static Object[][] LoginDataProvider2(ITestContext context, Method method) throws Exception {
	     FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
	     library.setCurrentTestMethod(method.getName());
	     String testName = context.getName();
	     String path = "";
	     if (context.getName().equalsIgnoreCase("Urgency_Test_Web") || context.getName().equalsIgnoreCase("Activities_Workflow_Web")) {   path = "data/" + testName + ".xlsx";
	     } else {
	      path = "data/Common_Data5.xlsx";
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
	     map.put("TestName", "Login");

	     Object[][] obj = { { map } };

	     return obj;
	    }
	    
	  //Created by Priyanka on 05/15/2016 to fetch data from ClientList excel sheet and LoginData json data.
	     @DataProvider(name="APILoginTest2")
	     public static Object[][] APILoginTestDataProvide(ITestContext context, Method method) throws Exception {
	         FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
	         library.setCurrentTestMethod(method.getName());
	         //String testName = context.getName();
	         String path = "data/EmailUpdate.xlsx";
	         library.openExcelSheet(path, "Sheet1", "read");
	         String contentType = library.getFromExcelRowAndColumn(1, "Content Type");
	         String contactsUrl = library.getFromExcelRowAndColumn(1, "Contact URL");
	         String accessTokenURL = library.getFromExcelRowAndColumn(1, "Access Token URL");
	         String ClientName = library.getFromExcelRowAndColumn(1, "Client Name");
	         String Address = library.getFromExcelRowAndColumn(1, "Address");
	         String Price = library.getFromExcelRowAndColumn(1, "Price");
	         String commission = library.getFromExcelRowAndColumn(1, "commission");
	         library.closeExcelSheet(path, "Sheet1", "read");
	         Map<String, Object> map = new HashMap<>();
	         
	         synchronized (AgentTestDataProvider.class) {
	             String jsonFilePath = "data/notifications/LoginData2.json";
	             Map<String, Object> LoginData = getJsonAsMap(jsonFilePath);
	             map.put("ContentType", contentType);
	             map.put("AccessTokenURL", accessTokenURL);
	             map.put("contactsUrl", contactsUrl);
	             map.put("ClientName", ClientName);
	             map.put("LoginData", LoginData);
	             map.put("Address", Address);
	             map.put("Price", Price);
	             map.put("commission", commission);
	             
	             Object[][] obj = { { map } };
	             return obj;
	          
	         }
	     }
	     
	     
		  //Created by Priyanka on 05/15/2016 to fetch data from ClientList excel sheet and LoginData json data.
	     @DataProvider(name="Emailcreated")
	     public static Object[][] Emailcreated(ITestContext context, Method method) throws Exception {
	         FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
	         library.setCurrentTestMethod(method.getName());
	         //String testName = context.getName();
	         String path = "data/SalesForceTest.xlsx";
	         library.openExcelSheet(path, "EmailIdForSalesForce", "read");
	         String EmailID = library.getFromExcelRowAndColumn(1, "LeadEmailID");
	         library.closeExcelSheet(path, "EmailIdForSalesForce", "read");
	         Map<String, Object> map = new HashMap<>();
	         
	         synchronized (AgentTestDataProvider.class) {
	             map.put("EmailID", EmailID);
	             
	             Object[][] obj = { { map } };
	             return obj;
	          
	         }
	     }
	     
	  // Updated By Puneet dated : - 27-07-16
	     @DataProvider(name = "SmokeTestTransactionUpdateForContractCancelForWebForExtendedSmoke")
	     public static Object[][] TransactionStageDataProviderForContractCancelForWebForWebForExtendedSmoke(ITestContext context, Method method) throws Exception {
	      FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
	      library.setCurrentTestMethod(method.getName());
	      String testName = context.getName();
	      String path = "data/" + testName + ".xlsx";
	      library.openExcelSheet(path, "UpdateTransactionForCancel", "read");
	      int rowCount = library.getExcelRowCount();
	      library.closeExcelSheet(path, "UpdateTransactionForCancel", "read");
	      Object[][] data = new Object[rowCount-1][1];
	      for (int i = 0; i < rowCount-1; i++) {
	       int j = i + 1;
	       library.openExcelSheet(path, "UpdateTransactionForCancel", "read");
	       String clientName = library.getFromExcelRowAndColumn(j, "ClientName");
	       String presentStage = library.getFromExcelRowAndColumn(j, "PresentStage");
	       String postStage = library.getFromExcelRowAndColumn(j, "PostStage");
	       String updateTransactionStage = library.getFromExcelRowAndColumn(j, "UpdateTransactionStage");
	       library.closeExcelSheet(path, "UpdateTransactionForCancel", "read");
	       
	       Map<String, Object> map = new HashMap<>();
	       
	       
	       library.openExcelSheet(path, "apitestdata", "read");
	       String loginurl = library.getFromExcelRowAndColumn(1, "LoginUrl");
	       String leadDetailsUrl = library.getFromExcelRowAndColumn(1, "LeadDetailsUrl");
	       
	       String loginDataFileName = library.getFromExcelRowAndColumn(1, "Login File");
	       String loginDataFilePath = library.getFromExcelRowAndColumn(1, "Login Data Path");
	       String notesApiUrl = library.getFromExcelRowAndColumn(1, "Notes Api");
	       String contentType = library.getFromExcelRowAndColumn(1, "Content Type");
	              String contactsUrl = library.getFromExcelRowAndColumn(1, "Contact URL");
	              String accessTokenURL = library.getFromExcelRowAndColumn(1, "Access Token URL");
	              String ClientName = library.getFromExcelRowAndColumn(1, "Client Name");

	       
	                   synchronized (AgentTestDataProvider.class) {
	        
	        Map<String, Object> apiData = new HashMap<>();
	        String jsonFilePath = loginDataFilePath + "/" + loginDataFileName + ".json";
	        Map<String, Object> jsonData = getJsonAsMap(jsonFilePath);
	        apiData.put("ContentType", "application/json");
	             apiData.put("AccessTokenURL", loginurl);
	             apiData.put("LeadDetailsUrl", leadDetailsUrl);

	        //map.put("Body",body);
	        apiData.put("LoginData", jsonData);
	        map.put("ContentType", contentType);
	                  map.put("AccessTokenURL", accessTokenURL);
	                  map.put("contactsUrl", contactsUrl);
	                  map.put("ClientName", ClientName);
	        map.put("apiData", apiData);

	       }
	                   library.closeExcelSheet(path, "apitestdata", "read");
	                   
	                   Map<String, String> transactionData = new HashMap<>();

	                   transactionData.put("ClientName", clientName);
	                   transactionData.put("presentStage", presentStage);
	                   transactionData.put("updateTransactionStage", updateTransactionStage);
	                   transactionData.put("postStage", postStage);
	                   map.put("transactionData",transactionData);
	       try {
	        data[i][0] = map;
	       } catch (Exception e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	       }
	      }

	      return data;

	     }
	     
	   //Puneet date: - 21July16
	     @DataProvider(name = "MadeAnOfferDataProviderForExtendedSmoke")
	     public static Object[][] MadeAnOfferDataProviderForExtendedSmoke(ITestContext context, Method method) throws Exception {
	      FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
	      library.setCurrentTestMethod(method.getName());
	      String testName = context.getName();
	      String path = "data/" + testName + ".xlsx";
	      library.openExcelSheet(path, "MakeOffer", "read");
	      int rowCount = library.getExcelRowCount();
	      library.closeExcelSheet(path, "MakeOffer", "read");
	      Object[][] data = new Object[rowCount-1][1];
	      for (int i = 0; i < rowCount-1; i++) {
	       int j = i + 1;
	       library.openExcelSheet(path, "MakeOffer", "read");
	       String clientName = library.getFromExcelRowAndColumn(j, "ClientName");
	       String address = library.getFromExcelRowAndColumn(j, "Address");
	       String offerDate = library.getFromExcelRowAndColumn(j, "Offer Date");
	       String offerPrice = library.getFromExcelRowAndColumn(j, "Offer Price");
	       String preStage = library.getFromExcelRowAndColumn(j, "Pre-Stage");
	       String postStage = library.getFromExcelRowAndColumn(j, "Post-Stage");
	       
	       library.closeExcelSheet(path, "MakeOffer", "read");
	       
	       library.openExcelSheet(path, "MadeAnOfferStage", "read");
	       String updateTransactionStage = library.getFromExcelRowAndColumn(j, "UpdateTransactionStage");
	       library.closeExcelSheet(path, "MadeAnOfferStage", "read");
	       
	       Map<String, Object> map = new HashMap<>();
	       
	       
	       library.openExcelSheet(path, "apitestdata", "read");
	       String loginurl = library.getFromExcelRowAndColumn(1, "LoginUrl");
	       String leadDetailsUrl = library.getFromExcelRowAndColumn(1, "LeadDetailsUrl");
	       
	       String loginDataFileName = library.getFromExcelRowAndColumn(1, "Login File");
	       String loginDataFilePath = library.getFromExcelRowAndColumn(1, "Login Data Path");
	       String contentType = library.getFromExcelRowAndColumn(1, "Content Type");
	              String contactsUrl = library.getFromExcelRowAndColumn(1, "Contact URL");
	              String accessTokenURL = library.getFromExcelRowAndColumn(1, "Access Token URL");
	              String ClientName = library.getFromExcelRowAndColumn(1, "Client Name");
	       //String notesApiUrl = library.getFromExcelRowAndColumn(1, "Notes Api");
	       
	                   synchronized (AgentTestDataProvider.class) {
	        
	        Map<String, Object> apiData = new HashMap<>();
	        String jsonFilePath = loginDataFilePath + "/" + loginDataFileName + ".json";
	        Map<String, Object> jsonData = getJsonAsMap(jsonFilePath);
	        apiData.put("ContentType", "application/json");
	             apiData.put("AccessTokenURL", loginurl);
	             apiData.put("LeadDetailsUrl", leadDetailsUrl);
	        //map.put("Body",body);
	        apiData.put("LoginData", jsonData);
	         map.put("ContentType", contentType);
	                  map.put("AccessTokenURL", accessTokenURL);
	                  map.put("contactsUrl", contactsUrl);
	                  map.put("ClientName", ClientName);
	        map.put("apiData", apiData);

	       }
	                   library.closeExcelSheet(path, "apitestdata", "read");
	                   
	                   Map<String, String> madeAnOfferData = new HashMap<>();

	                   madeAnOfferData.put("ClientName", clientName);
	                   madeAnOfferData.put("Address", address);
	                   madeAnOfferData.put("OfferDate", offerDate);
	                   madeAnOfferData.put("OfferPrice", offerPrice);
	                   madeAnOfferData.put("preStage", preStage);
	                   madeAnOfferData.put("postStage", postStage);
	                 
	                   
	                   madeAnOfferData.put("updateTransactionStage", updateTransactionStage);
	                   map.put("MadeOfferData",madeAnOfferData);
	       try {
	        data[i][0] = map;
	       } catch (Exception e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	       }
	      }

	      return data;

	     }
	     
	     @DataProvider(name = "APIGetContentsFunctional")
	     public static Object[][] APILoginTestDataProviderFunctional(ITestContext context, Method method) throws Exception {
	         FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
	         library.setCurrentTestMethod(method.getName());
	         // String testName = context.getName();
	         String path = "data/ClientListFunctional.xlsx";
	         library.openExcelSheet(path, "Sheet2", "read");
	         int rowCount = library.getExcelRowCount();
	         library.closeExcelSheet(path, "Sheet2", "read");
	         Object[][] data = new Object[rowCount-1][1];
	         library.openExcelSheet(path, "Sheet1", "read");
	         String contentType = library.getFromExcelRowAndColumn(1, "Content Type");
	         String contactsUrl = library.getFromExcelRowAndColumn(1, "Contact URL");
	         String newestreferredurl = library.getFromExcelRowAndColumn(1, "Newest referred Sort URL");
	         String oldestreferredurl = library.getFromExcelRowAndColumn(1, "Oldest referred Sort URL");
	         String mosturgenturl = library.getFromExcelRowAndColumn(1, "Most urgent Sort URL");
	         String accessTokenURL = library.getFromExcelRowAndColumn(1, "Access Token URL");
	         String firstnameurl = library.getFromExcelRowAndColumn(1, "First Name Sort URL");
	         
	         library.closeExcelSheet(path, "Sheet1", "read");
	         
	         Map<String, Object> apiData = new HashMap<>();
	         synchronized (AgentTestDataProvider.class) {
	             
	             String jsonFilePath = "data/notifications/LoginData.json";
	             Map<String, Object> jsonData = getJsonAsMap(jsonFilePath);
	             apiData.put("ContentType", contentType);
	             apiData.put("AccessTokenURL", accessTokenURL);
	             apiData.put("contactsUrl", contactsUrl);
	             apiData.put("newestreferredurl", newestreferredurl);
	             apiData.put("oldestreferredurl", oldestreferredurl);
	             apiData.put("mosturgenturl", mosturgenturl);
	             apiData.put("firstnameurl", firstnameurl);
	             //apidata.put("sortUrl", sortUrl);
	             apiData.put("LoginData", jsonData);
	         }
	         for (int i = 0; i < rowCount - 1; i++) {
	             int j = i + 1;
	             library.openExcelSheet(path, "Sheet2", "read");
	             String Order = library.getFromExcelRowAndColumn(j, "Order");
	             String SortURL = library.getFromExcelRowAndColumn(j, "Sort URL");
	             //Integer.parseInt(variable);
	             library.closeExcelSheet(path, "Sheet2", "read");
	             Map<String, String> orderData = new HashMap<>();
	             orderData.put("Order", Order);
	             orderData.put("SortURL", SortURL);
	             //orderData.put("variable", variable);
	             Map<String, Object> map = new HashMap<>();
	             map.put("apiData", apiData);
	             map.put("orderData", orderData);
	             try {
	                 data[i][0] = map;
	             } catch (Exception e) {
	                 // TODO Auto-generated catch block
	                 e.printStackTrace();
	             }
	             
	         }
	         library.closeExcelSheet(path, "Sheet2", "read");
	         
	         return data;
	         
	     }
	     
	     @DataProvider(name = "APIGetMetStage")
	     public static Object[][] APIGetMetStage(ITestContext context, Method method) throws Exception {
	         FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
	         library.setCurrentTestMethod(method.getName());
	         // String testName = context.getName();
	         String path = "data/ClientListFunctionalMet.xlsx";
	         library.openExcelSheet(path, "Sheet2", "read");
	         int rowCount = library.getExcelRowCount();
	         library.closeExcelSheet(path, "Sheet2", "read");
	         Object[][] data = new Object[rowCount-1][1];
	         library.openExcelSheet(path, "Sheet1", "read");
	         String contentType = library.getFromExcelRowAndColumn(1, "Content Type");
	         String contactsUrl = library.getFromExcelRowAndColumn(1, "Contact URL");
	         String newestreferredurl = library.getFromExcelRowAndColumn(1, "Newest referred Sort URL");
	         String oldestreferredurl = library.getFromExcelRowAndColumn(1, "Oldest referred Sort URL");
	         String mosturgenturl = library.getFromExcelRowAndColumn(1, "Most urgent Sort URL");
	         String accessTokenURL = library.getFromExcelRowAndColumn(1, "Access Token URL");
	         String firstnameurl = library.getFromExcelRowAndColumn(1, "First Name Sort URL");
	         
	         library.closeExcelSheet(path, "Sheet1", "read");
	         
	         Map<String, Object> apiData = new HashMap<>();
	         synchronized (AgentTestDataProvider.class) {
	             
	             String jsonFilePath = "data/notifications/LoginData.json";
	             Map<String, Object> jsonData = getJsonAsMap(jsonFilePath);
	             apiData.put("ContentType", contentType);
	             apiData.put("AccessTokenURL", accessTokenURL);
	             apiData.put("contactsUrl", contactsUrl);
	             apiData.put("newestreferredurl", newestreferredurl);
	             apiData.put("oldestreferredurl", oldestreferredurl);
	             apiData.put("mosturgenturl", mosturgenturl);
	             apiData.put("firstnameurl", firstnameurl);
	             //apidata.put("sortUrl", sortUrl);
	             apiData.put("LoginData", jsonData);
	         }
	         for (int i = 0; i < rowCount - 1; i++) {
	             int j = i + 1;
	             library.openExcelSheet(path, "Sheet2", "read");
	             String Order = library.getFromExcelRowAndColumn(j, "Order");
	             String SortURL = library.getFromExcelRowAndColumn(j, "Sort URL");
	             //Integer.parseInt(variable);
	             library.closeExcelSheet(path, "Sheet2", "read");
	             Map<String, String> orderData = new HashMap<>();
	             orderData.put("Order", Order);
	             orderData.put("SortURL", SortURL);
	             //orderData.put("variable", variable);
	             Map<String, Object> map = new HashMap<>();
	             map.put("apiData", apiData);
	             map.put("orderData", orderData);
	             try {
	                 data[i][0] = map;
	             } catch (Exception e) {
	                 // TODO Auto-generated catch block
	                 e.printStackTrace();
	             }
	             
	         }
	         library.closeExcelSheet(path, "Sheet2", "read");
	         
	         return data;
	         
	     }
    
    //Created by Priyanka on 05/15/2016 to fetch data from ClientList excel sheet and LoginData json data.
    @DataProvider(name="GetToKnowSection")
    public static Object[][] GetToKnowSection(ITestContext context, Method method) throws Exception {
        FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
        library.setCurrentTestMethod(method.getName());
        //String testName = context.getName();
        String path = "data/Reg_214_VerifyGetToKnowSection.xlsx";
        library.openExcelSheet(path, "Sheet1", "read");
        String contentType = library.getFromExcelRowAndColumn(1, "Content Type");
        String GettoKnow_API = library.getFromExcelRowAndColumn(1, "GettoKnow");
        String CityName= (String) library.getFromExcelRowAndColumn(1,"CityName");
        library.closeExcelSheet(path, "Sheet1", "read");
        Map<String, Object> map = new HashMap<>();
        synchronized (AgentTestDataProvider.class) {
            String jsonFilePath = "data/notifications/Gettoknow.json";
            Map<String, Object> Gettoknow = getJsonAsMap(jsonFilePath);
            map.put("ContentType", contentType);
            map.put("GettoKnow_API", GettoKnow_API);
            //map.put("Body",body);
            map.put("Gettoknow", Gettoknow);
            map.put("CityName", CityName);
            Object[][] obj = { { map } };
            return obj;
        }
    }
        
    @DataProvider(name = "Notification")
    public static Object[][] Notification(ITestContext context, Method method) throws Exception {
     FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
     library.setCurrentTestMethod(method.getName());
     String path = "data/Notification.xlsx";
     library.openExcelSheet(path, "Notification", "read");
     String Address = library.getFromExcelRowAndColumn(1, "Address");
     String Price = library.getFromExcelRowAndColumn(1, "Price");
     String Name = library.getFromExcelRowAndColumn(1, "Name");
     String Phone = library.getFromExcelRowAndColumn(1, "Phone");
     String City = library.getFromExcelRowAndColumn(1, "City");
     String Zip = library.getFromExcelRowAndColumn(1, "Zip");

     library.closeExcelSheet(path, "Notification", "read");
     Map<String, String> map = new HashMap<>();
     map.put("Address", Address);
     map.put("Price", Price);
     map.put("Name", Name);
     map.put("Phone", Phone);
     map.put("City", City);
     map.put("Zip", Zip);
     Object[][] obj = { { map } };

     return obj;
    }
        
        
        @DataProvider(name = "getsalesforceTime")
        public static Object[][] getsalesforceTime(ITestContext context, Method method) throws Exception {
         FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
         library.setCurrentTestMethod(method.getName());
         String testName = context.getName();
         String path = "data/SalesForceTime.xlsx";
         library.openExcelSheet(path, "SalesForceTime", "read");
         String SalesForceTime = library.getFromExcelRowAndColumn(1, "SalesForceTime");
         library.closeExcelSheet(path, "SalesForceTime", "read");
         Map<String, String> map = new HashMap<>();
         map.put("SalesForceTime", SalesForceTime);

         Object[][] obj = { { map } };

         return obj;
        }
    }


    
    
    

