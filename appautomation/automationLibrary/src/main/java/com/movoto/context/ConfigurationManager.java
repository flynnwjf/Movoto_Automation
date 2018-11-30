package com.movoto.context;

import java.net.InetAddress;
import java.net.UnknownHostException;
import com.movoto.data.DataManager;
import com.movoto.data.TestDTO;
import com.movoto.fixtures.impl.fixturelib.FixtureLibraryImpl;
import com.movoto.reporter.ITestResult;
import com.movoto.reporter.ReportManager;
import com.movoto.reporter.impl.TestResult;

public class ConfigurationManager {
	
	public FixtureLibraryImpl createContext(String testName){
		FixtureLibraryImpl library = null;
		init(testName);
		library = new FixtureLibraryImpl(readConfiguration(testName));
		return library;
	}
	
	
	private synchronized TestDTO readConfiguration(String testName){
		TestDTO dto = DataManager.getDataManager("").populateTestDataForTest(testName);
		return dto;
	}
	
	private void init(String name){		
		String host = null;
		try {
			host = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long start = System.currentTimeMillis();
		long end = System.currentTimeMillis();
		ITestResult result = new TestResult(name, host, start, end, null);
		ReportManager.setCurrentTestResult(result);
	}
	
}
