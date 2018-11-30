package com.movoto.scripts.seller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.movoto.common.JSONManager;
import com.movoto.context.ConfigurationManager;
import com.movoto.fixtures.FixtureLibrary;
import com.movoto.scripts.data.RestAPITestDataProvider;
import com.movoto.fixtures.impl.util.FileUtil;

public class RestAPITest {

	private FixtureLibrary fixtures;

	private String BASE_URL = "http://alpaca.san-mateo.movoto.net:4005/sell/leads";
	private Map<String, String> data = new HashMap<>();

	@BeforeTest
	public void setup(ITestContext context) {
		Reporter.log("Platform: " + context.getName(), true);
		Thread.currentThread().setName(context.getName());
		fixtures = new ConfigurationManager().createContext(context.getName());
	}

	/*@Test(testName="TEST_GET")
	@Parameters({"userid"})*/
	public void testGet(int userid) {
		String url = BASE_URL + "/posts/"+userid;
		String response = fixtures.HTTPGet(url);
		Reporter.log(response, true);
	}

	@Test(dataProvider="POST_DATA",dataProviderClass=RestAPITestDataProvider.class)
	public void createLeadTest(int rowIndex) {
		
		Map<String, Object> data = readDataFromExcel(rowIndex);
		
		
		
		String response = fixtures.HTTPPost(BASE_URL, data);
		Object lead_status = JSONManager.getValueFromJson("$.lead_status", response);
		Object lead_id = JSONManager.getValueFromJson("$.lead_id", response);
		Assert.assertEquals(lead_status, 2, "Lead not created");
		Reporter.log("Verify lead status is 2");
		Reporter.log("Lead_ID: "+lead_id);
	}

	
	private Map<String, Object> readDataFromExcel(int index){
		Map<String, Object> data = new HashMap<>();
		fixtures.openExcelSheet("", "", "read");
		String d1 = fixtures.getFromExcelRowAndColumn(index, "");
		data.put("", d1);
		
		return data;
	}
}
