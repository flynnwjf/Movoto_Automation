package com.movoto.api.pojo;

import java.util.HashMap;

import org.json.simple.JSONObject;



public class APIResponse 
{
	HashMap<String,JSONObject> listings;
	Address address;
	String addressInfo;
	int listPrice;
	int bedrooms;
	String bathroomsTotal;
	String listingUrl;
	
	public String getListingUrl() {
		return listingUrl;
	}
	public void setListingUrl(String listingUrl) {
		this.listingUrl = listingUrl;
	}
	public String getBathroomsTotal() {
		return bathroomsTotal;
	}
	public void setBathroomsTotal(String bathroomsTotal) {
		this.bathroomsTotal = bathroomsTotal;
	}
	public int getBedrooms() {
		return bedrooms;
	}
	public void setBedrooms(int bedrooms) {
		this.bedrooms = bedrooms;
	}
	public HashMap<String, JSONObject> getListings() {
		return listings;
	}
	public void setListngs(HashMap<String, JSONObject> listings) {
		this.listings = listings;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	
	public Address getAddress()
	{
		return address; 
	}
	public int getListPrice() {
		return listPrice;
	}
	public void setListPrice(int listPrice) {
		this.listPrice = listPrice;
	}
	
	
	public String getAddressInfo() {
		return addressInfo;
	}
	public void setAddressInfo(String addressInfo) {
		this.addressInfo = addressInfo;
	}
}
