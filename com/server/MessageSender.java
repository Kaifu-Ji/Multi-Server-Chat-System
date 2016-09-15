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
		case "responese":
			responseClient(client, message);
			break;

		default:
			break;
		}
	}

	void responseClient(ClientInfo client, String message)
	{
		client.write(message);
	}
}
