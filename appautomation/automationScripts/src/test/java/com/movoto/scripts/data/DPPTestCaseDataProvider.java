package com.movoto.scripts.data;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

import com.movoto.fixtures.FixtureLibrary;

public class DPPTestCaseDataProvider {

	@DataProvider(name = "TestDataForReg_68")
	public static Object[][] Reg_68_TestData(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String path = "data/ConsumerWebTestData.xlsx";
		library.openExcelSheet(path, "Reg_68_TestData", "read");
		String headerKey = library.getFromExcelRowAndColumn(1, "Header Key");
		String headerValue = library.getFromExcelRowAndColumn(1, "Header Value");
		String apiString = library.getFromExcelRowAndColumn(1, "API String");
		library.closeExcelSheet(path, "Reg_68_TestData", "read");

		Map<String, String> map = new HashMap<>();
		map.put("headerKey", headerKey);
		map.put("headerValue", headerValue);
		map.put("apiString", apiString);

		Object[][] obj = { { map } };
		return obj;
	}

	@DataProvider(name = "TestDataForReg_69")
	public static Object[][] Reg_69_TestData(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String path = "data/ConsumerWebTestData.xlsx";
		library.openExcelSheet(path, "Reg_69_TestData", "read");
		String headerKey = library.getFromExcelRowAndColumn(1, "Header Key");
		String headerValue = library.getFromExcelRowAndColumn(1, "Header Value");
		String apiString = library.getFromExcelRowAndColumn(1, "API String");
		String textOfMovotoLink = library.getFromExcelRowAndColumn(1, "Movoto Link");
		String textOfCityLink = library.getFromExcelRowAndColumn(1, "City Link");
		String textOfZipLink = library.getFromExcelRowAndColumn(1, "Zip Link");
		library.closeExcelSheet(path, "Reg_69_TestData", "read");

		Map<String, String> map = new HashMap<>();
		map.put("headerKey", headerKey);
		map.put("headerValue", headerValue);
		map.put("apiString", apiString);
		map.put("textOfMovotoLink", textOfMovotoLink);
		map.put("textOfCityLink", textOfCityLink);
		map.put("textOfZipLink", textOfZipLink);

		Object[][] obj = { { map } };
		return obj;
	}

	@DataProvider(name = "TestDataForReg_70")
	public static Object[][] Reg_70_TestData(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String path = "data/ConsumerWebTestData.xlsx";
		library.openExcelSheet(path, "Reg_70_TestData", "read");
		String headerKey = library.getFromExcelRowAndColumn(1, "Header Key");
		String headerValue = library.getFromExcelRowAndColumn(1, "Header Value");
		String apiString = library.getFromExcelRowAndColumn(1, "API String");
		library.closeExcelSheet(path, "Reg_70_TestData", "read");

		Map<String, String> map = new HashMap<>();
		map.put("headerKey", headerKey);
		map.put("headerValue", headerValue);
		map.put("apiString", apiString);
		Object[][] obj = { { map } };
		return obj;
	}

	@DataProvider(name = "TestDataForReg_71")
	public static Object[][] Reg_71_TestData(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String path = "data/ConsumerWebTestData.xlsx";
		library.openExcelSheet(path, "Reg_71_TestData", "read");
		String headerKey = library.getFromExcelRowAndColumn(1, "Header Key");
		String headerValue = library.getFromExcelRowAndColumn(1, "Header Value");
		String apiString = library.getFromExcelRowAndColumn(1, "API String");
		library.closeExcelSheet(path, "Reg_71_TestData", "read");

		Map<String, String> map = new HashMap<>();
		map.put("headerKey", headerKey);
		map.put("headerValue", headerValue);
		map.put("apiString", apiString);
		Object[][] obj = { { map } };
		return obj;
	}

	@DataProvider(name = "TestDataForReg_73")
	public static Object[][] Reg_73_TestData(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String path = "data/ConsumerWebTestData.xlsx";
		library.openExcelSheet(path, "Reg_73_TestData", "read");
		String headerKey = library.getFromExcelRowAndColumn(1, "Header Key");
		String headerValue = library.getFromExcelRowAndColumn(1, "Header Value");
		String apiString = library.getFromExcelRowAndColumn(1, "API String");
		String containsOfUrl = library.getFromExcelRowAndColumn(1, "Contains Of URL of School");
		String containsOfUrl1 = library.getFromExcelRowAndColumn(2, "Contains Of URL of School");
		String popOverContent = library.getFromExcelRowAndColumn(1, "Pop Over Content");
		library.closeExcelSheet(path, "Reg_73_TestData", "read");

		Map<String, String> map = new HashMap<>();
		map.put("headerKey", headerKey);
		map.put("headerValue", headerValue);
		map.put("apiString", apiString);
		map.put("containsOfUrl", containsOfUrl);
		map.put("containsOfUrl1", containsOfUrl1);
		map.put("popOverContent", popOverContent);
		Object[][] obj = { { map } };
		return obj;
	}

	@DataProvider(name = "TestDataForReg_74")
	public static Object[][] Reg_74_TestData(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String path = "data/ConsumerWebTestData.xlsx";
		library.openExcelSheet(path, "Reg_74_TestData", "read");
		String headerKey = library.getFromExcelRowAndColumn(1, "Header Key");
		String headerValue = library.getFromExcelRowAndColumn(1, "Header Value");
		String apiString = library.getFromExcelRowAndColumn(1, "API String");
		String urlContains = library.getFromExcelRowAndColumn(1, "URL Contains");
		library.closeExcelSheet(path, "Reg_74_TestData", "read");

		Map<String, String> map = new HashMap<>();
		map.put("headerKey", headerKey);
		map.put("headerValue", headerValue);
		map.put("apiString", apiString);
		map.put("urlContains", urlContains);
		Object[][] obj = { { map } };
		return obj;
	}

	@DataProvider(name = "TestDataForReg_75")
	public static Object[][] Reg_75_TestData(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String testName = context.getName();
		String path = "data/ConsumerWebTestData.xlsx";
		library.openExcelSheet(path, "Reg_75_TestData", "read");
		String userName = library.getFromExcelRowAndColumn(1, "UserName");
		String password = library.getFromExcelRowAndColumn(1, "Password");
		String headerKey = library.getFromExcelRowAndColumn(1, "Header Key");
		String headerValue = library.getFromExcelRowAndColumn(1, "Header Value");
		String apiString = library.getFromExcelRowAndColumn(1, "API String");
		library.closeExcelSheet(path, "Reg_75_TestData", "read");

		Map<String, String> map = new HashMap<>();
		map.put("Username", userName);
		map.put("Password", password);
		map.put("headerKey", headerKey);
		map.put("headerValue", headerValue);
		map.put("apiString", apiString);

		Object[][] obj = { { map } };
		return obj;
	}

	@DataProvider(name = "TestDataForReg_76")
	public static Object[][] Reg_76_TestData(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String path = "data/ConsumerWebTestData.xlsx";
		library.openExcelSheet(path, "Reg_76_TestData", "read");
		String headerKey = library.getFromExcelRowAndColumn(1, "Header Key");
		String headerValue = library.getFromExcelRowAndColumn(1, "Header Value");
		String apiString = library.getFromExcelRowAndColumn(1, "API String");
		String urlContent = library.getFromExcelRowAndColumn(1, "Content Of Link");
		library.closeExcelSheet(path, "Reg_76_TestData", "read");

		Map<String, String> map = new HashMap<>();
		map.put("headerKey", headerKey);
		map.put("headerValue", headerValue);
		map.put("apiString", apiString);
		map.put("urlContent", urlContent);
		Object[][] obj = { { map } };
		return obj;
	}

	@DataProvider(name = "TestDataForReg_77")
	public static Object[][] Reg_77_TestData(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String path = "data/ConsumerWebTestData.xlsx";
		library.openExcelSheet(path, "Reg_77_TestData", "read");
		String headerKey = library.getFromExcelRowAndColumn(1, "Header Key");
		String headerValue = library.getFromExcelRowAndColumn(1, "Header Value");
		String apiString = library.getFromExcelRowAndColumn(1, "API String");
		String urlContent = library.getFromExcelRowAndColumn(1, "Content Of Link");
		library.closeExcelSheet(path, "Reg_77_TestData", "read");

		Map<String, String> map = new HashMap<>();
		map.put("headerKey", headerKey);
		map.put("headerValue", headerValue);
		map.put("apiString", apiString);
		map.put("urlContent", urlContent);
		Object[][] obj = { { map } };
		return obj;
	}
	
	@DataProvider(name = "TestDataForReg_78")
	public static Object[][] Reg_78_TestData(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String path = "data/ConsumerWebTestData.xlsx";
		library.openExcelSheet(path, "Reg_78_TestData", "read");
		String headerKey = library.getFromExcelRowAndColumn(1, "Header Key");
		String headerValue = library.getFromExcelRowAndColumn(1, "Header Value");
		String apiString = library.getFromExcelRowAndColumn(1, "API String");
		library.closeExcelSheet(path, "Reg_78_TestData", "read");

		Map<String, String> map = new HashMap<>();
		map.put("headerKey", headerKey);
		map.put("headerValue", headerValue);
		map.put("apiString", apiString);

		Object[][] obj = { { map } };
		return obj;
	}

	@DataProvider(name = "TestDataForReg_83")
	public static Object[][] FC1NotificationDataProviderIOS(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String path = "data/ConsumerWebTestData.xlsx";
		library.openExcelSheet(path, "Reg_83_TestData", "read");
		String headerKey = library.getFromExcelRowAndColumn(1, "Header Key");
		String headerValue = library.getFromExcelRowAndColumn(1, "Header Value");
		String apiString = library.getFromExcelRowAndColumn(1, "API String");
		String defaultDownPaymentText = library.getFromExcelRowAndColumn(1, "DefaultDP");
		String downPaymentText = library.getFromExcelRowAndColumn(1, "DownPayment");
		String homePrice = library.getFromExcelRowAndColumn(1, "HomePrice");
		String defaultInterestRate = library.getFromExcelRowAndColumn(1, "DefaultInterestRate");//DownPaymentinperc
		String interestRate = library.getFromExcelRowAndColumn(1, "InterestRate");
		String interestInPerc = library.getFromExcelRowAndColumn(1, "InterestInPerc");
		String downPaymentInPerc = library.getFromExcelRowAndColumn(1, "DownPaymentInPerc");
		String loanPymentText = library.getFromExcelRowAndColumn(1, "LoanPyment");

		library.closeExcelSheet(path, "Reg_83_TestData", "read");

		Map<String, String> map = new HashMap<>();
		map.put("headerKey", headerKey);
		map.put("headerValue", headerValue);
		map.put("apiString", apiString);
		map.put("defaultDownPaymentText", defaultDownPaymentText);
		map.put("downPaymentText", downPaymentText);
		map.put("homePrice", homePrice);
		map.put("defaultInterestRate", defaultInterestRate);
		map.put("interestRate", interestRate);
		map.put("interestInPerc", interestInPerc);
		map.put("downPaymentInPerc", downPaymentInPerc);
		map.put("loanPymentText", loanPymentText);

		Object[][] obj = { { map } };
		return obj;
	}

	@DataProvider(name = "TestDataForReg_84")
	public static Object[][] Reg_84_TestData(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String path = "data/ConsumerWebTestData.xlsx";
		library.openExcelSheet(path, "Reg_84_TestData", "read");
		String lender = library.getFromExcelRowAndColumn(1, "Lender");
		String aprValue = library.getFromExcelRowAndColumn(1, "APR");
		String estimatedPayment = library.getFromExcelRowAndColumn(1, "Estimated Payment");
		String rateValue = library.getFromExcelRowAndColumn(1, "Rate");
		String urlContent = library.getFromExcelRowAndColumn(1, "URL Content");
		library.closeExcelSheet(path, "Reg_84_TestData", "read");

		Map<String, String> map = new HashMap<>();
		map.put("lender", lender);
		map.put("aprValue", aprValue);
		map.put("estimatedPayment", estimatedPayment);
		map.put("rateValue", rateValue);
		map.put("urlContent", urlContent);

		Object[][] obj = { { map } };
		return obj;
	}

	@DataProvider(name = "TestDataForReg_91")
	public static Object[][] Reg_91_TestData(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String path = "data/ConsumerWebTestData.xlsx";
		library.openExcelSheet(path, "Reg_91_TestData", "read");
		String headerKey = library.getFromExcelRowAndColumn(1, "Header Key");
		String headerValue = library.getFromExcelRowAndColumn(1, "Header Value");
		String apiString = library.getFromExcelRowAndColumn(1, "API String");
		library.closeExcelSheet(path, "Reg_91_TestData", "read");

		Map<String, String> map = new HashMap<>();
		map.put("headerKey", headerKey);
		map.put("headerValue", headerValue);
		map.put("apiString", apiString);

		Object[][] obj = { { map } };
		return obj;
	}
	
	@DataProvider(name = "TestDataForReg_111")
	public static Object[][] Reg_111_TestData(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String path = "data/ConsumerWebTestData.xlsx";
		library.openExcelSheet(path, "Reg_111_TestData", "read");
		String headerKey = library.getFromExcelRowAndColumn(1, "Header Key");
		String headerValue = library.getFromExcelRowAndColumn(1, "Header Value");
		String apiString = library.getFromExcelRowAndColumn(1, "API String");
		String messageContent = library.getFromExcelRowAndColumn(1, "Message Content");
		String commonDisclaimer = library.getFromExcelRowAndColumn(1, "Common Disclaimer");
		String name = library.getFromExcelRowAndColumn(1, "Name Of Visitor");
		String mailID = library.getFromExcelRowAndColumn(1, "Mail ID");
		String contactNumber = library.getFromExcelRowAndColumn(1, "Contact Number");
		String loginID = library.getFromExcelRowAndColumn(1, "Login ID");
		String loginPassword = library.getFromExcelRowAndColumn(1, "Login Password");
		library.closeExcelSheet(path, "Reg_111_TestData", "read");

		Map<String, String> map = new HashMap<>();
		map.put("headerKey", headerKey);
		map.put("headerValue", headerValue);
		map.put("apiString", apiString);
		map.put("messageContent", messageContent);
		map.put("commonDisclaimer", commonDisclaimer);
		map.put("name", name);
		map.put("mailID", mailID);
		map.put("contactNumber", contactNumber);
		map.put("loginID", loginID);
		map.put("loginPassword", loginPassword);

		Object[][] obj = { { map } };
		return obj;
	}
	
	@DataProvider(name = "TestDataForReg_79")
	public static Object[][] Reg_79_TestData(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String path = "data/ConsumerWebTestData.xlsx";
		library.openExcelSheet(path, "Reg_79_TestData", "read");
		String headerKey = library.getFromExcelRowAndColumn(1, "Header Key");
		String headerValue = library.getFromExcelRowAndColumn(1, "Header Value");
		String apiString = library.getFromExcelRowAndColumn(1, "API String");
		String urlContent = library.getFromExcelRowAndColumn(1, "URL Content");
		library.closeExcelSheet(path, "Reg_79_TestData", "read");

		Map<String, String> map = new HashMap<>();
		map.put("headerKey", headerKey);
		map.put("headerValue", headerValue);
		map.put("apiString", apiString);
		map.put("urlContent", urlContent);
		Object[][] obj = { { map } };
		return obj;
	}
	
	@DataProvider(name = "TestDataForReg_235")
	public static Object[][] Reg_235_TestData(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String path = "data/ConsumerWebTestData.xlsx";
		library.openExcelSheet(path, "Reg_235_TestData", "read");
		String headerKey = library.getFromExcelRowAndColumn(1, "Header Key");
		String headerValue = library.getFromExcelRowAndColumn(1, "Header Value");
		String apiString = library.getFromExcelRowAndColumn(1, "API String");
		String urlContent = library.getFromExcelRowAndColumn(1, "URL Content");
		library.closeExcelSheet(path, "Reg_235_TestData", "read");

		Map<String, String> map = new HashMap<>();
		map.put("headerKey", headerKey);
		map.put("headerValue", headerValue);
		map.put("apiString", apiString);
		map.put("urlContent", urlContent);
		Object[][] obj = { { map } };
		return obj;
	}
	
	@DataProvider(name = "TestDataForReg_237")
	public static Object[][] Reg_237_TestData(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String path = "data/ConsumerWebTestData.xlsx";
		library.openExcelSheet(path, "Reg_237_TestData", "read");
		String email = library.getFromExcelRowAndColumn(1, "Email");
		String name = library.getFromExcelRowAndColumn(1, "Name");
		String contactNumber = library.getFromExcelRowAndColumn(1, "Contact Number");
		library.closeExcelSheet(path, "Reg_237_TestData", "read");

		Map<String, String> map = new HashMap<>();
		map.put("email", email);
		map.put("name", name);
		map.put("contactNumber", contactNumber);
		Object[][] obj = { { map } };
		return obj;
	}
}
