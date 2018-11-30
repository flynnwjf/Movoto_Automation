package com.movoto.db;

import java.sql.*;

public class GetConnection {

	public Connection conn;//call getConnection() method of DriverManagerto get a Connection object
	public Statement stmt;
	public String url;    //JDBC's URL
	public String usr;
	public String pwd;
	
	public GetConnection(String url, String usr,String pwd){
		this.url=url;
		this.usr=usr;
		this.pwd=pwd;
        this.Connect();
	 }
	
	public GetConnection(String dbName){
        if (dbName!="db3")
        {
        	this.url="input a new url";
        	this.usr="input a new usr";
        	this.pwd="input a new pwd";
        }
        this.Connect();
	        
	 }
	
	 public void Connect(){
		try{
	         //call Class.forName() to load MySQL Driver
	         Class.forName("com.mysql.jdbc.Driver");
	         System.out.println("sucess to load MySQLDriver！");
	    }
		catch(ClassNotFoundException e1){
	         System.out.println("Can not find MySQL Driver!");
	         e1.printStackTrace();
	    }
		try {
	            this.conn = DriverManager.getConnection(url,usr,pwd);
	            this.stmt = conn.createStatement(); //create a Statement object
	            //CallableStatement cstmt = conn.prepareCall("{call  func_split_string('',,?)}");// Call procedure 
	            System.out.print("success to connect to DB！");
	           
	    } 
		catch (SQLException e){
	            e.printStackTrace();
	    }	       
	 }
	 
	 public ResultSet ExecuteQuery(String sql) throws SQLException
	 {
		 //String sqltest = "select func_nearByProperties('18813e69-8432-48de-9016-b3238269b125,228650e7-bf72-4da0-90a5-61bbfb38b6f7,38b9df3d-9100-4eed-9afe-20e2e03344b7,43b57633-d530-4a32-bc06-ed5bded6d240,44cfb647-73e0-434b-bb67-e79b9b24d18a,6576bdf1-b05b-4a1d-9da4-6bbb051af066,6caca3d9-f06a-4f45-8b80-996c24978e03,77d12dac-f560-4c29-8064-bb3931f90cc0,817288f7-23df-4a05-834d-94a715dbcd29,926b7cbc-c8fb-428c-8785-1b9b765448a9,9e17e268-ed86-482f-a66f-18918d83cb15,a4411f7b-6793-4e1b-b379-c39ecdf2a1bf,a6f9c84d-95a5-4190-9380-a7fe3f0d9a4a,aaffa8fc-d58c-442b-84ca-718efa880fc1,ae4770fe-7d51-4985-8836-66f638c067f8,af5f4550-33d2-42ff-95c0-f97de05c2ba3,c90c4799-cb45-434b-9ada-0668ec64fc4c,df0b4fff-9824-4476-8588-a245fcf30e0a,f51e29f8-d1de-4a9b-9398-dfb9b80b87e5,f9ba1df8-cc92-4d3b-a7eb-dcfabdb89152',47.604668,-122.197235,false)";    //要执行的SQL
		 ResultSet rs = this.stmt.executeQuery(sql);//create reserving data object
		 return rs;
	 }
	 
	 public void Disconect()
     {
         try {
			this.stmt.close();
			this.conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
     }
}
