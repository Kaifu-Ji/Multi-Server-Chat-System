package com.server;

import java.util.ArrayList;
import java.util.HashMap;

public class ClientManager
{
	HashMap<String, ClientInfo> clientList;
	ArrayList<String> lockList;
	ArrayList<String> clientNames;
	private static ClientManager instance;
	private ClientManager()
	{
		clientList = new HashMap<>();
		lockList = new ArrayList<>();
		clientNames = new ArrayList<>();
	}
	synchronized static ClientManager getInstance()
	{
		if(instance == null)
		{
			instance = new ClientManager();
		}
		return instance;
	}
	public synchronized void addClient(String clientname,ClientInfo client)
	{
		clientList.put(clientname, client);
		clientNames.add(clientname);
	}
	
	public synchronized void removeClient(String clientName)
	{
		clientList.remove(clientName);
		clientNames.remove(clientName);
	}
	
	public synchronized boolean nameExist(String name)
	{
		return (clientList.containsKey(name)||lockList.contains(name));
	}
	public synchronized void addLockName(String lockName)
	{
		lockList.add(lockName);
	}
	public synchronized void removeLockName(String name)
	{
		lockList.remove(name);
	}
	@SuppressWarnings("unchecked")
	public synchronized ArrayList<String> getClientsNameList()
	{
		return (ArrayList<String>) clientNames.clone();
	}
	public synchronized ClientInfo getClient(String clientName)
	{
		return clientList.get(clientName);
	}
}
