package com.movoto.helper;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.xml.XmlSuite;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jayway.jsonpath.JsonPath;
import com.movoto.reporter.ReportManager;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.relevantcodes.extentreports.ReporterType;

import net.minidev.json.JSONObject;

/**
 * Hello world!
 *
 */
public class CustomReporter implements IReporter {

	private ExtentReports extent;
	private Set<String> testSet = new HashSet<>();

	@Override
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
		extent = new ExtentReports("test-output/ExtentReportTestNG.html", true);
		for (ISuite suite : suites) {
			Map<String, ISuiteResult> result = suite.getResults();

			for (ISuiteResult r : result.values()) {
				ITestContext context = r.getTestContext();
				String name = context.getName();
				ExtentTest test = extent.startTest(name);

				testSet.clear();
				Date start = new Date(ReportManager.getTestResult(name).getStartMillis());
				Date end = new Date(System.currentTimeMillis());
				test.setStartedTime(start);
				test.setEndedTime(end);

				buildTestNodes(context.getPassedTests(), LogStatus.PASS, name, test);
				buildTestNodes(context.getFailedTests(), LogStatus.FAIL, name, test);
				buildTestNodes(context.getSkippedTests(), LogStatus.SKIP, name, test);

				extent.endTest(test);
				extent.flush();
			}
		}
		
		extent.close();

	}

	private void buildTestNodes(IResultMap tests, LogStatus status, String name, ExtentTest test) {
		String testName = name;

		if (tests.size() > 0) {

			for (ITestResult result : tests.getAllResults()) {

				String testMethodName = result.getName();
				if (!testSet.contains(testMethodName)) {
					testSet.add(testMethodName);
					ExtentTest testMethod = extent.startTest(testMethodName);

					// test.getTest().setStartedTime(getTime(result.getStartMillis()));
					// test.getTest().setEndedTime(getTime(result.getEndMillis()));;

					String message = "Test " + status.toString().toLowerCase() + "ed";

					if (result.getThrowable() != null)
						message = result.getThrowable().getMessage();

					List<String> outPut = ReportManager.getOutput(testName, testMethodName);// ReportManager.getOutput(name);
					if (outPut != null) {

						for (String str : outPut) {

							String method = JsonPath.read(str, "$.methodName");
							String iResult = JsonPath.read(str, "$.returnValue");
							String args = JsonPath.read(str, "$.params");
							String exStatus = JsonPath.read(str, "$.status");
							String reportMessage = message;
							String screenShot = null;
							try {
								screenShot = JsonPath.read(str, "$.screenshot");
								if (screenShot != null && screenShot.length() > 0) {
									screenShot = screenShot.replace("test-output/", "");
								}
							} catch (Exception e) {
								// TODO: handle exception
							}

							status = LogStatus.PASS;
							boolean failed = false;

							if (exStatus != null && exStatus.equalsIgnoreCase("FAILED")) {
								status = LogStatus.FAIL;
								failed = true;
								if (reportMessage.contains("Test passed")) {
									reportMessage = "Failed";
								}
							} else if (exStatus != null && exStatus.equalsIgnoreCase("SKIPPED")) {
								status = LogStatus.SKIP;
								failed = true;
								reportMessage = "Skipped!";
							} else if (exStatus != null && exStatus.equalsIgnoreCase("EXCEPTION")) {
								status = LogStatus.ERROR;
								reportMessage = "Error Occured!";
								failed = true;
							} else if (exStatus != null && exStatus.equalsIgnoreCase("PASSED")) {
								status = LogStatus.PASS;
								failed = false;
							} else if (exStatus == null || exStatus.equalsIgnoreCase("null")) {
								exStatus = "";
							}

							if (!failed) {
								reportMessage = "<span style='font-weight:bold;'>" + iResult + "</span>";
							} else {

								reportMessage = "<span style='font-weight:bold;'>" + reportMessage + "</span>";
							}

							if (screenShot != null) {
								testMethod.log(status, method + "[" + args + "]" + " : " + reportMessage,
										test.addScreenCapture(screenShot));
							} else {
								testMethod.log(status, method + "[" + args + "]" + " : " + reportMessage, exStatus);
							}

						}

					}

					test.appendChild(testMethod);
				}
			}
		}
	}

	private Date getTime(long millis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}

	static String splitCamelCase(String s) {
		return s.replaceAll(String.format("%s|%s|%s", "(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])",
				"(?<=[A-Za-z])(?=[^A-Za-z])"), " ");
	}

}
