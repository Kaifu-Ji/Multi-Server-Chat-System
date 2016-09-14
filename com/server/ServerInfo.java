package com.server;

public class ServerInfo
{
	String serverName;
	String address;
	int portForServer;
	int portForClient;
	public ServerInfo(String severName,String address,int clientPort,int serverPort)
	{
		super();
		this.address = address;
		this.portForServer = serverPort;
		this.serverName = severName;
		this.portForClient = clientPort;
	}
	
}
