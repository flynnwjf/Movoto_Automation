package com.movoto.fixtures.impl.ws;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.movoto.data.TestDTO;
import com.movoto.fixtures.WebServicesFixtures;
import net.minidev.json.JSONObject;

public class WebServicesFixturesImpl implements WebServicesFixtures {

	private TestDTO dto;
	private Response response;
	private Map<String, String> header;
	private String contentType = null;

	public WebServicesFixturesImpl(TestDTO dto) {
		this.dto = dto;
		header = new HashMap<>();
	}

	@Override
	public String HTTPGet(String URL) {
		this.response = getRequest().get(getURL(URL)).andReturn();
		header.clear();
		return response.asString();
	}

	@Override
	public String HTTPPost(String URL, Map<String, Object> data) {
		JSONObject jsonObject = new JSONObject(data);
		this.response = getRequest().body(jsonObject).post(getURL(URL)).andReturn();
		header.clear();
		return response.asString();
	}
	
	@Override
	public String HTTPPost(String URL, String body) {
		this.response = getRequest().body(body).post(getURL(URL)).andReturn();
		header.clear();
		return response.asString();
	}

	@Override
	public String HTTPDelete(String URL) {
		response = getRequest().delete(getURL(URL));
		header.clear();
		return response.asString();
	}

	@Override
	public String HTTPut(String URL, Map<String, Object> data) {
		JSONObject json = new JSONObject(data);
		response = getRequest().body(json).put(getURL(URL)).andReturn();
		return response.asString();
	}

	private URL getURL(String url) {
		if (url != null) {
			try {
				return new URL(url);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public void setRequestHeader(String key, String value) {
		header.put(key, value);
	}

	@Override
	public String getResponseHeaderValueForKey(String key) {
		// TODO Auto-generated method stub
		return response.getHeader(key);
	}

	@Override
	public Response getCurrentResponse() {
		// TODO Auto-generated method stub
		return response;
	}

	@Override
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	private RequestSpecification getRequest() {
		RequestSpecification rs = RestAssured.given();
		if (contentType != null) {
			rs.and().contentType(contentType);
		} else {
			rs.and().contentType(ContentType.JSON);
		}
		if (!header.isEmpty()) {
			rs.and().headers(header);
		}
		return rs.and();
	}

}
