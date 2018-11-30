package com.movoto.scripts.consumer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.reflect.TypeToken;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jayway.jsonpath.JsonPath;
import com.movoto.context.ConfigurationManager;
import com.movoto.fixtures.FixtureLibrary;
import com.movoto.scripts.BaseTest;
import com.movoto.scripts.CommonScenariosManager;
import com.movoto.scripts.consumer.Library.*;
import com.movoto.scripts.consumer.Library.utilities.Assertion;
import com.movoto.scripts.consumer.Library.utilities.utilities;
import com.movoto.scripts.consumer.Library.utilities.utilities.FileManager;
import com.movoto.scripts.consumer.Library.utilities.utilities.JsonManager;
import com.movoto.scripts.consumer.Library.TestModels.TestPage;
import com.movoto.scripts.data.SiteMapDataProvider;
import com.movoto.scripts.data.MapSearchDataProvider;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;

public class SiteMapBaseTest extends TestPage {

	private String SiteMapURL;
	private String SiteMapName;
	private String TestState;
	private String TestData;
	private int circle=0;
	public SiteMapBaseTest(String testData,String pageURL,String testState) {
		this.TestData =testData;
		this.SiteMapURL = pageURL;
		this.TestState = testState;
		this.SiteMapName = ((LinkedHashMap)JsonPath.read(testData, "$")).get("pageName").toString();
	}
	private static JsonManager jsonManager = new utilities.JsonManager();
	
	@BeforeMethod
	public void beforeMehtod(Method method) {
		this.library = new ConfigurationManager().createContext("SiteMapBaseTest");
		library.setCurrentTestMethod(method.getName());
		scenarios = CommonScenariosManager.getCommonScenarios(thiscontext, library);
	}
	/*
	@BeforeMethod
	public void beforeMehtod(Method method) {
		System.out.println("BEFORE METHOD in BASE TEST");
		library.setCurrentTestMethod(method.getName()+String.valueOf(circle));
		scenarios = CommonScenariosManager.getCommonScenarios(thiscontext, library);
	}
	*/
	@Test
	public void SiteMapTest(ITestContext context) throws Exception
	{
		this.library.getDriver().get(this.SiteMapURL);
		String result;
		if (!this.SiteMapName.equals("Listings_Cities"))
		{
			if (this.SiteMapName.equals("Demographics_Cities"))
			{
				System.out.println("");
			}
			List<SiteMap> siteMapModels_Actual = new ArrayList<SiteMap>();
			List<SiteMap> siteMapModels_Expect = new ArrayList<SiteMap>();
			siteMapModels_Actual = this.BuildModels(SiteMap.class,this.SiteMapName);
			siteMapModels_Expect = (List<SiteMap>)SiteMapDataProvider.SiteMapArray(SiteMap.class,new TypeToken<List<SiteMap>>(){}.getType(),this.TestData,this.TestState);
			result= new Assertion().areEqual(siteMapModels_Expect,siteMapModels_Actual);
			
			if (!result.equals("[]"))
			{
				System.out.println(result);
				FileManager.WriteFileContent(String.format("test-output/%s_%s.json",this.TestState,this.SiteMapName),result);
			}
			Assert.assertEquals(result, "[]", result);
		}
		else
		{
			SiteMap siteMapModel_Actual = new SiteMap();
			SiteMap siteMapModel_Expect = new SiteMap();
			siteMapModel_Actual = this.BuildModel(SiteMap.class,this.SiteMapName);
			siteMapModel_Expect =(SiteMap)SiteMapDataProvider.SiteMap(SiteMap.class,this.TestData,this.TestState);
			
			result= new Assertion().areEqual(siteMapModel_Expect,siteMapModel_Actual);
			
			if (!result.equals("{}"))
			{
				System.out.println(result);
				FileManager.WriteFileContent(String.format("test-output/%s_%s.json",this.TestState,this.SiteMapName),result);
			}
			Assert.assertEquals(result, "{}", result);
		}
		System.out.println(result);
		
	}

	@AfterMethod
	public void cleanUp() {
		if (this.library != null) {
			//scenarios.logOut();
			this.library.quit();
		}
	}
}
