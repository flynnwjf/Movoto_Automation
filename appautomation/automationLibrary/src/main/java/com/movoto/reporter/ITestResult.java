package com.movoto.reporter;

import java.util.List;


public interface ITestResult {
	//
	// Test status
	//
	public static final int SUCCESS = 1;
	public static final int FAILURE = 2;
	public static final int SKIP = 3;
	public static final int SUCCESS_PERCENTAGE_FAILURE = 4;
	public static final int STARTED = 16;

	/**
	 * @return The status of this result, using one of the constants above.
	 */
	public int getStatus();
	public void setStatus(int status);
	/**
	 * @return The throwable that was thrown while running the method, or null
	 *         if no exception was thrown.
	 */
	public Throwable getThrowable();

	/**
	 * @return the start date for this test, in milliseconds.
	 */
	public long getStartMillis();

	/**
	 * @return the end date for this test, in milliseconds.
	 */
	public long getEndMillis();

	public void setEndMillis(long millis);

	/**
	 * @return The name of this TestResult, typically identical to the name of
	 *         the method.
	 */
	public String getName();
	public void setName(String name);

	/**
	 * @return true if if this test run is a SUCCESS
	 */
	public boolean isSuccess();

	/**
	 * @return The host where this suite was run, or null if it was run locally.
	 *         The returned string has the form: host:port
	 */
	public String getHost();
	public void setHost(String host);
	
	public List<String> getOutput();
	public List<String> getOutput(String methodName);
	
	public void appendInstruction(String o);
	
	public void setMethod(String method);

}
