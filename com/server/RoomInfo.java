package com.server;

import java.util.ArrayList;

public class RoomInfo
{
	String roomName;
	ArrayList<String> clientsInRoom;
	String roomOwner;

	public RoomInfo(String roomName, String roomOwner)
	{
		super();
		this.roomName = roomName;
		this.clientsInRoom = new ArrayList<>();
		this.roomOwner = roomOwner;
	}

	public synchronized void addClient(String clientName)
	{
		clientsInRoom.add(clientName);
	}

	public synchronized void deleteClient(String clientName)
	{

		// for (String string : clientsInRoom)
		// {
		// System.out.println(string);
		// }
		clientsInRoom.remove(clientName);
		// for (String string : clientsInRoom)
		// {
		// System.out.println(string);
		// }
	}

	public String[] listClients()
	{
		String[] result = new String[1];
		synchronized (clientsInRoom)
		{
			return clientsInRoom.toArray(result);
		}
	}
}
