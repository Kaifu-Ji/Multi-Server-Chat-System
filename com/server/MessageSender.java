package com.server;

public class MessageSender extends Thread
{

	String task;
	String message;
	String roomName;
	ClientInfo client;

	public MessageSender(String task, String message, String roomName, String clientName)
	{
		super();
		this.task = task;
		this.message = message;
		this.roomName = roomName;
		this.client = ClientManager.getInstance().getClient(clientName);
	}

	public void run()
	{
		switch (task)
		{
		case "response":
			responseClient(client, message);
			break;
		case "roomBroadcast":
			broadcastInRoom(roomName, message,client.clientName);
			break;
		default:
			break;
		}
	}

	void responseClient(ClientInfo client, String message)
	{
		System.out.println(message + "send to"+client.clientName);
		client.write(message);
	}
	void broadcastInRoom(String roomName,String message,String clientName)
	{
		String clients[];
		RoomInfo room = RoomManager.getInstance().getRoom(roomName);
		synchronized (room)
		{
			clients = room.listClients();
		}
		for (String client : clients)
		{
			if(client!= null &&!client.equals(clientName))
			{
				ClientManager.getInstance().getClient(client).write(message);
			}
		}
		
	}
}
