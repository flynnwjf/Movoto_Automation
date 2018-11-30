package com.movoto.scripts.agent;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.movoto.scripts.BaseTest;
import com.movoto.scripts.data.AgentTestDataProvider;

public class LatestAppDownload extends BaseTest {

	@Test(dataProvider = "DownloadApp", dataProviderClass = AgentTestDataProvider.class)
	public void DownloadApp(Map<String, String> data) 
	{
		if (data != null) 
		{
			scenarios.LoginToDownloadLatestAppIos(data.get("name"), data.get("pass"));
			library.wait(5);
			//scenarios.sucess();
					
		}
	}
	
	@Test
	public void DownloadFile()
	{
		
		 	    
		library.click("xpath->.//*[text()='Movoto Agent Fast Connect AWS']");
		library.wait(5);
		library.click("xpath->(.//i[@class='icon-arrow-down'])[1]");
		library.wait(5);
		library.click("xpath->.//*[@id='package_link']/span");
		library.wait(400);
		
		scenarios.deleteApp();
		scenarios.MoveApp();
		
		

	   
		
		//library.wait(20);
				
		
	}
}
