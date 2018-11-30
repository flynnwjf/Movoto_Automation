package com.movoto.scripts.dsp;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.utils.JSONParserForAutomationNG;

public class Reg_106_VerifyThatReviewsSection extends BaseTest  {
	JSONParserForAutomationNG jsonParser;
    JSONObject jsonObj;
    WebDriver wDriver;  
	 
   @Test
  @Parameters("dataProviderPath")
   public void verifyThatReviewsSection(String dataProviderPath)
	  {
		init(dataProviderPath);
		if (jsonObj != null) {
			verifyThatReviewsSection(jsonObj);
		}
		else{
			  Assert.assertFalse(false);
		  }
	}
	  
	public void verifyThatReviewsSection(JSONObject Data) {
		// Verify avgQuality/reviewsqty is the same with <ReviewSummary_API>
		// response
		library.wait(10);
		verifyReviewAndAverageQuantityWithAPI(Data);

		// Verify "Reviews for <Schools Name>" is displayed
		verifySchoolName(Data);

		// Verify the total count of all reviews is <= 10
		totalCountAllReview();

		// Veriy "All <Schools Name> Reviews" is displayed and the url contains
		// "http://www.greatschools.org/" and "<Schools Name>/reviews/"
		verifyUrl();

		// Click on Read Full Review
		clickonReadFullReview(Data);

		// Verify the quality/ReviewContent/submittedbywho of each review is the
		// same with <ReviewSummary_API>
		verifyQualityReviewContentSubmiitedByWho(Data);

		// Verify the detail hide
		verifyReviewSectionAfterClickUP();
	}

	public void init(String jsonPath)
	{
		try {
			jsonParser = new JSONParserForAutomationNG(jsonPath);
			jsonObj = jsonParser.getNode("baseInfo");
		} catch (Exception exc) {
			System.out.println("Exception in BasicInfo:init() ->" + exc.getMessage());
		}
	}

	

	public void verifyReviewAndAverageQuantityWithAPI(JSONObject Data) {
		// VerifyavgQuality/reviewsqty is the same with <ReviewSummary_API response
		
		String response =  getResponseFromApi(Data, "ReviewSumarry_API");

		// Verify for the review Quantity with API
		library.wait(5);
		String review = library.getTextFrom("SCHOOL.review");
		String reviewQuantityUI[] = review.split(" ");
		String reviewquantity = (String) library.getValueFromJson("$.[0].count", response).toString();
		int reviewquantityAPI = Integer.parseInt(reviewquantity);
		Assert.assertEquals(Integer.parseInt(reviewQuantityUI[0].substring(1)), reviewquantityAPI);

		// Verify for the Average Quantity with API
		int avgQuantityUI = library.getElementCount("SCHOOL.averageQuantity");
		Integer avgQuantity = (Integer) library.getValueFromJson("$.[0].avgQuality", response);
		int avgQuantitynApi = (int) avgQuantity;
		Assert.assertEquals(avgQuantityUI, avgQuantitynApi);
	}
	          
	  public void verifySchoolName(JSONObject Data)
	   {
		library.waitForElement("REVIEW.schoolName");
		scrollElement("REVIEW.schoolName");
		String verifyReviewSchoolNameUI = library.getTextFrom("REVIEW.schoolName");
		System.out.println(verifyReviewSchoolNameUI);
		String SchooName = Data.get("SchoolName").toString();
		System.out.println(SchooName);
		Assert.assertTrue(verifyReviewSchoolNameUI.contains(SchooName), "Verify School Name");
	    }

	 public void totalCountAllReview()
	 {
		// Verify the total count of all reviews is <= 10
		scrollElement("REVIEW.schoolName");
		int totalcountAllreview = library.getElementCount("REVIEW.reviewpanel");
		Assert.assertTrue(totalcountAllreview < 10, "total count all review is verified");
	  }
	public void verifyUrl() {
		scrollElement("REVIEW.middleschool");
		boolean flag = library.verifyPageContainsElement("REVIEW.middleschool");
		Assert.assertTrue(flag, "School Name varifed");
		//library.getDriver().findElement(By.linkText("All James Wright Middle School Reviews")).click();
		WebDriverWait wait = new WebDriverWait(library.getDriver(), 250); 
		//WebElement elements = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("All James Wright Middle School Reviews")));
	    //elements.click();
	    //wait.until(ExpectedConditions.urlContains("http://www.greatschools.org"));
		String currenturl = library.getUrl();
		//String currenurl = library.getDriver().getCurrentUrl();
		Assert.assertTrue(currenturl.contains("http://www.greatschools.org"),"url contains    http://www.greatschools.org");
		Assert.assertTrue(currenturl.contains("James-Wright-Middle-School/reviews/"),"url contains    James-Wright-Middle-School/reviews/");
	}

	public void clickonReadFullReview(JSONObject Data)
	{
		library.getDriver().navigate().to(Data.get("ApplicationUrl").toString());
		library.waitForElement("REVIEW.middleschool");
		scrollElement("REVIEW.middleschool");
		library.scrollToElement("REVIEW.schoolName");
		library.wait(5);
		boolean flag=library.verifyPageContainsElement("REVIEW.readfullreview");
		Assert.assertTrue(flag, "verified the full review");
		library.isJSEClicked("REVIEW.readfullreview");
	}
	public String getResponseFromApi(JSONObject Data, String ApiName) {
		  library.setRequestHeader("X-MData-Key", Data.get("X-MData-Key").toString());
		  String response = library.HTTPGet(Data.get(ApiName).toString());
		  return response;
		}
	

	public void verifyQualityReviewContentSubmiitedByWho(JSONObject Data) {
		// Verify the quality/ReviewContent/submittedbywho of each review is the
		// same with <ReviewSummary_API>
		String response = getResponseFromApi(Data, "ReviewDetails_API");
		library.wait(15);
		scrollElement("REVIEW.schoolName");

		// Verify 1st quality
		int quality1stUI = library.getElementCount("REVIEW.reviewqualitya1st");
		String quality1st = library.getValueFromJson("$.[0].quality", response).toString();
		Assert.assertEquals(quality1stUI, Integer.parseInt(quality1st));

		// Verify 2nd quality
		int quality2ndUI = library.getElementCount("REVIEW.reviewqualitya2nd");
		String quality2nd = library.getValueFromJson("$.[1].quality", response).toString();
		Assert.assertEquals(quality2ndUI, Integer.parseInt(quality2nd));

		// Verify 1st submittedbyWho
		String subimitedbywhoUI = library.getTextFrom("REVIEW.submitedbyparent");
		String subimitedbywhoUIAPI = library.getValueFromJson("$.[0].who", response).toString();
		Assert.assertTrue(subimitedbywhoUI.contains(subimitedbywhoUIAPI), "verify the submit by who");

		// Verify 2nd submittedbyWho
		String subimitedbywho2ndUI = library.getTextFrom("REVIEW.reviewquantity.fullreivew");
		String subimitedbywhoUI2ndAPI = library.getValueFromJson("$.[1].who", response).toString();
		Assert.assertTrue(subimitedbywho2ndUI.contains(subimitedbywhoUI2ndAPI), "Verify the submit by who");

		// Verify 1st reviewcomment
		String ReviewCommentUI = library.getTextFrom("REVIEW.reviewcomment");
		String ReviewCommentAPI = library.getValueFromJson("$.[0].comments", response).toString();
		Assert.assertTrue(ReviewCommentUI.contains(ReviewCommentAPI.replace("  ", " ").trim()));

		// Verify 2nd reviewcomment
		String ReviewComment2ndUI = library.getTextFrom("REVIEW.reviewcomment1");
		String ReviewComment2ndAPI = library.getValueFromJson("$.[1].comments", response).toString();
		Assert.assertTrue(ReviewComment2ndUI.contains(ReviewComment2ndAPI), "verified 2nd review comment");
	}

	public void scrollElement(String element) 
	{   
		library.waitForElement(element);
		JavascriptExecutor jse = (JavascriptExecutor) library.getDriver();
		WebElement elements = library.findElement(element);
		jse.executeScript("arguments[0].scrollIntoView();", elements);
	}

	public void verifyReviewSectionAfterClickUP() {
		// Verify the detail hide
		library.scrollToElement("REVIEW.hidelink");
		library.wait(5);
		library.click("REVIEW.hidelink");
		library.wait(5);
		boolean flag = library.verifyPageNotContainsElement("REVIEW.reviewcomment");
		Assert.assertTrue(flag, "Review comment section not visibile after click up");
	}
}
