package com.movoto.scripts.agent;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.AgentTestDataProvider;
//import com.movoto.scripts.salesforce.CommonScenariosSF;

public class ClientsDetailsTestingWithApi extends BaseTest {

    // CommonScenariosSF sfScenarios = new CommonScenariosSF(sfLibrary);

    @Test(dataProvider = "LoginTestData", dataProviderClass = AgentTestDataProvider.class)
    public void loginTest(Map<String, String> data) {
        if (data != null) {
            scenarios.loginToAgentAppWithUsernameAndPassword(data.get("Username"), data.get("Password"));
            scenarios.verifyLoginSuccess();
        }
    }

    // Verify lead details.verify lead opportunities
    @Test(dependsOnMethods = "loginTest", dataProvider = "APITesting", dataProviderClass = AgentTestDataProvider.class)
    public void leadDetails(Map<String, Object> data) {

        scenarios.goToClientDetailsPage(data);
        // verification
        scenarios.verifyMailWithApiResponce(data);
    }

    // verify Opportunities, verify multiple opportunities
    @Test(dependsOnMethods = "leadDetails", dataProvider = "APITesting", dataProviderClass = AgentTestDataProvider.class)
    public void leadOpportunities(Map<String, Object> data) {
        scenarios.goToTransactionDetails();
        // verifycation
        scenarios.verifyOpportunitiesWithApiResponse(data);
        library.wait(5);

    }
    
    //Properties data testing with API response
    @Test(dependsOnMethods = "leadOpportunities", dataProvider = "APITesting", dataProviderClass = AgentTestDataProvider.class)
    public void notes(Map<String, Object> data) {
        scenarios.navigateToNotes();
        scenarios.setTokenAndUserId(data);
        scenarios.verifyNotesResponseWithApi(data);
    }

    // Properties data testing with Api response
    @Test(dependsOnMethods = "notes", dataProvider = "APITesting", dataProviderClass = AgentTestDataProvider.class)
    public void properties(Map<String, Object> data) {
        library.click("TRANSACTION.properties");
        scenarios.setTokenAndUserId(data);
        scenarios.verifyPropertiesResponseWithApi(data);

    }
}