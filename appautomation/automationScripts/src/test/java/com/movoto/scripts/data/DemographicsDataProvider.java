package com.movoto.scripts.data;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

import com.movoto.fixtures.FixtureLibrary;

public class DemographicsDataProvider {
	
	@DataProvider(name = "TestDataforReg211")
	public static Object[][] Reg229DataProvider(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String path = "data/Reg_211_VerifyNewListingsSection.xlsx";
        library.openExcelSheet(path, "Sheet1", "read");
		String CityName = library.getFromExcelRowAndColumn(1,"CityName");
		String userName = library.getFromExcelRowAndColumn(1, "Username");
		String password = library.getFromExcelRowAndColumn(1, "Password");

		library.closeExcelSheet(path, "Sheet1", "read");

		Map<String, Object> map = new HashMap<>();
		map.put("CityName", CityName);
		map.put("Username", userName);
		map.put("Password", password);
		

		Object[][] obj = { { map } };

		return obj;
	}
	
	@DataProvider(name = "TestDataforReg218")
	public static Object[][] Reg218DataProvider(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String path = "data/Reg_218_ChartWorksWell.xlsx";
        library.openExcelSheet(path, "Sheet2", "read");
		String Chart1 = library.getFromExcelRowAndColumn(1,"ChartName");
		String Chart2 = library.getFromExcelRowAndColumn(2,"ChartName");
		String Chart3 = library.getFromExcelRowAndColumn(3,"ChartName");
		String Chart4 = library.getFromExcelRowAndColumn(4,"ChartName");
		String Chart5 = library.getFromExcelRowAndColumn(5,"ChartName");
		String Chart6 = library.getFromExcelRowAndColumn(6,"ChartName");
		String Chart7 = library.getFromExcelRowAndColumn(7,"ChartName");
		String Chart8 = library.getFromExcelRowAndColumn(8,"ChartName");
		String Chart9 = library.getFromExcelRowAndColumn(9,"ChartName");
		String Chart10 = library.getFromExcelRowAndColumn(10,"ChartName");
		String Chart11= library.getFromExcelRowAndColumn(11,"ChartName");
		String Chart12= library.getFromExcelRowAndColumn(12,"ChartName");
        library.closeExcelSheet(path, "Sheet2", "read");
		Map<String, String> map = new HashMap<>();
		map.put("Chart1",Chart1);
		map.put("Chart2", Chart2);
		map.put("Chart3", Chart3);
		map.put("Chart4", Chart4);
		map.put("Chart5", Chart5);
		map.put("Chart6", Chart6);
		map.put("Chart7", Chart7);
		map.put("Chart8", Chart8);
		map.put("Chart9", Chart9);
		map.put("Chart10", Chart10);
		map.put("Chart11", Chart11);
		map.put("Chart12", Chart12);		
		Object[][] obj = { { map } };
		return obj;
	}
	
	
	
	
}
