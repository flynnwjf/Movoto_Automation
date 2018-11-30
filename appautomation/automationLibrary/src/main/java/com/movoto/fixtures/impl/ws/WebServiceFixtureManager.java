package com.movoto.fixtures.impl.ws;

import com.movoto.data.TestDTO;
import com.movoto.fixtures.WebServicesFixtures;

public class WebServiceFixtureManager {

	public static WebServiceFixtureManager getWebServiceManager() {
		return new WebServiceFixtureManager();
	}
	
	public WebServicesFixtures getWebServicesFixtures(TestDTO dto){
		WebServicesFixtures fixtures = new WebServicesFixturesImpl(dto);	
		return fixtures;
	}
	
}
