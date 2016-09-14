package com.server;

import java.net.Socket;

public class ClientInfo
{
	String clientName;
	String Room;
	String RoomOnwed;
	Socket clientSocket;
	
	public ClientInfo(String clientName, String room, String roomOnwed, Socket clientSocket)
	{
		super();
		this.clientName = clientName;
		Room = room;
		RoomOnwed = roomOnwed;
		this.clientSocket = clientSocket;
	}
}
