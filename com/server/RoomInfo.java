package com.server;

import java.util.ArrayList;

public class RoomInfo
{
	String roomName;
	ArrayList<String> clientsInRoom;

	public RoomInfo(String roomName)
	{
		super();
		this.roomName = roomName;
		this.clientsInRoom = new ArrayList<>();
	}

	public synchronized void addClient(String clientName)
	{
		clientsInRoom.add(clientName);
	}
}
