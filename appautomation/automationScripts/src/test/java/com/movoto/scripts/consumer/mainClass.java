package com.movoto.scripts.consumer;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.movoto.scripts.BaseTest;
import com.movoto.scripts.consumer.Library.utilities.ExcelManager;
import com.movoto.scripts.consumer.Library.utilities.utilities;
import com.movoto.scripts.consumer.Library.*;
import com.movoto.scripts.consumer.Library.utilities.ExcelManager.CodeReviewStatus;
import com.movoto.scripts.consumer.Library.utilities.ExcelManager.DataStarterIndex;
import com.movoto.scripts.consumer.Library.utilities.ExcelManager.CodeReviewStatus.BrowserInfo;
import com.movoto.scripts.consumer.Library.utilities.ExcelManager.CodeReviewStatus.PlantFrom;
import com.movoto.scripts.consumer.Library.utilities.ExcelManager.CodeReviewStatus.StatusPhase;
import com.movoto.scripts.data.SiteMapDataProvider;
import com.movoto.scripts.data.MapSearchDataProvider;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

public class mainClass {
public static String OutputFilePath = "/users/vivian/Documents/SiteMapDemo/";
	
	public static void main(String args[]) throws Exception{
		
		ExcelManager excel= new ExcelManager("/Users/vivian/Documents/SiteMapDemo/Consumer Web Review Status.xlsx","Review status records","Read");
		Sheet sheet = excel.sheet;
		
		com.movoto.scripts.consumer.Library.utilities.ExcelManager.CodeReviewStatus codeReviewStatuses = excel.getCodeReviewStatusInfo(sheet);

		
		List<String> javaFileNames = utilities.FileManager.ReadFileContent("/users/vivian/Documents/SiteMapDemo/JAVAFileNameList.txt");
		List<Map<String,String>> caseIDs = new ArrayList<Map<String,String>>();
		//codeReviewStatuses.PlantFroms.get(0).BrowserInfos.get(0).Records.stream().map(record -> record.CaseID).collect(Collectors.toList());

		Map<Map<String,String>,String> caseIDWithJavaFileNames=new HashMap<Map<String,String>,String>();
/*		codeReviewStatuses.PlantFroms.forEach(plantForm ->{
			plantForm.BrowserInfos.forEach(browserInfo ->{
				caseIDs.addAll(browserInfo.Records.stream().map(record -> record.CaseID).collect(Collectors.toList()));
			});
		});*/
		utilities.FileManager.WriteFileContent(OutputFilePath+"notFoundPropertiesFiles.txt",new ArrayList<String>());
		codeReviewStatuses.PlantFroms.forEach(planForm -> {
			
			planForm.BrowserInfos.forEach(browserInfo -> {
				String plantFormNameWithBrowserName=(planForm.PlantFormName+browserInfo.BrowserName).replace("PCIE11","WindowsIE").replace("PCChrome","WindowsChrome").replaceAll("MobileAndroid", "AndroidChrome").replaceAll("MobileiOS", "IOSSafari");
				caseIDs.addAll(browserInfo.Records.stream().map(record -> record.CaseID).collect(Collectors.toList()));
				caseIDs.forEach(caseID->{
					String wholeCaseID = caseID.get("caseID").toString();
					String id=wholeCaseID.substring(4, wholeCaseID.length());
					List<String> javaFileNames4CurrentCaseID=javaFileNames.stream().filter(javaFileName->
					javaFileName.contains(id)).collect(Collectors.toList()
							);
					String javaFileName4CurrentCaseID =javaFileNames4CurrentCaseID.get(0);
					//if (javaFileNames4CurrentCaseID.size()==2 && browserInfo.BrowserName.contains("Android") && !javaFileName4CurrentCaseID.contains("_And")) javaFileName4CurrentCaseID =javaFileNames4CurrentCaseID.get(1);
					caseIDWithJavaFileNames.put(caseID,javaFileName4CurrentCaseID);
				});
				
				List<String> tests_ForNewReivewed=new ArrayList<String>();
				List<String> tests_Updated=new ArrayList<String>();
				List<String> tests_Passed2Jenkins=new ArrayList<String>();
						//Arrays.asList(String.format("	   <!--Below are cases for %s-->",plantFormNameWithBrowserName)));

				
				List<String> regressionXMLFileName_Tags = new ArrayList<String>();
				browserInfo.Records.forEach(record -> {
					//System.out.println("StatusPhase.ReadyForReview && StatusPhase.Reviewed: " + record.Statuzz.get(StatusPhase.ReadyForReview)+String.valueOf(( record.Statuzz.get(StatusPhase.ReadyForReview).equals("Yes")&& record.Statuzz.get(StatusPhase.Reviewed).equals("")))+String.valueOf(record.Statuzz.get(StatusPhase.ReadyForReview).equals("Yes"))+String.valueOf(record.Statuzz.get(StatusPhase.Reviewed).equals("")));
					//System.out.println("StatusPhase.Updated && StatusPhase.Accepted: " + String.valueOf((record.Statuzz.get(StatusPhase.Updated)=="Yes" && record.Statuzz.get(StatusPhase.Accepted)=="")));
 					
						String value_ReadyForReview=record.Statuzz.get(StatusPhase.ReadyForReview);
						String value_Reviewed=record.Statuzz.get(StatusPhase.Reviewed);
						String value_Updated=record.Statuzz.get(StatusPhase.Updated);
						String value_Accepted=record.Statuzz.get(StatusPhase.Accepted);
						//String value_Executed=record.Statuzz.get(StatusPhase.Executed);
						//if (value_Executed.equals("Yes"))
						//{
						String javaFileName = caseIDWithJavaFileNames.get(record.CaseID).toString();
						String movotoPage4ThisCase =record.CaseID.get("movotoPage").toString();
							if (( value_ReadyForReview.equals("Yes")&& value_Reviewed.trim().equals("")))//||(value_Updated.equals("Yes") && value_Accepted.trim().equals(""))
	 						{
	 							try {
	 				 				if (!regressionXMLFileName_Tags.contains("ReadyForReview")) regressionXMLFileName_Tags.add("ReadyForReview");
	 								tests_ForNewReivewed.addAll(GetTestContent(javaFileName,movotoPage4ThisCase,plantFormNameWithBrowserName));
	 								
	 							} catch (FileNotFoundException e) {
	 								// TODO Auto-generated catch block
	 								e.printStackTrace();
	 							}
	 						}
	 						if ((value_Updated.equals("Yes")&& (value_Accepted.trim().equals("")||value_Accepted.contains("UpdatedAgain_")||value_Accepted.equals("N/A"))))
	 						{
	 							try {
	 								if (!regressionXMLFileName_Tags.contains("UpdatedForReview")) regressionXMLFileName_Tags.add("UpdatedForReview");
	 								tests_Updated.addAll(GetTestContent(javaFileName,movotoPage4ThisCase,plantFormNameWithBrowserName));
	 							} catch (FileNotFoundException e) {
	 								// TODO Auto-generated catch block
	 								e.printStackTrace();
	 							}
	 						}
	 						
	 						if ((value_Reviewed.equals("Pass")&&value_Updated.trim().equals("") && value_Accepted.trim().equals(""))||(value_Reviewed.equals("Fail")&& value_Accepted.equals("Yes")))
	 						{
	 							try {
	 								if (!regressionXMLFileName_Tags.contains("Passed2Jenkins")) regressionXMLFileName_Tags.add("Passed2Jenkins");
	 								tests_Passed2Jenkins.addAll(GetTestContent(javaFileName,movotoPage4ThisCase,plantFormNameWithBrowserName));
	 							} catch (FileNotFoundException e) {
	 								// TODO Auto-generated catch block
	 								e.printStackTrace();
	 							}
	 						}
						//}
 						
					/*	else if (value_ReadyForReview.equals(""))
 						{
 							try {
 								regressionXMLFileName_Tags.add("FailExecution");
 								tests_Passed2Jenkins.addAll(GetTestContent(caseIDWithJavaFileNames.get(record.CaseID),plantFormNameWithBrowserName));
 							} catch (FileNotFoundException e) {
 								// TODO Auto-generated catch block
 								e.printStackTrace();
 							}
 						}*/
	 						System.out.println(tests_ForNewReivewed.toString());
					
				});

					if (regressionXMLFileName_Tags.size()>0) {

						regressionXMLFileName_Tags.forEach(regressionXMLFileName_Tag ->{

							List<String> tests = new ArrayList<String>();
							switch(regressionXMLFileName_Tag)
							{
								case "ReadyForReview":
								{
									tests.addAll(tests_ForNewReivewed);
									System.out.println(tests_ForNewReivewed.toString());
									break;
								}
								case "UpdatedForReview":
								{
									tests.addAll(tests_Updated);
									break;
								}
								case "Passed2Jenkins":
								{
									tests.addAll(tests_Passed2Jenkins);
									break;
								}
								
							}
							try {
								CreateRegressionXMLWithContent(String.format(OutputFilePath+"Regression_%s_%s.xml", plantFormNameWithBrowserName,regressionXMLFileName_Tag),tests);
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}});
					}
					

			});
			
		});

		System.out.println("");
	}
	public static List<String> GetTestContent(String javaFileName,String movotoPage,String planFormNameWithBrowserName) throws FileNotFoundException
	{
		List<String> tests = new ArrayList<String>();
		List<String> testTemplate = utilities.FileManager.ReadFileContent("Regression_TestFormat.xml");
		List<String> notFoundPropertiesFiles = new ArrayList<String>();
		testTemplate.forEach(testFormatLine ->{
			if (testFormatLine.contains("JavaFileName")) testFormatLine = testFormatLine.replace("JavaFileName", javaFileName);
			if (testFormatLine.contains("PlantformBrowser")) {
				testFormatLine = testFormatLine.replace("PlantformBrowser", planFormNameWithBrowserName);
				String propertiesFileName = String.format("config/%s_%s.properties",javaFileName,planFormNameWithBrowserName);
				if (!utilities.FileManager.FileExist(propertiesFileName)) 
					{
					System.out.println(String.format("%s not exist", propertiesFileName));
					notFoundPropertiesFiles.add(String.format("%s not exist", propertiesFileName));
					}
			}
			if (!(movotoPage.toLowerCase().equals("dpp")||movotoPage.toLowerCase().equals("map search")))
			{
				if (testFormatLine.contains("Map_Search/MapDataProvider")) testFormatLine = testFormatLine.replace("Map_Search/MapDataProvider", String.format("%s/%sDataProvider",movotoPage.toUpperCase(),movotoPage.toUpperCase()));
				if (testFormatLine.contains("com.movoto.scripts.agent")) testFormatLine = testFormatLine.replace("com.movoto.scripts.agent", String.format("com.movoto.scripts.%s",movotoPage.toLowerCase()));
			}
			tests.add(testFormatLine);
		});
		if (notFoundPropertiesFiles.size()>0) utilities.FileManager.AppendFileContent(OutputFilePath+"notFoundPropertiesFiles.txt",notFoundPropertiesFiles);
		return tests;
	}
	public static void CreateRegressionXMLWithContent(String xmlFilePath,List<String> testsContent) throws FileNotFoundException
	{
		List<String> xmlContent = new ArrayList<String>();
		utilities.FileManager.ReadFileContent("Regression_Format.xml").forEach(xmlContentLine ->{
			if (xmlContentLine.contains("<tests>")) {
				testsContent.forEach(testLine -> xmlContent.add(testLine));//System.out.println(testLine);
				
			}
			else
			{
				//System.out.println(xmlContentLine);
				xmlContent.add(xmlContentLine);
			}
	
		});
		//xmlContent.forEach(xmlContentLine -> System.out.println(xmlContentLine));
		utilities.FileManager.WriteFileContent(xmlFilePath,xmlContent);
	}
}
