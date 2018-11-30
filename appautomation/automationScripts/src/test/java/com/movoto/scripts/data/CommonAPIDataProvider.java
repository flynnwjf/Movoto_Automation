package com.movoto.scripts.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.stream.Collectors;
import java.util.*;

import org.apache.commons.collections.comparators.ComparableComparator;
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
import com.movoto.fixtures.FixtureLibrary;
import com.movoto.scripts.consumer.Library.*;
import com.movoto.scripts.consumer.Library.utilities.utilities;
import com.movoto.scripts.consumer.Library.utilities.utilities.FileManager;
import com.movoto.scripts.consumer.Library.utilities.utilities.JsonManager;
import com.movoto.utils.JSONParserForAutomationNG;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.internal.JsonContext;
public class CommonAPIDataProvider {
	@DataProvider(name="getAPITestData")
	@Parameters({"dataProviderPath"})
	public static Object[][] getAPITestData(ITestContext context) throws IOException
	{
		Reporter.log("Platform: " + context.getName(), true);
		
		String apiInfoJsonFilePath= context.getCurrentXmlTest().getParameter("dataProviderPath");
		//String apiInfo = getData(apiInfoJsonFilePath);
		LinkedHashMap apiInfosMap = new LinkedHashMap<>();
		
		apiInfosMap = (LinkedHashMap)JsonPath.read(new File(apiInfoJsonFilePath),"$");
		Object[][] obj = { { apiInfosMap } };
		return obj;
		
	}
}
