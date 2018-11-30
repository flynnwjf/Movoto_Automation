package com.movoto.scripts.agent;

import java.util.Map;

import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.AgentTestDataProvider;

public class LeadCreationInSalesForceForAndroid extends BaseTest {
  

	@Test(dataProvider = "LeadCreationData", dataProviderClass = AgentTestDataProvider.class, priority = 1)
	public void createLeadInSalesforce(Map<String, Object> data) {
		if (data != null) {
			
			scenarios.createLeadInSalesforceForAndroid(data);
			  
		}
	}
}
