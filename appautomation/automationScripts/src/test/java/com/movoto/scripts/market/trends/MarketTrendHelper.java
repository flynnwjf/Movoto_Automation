package com.movoto.scripts.market.trends;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;
import org.testng.Assert;

import com.movoto.fixtures.FixtureLibrary;

public class MarketTrendHelper {
  protected FixtureLibrary library;

  public MarketTrendHelper(FixtureLibrary library) {
    this.library = library;
  }

  // Returns a port from url
  public String init(String url) {
    return extractPort(url);
  }


  public void verifyUrlListings(JSONObject data, String pagename, String url) {
    library.wait(7);
    String neighbourUrl = library.getAttributeOfElement("href", pagename + ".listing");
    String neighbourUrlend = neighbourUrl.split(init(url))[1];
    library.wait(1);
    StringBuilder sb = new StringBuilder();
    sb.append("/").append(data.get(pagename + ".city").toString().toLowerCase()).append("-")
        .append(data.get(pagename + ".state").toString().toLowerCase()).append("/")
        .append(data.get(pagename + ".neighbourHood").toString().toLowerCase()).append("/");
    library.wait(1);
    System.out.println("neighbourUrlend:" + neighbourUrlend + ",str:" + sb.toString());
    Assert.assertEquals(neighbourUrlend.trim(), sb.toString());
  }

  /*
   * Verify url of "Schools" under map ends with schools/<City>-<State>
   */

  public void urlUnderSchoolValidation(JSONObject data, String pageurl, String pageName) {

    String schoolsUrl = library.getAttributeOfElement("href", pageName + ".schoolurl")
        .split(extractPort(pageurl))[1];

    String endUrl = "/schools/" + data.get(pageName + ".city").toString() + "-"
        + data.get(pageName + ".state").toString() + "/";
    Assert.assertTrue(schoolsUrl.equals(endUrl),
        "Url of Schoolsundermap ends with schools/City-State verified");
  }

  /*
   * Test Case 3 // * Click tag "All Types" "Single Family" "Condo/Twnhm" Verify data in Market
   * Snapshot/Market // * Statistics/Market Data sections are matched to <market-trends_cityAPI> //
   */
  public void marketSnapShotDataValidation(JSONObject data, String type, String apiType,
      String snapShotResp, String pageName) {

    for (int j = 1; j <= 3; j++) {
      if (j == 1) {
        library.isJSEClicked(pageName + ".snapshotlevelOne");
        library.wait(3);
        type = "alltype";
        apiType = "allType";
      } else if (j == 2) {
        library.isJSEClicked(pageName + ".snapshotleveltwo");
        library.wait(3);
        type = "single";
        apiType = "singleType";
      } else {
        library.isJSEClicked(pageName + ".snapshotlevelthree");
        library.wait(3);
        type = "condo";
        apiType = "condoType";
      }
      library.wait(2);
      int allTypeCount = library.getElementCount("xpath->(.//*[@class='allType']/tr)");
      library.wait(2);
      for (int i = 1; i <= allTypeCount; i++) {
        library.wait(1);
        switch (i) {
          case 1:
            if (pageName.equals("MARKET_TREND_ZIP_CODE_PAGE")
                || pageName.equals("MARKET_TREND_NEIGHBOURHOOD_PAGE")) {
              library.wait(1);
              String allTypeTotalInvOneMonthAgo = library
                  .getValueFromJson("$.one_month_ago." + apiType + "TotalInventory", snapShotResp)
                  .toString();

              if (!StringUtils.isEmpty(library.getTextFrom("xpath->.//*[@id='" + type + "']/tr[" + i + "]/td[3]").replaceAll("[—]", "")))
                Assert.assertEquals(allTypeTotalInvOneMonthAgo,
                    library.getTextFrom("xpath->.//*[@id='" + type + "']/tr[" + i + "]/td[3]").split("\n")[0].split(" ")[0]);
            } else {
              String allTypeTotalInv = library
                  .getValueFromJson("$.one_year_ago." + apiType + "TotalInventory", snapShotResp)
                  .toString();
              library.wait(1);
              Assert.assertEquals(allTypeTotalInv,
                  library.getTextFrom("xpath->.//*[@id='" + type + "']/tr[" + i + "]/td[4]").split("\n")[0].split(" ")[0]);
            }
            break;
          case 2:
            if (pageName.equals("MARKET_TREND_ZIP_CODE_PAGE")
                || pageName.equals("MARKET_TREND_NEIGHBOURHOOD_PAGE")) {
              library.wait(1);
              String medianPriceOneMonthAgo =
                  library.getTextFrom("xpath->.//*[@id='" + type + "']/tr[" + i + "]/td[3]").split("\n")[0].split(" ")[0];
              String allTypeTotalInvOneMonthAgo = library
                  .getValueFromJson("$.one_month_ago." + apiType + "MedianListPrice", snapShotResp)
                  .toString();

              library.wait(1);
              if (!StringUtils.isEmpty(library.getTextFrom("xpath->.//*[@id='" + type + "']/tr[" + i + "]/td[3]").replaceAll("[—]", "")))
                Assert.assertEquals(allTypeTotalInvOneMonthAgo,
                    medianPriceOneMonthAgo.replaceAll("[$,]", ""));
            } else {
              String str2 =
                  library.getTextFrom("xpath->.//*[@id='" + type + "']/tr[" + i + "]/td[4]");
              String medianPrice = library
                  .getValueFromJson("$.one_year_ago." + apiType + "MedianListPrice", snapShotResp)
                  .toString();
              library.wait(1);
              Assert.assertEquals(medianPrice, str2.replaceAll("[$,]", "").split("\n")[0].split(" ")[0]);

            }

            break;
          case 3:
            if (pageName.equals("MARKET_TREND_ZIP_CODE_PAGE")
                || pageName.equals("MARKET_TREND_NEIGHBOURHOOD_PAGE")) {
              library.wait(1);
              String distressedOneMonthAgo =
                  library.getTextFrom("xpath->.//*[@id='" + type + "']/tr[" + i + "]/td[3]").split("\n")[0].split(" ")[0];

              String allTypeTotalInvOneMonthAgo =
                  library.getValueFromJson("$.one_month_ago." + apiType + "DistressedPercent",
                      snapShotResp).toString();
              library.wait(1);

              if (!StringUtils.isEmpty(library.getTextFrom("xpath->.//*[@id='" + type + "']/tr[" + i + "]/td[3]").replaceAll("[—]", "")))
                Assert.assertEquals(allTypeTotalInvOneMonthAgo, distressedOneMonthAgo);
            } else {

              String str3 =
                  library.getTextFrom("xpath->.//*[@id='" + type + "']/tr[" + i + "]/td[4]");

              String distressed = library
                  .getValueFromJson("$.one_year_ago." + apiType + "DistressedPercent", snapShotResp)
                  .toString();
              library.wait(1);
              Assert.assertEquals(distressed, str3.split("\n")[0].split(" ")[0]);
            }

            break;
          case 4:
            if (pageName.equals("MARKET_TREND_ZIP_CODE_PAGE")
                || pageName.equals("MARKET_TREND_NEIGHBOURHOOD_PAGE")) {
              library.wait(1);
              String medianDomOneMonthAgo =
                  library.getTextFrom("xpath->.//*[@id='" + type + "']/tr[" + i + "]/td[3]").split("\n")[0].split(" ")[0];

              String allTypeTotalInvOneMonthAgo =
                  library.getValueFromJson("$.one_month_ago." + apiType + "MedianDom", snapShotResp)
                      .toString();
              library.wait(1);

              if (!StringUtils.isEmpty(library.getTextFrom("xpath->.//*[@id='" + type + "']/tr[" + i + "]/td[3]").replaceAll("[—]", "")))
                Assert.assertEquals(allTypeTotalInvOneMonthAgo, medianDomOneMonthAgo);

            } else {
              String str4 =
                  library.getTextFrom("xpath->.//*[@id='" + type + "']/tr[" + i + "]/td[4]");
              String medianDays =
                  library.getValueFromJson("$.one_year_ago." + apiType + "MedianDom", snapShotResp)
                      .toString();
              library.wait(1);
              Assert.assertEquals(medianDays, str4.split("\n")[0].split(" ")[0]);
            }

            break;
          case 5:

            if (pageName.equals("MARKET_TREND_ZIP_CODE_PAGE")
                || pageName.equals("MARKET_TREND_NEIGHBOURHOOD_PAGE")) {
              library.wait(1);
              String medianHouse =
                  library.getTextFrom("xpath->.//*[@id='" + type + "']/tr[" + i + "]/td[3]").split("\n")[0].split(" ")[0];

              String allTypeTotalInvOneMonthAgo = library
                  .getValueFromJson("$.one_month_ago." + apiType + "MedianHouseSize", snapShotResp)
                  .toString();
              library.wait(1);

              if (!StringUtils.isEmpty(library.getTextFrom("xpath->.//*[@id='" + type + "']/tr[" + i + "]/td[3]").replaceAll("[—]", "")))
                Assert.assertEquals(allTypeTotalInvOneMonthAgo, medianHouse.replaceAll(",", ""));
            } else {
              String str5 =
                  library.getTextFrom("xpath->.//*[@id='" + type + "']/tr[" + i + "]/td[4]");
              String medianHouseSize = library
                  .getValueFromJson("$.one_year_ago." + apiType + "MedianHouseSize", snapShotResp)
                  .toString();
              library.wait(1);
              Assert.assertEquals(medianHouseSize, str5.replaceAll(",", "").split("\n")[0].split(" ")[0]);
            }

            break;
          case 6:

            if (pageName.equals("MARKET_TREND_ZIP_CODE_PAGE")
                || pageName.equals("MARKET_TREND_NEIGHBOURHOOD_PAGE")) {
              library.wait(1);
              String medianSqft =
                  library.getTextFrom("xpath->.//*[@id='" + type + "']/tr[" + i + "]/td[3]").split("\n")[0].split(" ")[0];

              String allTypeTotalInvOneMonthAgo =
                  library.getValueFromJson("$.one_month_ago." + apiType + "MedianPricePerSqftHouse",
                      snapShotResp).toString();

              if (!StringUtils.isEmpty(library.getTextFrom("xpath->.//*[@id='" + type + "']/tr[" + i + "]/td[3]").replaceAll("[—]", "")))
                Assert.assertEquals(allTypeTotalInvOneMonthAgo, medianSqft);
            } else {
              String str6 =
                  library.getTextFrom("xpath->.//*[@id='" + type + "']/tr[" + i + "]/td[4]");
              String medianSqft =
                  library.getValueFromJson("$.one_year_ago." + apiType + "MedianPricePerSqftHouse",
                      snapShotResp).toString();
              library.wait(1);
              Assert.assertEquals(medianSqft, str6.split("\n")[0].split(" ")[0]);
            }

            break;
          default:
            System.out.println(
                "Verified all fileds in Market Trends Snapshot:" + apiType + ",data Successfully");
        }
      }
    }

  }

  /*
   * Verify URL of home under Sale
   */
  public void urlHomeSaleValidation(JSONObject data, String pageUrl, String pageName) {
    String urlUi;
    
    library.wait(1);
    String zipCode = data.get(pageName + ".zipcode").toString();
    String homeSaleUrl = library.getAttributeOfElement("href", pageName + ".commandBtn");
    String endUrl = homeSaleUrl.split(extractPort(pageUrl))[1];

    if (StringUtils.isEmpty(zipCode)) {
      urlUi = "/" + data.get(pageName + ".city").toString() + "-"
          + data.get(pageName + ".state").toString() + "/";

    } else {
      urlUi = "/" + data.get(pageName + ".state").toString() + "/"
          + data.get(pageName + ".zipcode").toString() + "/";
    }


    Assert.assertTrue(endUrl.equals(urlUi), "verified home for Sale url");
  }

  /*
   * Verify url of "Listings" under map ends with <City>-<State>
   */
  public void urlListingUndermapValidation(JSONObject data) {


    library.wait(5);
    String listingUrl = library.getAttributeOfElement("href", "MARKET_TREND_CITYPAGE.listingurl");
    String end = listingUrl.split("3025")[1];
    Assert.assertTrue(
        end.endsWith(data.get("MARKET_TREND_CITYPAGE.city").toString() + "-"
            + data.get("MARKET_TREND_CITYPAGE.state").toString() + "/"),
        "Url of Listings undermap ends with schools/City-State verified");

  }



  public void urldemographicValidation(String demgApiResp, String urlClick) {
    String dmgInfo = null;
    String port = extractPort(urlClick);
    library.wait(3);
    if (StringUtils.isEmpty(demgApiResp)) {
      library.isJSEClicked("MARKET_TREND_CITYPAGE.demographicPopUpClose");
      System.out.println("Demographic API response is empty");
      return;
    } else {
      dmgInfo = urlClick.split(port)[1];
      library.wait(8);
      Assert.assertEquals(dmgInfo,
          "/" + library.getValueFromJson("$.[0].demographicsPageUrl", demgApiResp).toString());
      library.isJSEClicked("MARKET_TREND_CITYPAGE.demographicPopUpClose");

    }

  }

  /*
   * Verify url for each level in bread-crumbs is as below:
   * 
   * 1st: <HomePageURL>
   * 
   * 2nd: <HomePageURL>/<State>
   * 
   * 3rd: <HomePageURL>/<State>/<City>
   * 
   * 4th: Current page without link.
   */
  public void urlUnderListingValidation(JSONObject data, int crumbsCount, String crumbsPath,
      String url, String state, String city) {
    for (int i = 1; i <= crumbsCount; i++) {
      switch (i) {
        case 1:
          /*
           * 1st: <HomePageURL>
           */
          library.wait(2);
          String val1 = data.get(url).toString();
      //    String val2 = library.getAttributeOfElement("href", crumbsPath + "/" + "li[" + i + "]/a");

          Assert.assertEquals(data.get(url).toString(),
              library.getAttributeOfElement("href", crumbsPath + "/" + "li[" + i + "]/a"));

          break;
        case 2:
          /*
           * 2nd: <HomePageURL>/<State>
           */
          String homePageURLState = data.get(url).toString() + state + "/";
          Assert.assertEquals(homePageURLState,
              library.getAttributeOfElement("href", crumbsPath + "/" + "li[" + i + "]/a"));

          break;
        case 3:
          /*
           * 3rd: <HomePageURL>/<State>/<City>
           */
          String homepageStateCity = data.get(url).toString() + city + "-" + state + "/";
          Assert.assertEquals(homepageStateCity,
              library.getAttributeOfElement("href", crumbsPath + "/" + "li[" + i + "]/a"));

          break;
        case 4:
          /*
           * 4th: current page without link
           */

          Assert.assertEquals(null,
              library.getAttributeOfElement("href", crumbsPath + "/" + "li[" + i + "]/span"));
          break;
      }
    }

  }


  /*
   * Verify url of "Change Neighborhood" button is <HomePageURL>/market-trends/
   */
  public void urlChangeCityUrlBtn(JSONObject data, String pageName) {

    String chngCityurl = library.getAttributeOfElement("href", pageName + ".changeCitybtn");
    Assert.assertTrue(chngCityurl
        .equalsIgnoreCase(data.get(pageName + ".homePageurl").toString() + "market-trends/"));
  }

  private String extractPort(String url) {
    String arr[] = url.split(":");
    return arr[2].split("/")[0];
  }
}
