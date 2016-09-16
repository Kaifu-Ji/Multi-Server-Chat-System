package com.server;

import java.util.ArrayList;
import java.util.HashMap;

public class RoomManager
{
	HashMap<String, RoomInfo> roomList;
	ArrayList<String> lockRoom;
	ArrayList<String> roomNames;
	HashMap<String, String> remoteRooms;
	private static RoomManager instance;

	private RoomManager()
	{
		roomList = new HashMap<>();
		lockRoom = new ArrayList<>();
		roomNames = new ArrayList<>();
		remoteRooms = new HashMap<>();
	}

	public static synchronized RoomManager getInstance()
	{
		if (instance == null)
		{
			instance = new RoomManager();
		}
		return instance;
	}
	public RoomInfo getRoom(String roomName)
	{
		return roomList.get(roomName);
	}
	
	public void lockRoom(String roomName)
	{
		synchronized(lockRoom)
		{
			lockRoom.add(roomName);
		}
	}
	
	public void removeLockRoom(String roomName)
	{
		synchronized (lockRoom)
		{
			lockRoom.remove(roomName);
		}
	}

	public void addRemoteRoom(String roomName, String remoteServer)
	{
		synchronized (roomNames)
		{
			roomNames.add(roomName);
		}
		synchronized (remoteRooms)
		{
			remoteRooms.put(remoteServer, remoteServer);
		}
	}

	public void joinRoom(String clientName, String roomName)
	{
		RoomInfo room;
		synchronized (roomList)
		{
			room = roomList.get(roomName);
		}
		room.addClient(clientName);
	}

	public void createRoom(String roomName,String roomOwner)
	{
		RoomInfo room = new RoomInfo(roomName,roomOwner);
		synchronized (roomList)
		{
			roomList.put(roomName, room);
		}
		synchronized (roomNames)
		{
			roomNames.add(roomName);
		}
	}
	public boolean roomExist(String roomName)
	{
		return (roomList.containsKey(roomName)||lockRoom.contains(roomName));
	}

	public String[] roomList()
	{
		String[] result = new String[1];
		synchronized (roomNames)
		{
			return roomNames.toArray(result);
		}
	}
}
