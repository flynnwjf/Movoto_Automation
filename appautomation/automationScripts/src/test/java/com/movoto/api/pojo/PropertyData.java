package com.movoto.api.pojo;



public class PropertyData
{
	String status;
    long price;
    double noOfBathRoom;
    double noOfRoom,area;
    String addr[];
    String addrDet;
    String photoCount;
    String zipCode;
    String nieghborhood;
    String state;
    
    
    public String getState() {
		return state;
	}
	public void setState(String state) {
		if(state.contains(", "))
		this.state = state.replace(", ", "");
		else
		this.state=state;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getNieghborhood() {
		return nieghborhood;
	}
	public void setNieghborhood(String nieghborhood) {
		this.nieghborhood = nieghborhood;
	}
	public String getPhotoCount() {
		return photoCount;
	}
	public void setPhotoCount(String photoCount) {
		this.photoCount = photoCount;
	}
	public String getAddrDet() {
		return addrDet;
	}
	public void setAddrDet(String addrDet) {
		this.addrDet = addrDet;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public long getPrice() {
		return price;
	}
	public void setPrice(long price) {
		this.price = price;
	}
	public double getNoOfBathRoom() {
		return noOfBathRoom;
	}
	public void setNoOfBathRoom(double noOfBathRoom) {
		this.noOfBathRoom = noOfBathRoom;
	}
	public double getNoOfRoom() {
		return noOfRoom;
	}
	public void setNoOfRoom(double noOfRoom) {
		this.noOfRoom = noOfRoom;
	}
	public double getArea() {
		return area;
	}
	public void setArea(double area) {
		this.area = area;
	}
	public String[] getAddr() {
		return addr;
	}
	public void setAddr(String[] addr) {
		this.addr = addr;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	String city;
	public PropertyData()
	{
		
	}

}
