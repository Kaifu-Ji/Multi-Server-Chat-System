package com.server;

import java.util.ArrayList;

public class ServerManager
{
	ArrayList<ServerInfo> serverList;
	String myname;
	private static ServerManager instance;
	private ServerManager()
	{
		serverList = new ArrayList<>();
	}
	synchronized static ServerManager getInstance()
	{
		if(instance == null)
		{
			instance = new ServerManager();
		}
		return instance;
	}
	public synchronized void addServer(ServerInfo server)
	{
		serverList.add(server);
	}
	public ArrayList<ServerInfo> getList()
	{
		return serverList;
	}
	public synchronized void setMyname(String myName)
	{
		this.myname = myName;
	}
	public synchronized String getMyName()
	{
		return myname;
	}
}
