package com.movoto.reporter.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.movoto.reporter.ITestResult;

public class TestResult implements ITestResult {

	private int m_status = -1;
	private Throwable m_throwable = null;
	private long m_startMillis = 0;
	private long m_endMillis = 0;
	private String m_host;
	private String m_name = null;
	private List<String> m_output = null;
	private Map<String, List<String>> instruction_map = null;
	private String currentMethod = null;

	public TestResult(String name, String host, long start, long end, Throwable throwable) {
		m_name = name;
		m_host = host;
		m_startMillis = start;
		m_endMillis = end;
		m_throwable = throwable;
		instruction_map = Collections.synchronizedMap(new HashMap<>());
		m_output = Collections.synchronizedList(new ArrayList<>());
	}

	@Override
	public int getStatus() {
		return m_status;
	}

	@Override
	public void setStatus(int status) {
		m_status = status;
	}

	@Override
	public Throwable getThrowable() {
		return m_throwable;
	}

	@Override
	public long getStartMillis() {
		return m_startMillis;
	}

	@Override
	public long getEndMillis() {
		return m_endMillis;
	}

	@Override
	public void setEndMillis(long millis) {
		m_endMillis = millis;
	}

	@Override
	public String getName() {
		return m_name;
	}

	@Override
	public void setName(String name) {
		m_name = name;
	}

	@Override
	public boolean isSuccess() {
		return ITestResult.SUCCESS == m_status;
	}

	@Override
	public String getHost() {
		return m_host;
	}

	@Override
	public void setHost(String host) {
		m_host = host;
	}

	@Override
	public synchronized List<String> getOutput() {
		return m_output;
	}

	@Override
	public synchronized void appendInstruction(String i) {
		instruction_map.get(currentMethod).add(i);
		m_output.add(i);
	}

	@Override
	public String toString() {
		String str = "Name: " + m_name + " Host: " + m_host;
		return str;
	}

	@Override
	public void setMethod(String method) {
		if (!method.equalsIgnoreCase(currentMethod)) {
			this.currentMethod = method;
			List<String> instructions = Collections.synchronizedList(new ArrayList<>());
			instruction_map.put(currentMethod, instructions);
		}

	}

	@Override
	public List<String> getOutput(String methodName) {
		return instruction_map.get(methodName);
	}
}
