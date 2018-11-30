package com.movoto.scripts.market.trends;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.utils.JSONParserForAutomationNG;
/*
 * Govind Kalria
 */

public class Reg_224_NeighborhoodMarketTrendsCorrectlyDisplays extends BaseTest {

  JSONParserForAutomationNG jsonParser;
  JSONObject jsonObj;
  MarketTrendHelper marketTrendHelper;
  String pageUrl;
  private String pageName;

  @Test
  @Parameters("dataProviderPath")
  public void verifySearchInfo(String dataProviderPath) throws ParseException {
    init(dataProviderPath);
    if (jsonObj != null) {
      verifyUrlsNewListing(jsonObj);
      verifyDemographicClick(jsonObj);
      verifyListingUnderMapUrl(jsonObj);
      verifySchoolurl(jsonObj);
      verifyURLUnderBreadCrumbs(jsonObj);
      verifyUrlForHomeSale(jsonObj);
      verifyChangeCityUrlBtn(jsonObj);
      verifyBrowseMarketArea(jsonObj);
      verifyMarketSnapShotData(jsonObj);
      verifyArticlesInGetToKnowSection(jsonObj);

    } else {
      Assert.assertFalse(false);
    }

  }


  @SuppressWarnings("unchecked")
  public String apiResp() {
    String contentType = String.valueOf(jsonObj.get("MARKET_TREND_NEIGHBOURHOOD_PAGE.contentType"));
    String API = String.valueOf(jsonObj.get("MARKET_TREND_NEIGHBOURHOOD_PAGE.articleAPI"));
    library.setContentType(contentType);
    Map<String, Object> apidata = new HashMap<>();
    apidata = (Map<String, Object>) jsonObj.get("MARKET_TREND_NEIGHBOURHOOD_PAGE.postdata");
    String response = library.HTTPPost(API, apidata);
    return response;
  }

  public void verifyArticlesInGetToKnowSection(JSONObject data) {
    String pageUrl = data.get("MARKET_TREND_NEIGHBOURHOOD_PAGE.url").toString();
    String port = extractPort(pageUrl);
    String response = apiResp();
    int articleCount =
        library.getElementCount("MARKET_TREND_NEIGHBOURHOOD_PAGE.getToKnowArticleCount");
    int j = 0;
    for (int i = 1; i <= articleCount; i++, j++) {
      String uiUrl = library.getAttributeOfElement("href",
          data.get("MARKET_TREND_NEIGHBOURHOOD_PAGE.getToKnowArticles").toString() + "/li[" + i
              + "]/a")
          .split(port)[1];
      String uiTitle = library
          .getTextFrom(data.get("MARKET_TREND_NEIGHBOURHOOD_PAGE.getToKnowArticles").toString()
              + "/li[" + i + "]/a");
      library.wait(1);
      String jsonTitle = library.getValueFromJson("$.data[" + j + "].title", response).toString();
      String jsonUrl = "/" + library.getValueFromJson("$.data[" + j + "].URL", response).toString();

      Assert.assertEquals(uiUrl, jsonUrl);
      Assert.assertEquals(uiTitle, jsonTitle);
    }

  }

  private void verifyURLUnderBreadCrumbs(JSONObject data) {
    int crumbsCount = library.getElementCount(pageName + ".crumbsCount");
    String crumbsPath = data.get("MARKET_TREND_NEIGHBOURHOOD_PAGE.crumbsPath").toString();
    String url = pageName + ".homePageurl";
    String state = data.get(pageName + ".state").toString();
    String city = data.get(pageName + ".city").toString();
    marketTrendHelper.urlUnderListingValidation(data, crumbsCount, crumbsPath, url, state, city);

  }

  private void init(String dataProviderPath) {
    try {
      pageName = "MARKET_TREND_NEIGHBOURHOOD_PAGE";
      marketTrendHelper = new MarketTrendHelper(library);
      jsonParser = new JSONParserForAutomationNG(dataProviderPath);
      jsonObj = jsonParser.getNode("neighbourhoodInfo");
      pageUrl = jsonObj.get("MARKET_TREND_NEIGHBOURHOOD_PAGE.url").toString();
      library.setRequestHeader("X-MData-Key", jsonObj.get("X-MData-Key").toString());

    } catch (Exception exc) {
      System.out.println("Exception in BasicInfo:init() ->" + exc.getMessage());
    }

  }


  private void verifyListingUnderMapUrl(JSONObject data) {
    String url = data.get(pageName + ".url").toString();
    marketTrendHelper.verifyUrlListings(data, pageName, url);
  }

  private void verifyBrowseMarketArea(JSONObject data) {
    Assert.assertEquals(
        data.get(pageName + ".homePageurl").toString() + data.get(pageName + ".state").toString()
            + "/" + "market-trends/",
        library.getAttributeOfElement("href", pageName + ".browsebyMarketArea"));

  }


  private void verifyChangeCityUrlBtn(JSONObject data) {
    marketTrendHelper.urlChangeCityUrlBtn(data, pageName);
  }


  public void verifySchoolurl(JSONObject data) {
    marketTrendHelper.urlUnderSchoolValidation(data, pageUrl, pageName);
  }



  /*
   * Verify url of "Home for Sale" ends with <City>-<State>/<Neighborhood>
   */

  public void verifyUrlForHomeSale(JSONObject data) {

    String homeSaleUrl = library.getAttributeOfElement("href", pageName + ".commandBtn");
    String homeSaleUrlEnd = "/" + data.get(pageName + ".city").toString() + "-"
        + data.get(pageName + ".state").toString() + "/"
        + data.get(pageName + ".neighbourHood").toString() + "/";

    Assert.assertTrue(homeSaleUrl.endsWith(homeSaleUrlEnd));
  }

  /*
   * Verify Text under bread crumbs is Real Estate Trends and Statistics <City>
   * 
   */
  @Test
  public void verifyTextUnderBreadCums() {

    // System.out.println(xpath->(.//*[@class='nav']/ul/li/a)[1]);
    // Assert.assertTrue(library.getTextFrom("BREADCUM_TEXT").contains("MOVOTO REAL ESTATE") ==
    // true,
    // "verified text under breadcumbs");
  }

  /*
   * Verify url of "Change Neighborhood" button is <HomePageURL>/market-trends/
   */

  public void verifyUrlChangeNeighBourbutton(JSONObject data) {
    Assert.assertTrue(
        library.getAttributeOfElement("href", "CHNG_NEIGHBOUR_BTN")
            .equals(data.get("HOME_PAGE").toString() + "/market-trends/"),
        "verified url of change neighbourhood button");
  }



  /*
   * 
   * Click "Demographics" under map /* Verify pop up info is the same with <CityZipCode_API> Need to
   * clarify
   */

  public String demographicApiResp(String dmgApi) {
    String response = library.HTTPGet(dmgApi);
    return response;
  }

  public void verifyDemographicClick(JSONObject data) {
    System.out.println("verifyDemographicClick  Start");
    library.wait(3);
    String cityAPIresponse = cityAPIresp(data);

    library.wait(10);
    library.isJSEClicked("MARKET_TREND_NEIGHBOURHOOD_PAGE.demographicPopupInfo");
    library.wait(3);
    int popUpContentCnt =
        library.getElementCount("MARKET_TREND_NEIGHBOURHOOD_PAGE.popUpContentCount");

    for (int i = 0; i <= popUpContentCnt; i++) {
      library.wait(1);
      int j = i + 1;
      if (j > popUpContentCnt)
        break;
      if (library.getValueFromJson("$.[" + i + "].demographicsPageUrl", cityAPIresponse) != null) {
        String Arr[] =
            library.getValueFromJson("$.[" + i + "].demographicsPageUrl", cityAPIresponse)
                .toString().split("-");
        library.wait(2);
//        String xPathx=data.get("MARKET_TREND_NEIGHBOURHOOD_PAGE.demographicPopUp").toString();
        String str = library.getTextFrom("xpath->.//*[@id='body']/..//div[@class='linksList']/ul/li["+j+"]/a");
        str = str.toLowerCase();
        Assert.assertEquals(Arr[0], str.split(",")[0]);
      }

    }

    library.isJSEClicked("MARKET_TREND_NEIGHBOURHOOD_PAGE.popUpClose");
    library.wait(3);
    System.out.println("verifyDemographicClick  end");
  }

  public void verifyMarketSnapShotData(JSONObject data) {
    System.out.println("verifyMarketSnapShotData  Start");
    String pageName = "MARKET_TREND_NEIGHBOURHOOD_PAGE";
    String type = null;
    String apiType = null;
    String snapShotResp = snapShotIntialize(data,
        data.get("MARKET_TREND_NEIGHBOURHOOD_PAGE.snapShotAPIdaily").toString());
    library.wait(2);
    marketTrendHelper.marketSnapShotDataValidation(data, type, apiType, snapShotResp, pageName);
    System.out.println("verifyMarketSnapShotData  end");
  }

  private String snapShotIntialize(JSONObject data, String api) {
	library.setRequestHeader("X-MData-Key", data.get("X-MData-Key").toString());  
    String snapShotResp = library.HTTPGet(api);
    library.wait(3);
    return snapShotResp;
  }

  // library.getAttributeOfElement("href", crumbsPath + "/" + "li[" + i + "]/span"));
  /*
   * Verify url of "Browse by Market area" is <HomePageURL>/<state>/market-trends/
   *
   * http://alpaca.san-mateo.movoto.net:3025/va/market-trends/
   */
  // @Test
  public void verifyBrowseByMarketArea(JSONObject data) {
    System.out.println(library.getAttributeOfElement("href", "MARKET_AREA.tooltip"));
    System.out.println(data.get("HOME_PAGE").toString()
        + data.get("NEIGHBOUR_STATE").toString().toLowerCase() + "/market-trends/");
    Assert.assertEquals(library.getAttributeOfElement("href", "MARKET_AREA.tooltip"),
        data.get("HOME_PAGE").toString() + data.get("NEIGHBOUR_STATE").toString().toLowerCase()
            + "/market-trends/");
  }

  /*
   * 6. Verify Price/PropertyType /Bedrooms /Bathrooms /Address /City /CardURL /photo for all the
   * properties is the same as <NewListingsNearby_API> response
   * 
   * 
   */

  public void verifyUrlsNewListing(JSONObject jsonObj) throws ParseException {
    String response = getRespNearByAPI(jsonObj);
    scenarios.verifyNewListingPropertyWithApi(jsonObj, 4, response);
  }

  public String cityAPIresp(JSONObject data) {
	library.setRequestHeader("X-MData-Key", data.get("X-MData-Key").toString());
    String resp = library.HTTPGet(data.get("MARKET_TREND_NEIGHBOURHOOD_PAGE.cityAPI").toString());
    library.wait(2);
    return resp;
  }


  private String extractPort(String url) {
    String arr[] = url.split(":");
    return arr[2].split("/")[0];
  }

  public String getRespNearByAPI(JSONObject data) {
    String resp = library.HTTPGet(data.get("MARKET_TREND_NEIGHBOURHOOD_PAGE.newListingNearByAPI").toString());
    return resp;
  }

}
