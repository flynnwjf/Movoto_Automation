package com.movoto.utils.appium;

import java.net.ServerSocket;

public class AvailabelPorts {
	
	public String getPort() throws Exception
	{
		//Creates a server socket, bound to the specified port.
		ServerSocket socket = new ServerSocket(0);
		
		//Enable/disable the SO_REUSEADDR socket option.
		socket.setReuseAddress(true);
		
		//Returns the port number on which this socket is listening.
		String port = Integer.toString(socket.getLocalPort()); 
		
		//Closes this socket.
		socket.close();
		
		return port;
	}

}
