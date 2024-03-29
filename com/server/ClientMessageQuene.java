package com.server;

import java.util.concurrent.LinkedBlockingQueue;

public class ClientMessageQuene
{
	LinkedBlockingQueue<Message> messageQueue;
	private static ClientMessageQuene instance;

	private ClientMessageQuene()
	{
		messageQueue = new LinkedBlockingQueue<>();
	}

	public static ClientMessageQuene getInstance()
	{
		if (instance == null)
		{
			instance = new ClientMessageQuene();
		}
		return instance;
	}

	public void addMessage(Message m)
	{
		messageQueue.add(m);
	}

	public Message takeMessage() throws InterruptedException
	{
		return messageQueue.take();

	}
}
