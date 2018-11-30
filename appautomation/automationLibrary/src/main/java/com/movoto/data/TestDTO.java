package com.movoto.data;

import org.openqa.selenium.WebDriver;

public interface TestDTO {

	void setObjectRepository(ObjectRepository repository);

	ObjectRepository getObjectRepository();

	void setTestParameters(TestParameters parameters);

	TestParameters getTestParameters();

	void setTestProperties(TestProperties properties);

	TestProperties getTestProperties();

	void setDriver(WebDriver driver);

	WebDriver getDriver();
}
