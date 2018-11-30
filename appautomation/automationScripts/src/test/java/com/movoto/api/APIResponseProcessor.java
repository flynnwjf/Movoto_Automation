package com.movoto.api;




import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.movoto.api.pojo.APIResponse;
import com.movoto.api.pojo.Address;

public class APIResponseProcessor {
	
	JSONParser parser;
	JSONObject jsonObject;
	JSONArray jsonArray;
	APIResponse apiResponse;
	public APIResponseProcessor()
	{
		parser=new JSONParser();
	}
	
	public APIResponse processResponse(String response)
	{
		
		try
		{
			Object obj=parser.parse(response);
			apiResponse=new APIResponse();
		
			System.out.println("INSTANCE OF "+ (obj instanceof JSONArray));
			if(obj instanceof JSONArray)
			{
				jsonArray=(JSONArray)parser.parse(response);
			}
			else
			{
		     jsonObject=(JSONObject)parser.parse(response);
		     jsonArray=(JSONArray)jsonObject.get("listings");
		
			}
		
	//	apiResponse.setListPrice(Integer.parseInt(jsonObject.get("listPrice").toString()));
		processAPIArray();
		
		}catch(ParseException pe)
		{
			System.out.println("Exception in APIResponseProcessor:processResponse:parserexception=="+pe.getMessage());
		}catch(Exception ex)
		{
			System.out.println("Exception in APIResponseProcessor:processResponse:exception=="+ex.getMessage());
		}
		return apiResponse;
		
	}
	public void processAPIArray() 
	{
		HashMap<String,JSONObject> listings=new HashMap<String,JSONObject>();
		for(int i=0;i<jsonArray.size();i++)
		{
			listings.put(i+"",(JSONObject)jsonArray.get(i));
		}
		apiResponse.setListngs(listings);
	}
	
	public APIResponse getAddress(JSONObject jsonAddress)
	{
		try
		{
		Address address=new Address();
		JSONObject jsonObject=(JSONObject)jsonAddress.get("address");
		address.setAddressInfo(jsonObject.get("addressInfo").toString());
		System.out.println(jsonAddress.toJSONString());
		address.setCity(jsonObject.get("city").toString());
		apiResponse.setAddress(address);
		apiResponse.setListingUrl(jsonAddress.get("listingUrl").toString());
		apiResponse.setListPrice(Integer.parseInt(jsonAddress.get("listPrice").toString()));
		apiResponse.setBedrooms(Integer.parseInt(jsonAddress.get("bedrooms").toString()));
		apiResponse.setBathroomsTotal(jsonAddress.get("bathroomsTotal").toString());
		}catch(Exception exc)
		{
			System.out.println("Exception in APIResponseProcessor:getAddress=="+exc.getMessage());
		}
		return apiResponse;
	}

}
