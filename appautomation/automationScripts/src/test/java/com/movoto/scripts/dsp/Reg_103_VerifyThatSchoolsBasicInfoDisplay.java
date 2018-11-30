package com.movoto.scripts.dsp;

import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.movoto.scripts.BaseTest;
import com.movoto.utils.JSONParserForAutomationNG;

public class Reg_103_VerifyThatSchoolsBasicInfoDisplay extends BaseTest {
	
	JSONParserForAutomationNG jsonParserAuto;
	JSONObject jsonObj;
	WebDriver wDriver;  
	 
	@Test
	@Parameters("dataProviderPath")
	public void verifySchoolsBasicInfo(String dataProviderPath){
		
		init(dataProviderPath);   
		
	    if (jsonObj != null){
			verifySchoolsBasicInfo(jsonObj);   
		}
	    else{
			Assert.assertFalse(false);
		}
	    
	}
	
	public void init(String jsonPath){
		 try{
			 jsonParserAuto = new JSONParserForAutomationNG(jsonPath);
			 jsonObj = jsonParserAuto.getNode("baseInfo");
		 }
		 catch(Exception exc){
			 System.out.println("Exception in init(String jsonPath) ->" + exc.getMessage());
		 }
	}
	
	public void verifySchoolsBasicInfo(JSONObject data){     
		library.wait(20);
		String response = getResponseBasicInfoApi(data);
		//Verify the school name matching with api response
		verifySchoolName(response);
		//Verify the T/s ration matching with api response
		verifyTSRation();
		//Verify the School Grade matching with api response
		verifySchoolGrade(response);
		//Verify the School Type matching with api response
		verifySchoolType( response);
		//Verify the School subType matching with api response
		verifySchoolSubType(response);
		//Verify the School rating matching with api response
		verifySchoolRating(data);
		//verify the url pattern
		verifyUrlpattern(data);
		verifyBreadcrumLogic(data);
		//verify the tool tip
		verifyToolTip();		  
	}
	
	public String getResponseBasicInfoApi(JSONObject data){
		library.setRequestHeader("X-MData-Key", data.get("X-MData-Key").toString());
		String response = library.HTTPGet(data.get("Url-baseinfo").toString());
		return response; 
	}
	  
	public void verifySchoolName(String response){
		//Verify the school name matching with api response
		String SchoolNameAPI = library.getValueFromJson("$.name", response).toString();
		String SchoolNameUI = library.getTextFrom("SCHOOLPAGE.schoolnames");
		Assert.assertEquals(SchoolNameAPI, SchoolNameUI);
	}
		  
	public void verifyTSRation(){     
		//Since in API response for T/s ratio value not mention in API. As per the mail we check only the UI contains value or not
		JavascriptExecutor jse = (JavascriptExecutor)library.getDriver();
	    String retValue = (String)jse.executeScript("return document.getElementsByClassName(\"col-xs-6\")[1].textContent");
	    Assert.assertTrue(retValue.contains("-"), "verifed T/S Ration");
	}
	
	public void verifySchoolGrade(String response){      
		//Verify the School Grade matching with api response
		String GradeAPI = library.getValueFromJson("$.level", response).toString();
		String GradeUI = library.getTextFrom("SCHOOLPAGE.schoolgrade");
		Assert.assertEquals(GradeAPI, GradeUI);
	}
	
	public void verifySchoolType(String response){      
		//Verify the School Type matching with api response
		String TypeAPI = library.getValueFromJson("$.type", response).toString();
		String TypeUI = library.getTextFrom("SCHOOLPAGE.schooltype");
		Assert.assertEquals(TypeAPI, TypeUI.toLowerCase());  
	}
	
	public void verifySchoolSubType(String response){    
		//Verify the School subType matching with api response
		String SubTypeAPI = library.getValueFromJson("$.subType", response).toString();
		String SubTypeUI = library.getTextFrom("SCHOOLPAGE.schoolsubtype");
		Assert.assertEquals(SubTypeAPI, SubTypeUI); 
	}
	  
	public String getResponseStateRating_API(JSONObject Data){
		library.setRequestHeader("X-MData-Key",Data.get("X-MData-Key").toString());
		String response = library.HTTPGet(Data.get("StateRating_API").toString());
		return response; 	  
	}
		
	public void verifySchoolRating(JSONObject Data){
		//Verify the Schoolrating matching with api response
		String response1 = getResponseStateRating_API(Data);
		String StateRatingAPI = library.getValueFromJson("$..[0].rating", response1).toString();
		// library.wait(15);
		library.waitForElement("SCHOOLPAGE.schoolrating");
		String StateRatingUI = library.getTextFrom("SCHOOLPAGE.schoolrating");
		Assert.assertTrue(StateRatingAPI.contains(StateRatingUI), "Rating matching with UI");
	}
	
	public void verifyUrlpattern(JSONObject Data){    		  
		String VerifyUrlUI=library.getAttributeOfElement("href", "SCHOOLPAGE.seeactivelisting"); 
		String City=Data.get("City").toString();
		String State=Data.get("State").toString();
		String new7=Data.get("new-7").toString();
		String VerifyUrl=City+"-"+State+"/"+new7+"/";
		Assert.assertTrue(VerifyUrlUI.contains(VerifyUrl), "Verfiied City,State,new7");			 
	}
	
	public void verifyBreadcrumLogic(JSONObject Data){
		int count = library.getElementCount("SCHOOLPAGE.stateratelist");
		boolean flag = library.findElement("SCHOOLPAGE.schoolnameverify").isDisplayed();
		if(flag){
			String verifyLinkAPI[] = {Data.get("1st_node").toString(),Data.get("2nd_node").toString(),Data.get("3rd_node").toString()};
	        for(int i=0; i<count-1; i++){
	        	String lnkDomain = library.getDriver().findElement(By.xpath("(//ul[@class='crumbs']/li)["+(i+1)+"]/a")).getText();
			    Assert.assertEquals(lnkDomain, verifyLinkAPI[i].toString());
			}
	        String schoolname=library.getTextFrom("SCHOOLPAGE.breadcumschoolname");
	        Assert.assertEquals(schoolname,Data.get("SchoolName").toString());
		}	  
	}
	

	  
	public void verifyToolTip(){      
		//verify the tool tip
		library.click("SCHOOLPAGE.greatschoolsrating");
		library.wait(5);
		String toolTiptext= library.getTextFrom("SCHOOLPAGE.tooltip");
		library.wait(5);
		Assert.assertTrue((toolTiptext.length()>50), "Verify length of text in tip window sucessully");
	}
	
}
