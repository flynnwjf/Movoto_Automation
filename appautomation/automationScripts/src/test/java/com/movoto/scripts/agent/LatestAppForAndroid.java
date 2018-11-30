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

public class LatestAppForAndroid extends BaseTest 
{
	@Test
	public void DownloadAndroid()
	{
		library.wait(5);
		library.click("xpath->.//*[@id='gv1_HyperLink1_17']");
		library.wait(200);
		scenarios.deleteAppAndroid();
		scenarios.MoveAppForAndroid();
		
	}


}
