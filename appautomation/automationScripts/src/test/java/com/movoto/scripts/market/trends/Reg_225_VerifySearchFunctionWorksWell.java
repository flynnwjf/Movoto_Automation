package com.movoto.scripts.market.trends;

/*
 * Govind Kalria
 */
/*
 * Reg-225:Verify search function works well
 */
import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.utils.JSONParserForAutomationNG;

public class Reg_225_VerifySearchFunctionWorksWell extends BaseTest {

  JSONParserForAutomationNG jsonParser;
  JSONObject jsonObj;
  List<String> values;

  
  @Test
  @Parameters("dataProviderPath")
  public void verifySearchInfo(String dataProviderPath) {
    init(dataProviderPath);
    if (jsonObj != null) {
      searchValidation(jsonObj);
    } else {
      Assert.assertFalse(false);
    }

  }

  public void init(String jsonPath) {
    try {

      jsonParser = new JSONParserForAutomationNG(jsonPath);
      jsonObj = jsonParser.getNode("searchData");


    } catch (Exception exc) {
      System.out.println("Exception in BasicInfo:init() ->" + exc.getMessage());
    }
  }

  public void searchValidation(JSONObject data) {
    values = new LinkedList<>();
    values.add(data.get("SEARCH_HOMEPAGE.searchCity").toString());
    values.add(data.get("SEARCH_HOMEPAGE.searchFinancialDistrict").toString());
    values.add(data.get("SEARCH_HOMEPAGE.searchZipCode").toString());
    library.wait(3);
    String val;
    for (String value : values) {
      switch (value) {
        case "10011":
          library.wait(2);
          val = common(value, data);
          Assert.assertEquals(val, data.get("SEARCH_HOMEPAGE.searchMarketTrendZipCode").toString());
          library.wait(1);
          library.clear("SEARCH_HOMEPAGE.clearText");
          library.wait(2);
          break;
        case "new york":
          library.wait(2);
          val = common(value, data);
          Assert.assertEquals(val, data.get("SEARCH_HOMEPAGE.searchMarketTrendCity").toString());
          library.wait(1);
          library.clear("SEARCH_HOMEPAGE.clearText");
          library.wait(2);
          break;
        case "financial district":
          library.wait(2);
          val = common(value, data);
          Assert.assertEquals(val,
              data.get("SEARCH_HOMEPAGE.searchMarketTrendNeighbourHood").toString());
          library.wait(1);
          library.clear("SEARCH_HOMEPAGE.clearText");
          library.wait(2);
          break;
      }

    }
  }



  private String common(String value, JSONObject data) {
    String str;
    if (library.getCurrentPlatform().equals("Android") || library.getCurrentPlatformType().equalsIgnoreCase("IOS_WEB")) {
      library.wait(3);
      library.typeDataInto(value, "SEARCH_HOMEPAGE.searchInput");
      library.wait(5);
      library.isJSEClicked("SEARCH_HOMEPAGE_ANDRD.searchBtn");
      library.wait(5);
      str = library.getAttributeOfElement("href", "SEARCH_HOMEPAGE_ANDRD.href");
      library.wait(2);
      library.isJSEClicked("SEARCH_HOMEPAGE.close");
      library.wait(10);
    } else {
      library.typeDataInto(value, "SEARCH_HOMEPAGE.searchInput");
      library.wait(5);
      library.isJSEClicked("SEARCH_HOMEPAGE.searchBtn");
      library.wait(5);
//      library.switchToWindow();
//      library.wait(3);
      str = library.getAttributeOfElement("href", "SEARCH_HOMEPAGE.switchWindow");
      library.wait(2);
      library.isJSEClicked("SEARCH_HOMEPAGE.close");
      library.wait(3);

    }
    return str;
  }
}
