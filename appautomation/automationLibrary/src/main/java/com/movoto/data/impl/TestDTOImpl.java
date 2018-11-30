package com.movoto.data.impl;

import org.openqa.selenium.WebDriver;

import com.movoto.data.ObjectRepository;
import com.movoto.data.TestDTO;
import com.movoto.data.TestParameters;
import com.movoto.data.TestProperties;

public class TestDTOImpl implements TestDTO {

	private ObjectRepository repository;
	private TestParameters parameters;
	private TestProperties properties;
	private WebDriver driver;
	
	@Override
	public void setObjectRepository(ObjectRepository repository) {
		this.repository = repository;
	}

	@Override
	public ObjectRepository getObjectRepository() {
		return this.repository;
	}

	@Override
	public void setTestParameters(TestParameters parameters) {
		this.parameters = parameters;
	}

	@Override
	public TestParameters getTestParameters() {
		return this.parameters;
	}

	@Override
	public void setTestProperties(TestProperties properties) {
		this.properties = properties;
	}

	@Override
	public TestProperties getTestProperties() {
		return this.properties;
	}

	@Override
	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	@Override
	public synchronized WebDriver getDriver() {
		return driver;
	}

}
