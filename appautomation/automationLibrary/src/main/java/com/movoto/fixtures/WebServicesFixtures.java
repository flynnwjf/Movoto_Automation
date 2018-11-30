package com.movoto.fixtures;

import java.util.Map;
import com.jayway.restassured.response.Response;

public interface WebServicesFixtures {

	String HTTPGet(String URL);

	String HTTPPost(String URL, Map<String, Object> data);
	
	String HTTPPost(String URL, String body);

	String HTTPDelete(String URL);

	String HTTPut(String URL, Map<String, Object> data);

	void setRequestHeader(String key, String value);

	String getResponseHeaderValueForKey(String key);

	Response getCurrentResponse();
	
	void setContentType(String contentType);
	
}
