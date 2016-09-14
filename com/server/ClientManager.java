package com.server;

import java.util.ArrayList;
import java.util.HashMap;

public class ClientManager
{
	HashMap<String, ClientInfo> clientList;
	ArrayList<String> lockList;
	private static ClientManager instance;
	private ClientManager()
	{
		clientList = new HashMap<>();
		lockList = new ArrayList<>();
	}
	synchronized static ClientManager getInstance()
	{
		if(instance == null)
		{
			instance = new ClientManager();
		}
		return instance;
	}
	public synchronized void addServer(String clientname,ClientInfo client)
	{
		clientList.put(clientname, client);
	}
	public synchronized boolean nameExist(String name)
	{
		return (clientList.containsKey(name)||lockList.contains(name));
	}
}
