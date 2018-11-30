package com.movoto.scripts.dsp;


import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.movoto.scripts.BaseTest;
import com.movoto.utils.JSONParserForAutomationNG;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Reg_105_VerifyThatTestScoresSectionWorks extends BaseTest {
	JSONParserForAutomationNG jsonParser;
	JSONObject jsonObj;
	WebDriver wDriver;

	public void init(String jsonPath) {
		try {

			jsonParser = new JSONParserForAutomationNG(jsonPath);
			jsonObj = jsonParser.getNode("testscoresection");

		} catch (Exception exc) {
			System.out.println("Exception in TestScoreSection:init() ->"
					+ exc.getMessage());
		}
	}

	@Test
	@Parameters("dataProviderPath")
	public void VerifyThatTestScoresSectionWorks(String dataProviderPath) throws ParseException {
		init(dataProviderPath);
		if (jsonObj != null) {
			VerifyThatTestScoresSectionWorks(jsonObj);
		}
		else{
			Assert.assertTrue(false, "Please check data for provided for script");
		  }
	}

	public void VerifyThatTestScoresSectionWorks(JSONObject data) throws ParseException {
		// API Common functions
		String schoolNameAPI 	= getApiHeaderAndGetResponse(data, "BasicInfo_API");
		String testScoreAPI 	= getApiHeaderAndGetResponse(data, "TestScore_API");
		String WSASAPI 			= getApiHeaderAndGetResponse(data, "WSAS_API");
		
		// Verify "WSAS Test Scores for <School Name>" is displayed
		verifyWsasTestScoresForSchoolNamesIsDisplayed(schoolNameAPI);

		// Verify GradeNumbers is the same with <TestScore_API> response
		// Grade 6, Grade 7 and Grade 8 is same with API
		verifyGradeNumbersIsSameWithTestScoreApiResponse(testScoreAPI);

		// Verify the title and years of each chart is the same with
		// <TestScore_API> response
		verifyAllGradeTitleAndEachYearOfChart(testScoreAPI);

		// Click WSAS Scoring - Verify the text is the same with <WSAS_API> response
		verifyWsasScoringTextIsSameWithWsasApi(WSASAPI);

		// Click UP button on this section - Verify the detail info is hided
		verifyWsasTestScoresDetailInfoIsHided();

	}

	// API functions
	 	public String getApiHeaderAndGetResponse(JSONObject Data, String apiName) {
	 		library.setRequestHeader("X-MData-Key", Data.get("X-MData-Key")
	 				.toString());
	 		String response = library.HTTPGet(Data.get(apiName)
	 				.toString());
	 		return response;
	 	}

	 	public void verifyWsasTestScoresForSchoolNamesIsDisplayed(String responseSchoolName){
	 		library.wait(5);
	 		String SchoolNameAPI=library.getValueFromJson("$.name", responseSchoolName).toString();
	 		library.waitForElement("SCHOOLPAGETESTSCORESECTION.wsasheader");
	 		String SchoolNameUI=library.getTextFrom("SCHOOLPAGETESTSCORESECTION.wsasheaderschoolname");
	 		SchoolNameUI = SchoolNameUI.replace("for ", "");
	 		Assert.assertEquals(SchoolNameAPI, SchoolNameUI);
	 	}

	public void verifyGradeNumbersIsSameWithTestScoreApiResponse(String responsetestScores) {

		// Verify GradeNumbers is the same with <TestScore_API> response
		// Grade 6, Grade 7 and Grade 8 is same with API
		int iGradeCount = library.getElementCount("SCHOOLPAGETESTSCORESECTION.gradecount");
		for (int i = 1; i <= iGradeCount; i++) {
			WebElement UIGrades = library.findElement("//div[@role='Tabs']/a["+ i + "]" );
			String UIGradesText = UIGrades.getText();
			String uiGradeRemoveText = UIGradesText.replace("Grade ", "");
			String apiGrade = library.getValueFromJson("$.details[" + (i - 1) + "].grade", responsetestScores).toString();
			String apiGrade1 = apiGrade.replace("[", "");
			String apiGrade2 = apiGrade1.replace("]", "");
			String apiGrade3 = apiGrade2.replace("\"", "");
			Assert.assertEquals(uiGradeRemoveText, apiGrade3);
		}
	}

	
	public Map<String, JSONArray> getAPIResponseIntoMap(String response1) throws ParseException{
		JSONParser parser=new JSONParser();
		JSONObject jsonObject=(JSONObject)parser.parse(response1);
		JSONArray array=(JSONArray)jsonObject.get("details");
		JSONObject json=null;
		JSONArray subArray=null;
		Map<String, JSONArray> yearMap=new HashMap<String,JSONArray>();
		
		
		for(int i=0;i<array.size();i++){
			json=(JSONObject)array.get(i);
			subArray=(JSONArray)json.get("subjects");
			yearMap.put("Grade "+json.get("grade").toString(), subArray);
		}
		return yearMap;
	}
	
	// verifyAllGradeTitleAndEachYearOfChart
	public void verifyAllGradeTitleAndEachYearOfChart(String response1) throws ParseException{
		int iGradeCount 		= library.getElementCount("SCHOOLPAGETESTSCORESECTION.gradecount");
		JSONArray subArray=null;
		JSONObject jsonSub=null;
		Map<String,JSONArray> gradeWise=getAPIResponseIntoMap(response1);
		for (int i=1; i<=iGradeCount;i++){
			library.wait(5);
			library.scrollToElement("SCHOOLPAGETESTSCORESECTION.grade6text");
			library.wait(5);
			library.findElement(".//*[@id='scroePanel_0']/div[1]/a[" + i + "]").click();
			library.wait(5);
			String sGrade = library.getTextFrom(".//*[@id='scroePanel_0']/div[1]/a[" + i + "]");
			subArray=(JSONArray)gradeWise.get(sGrade);
			int iSubjectsCount = library.getElementCount("(//*[@id='scroePanel_0']//*[@class='gradeTests'])[" + i + "]/a");
			for (int j=1; j<=iSubjectsCount; j++){
				jsonSub=((JSONObject)subArray.get(j-1));
				String subName=(((JSONObject)jsonSub.get("main")).get("name")).toString();
				library.wait(2);
				String iSubject = library.getTextFrom("((//*[@id='scroePanel_0']//*[@class='gradeTests'])[" + i + "]/a)[" + j +"]");
				Assert.assertEquals(iSubject, subName, "Subject UI and API value doesnot match");
				int iYearsCount = library.getElementCount("(//*[@id='scroePanel_0']//*[@class='gradeTests'])[" + i + "]//*[@id='scroePanel_0_" + (i-1) + "_" + (j-1) + "']//*[name()='g'][6]//*[name()='text']" );
				for (int k=1; k<=iYearsCount; k++){
					library.wait(2);
					String iYear = library.getTextFrom("(//*[@id='scroePanel_0']//*[@class='gradeTests'])["+ i + "]//*[@id='scroePanel_0_"+ (i-1) +"_" + (j-1) + "']//*[name()='g'][6]//*[name()='text'][" + (k) + "]");
					JSONObject yearWise=(JSONObject)((JSONObject)jsonSub.get("main")).get("yearScore");
					Assert.assertTrue(yearWise.containsKey(iYear), "UI value matching with API");
					WebElement we1 =   library.getDriver().findElement(By.xpath("(//*[@id='scroePanel_0']//*[@class='gradeTests'])["+ i + "]//*[@id='scroePanel_0_"+ (i-1) +"_" + (j-1) + "']//div[@data-testchart-name='" + iSubject + "']/div//*[name()='svg']//*[name()='g']//*[name()='text'][" + k + "]"));
					WebElement ChartBar =   library.getDriver().findElement(By.xpath("(//*[@id='scroePanel_0']//*[@class='gradeTests'])["+ i + "]//*[@id='scroePanel_0_"+ (i-1) +"_" + (j-1) + "']//div[@data-testchart-name='" + iSubject + "']/div//*[name()='svg']//*[name()='g']//*[name()='g']//*[name()='rect'][" + k + "]"));
					library.wait(5);
					if (library.getCurrentBrowser().contains("Safari")) {
						library.mouseHoverJScript(we1);
						library.wait(5);
						library.mouseHoverJScript(ChartBar);
						library.wait(5);
					} else {
						Actions action1 = new Actions(library.getDriver());
						action1
							.moveToElement(we1)
							.moveToElement(ChartBar)
							.clickAndHold(ChartBar)
							.build()
							.perform();
					}
					WebElement iYearSubValue = library.getDriver().findElement(By.xpath("(//*[@id='scroePanel_0']//*[@class='gradeTests'])["+ i + "]//*[@id='scroePanel_0_"+ (i-1) +"_" + (j-1) + "']//div[@data-testchart-name='" + iSubject + "']/div//*[name()='svg']//*[name()='g']//*[name()='g']//*[name()='rect'][1]//..//..//..//*[name()='g']//*[name()='text'][1]//*[name()='tspan'][4]"));
					String iYearValue = iYearSubValue.getText();
					String apiYearValue = yearWise.get(iYear).toString();
					boolean blnFound = apiYearValue.contains(iYearValue);
					Assert.assertTrue(blnFound, "UI value and API value doesnot match");
				}
			}
		}
	}

	public void verifyWsasScoringTextIsSameWithWsasApi(String responseWSAS) {
		// Click WSAS Scoring - Verify the text is the same with <WSAS_API>
		// response
		library.wait(5);
		WebElement wwsasscoring = library
				.findElement("SCHOOLPAGETESTSCORESECTION.wwsasscoring");
		wwsasscoring.click();
		library.wait(5);
		WebElement wwsasscoringtext = library
				.findElement("SCHOOLPAGETESTSCORESECTION.wwsasscoringtext");
		library.wait(5);
		String UIwsastext = wwsasscoringtext.getText();
		String UIwsastext1 = UIwsastext.replace(" ", "");
		String apiDescription = library.getValueFromJson(
				"$.tests[0].description", responseWSAS).toString();
		String apiDescription1 = apiDescription.replace(" ", "");
		Assert.assertEquals(UIwsastext1, apiDescription1);
	}

	public void verifyWsasTestScoresDetailInfoIsHided() {
		// Click UP button on this section - Verify the detail info is hided
		library.scrollToElement("SCHOOLPAGETESTSCORESECTION.wsasheader");
		library.click("SCHOOLPAGETESTSCORESECTION.wsasheader");
		boolean ishidden = library
				.verifyPageNotContainsElement("SCHOOLPAGETESTSCORESECTION.grade6");
		Assert.assertTrue(ishidden);
	}
}



