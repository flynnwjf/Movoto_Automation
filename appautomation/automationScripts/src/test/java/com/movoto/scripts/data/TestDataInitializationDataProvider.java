package com.movoto.scripts.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Collectors;
import java.util.*;

import org.apache.commons.collections.comparators.ComparableComparator;
import org.apache.commons.lang.StringUtils;

//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.movoto.common.JSONManager;
import com.movoto.db.GetConnection;
import com.movoto.fixtures.FixtureLibrary;
import com.movoto.scripts.apis.CommonAPITest;
import com.movoto.scripts.consumer.Library.*;
import com.movoto.scripts.consumer.Library.utilities.utilities;
import com.movoto.scripts.consumer.Library.utilities.utilities.FileManager;
import com.movoto.scripts.consumer.Library.utilities.utilities.JsonManager;
import com.movoto.utils.JSONParserForAutomationNG;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.internal.JsonContext;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class TestDataInitializationDataProvider {
	private static JsonManager jsonManager = new utilities.JsonManager();
	public static JsonContext testDataContent;
	public static LinkedHashMap CommonDataProvider(ITestContext context,String specificSection4Data) throws JsonSyntaxException, JsonIOException, InstantiationException, IllegalAccessException, IOException, SQLException
	{
		
		String dataProviderPath = context.getCurrentXmlTest().getParameter("dataProviderPath");
		testDataContent= ((JsonContext)jsonManager.GetJsonObjectByJPath(dataProviderPath));
		LinkedHashMap refreshESRequestData = (testDataContent.read("RefreshPropertyDetailFromDB2ES"));
		String dbName=testDataContent.read("$."+specificSection4Data+".DB.serverName");
		LinkedHashMap dbLoginInfo= ((JsonContext)jsonManager.GetJsonObjectByJPath("data/DB/DataBase.json")).read(dbName);
		String functionName4SQL = testDataContent.read("$."+specificSection4Data+".DB.functionName4SQL");
		
		LinkedHashMap map = new LinkedHashMap();
		map.put("dbLoginInfo", dbLoginInfo);
		map.put("functionName4SQL", functionName4SQL);
		map.put("refreshESRequestData", refreshESRequestData);
		return map;
	}
	
	@DataProvider(name="nearbyAwesomeHomesInitializationDataProvider")
	public static Object[][] NearbyAwesomeHomesInitializationDataProvider(ITestContext context) throws JsonSyntaxException, JsonIOException, InstantiationException, IllegalAccessException, IOException, SQLException
	{
		LinkedHashMap map = CommonDataProvider(context,"nearbyAwesomeHomesInitialization");
		String shellFileName = testDataContent.read("$.nearbyAwesomeHomesInitialization.remoteJob4NearbySearchAPI.shellFileName");
		String serverName = testDataContent.read("$.nearbyAwesomeHomesInitialization.remoteJob4NearbySearchAPI.serverName");
		LinkedHashMap loginInfo4RemoteServer= ((JsonContext)jsonManager.GetJsonObjectByJPath("data/DB/DataBase.json")).read(serverName);
		LinkedHashMap nearbySearchAPIRequestInfo = GetNearbySearchAPIRequestInfoFromES(testDataContent.read("$.nearbyAwesomeHomesInitialization.requestData4NearBySearchByLocationAPI"));
		String functionName4SQL = testDataContent.read("$.nearbyAwesomeHomesInitialization.DB.functionName4SQL");
		map.put("remoteShellFileName", shellFileName);
		map.put("loginInfo4RemoteServer", loginInfo4RemoteServer);
		map.put("nearbySearchListingIDs", nearbySearchAPIRequestInfo.get("listingIDs").toString());
		map.put("nearbySearchPropertyIDs", nearbySearchAPIRequestInfo.get("propertyIDs").toString());
		map.put("lantitude4BaseProperty", testDataContent.read("$.nearbyAwesomeHomesInitialization.location.latitude"));
		map.put("longitude4BaseProperty", testDataContent.read("$.nearbyAwesomeHomesInitialization.location.longitude"));
		Object[][] obj = { { map } };
		return obj;
	}
	
	
	public static Object[][] NewListingsAndOpenHouseInitializationDataProvider(ITestContext context,String forWho) throws JsonSyntaxException, JsonIOException, InstantiationException, IllegalAccessException, IOException, SQLException
	{
		
		
		LinkedHashMap map = CommonDataProvider(context,"nearbyAwesomeHomesInitialization");
		String functionName4SQL = testDataContent.read("$.nearbyAwesomeHomesInitialization.DB.functionName4SQL");
		map.put("functionName4SQL", functionName4SQL);
		
		String attributeName4Section="nearbyNewListings4DPP";
		String attributeNameInJsonDataFile=attributeName4Section+".nearbyNewListings4DPPAPIRequestInfo";
		switch(forWho)
		{
		case "forDPP":
			attributeName4Section="nearbyNewListings4DPP";
			attributeNameInJsonDataFile=attributeName4Section+".nearbyNewListings4DPPAPIRequestInfo";
			break;
		case "forCity":
			attributeName4Section="nearbyNewListings4City";
			attributeNameInJsonDataFile=attributeName4Section+".nearbyNewListingsAndOpenHouse4CityAPIRequestInfo";
			break;
		case "forZipCode":
			attributeName4Section="nearbyNewListings4ZipCode";
			attributeNameInJsonDataFile=attributeName4Section+".nearbyNewListingsAndOpenHouse4ZipCodeAPIRequestInfo";
			break;
		}
		LinkedHashMap nearbySearchAPIRequestInfo = GetNearbySearchAPIRequestInfoFromES(testDataContent.read(String.format("$.newListingsAndOpenHouseInitialization.%s",attributeNameInJsonDataFile)));
		
		map.put("nearbySearchListingIDs", nearbySearchAPIRequestInfo.get("listingIDs").toString());
		map.put("nearbySearchPropertyIDs", nearbySearchAPIRequestInfo.get("propertyIDs").toString());
		map.put("lantitude4BaseProperty", testDataContent.read(String.format("$.newListingsAndOpenHouseInitialization.%s.location.latitude",attributeName4Section)));
		map.put("longitude4BaseProperty", testDataContent.read(String.format("$.newListingsAndOpenHouseInitialization.%s.location.longitude",attributeName4Section)));
		Object[][] obj = { { map } };
		return obj;
	}
	
	@DataProvider(name="newListingsAndOpenHouse4DPPInitializationDataProvider")
	public static Object[][] NewListingsAndOpenHouse4DPPInitializationDataProvider(ITestContext context) throws JsonSyntaxException, JsonIOException, InstantiationException, IllegalAccessException, IOException, SQLException
	{
		
		return NewListingsAndOpenHouseInitializationDataProvider(context,"forDPP");
	}
	
	@DataProvider(name="newListingsAndOpenHouse4CityInitializationDataProvider")
	public static Object[][] NewListingsAndOpenHouse4CityInitializationDataProvider(ITestContext context) throws JsonSyntaxException, JsonIOException, InstantiationException, IllegalAccessException, IOException, SQLException
	{
		return NewListingsAndOpenHouseInitializationDataProvider(context,"forCity");
	}
	
	@DataProvider(name="newListingsAndOpenHouse4ZipCodeInitializationDataProvider")
	public static Object[][] NewListingsAndOpenHouse4ZipCodeInitializationDataProvider(ITestContext context) throws JsonSyntaxException, JsonIOException, InstantiationException, IllegalAccessException, IOException, SQLException
	{
		return NewListingsAndOpenHouseInitializationDataProvider(context,"forZipCode");
	}
	
	public static LinkedHashMap Statistics4SnapShotInitializationDataProvider(ITestContext context) throws JsonSyntaxException, JsonIOException, InstantiationException, IllegalAccessException, IOException, SQLException
	{
		LinkedHashMap map = CommonDataProvider(context,"Statistics4SnapShotInitialization");
		
		return map;
	}
	
	@DataProvider(name="statistics4SnapShot4CityInitializationDataProvider")
	public static Object[][] Statistics4SnapShot4CityInitializationDataProvider(ITestContext context) throws JsonSyntaxException, JsonIOException, InstantiationException, IllegalAccessException, IOException, SQLException
	{
		LinkedHashMap map = new LinkedHashMap();
		map = (LinkedHashMap) ((LinkedHashMap)Statistics4SnapShotInitializationDataProvider(context)).clone();
		
		map.put("area","city");
		map.put("cityID", testDataContent.read("$.Statistics4SnapShotInitialization.Statistics4CityMarketTrendsAPIRequestInfo.inputParameter4API.cityID").toString());
		map.put("marketTrendsAPIRequestInfo", testDataContent.read("$.Statistics4SnapShotInitialization.Statistics4CityMarketTrendsAPIRequestInfo"));
		Object[][] obj = { { map } };
		return obj;
	}
	@DataProvider(name="statistics4SnapShot4ZipCodeInitializationDataProvider")
	public static Object[][] Statistics4SnapShot4ZipCodeInitializationDataProvider(ITestContext context) throws JsonSyntaxException, JsonIOException, InstantiationException, IllegalAccessException, IOException, SQLException
	{
		LinkedHashMap map = new LinkedHashMap();
		map = (LinkedHashMap) ((LinkedHashMap)Statistics4SnapShotInitializationDataProvider(context)).clone();
		
		map.put("area","zipcode");
		map.put("zipcode", testDataContent.read("$.Statistics4SnapShotInitialization.Statistics4ZipCodeMarketTrendsAPIRequestInfo.inputParameter4API.zipCode").toString());
		map.put("marketTrendsAPIRequestInfo", testDataContent.read("$.Statistics4SnapShotInitialization.Statistics4ZipCodeMarketTrendsAPIRequestInfo"));
		Object[][] obj = { { map } };
		return obj;
	}
	@DataProvider(name="statistics4SnapShot4NeighborhoodInitializationDataProvider")
	public static Object[][] Statistics4SnapShot4NeighborhoodInitializationDataProvider(ITestContext context) throws JsonSyntaxException, JsonIOException, InstantiationException, IllegalAccessException, IOException, SQLException
	{
		LinkedHashMap map = new LinkedHashMap();
		map = (LinkedHashMap) ((LinkedHashMap)Statistics4SnapShotInitializationDataProvider(context)).clone();
		
		map.put("area","neighborhood");
		map.put("neighborhoodID", testDataContent.read("$.Statistics4SnapShotInitialization.Statistics4NeighborhoodMarketTrendsAPIRequestInfo.inputParameter4API.neighborhoodID").toString());
		map.put("marketTrendsAPIRequestInfo", testDataContent.read("$.Statistics4SnapShotInitialization.Statistics4NeighborhoodMarketTrendsAPIRequestInfo"));
		Object[][] obj = { { map } };
		return obj;
	}
	
	public static LinkedHashMap GetNearbySearchAPIRequestInfoFromES(LinkedHashMap requestData4API) throws JsonSyntaxException, JsonIOException, InstantiationException, IllegalAccessException, IOException
	{
		LinkedHashMap requestInfoForInitializeNearBySearchProperties = new LinkedHashMap();
		
		//JsonContext testDataContent= ((JsonContext)jsonManager.GetJsonObjectByJPath("data/DataInitialization/NearBySearch.json"));
		
		LinkedHashMap testdata = requestData4API;
		JsonContext testDataJsonContext= new JsonContext();
		testDataJsonContext.parse(testdata);
		String apiResponse = CommonAPITest.getAPIResponse("config/ESAPIs.json",testdata);
		List<String> listingIDs = new ArrayList(JsonPath.read(apiResponse, "$.._id"));
		List<String> propertyIDs = new ArrayList(JsonPath.read(apiResponse, "$..propertyId"));
		requestInfoForInitializeNearBySearchProperties.put("listingIDs", StringUtils.join(listingIDs,",")) ;
		requestInfoForInitializeNearBySearchProperties.put("propertyIDs", StringUtils.join(propertyIDs,",")) ;
		return requestInfoForInitializeNearBySearchProperties;
	}
	

	
	
	

}
