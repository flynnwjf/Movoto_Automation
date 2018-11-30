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

public class MapSearchDataProvider {

	@DataProvider(name = "MapSearchForCity")
	public static Object[][] MapSearchForCity(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		Map<String, Object> map = null;
		library.setCurrentTestMethod(method.getName());
		String testName = context.getName();
		String path;
		if (context.getName().equalsIgnoreCase("MapSearchForCity_Web")
				|| context.getName().equalsIgnoreCase("Reg_202_SortingFunctoinality_MACSafari")
				|| context.getName().equalsIgnoreCase("Reg_202_SortingFunctoinality_AndroidChrome")
				|| context.getName().equalsIgnoreCase("Reg_201_FilterFunctionality")
				|| context.getName().equalsIgnoreCase("Reg_202_Verify_Sorting_Functionality_IOSSafari")
				|| context.getName().equalsIgnoreCase("Reg_202_Verify_Sorting_Functionality_WindowsChrome")
				|| context.getName().equalsIgnoreCase("Reg_202_Verify_Sorting_Functionality_WindowsIE")) {

			path = "data/" + testName + ".xlsx";
		} else {
			path = "data/Common_Data.xlsx";
		}
		if (path != null) {
			library.openExcelSheet(path, "MapSearchForCity", "read");
			String contentType = library.getFromExcelRowAndColumn(1, "Content Type");
			String loginDataPath = library.getFromExcelRowAndColumn(1, "Login Data Path");
			String mapSearchApi = library.getFromExcelRowAndColumn(1, "MapSearchApi");
			String host = library.getFromExcelRowAndColumn(1, "Host");
			String connection = library.getFromExcelRowAndColumn(1, "Connection");
			String userAgent = library.getFromExcelRowAndColumn(1, "UserAgent");
			String xMdatakey = library.getFromExcelRowAndColumn(1, "XMdataKey");
			String acceptEncoding = library.getFromExcelRowAndColumn(1, "Accept-Encoding");
			String contentLength = library.getFromExcelRowAndColumn(1, "Content-Length");
			String CityName = library.getFromExcelRowAndColumn(1, "CityName");
			String UrlPart = library.getFromExcelRowAndColumn(1, "UrlPart");
			String CardSorted = library.getFromExcelRowAndColumn(1, "CardSorted");
			String SortBySqftInUrl = library.getFromExcelRowAndColumn(1, "SortBySqftInUrl");
			String SortByDefaultOption = library.getFromExcelRowAndColumn(1, "SortByDefaultOption");
			library.closeExcelSheet(path, "MapSearchForCity", "read");

			map = new HashMap<>();
			synchronized (MapSearchDataProvider.class) {
				String jsonFilePath = "data/Map_Search/Map_Search_For_City.json";
				Map<String, Object> jsonData = getJsonAsMap(jsonFilePath);

				map.put("ContentType", contentType);
				map.put("acceptEncoding", acceptEncoding);
				map.put("LoginDataPath", loginDataPath);
				map.put("MapSearchApi", mapSearchApi);
				map.put("Host", host);
				map.put("Connection", connection);
				map.put("UserAgent", userAgent);
				map.put("XMdatakey", xMdatakey);
				map.put("AcceptEncoding", acceptEncoding);
				map.put("ContentLength", contentLength);
				map.put("CityName", CityName);
				map.put("UrlPart", UrlPart);
				map.put("CardSorted", CardSorted);
				map.put("SortBySqftInUrl", SortBySqftInUrl);
				map.put("SortByDefaultOption", SortByDefaultOption);
				
				map.put("JsonData", jsonData);
			}
		}
		Object[][] obj = { { map } };

		return obj;
	}

	@DataProvider(name = "MapSearchForWeb")
	public static Object[][] MapSearchForWeb(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		Map<String, Object> map = null;
		library.setCurrentTestMethod(method.getName());
		String testName = context.getName();
		
		String path;
		if (context.getName().equalsIgnoreCase("Reg_92_MapSearch_WindowsChrome")
				|| context.getName().equalsIgnoreCase("Reg_202_Verify_Sorting_Functionality")
				|| context.getName().equalsIgnoreCase("Reg_202_Verify_Sorting_Functionality_And")
				|| context.getName().equalsIgnoreCase("Reg_201_FilterFunctionality")
				|| context.getName().equalsIgnoreCase("Reg_202_Verify_Sorting_Functionality_IOS")
				|| context.getName().equalsIgnoreCase("Reg_92_MapSearch_MacSafari") 
				|| context.getName().equalsIgnoreCase("Reg_92_MapSearch_AndroidChrome")
				|| context.getName().equalsIgnoreCase("Reg_92_MapSearch_IOSSafari") 
				|| context.getName().equalsIgnoreCase("Reg_92_MapSearch_WindowsIE") ) {
			path = "data/" + testName + ".xlsx";
		} else {
			path = "data/Common_Data.xlsx";
		}
		if (path != null) {
			library.openExcelSheet(path, "MapSearchForCity", "read");
			String contentType = library.getFromExcelRowAndColumn(1, "Content Type");
			String loginDataPath = library.getFromExcelRowAndColumn(1, "Login Data Path");
			String mapSearchApi = library.getFromExcelRowAndColumn(1, "MapSearchApi");
			String host = library.getFromExcelRowAndColumn(1, "Host");
			String connection = library.getFromExcelRowAndColumn(1, "Connection");
			String userAgent = library.getFromExcelRowAndColumn(1, "UserAgent");
			String xMdatakey = library.getFromExcelRowAndColumn(1, "XMdataKey");
			String acceptEncoding = library.getFromExcelRowAndColumn(1, "Accept-Encoding");
			String contentLength = library.getFromExcelRowAndColumn(1, "Content-Length");

			String CityText = library.getFromExcelRowAndColumn(1, "CityText");
			String ZipText = library.getFromExcelRowAndColumn(1, "ZipText");
			String NeighborhoodText = library.getFromExcelRowAndColumn(1, "NeighborhoodText");
			
			String CityName = library.getFromExcelRowAndColumn(1, "CityName");
			String neighborhood = library.getFromExcelRowAndColumn(1, "Neighborhood");
			String zipCode = library.getFromExcelRowAndColumn(1, "ZipCode");
			String pageNumber = library.getFromExcelRowAndColumn(1, "PageNumber");
			String noOfRecordsPerPage = library.getFromExcelRowAndColumn(1, "NoOfRecordsPerPage");
			String cityNameInDisplayString = library.getFromExcelRowAndColumn(1, "CityNameInDisplayString");
			String minNoOfTotalRecords = library.getFromExcelRowAndColumn(1, "MinTotalNoOfTotalRecords");
			String neighborhoodURL=library.getFromExcelRowAndColumn(1,"NeighborhoodURL");
			library.closeExcelSheet(path, "MapSearchForCity", "read");
			map = new HashMap<>();
			synchronized (MapSearchDataProvider.class) {
				String jsonFilePath=null;
				if(context.getName().equalsIgnoreCase("Reg_92_MapSearch_MacSafari") || 
						context.getName().equalsIgnoreCase("Reg_92_MapSearch_WindowsChrome") ||
						context.getName().equalsIgnoreCase("Reg_92_MapSearch_WindowsIE") ||
						context.getName().equalsIgnoreCase("Reg_92_MapSearch_AndroidChrome") ||
						context.getName().equalsIgnoreCase("Reg_92_MapSearch_IOSSafari") ){
					jsonFilePath = "data/Map_Search/Map_Search_For_All.json";
				}else{
					jsonFilePath = "data/Map_Search/Map_Search_For_City.json";
				}
				Map<String, Object> jsonData = getJsonAsMap(jsonFilePath);

				map.put("ContentType", contentType);
				map.put("acceptEncoding", acceptEncoding);
				map.put("LoginDataPath", loginDataPath);
				map.put("MapSearchApi", mapSearchApi);
				map.put("Host", host);
				map.put("Connection", connection);
				map.put("UserAgent", userAgent);
				map.put("XMdatakey", xMdatakey);
				map.put("AcceptEncoding", acceptEncoding);
				map.put("ContentLength", contentLength);
				map.put("CityName", CityName);
				map.put("Neighborhood", neighborhood);
				map.put("ZipCode", zipCode);
				map.put("PageNumber", pageNumber);
				map.put("NoOfRecordsPerPage", noOfRecordsPerPage);
				map.put("CityNameInDisplayString", cityNameInDisplayString);
				map.put("MinNoOfTotalRecords", minNoOfTotalRecords);
				map.put("NeighborhoodURL", neighborhoodURL);
				map.put("CityText", CityText);
				map.put("ZipText", ZipText);
				map.put("NeighborhoodText", NeighborhoodText);
				map.put("JsonData", jsonData);
			}
		}
		Object[][] obj = { { map } };
		return obj;
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

	@DataProvider(name = "MapSearchForZipCode")
	public static Object[][] MapSearchForZipCode(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String testName = context.getName();
		String path = "";
		if (context.getName().equalsIgnoreCase("MapSearchForZipCode_Web")) {
			path = "data/" + testName + ".xlsx";
		} else {
			path = "data/Common_Data.xlsx";
		}
		library.openExcelSheet(path, "MapSearchForCity", "read");
		String contentType = library.getFromExcelRowAndColumn(1, "Content Type");
		String loginDataPath = library.getFromExcelRowAndColumn(1, "Login Data Path");
		String mapSearchApi = library.getFromExcelRowAndColumn(1, "MapSearchApi");
		String host = library.getFromExcelRowAndColumn(1, "Host");
		String connection = library.getFromExcelRowAndColumn(1, "Connection");
		String userAgent = library.getFromExcelRowAndColumn(1, "UserAgent");
		String xMdatakey = library.getFromExcelRowAndColumn(1, "XMdataKey");
		String acceptEncoding = library.getFromExcelRowAndColumn(1, "Accept-Encoding");
		String contentLength = library.getFromExcelRowAndColumn(1, "Content-Length");
		String zipCode = library.getFromExcelRowAndColumn(1, "Zipcode");

		library.closeExcelSheet(path, "MapSearchForZipCode_Web", "read");

		Map<String, Object> map = new HashMap<>();
		synchronized (MapSearchDataProvider.class) {
			String jsonFilePath = "data/Map_Search/Map_Search_For_City.json";
			Map<String, Object> jsonData = getJsonAsMap(jsonFilePath);

			map.put("ContentType", contentType);
			map.put("acceptEncoding", acceptEncoding);
			map.put("LoginDataPath", loginDataPath);
			map.put("MapSearchApi", mapSearchApi);
			map.put("Host", host);
			map.put("Connection", connection);
			map.put("UserAgent", userAgent);
			map.put("XMdatakey", xMdatakey);
			map.put("AcceptEncoding", acceptEncoding);
			map.put("ContentLength", contentLength);
			map.put("ZipCode", zipCode);
			map.put("JsonData", jsonData);

			Object[][] obj = { { map } };

			return obj;
		}
	}

	@DataProvider(name = "MapSearchForNeighborhood")
	public static Object[][] MapSearchForNeighborhood(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String testName = context.getName();
		String path = "";
		if (context.getName().equalsIgnoreCase("MapSearchForNeighborhood_Web")) {
			path = "data/" + testName + ".xlsx";
		} else {
			path = "data/Common_Data.xlsx";
		}
		library.openExcelSheet(path, "MapSearchForCity", "read");
		String contentType = library.getFromExcelRowAndColumn(1, "Content Type");
		String loginDataPath = library.getFromExcelRowAndColumn(1, "Login Data Path");
		String mapSearchApi = library.getFromExcelRowAndColumn(1, "MapSearchApi");
		String host = library.getFromExcelRowAndColumn(1, "Host");
		String connection = library.getFromExcelRowAndColumn(1, "Connection");
		String userAgent = library.getFromExcelRowAndColumn(1, "UserAgent");
		String xMdatakey = library.getFromExcelRowAndColumn(1, "XMdataKey");
		String acceptEncoding = library.getFromExcelRowAndColumn(1, "Accept-Encoding");
		String contentLength = library.getFromExcelRowAndColumn(1, "Content-Length");
		String neighborhood = library.getFromExcelRowAndColumn(1, "Neighborhood");

		library.closeExcelSheet(path, "MapSearchForCity", "read");

		Map<String, Object> map = new HashMap<>();
		synchronized (MapSearchDataProvider.class) {
			String jsonFilePath = "data/Map_Search/Map_Search_For_City.json";
			Map<String, Object> jsonData = getJsonAsMap(jsonFilePath);

			map.put("ContentType", contentType);
			map.put("acceptEncoding", acceptEncoding);
			map.put("LoginDataPath", loginDataPath);
			map.put("MapSearchApi", mapSearchApi);
			map.put("Host", host);
			map.put("Connection", connection);
			map.put("UserAgent", userAgent);
			map.put("XMdatakey", xMdatakey);
			map.put("AcceptEncoding", acceptEncoding);
			map.put("ContentLength", contentLength);
			map.put("Neighborhood", neighborhood);
			map.put("JsonData", jsonData);

			Object[][] obj = { { map } };

			return obj;
		}

	}

	private static List<Object> toList(JSONArray array) throws JsonException {
		List<Object> list = new ArrayList<>();
		for (int i = 0; i < array.size(); i++) {
			Object value = array.get(i);
			if (value instanceof JSONArray) {
				value = toList((JSONArray) value);
			} else if (value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			list.add(value);
		}
		return list;
	}

	@DataProvider(name = "FilterFunctionality")
	public static Object[][] MapSearchForFilterFunctionality(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String testName = context.getName();
		String path = "";
		if (context.getName().equalsIgnoreCase("Reg_201_FilterFunctionality_MACSafari")
				|| context.getName().equalsIgnoreCase("Reg_200_FilterFunctionality_MACSafari")
				|| context.getName().equalsIgnoreCase("Reg_201_FilterFunctionality_AndroidChrome")
				|| context.getName().equalsIgnoreCase("Reg_200_FilterFunctionality_AndroidChrome")
				|| context.getName().equalsIgnoreCase("Reg_200_FilterFunctionality_And")
				|| context.getName().equalsIgnoreCase("Reg_201_FilterFunctionality_IOSSafari")
				|| context.getName().equalsIgnoreCase("Reg_200_FilterFunctionality_IOSSafari")
				|| context.getName().equalsIgnoreCase("Reg_200_FilterFunctionality_WindowsIE")
				|| context.getName().equalsIgnoreCase("Reg_200_FilterFunctionality_WindowsChrome")
				|| context.getName().equalsIgnoreCase("Reg_201_FilterFunctionality_WindowsIE")
				|| context.getName().equalsIgnoreCase("Reg_201_FilterFunctionality_WindowsChrome")) {
			path = "data/" + testName + ".xlsx";
		} else {
			path = "data/Common_Data.xlsx";
		}

		library.openExcelSheet(path, "MapSearchForCity", "read");
		String contentType = library.getFromExcelRowAndColumn(1, "Content Type");
		String loginDataPath = library.getFromExcelRowAndColumn(1, "Login Data Path");
		String mapSearchApi = library.getFromExcelRowAndColumn(1, "MapSearchApi");
		String host = library.getFromExcelRowAndColumn(1, "Host");
		String connection = library.getFromExcelRowAndColumn(1, "Connection");
		String userAgent = library.getFromExcelRowAndColumn(1, "UserAgent");
		String xMdatakey = library.getFromExcelRowAndColumn(1, "XMdataKey");
		String acceptEncoding = library.getFromExcelRowAndColumn(1, "Accept-Encoding");
		String contentLength = library.getFromExcelRowAndColumn(1, "Content-Length");
		String MinSqft = library.getFromExcelRowAndColumn(1, "MinSqft");
		String DefaultValueForSqft = library.getFromExcelRowAndColumn(1, "DefaultValueForSqft");

		String MinPriceByDefaultOption = library.getFromExcelRowAndColumn(1, "MinPriceByDefaultOption");
		String MaxPriceByDefaultOption = library.getFromExcelRowAndColumn(1, "MaxPriceByDefaultOption");
		String MinPrice = library.getFromExcelRowAndColumn(1, "MinPrice");
		String MaxPrice = library.getFromExcelRowAndColumn(1, "MaxPrice");
		String DefaultBedValue = library.getFromExcelRowAndColumn(1, "DefaultBedValue");
		String DefaultBathValue = library.getFromExcelRowAndColumn(1, "DefaultBathValue");
		String minBedValue = library.getFromExcelRowAndColumn(1, "MinBedValue");
		String minBathValue = library.getFromExcelRowAndColumn(1, "MinBathValue");
		library.closeExcelSheet(path, "MapSearchForCity", "read");

		Map<String, Object> map = new HashMap<>();
		synchronized (MapSearchDataProvider.class) {
			String jsonFilePath = "";
			if(context.getName().equalsIgnoreCase("Reg_201_FilterFunctionality_MACSafari")
					|| context.getName().equalsIgnoreCase("Reg_201_FilterFunctionality_AndroidChrome")
					|| context.getName().equalsIgnoreCase("Reg_201_FilterFunctionality_WindowsIE")
					|| context.getName().equalsIgnoreCase("Reg_201_FilterFunctionality_WindowsChrome")){
				 jsonFilePath = "data/Map_Search/SanMateoCitySearch.json";
			}
			else{
			 jsonFilePath = "data/Map_Search/FilterFunctionality.json";
			}
			Map<String, Object> jsonData = getJsonAsMap(jsonFilePath);

			map.put("ContentType", contentType);
			map.put("acceptEncoding", acceptEncoding);
			map.put("LoginDataPath", loginDataPath);
			map.put("MapSearchApi", mapSearchApi);
			map.put("Host", host);
			map.put("Connection", connection);
			map.put("UserAgent", userAgent);
			map.put("XMdatakey", xMdatakey);
			map.put("AcceptEncoding", acceptEncoding);
			map.put("ContentLength", contentLength);
			map.put("MinSqft", MinSqft);
			map.put("DefaultValueForSqft", DefaultValueForSqft);
			map.put("MinPriceByDefaultOption", MinPriceByDefaultOption);
			map.put("MaxPriceByDefaultOption", MaxPriceByDefaultOption);
			map.put("MinPrice", MinPrice);
			map.put("MaxPrice", MaxPrice);

			map.put("DefaultBedValue", DefaultBedValue);
			map.put("DefaultBathValue", DefaultBathValue);
			map.put("MinBedValue", minBedValue);
			map.put("MinBathValue", minBathValue);
			map.put("JsonData", jsonData);

			Object[][] obj = { { map } };
			return obj;
		}
	}

	@DataProvider(name = "MapSearchOnAndroid")
	public static Object[][] MapSearchForCityForMobile(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String testName = context.getName();
		String path = "";
		if (context.getName().equalsIgnoreCase("MapSearch_Android")) {
			path = "data/" + testName + ".xlsx";
		} else {
			path = "data/Common_Data.xlsx";
		}
		library.openExcelSheet(path, "MapSearchForCity", "read");
		String contentType = library.getFromExcelRowAndColumn(1, "Content Type");
		String loginDataPath = library.getFromExcelRowAndColumn(1, "Login Data Path");
		String mapSearchApi = library.getFromExcelRowAndColumn(1, "MapSearchApi");
		String host = library.getFromExcelRowAndColumn(1, "Host");
		String connection = library.getFromExcelRowAndColumn(1, "Connection");
		String userAgent = library.getFromExcelRowAndColumn(1, "UserAgent");
		String xMdatakey = library.getFromExcelRowAndColumn(1, "XMdataKey");
		String acceptEncoding = library.getFromExcelRowAndColumn(1, "Accept-Encoding");
		String contentLength = library.getFromExcelRowAndColumn(1, "Content-Length");
		String CityName = library.getFromExcelRowAndColumn(1, "CityName");
		String neighborhood = library.getFromExcelRowAndColumn(1, "Neighborhood");
		String zipCode = library.getFromExcelRowAndColumn(1, "ZipCode");
		String pageNumber = library.getFromExcelRowAndColumn(1, "PageNumber");
		String noOfRecordsPerPage = library.getFromExcelRowAndColumn(1, "NoOfRecordsPerPage");

		library.closeExcelSheet(path, "MapSearchForCity", "read");

		Map<String, Object> map = new HashMap<>();
		synchronized (MapSearchDataProvider.class) {
			String jsonFilePath = "data/Map_Search/Map_Search_For_City.json";
			Map<String, Object> jsonData = getJsonAsMap(jsonFilePath);

			map.put("ContentType", contentType);
			map.put("acceptEncoding", acceptEncoding);
			map.put("LoginDataPath", loginDataPath);
			map.put("MapSearchApi", mapSearchApi);
			map.put("Host", host);
			map.put("Connection", connection);
			map.put("UserAgent", userAgent);
			map.put("XMdatakey", xMdatakey);
			map.put("AcceptEncoding", acceptEncoding);
			map.put("ContentLength", contentLength);
			map.put("CityName", CityName);
			map.put("Neighborhood", neighborhood);
			map.put("ZipCode", zipCode);
			map.put("PageNumber", pageNumber);
			map.put("NoOfRecordsPerPage", noOfRecordsPerPage);
			map.put("JsonData", jsonData);

			Object[][] obj = { { map } };
			return obj;
		}
	}

	@DataProvider(name = "HotLead")
	public static Object[][] MapSearchForHotLead(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String testName = context.getName();
		String path = "";
		if (context.getName().equalsIgnoreCase("Reg_94_Verify_HotLead_Functionality")
				|| context.getName().equalsIgnoreCase("Reg_94_Verify_HotLead_Functionality_AndroidWeb")
				|| context.getName().equalsIgnoreCase("Reg_94_Verify_HotLead_Functionality_Chrome")
				|| context.getName().equalsIgnoreCase("Reg_94_Verify_HotLead_Functionality_IE")) {
			path = "data/" + testName + ".xlsx";
		} else {
			path = "data/Common_Data.xlsx";
		}
		library.openExcelSheet(path, "MapSearchForCity", "read");
		String contentType = library.getFromExcelRowAndColumn(1, "Content Type");
		String loginDataPath = library.getFromExcelRowAndColumn(1, "Login Data Path");
		String mapSearchApi = library.getFromExcelRowAndColumn(1, "MapSearchApi");
		String host = library.getFromExcelRowAndColumn(1, "Host");
		String connection = library.getFromExcelRowAndColumn(1, "Connection");
		String userAgent = library.getFromExcelRowAndColumn(1, "UserAgent");
		String xMdatakey = library.getFromExcelRowAndColumn(1, "XMdataKey");
		String acceptEncoding = library.getFromExcelRowAndColumn(1, "Accept-Encoding");
		String contentLength = library.getFromExcelRowAndColumn(1, "Content-Length");
		String City = library.getFromExcelRowAndColumn(1, "CityName");

		library.closeExcelSheet(path, "MapSearchForCity", "read");

		Map<String, Object> map = new HashMap<>();
		synchronized (MapSearchDataProvider.class) {
			String jsonFilePath = "data/Map_Search/Map_Search_For_City.json";
			Map<String, Object> jsonData = getJsonAsMap(jsonFilePath);

			map.put("ContentType", contentType);
			map.put("acceptEncoding", acceptEncoding);
			map.put("LoginDataPath", loginDataPath);
			map.put("MapSearchApi", mapSearchApi);
			map.put("Host", host);
			map.put("Connection", connection);
			map.put("UserAgent", userAgent);
			map.put("XMdatakey", xMdatakey);
			map.put("AcceptEncoding", acceptEncoding);
			map.put("ContentLength", contentLength);
			map.put("City", City);
			map.put("JsonData", jsonData);

			Object[][] obj = { { map } };
			return obj;
		}
	}

	@DataProvider(name = "SaveSearchFunctionality")

	public static Object[][] SaveSearchFunctionality(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String testName = context.getName();
		String path = "";
		if (context.getName().equalsIgnoreCase("Reg_197_Save_Search_Functionality_Chrome")
				|| context.getName().equalsIgnoreCase("Reg_197_Save_Search_Functionality_AndroidWeb")
				|| context.getName().equalsIgnoreCase("Reg_197_Save_Search_Functionality_IE")
				|| context.getName().equalsIgnoreCase("Reg_197_Save_Search_Functionality_Safari")) {
			path = "data/" + testName + ".xlsx";
		} else {
			path = "data/Common_Data.xlsx";
		}
		library.openExcelSheet(path, "MapSearchForCity", "read");
		String zipCode = library.getFromExcelRowAndColumn(1, "zipCode");

		library.closeExcelSheet(path, "MapSearchForCity", "read");

		Map<String, String> map = new HashMap<>();
		synchronized (MapSearchDataProvider.class) {
			String jsonFilePath = "data/Map_Search/Map_Search_For_City.json";
			Map<String, Object> jsonData = getJsonAsMap(jsonFilePath);

			map.put("ZipCode", zipCode);

			Object[][] obj = { { map } };

			return obj;
		}
	}

	@DataProvider(name = "MortgageCalculator")
	public static Object[][] FC1NotificationDataProviderIOS(ITestContext context, Method method) throws Exception {
		FixtureLibrary library = (FixtureLibrary) context.getAttribute("LIBRARY");
		library.setCurrentTestMethod(method.getName());
		String path = "data/Reg_83_MortgageCalculator.xlsx";
		library.openExcelSheet(path, "Sheet1", "read");
		String DefaultDownPaymentText = library.getFromExcelRowAndColumn(1, "DefaultDownPaymentText");
		String DownPaymentText = library.getFromExcelRowAndColumn(1, "DownPaymentText");
		String HomePrice = library.getFromExcelRowAndColumn(1, "HomePrice");
		String DefaultInterestRate = library.getFromExcelRowAndColumn(1, "DefaultInterestRate");
		String InterestRate = library.getFromExcelRowAndColumn(1, "InterestRate");
		String InterestinPerc = library.getFromExcelRowAndColumn(1, "InterestinPerc");
		String DownPaymentinperc = library.getFromExcelRowAndColumn(1, "DownPaymentinperc");
		String LoanPymentText = library.getFromExcelRowAndColumn(1, "LoanPymentText");

		library.closeExcelSheet(path, "Sheet1", "read");

		Map<String, String> map = new HashMap<>();
		map.put("DefaultDownPaymentText", DefaultDownPaymentText);
		map.put("DownPaymentText", DownPaymentText);
		map.put("HomePrice", HomePrice);
		map.put("DefaultInterestRate", DefaultInterestRate);
		map.put("InterestRate", InterestRate);
		map.put("InterestinPerc", InterestinPerc);
		map.put("DownPaymentinperc", DownPaymentinperc);
		map.put("LoanPymentText", LoanPymentText);

		Object[][] obj = { { map } };
		return obj;
	}
}
