package com.movoto.tests;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.movoto.context.ConfigurationManager;
import com.movoto.fixtures.FixtureLibrary;
import com.movoto.reporter.ReportManager;
import java.util.List;
import org.testng.AssertJUnit;
import org.testng.ITestContext;

public class ExcelTest {
	
	private FixtureLibrary library;

	@BeforeTest
	public void startTest(final ITestContext testContext) {
	    System.out.println(testContext.getName()); // it prints "Check name test"
		library = new ConfigurationManager().createContext(testContext.getName());
	    //library = FixtureLibraryFactory.getLibrary(testContext.getName());
	}

	@Parameters({ "file", "sheet", "mode" })
	@Test(testName="readExcel")
	public void readFromExcel(String file, String sheet, String mode) {
		System.out.println("File: " + file + " Sheet: " + sheet + " Mode: " + mode);
		library.openExcelSheet(file, sheet, mode);
		String woid = library.getFromExcelRowAndColumn(1, "WOID");
		System.out.println("WOID: " + woid);
		library.closeExcelSheet(file, sheet, mode);
	}
	
	@AfterTest
	public void cleanUp(){
	/*	List<String> outPut = ReportManager.getOutput();
		System.out.println("*****************Out put start*********************");
		System.out.println(ReportManager.getCurrentTestResult().toString());
		for(String str:outPut){
			System.out.println(str);
		}
		System.out.println("*****************Out put end*********************");*/
	}
	
/*	@Parameters({ "file", "sheet", "mode" })
	@Test(testName="writeExcel")
	public void writeToExcel(String file, String sheet, String mode) {
		System.out.println("File: " + file + " Sheet: " + sheet + " Mode: " + mode);

		library.openExcelSheet(file, sheet, mode);
		try {
			Thread.sleep(10000);
		} catch (Exception e) {
			// TODO: handle exception
		}
		library.writeToExcel("WEB-557123", 3, 5);
		library.closeExcelSheet(file, sheet, mode);
	}
	*/
}
