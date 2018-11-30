package com.movoto.scripts.DataInitialization;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.jayway.jsonpath.internal.JsonContext;
import com.movoto.db.GetConnection;
import com.movoto.scripts.apis.CommonAPITest;
import com.movoto.scripts.consumer.Library.utilities.RemoteServer;
import com.movoto.scripts.consumer.Library.utilities.utilities;
import com.movoto.scripts.consumer.Library.utilities.utilities.JsonManager;
import com.movoto.scripts.data.TestDataInitializationDataProvider;

public class ConsumerSiteData {

	private static JsonManager jsonManager = new utilities.JsonManager();
	
	@Test(dataProvider="nearbyAwesomeHomesInitializationDataProvider",dataProviderClass=TestDataInitializationDataProvider.class)
	public void NearbyAwesomeHomesInitialization(LinkedHashMap data) throws SQLException
	{
	    RemoteServer.executeFile((LinkedHashMap)data.get("loginInfo4RemoteServer"),data.get("remoteShellFileName").toString());
		this.updateDB4NearbyProperties(data);
		RefreshBasicInfoOfProperty2ES(Arrays.asList(data.get("nearbySearchPropertyIDs").toString()),(LinkedHashMap)data.get("refreshESRequestData"));
		System.out.print("pause");
		RemoteServer.executeFile((LinkedHashMap)data.get("loginInfo4RemoteServer"),data.get("remoteShellFileName").toString());
		System.out.print("finished for NearbyAwesomeHomesInitialization");
	}

	@Test(dataProvider="newListingsAndOpenHouse4DPPInitializationDataProvider",dataProviderClass=TestDataInitializationDataProvider.class)
	public void NewListingsAndOpenHouse4DPPInitialization(LinkedHashMap data) throws SQLException
	{
		NewListingsAndOpenHouseInitialization(data);
	}

	@Test(dataProvider="newListingsAndOpenHouse4CityInitializationDataProvider",dataProviderClass=TestDataInitializationDataProvider.class)
	public void NewListingsAndOpenHouse4CityInitialization(LinkedHashMap data) throws SQLException
	{
		NewListingsAndOpenHouseInitialization(data);
	}
	

	@Test(dataProvider="newListingsAndOpenHouse4ZipCodeInitializationDataProvider",dataProviderClass=TestDataInitializationDataProvider.class)
	public void NewListingsAndOpenHouse4ZipCodeInitialization(LinkedHashMap data) throws SQLException
	{
		NewListingsAndOpenHouseInitialization(data);
	}
	
	public void NewListingsAndOpenHouseInitialization(LinkedHashMap data) throws SQLException
	{
		//RemoteServer.executeFile((LinkedHashMap)data.get("loginInfo4RemoteServer"),data.get("remoteShellFileName").toString());
		this.updateDB4NearbyProperties(data);
		RefreshBasicInfoOfProperty2ES(Arrays.asList(data.get("nearbySearchPropertyIDs").toString().split(",")),(LinkedHashMap)data.get("refreshESRequestData"));
		//RemoteServer.executeFile((LinkedHashMap)data.get("loginInfo4RemoteServer"),data.get("remoteShellFileName").toString());
		System.out.print("finished for NewListingsAndOpenHouseInitialization");
	}
	
	public void Statistics4SnapShotInitialization(LinkedHashMap data) throws SQLException
	{
		this.updateDB4MarketStatistics(data);
		CommonAPITest.getAPIResponse((LinkedHashMap)data.get("marketTrendsAPIRequestInfo"));
		System.out.print("finished for Statistics4SnapShotInitialization");
	}
	
	@Test(dataProvider="statistics4SnapShot4CityInitializationDataProvider",dataProviderClass=TestDataInitializationDataProvider.class)
	public void Statistics4SnapShot4CityInitialization(LinkedHashMap data) throws SQLException
	{
		Statistics4SnapShotInitialization(data);
	}
		
	@Test(dataProvider="statistics4SnapShot4ZipCodeInitializationDataProvider",dataProviderClass=TestDataInitializationDataProvider.class)
	public void Statistics4SnapShot4ZipCodeInitialization(LinkedHashMap data) throws SQLException
	{
		Statistics4SnapShotInitialization(data);
	}
	
	@Test(dataProvider="statistics4SnapShot4NeighborhoodInitializationDataProvider",dataProviderClass=TestDataInitializationDataProvider.class)
	public void Statistics4SnapShot4NeighborhoodInitialization(LinkedHashMap data) throws SQLException
	{
		Statistics4SnapShotInitialization(data);
	}
	
	public void updateDB4NearbyProperties(LinkedHashMap data) throws SQLException 
	{
		String sql =String.format("select %s('%s',%s,%s,false)",data.get("functionName4SQL").toString(), data.get("nearbySearchListingIDs").toString(),data.get("lantitude4BaseProperty").toString(),data.get("longitude4BaseProperty").toString());
		this.updateDB(data,sql);
	}
	
	public void updateDB4MarketStatistics(LinkedHashMap data) throws SQLException 
	{
		String area = data.get("area").toString();
		String key;
		if (data.containsKey(area)) key= data.get(area).toString();
		else key= data.get(area+"ID").toString();
		String sql =String.format("select %s('%s','%s')",data.get("functionName4SQL").toString(),area,key);
		this.updateDB(data,sql);
		System.out.println("");
	}
	
	public String updateDB(LinkedHashMap data,String sql) throws SQLException 
	{
		LinkedHashMap dbLoginInfo = (LinkedHashMap)data.get("dbLoginInfo");
 		GetConnection conn = new GetConnection(dbLoginInfo.get("url").toString(),dbLoginInfo.get("usr").toString(),dbLoginInfo.get("pwd").toString());
		ResultSet result = conn.ExecuteQuery(sql);
		//String rs = result.getString(1);
		conn.Disconect();
		return "";
		
	}
	
	public void RefreshBasicInfoOfProperty2ES(List<String> propertyIDs,LinkedHashMap RefreshPropertyDetailFromDB2ES)
	{
		propertyIDs.forEach(propertyID->{
				LinkedHashMap propertyIDMap =new LinkedHashMap();
				propertyIDMap.put("propertyID", propertyID);
				RefreshPropertyDetailFromDB2ES.put("inputParameter4API",propertyIDMap);
				CommonAPITest.getAPIResponse(RefreshPropertyDetailFromDB2ES);
		});
	}
	
}
