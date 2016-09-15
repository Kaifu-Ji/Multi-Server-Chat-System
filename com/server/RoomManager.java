package com.server;

import java.util.ArrayList;
import java.util.HashMap;

public class RoomManager
{
	HashMap<String, RoomInfo> roomList;
	ArrayList<String> lockRoom;
	ArrayList<String> roomNames;
	private static RoomManager instance;
	private RoomManager()
	{
		roomList = new HashMap<>();
		lockRoom = new ArrayList<>();
		roomNames = new ArrayList<>();
	}
	public static synchronized RoomManager getInstance()
	{
		if(instance == null)
		{
			instance = new RoomManager();
		}
		return instance;
	}
	public void joinRoom(String clientName,String roomName)
	{
		RoomInfo room;
		synchronized(roomList)
		{
			room = roomList.get(roomName);
		}
		room.addClient(clientName);
	}
	public void createRoom(String roomName)
	{
		RoomInfo room = new RoomInfo(roomName);
		synchronized(roomList)
		{
			roomList.put(roomName, room);
		}
		synchronized (roomNames)
		{
			roomNames.add(roomName);
		}
	}
}
