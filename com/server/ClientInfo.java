package com.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientInfo
{
	String clientName;
	String room;
	String roomOnwed;
	Socket clientSocket;
	BufferedWriter writer;
	BufferedReader reader;

	public ClientInfo(String clientName, String room, String roomOnwed, Socket clientSocket)
	{
		super();
		this.clientName = clientName;
		this.room = room;
		this.roomOnwed = roomOnwed;
		this.clientSocket = clientSocket;
		try
		{
			reader = new BufferedReader(
					new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
			writer = new BufferedWriter(
					new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// public synchronized boolean readyToRead() throws IOException
	// {
	// return reader.ready();
	// }
	public synchronized String read() throws IOException
	{
		if (reader.ready())
		{
			return reader.readLine();
		}
		return null;
	}

	public void write(String message)
	{
		try
		{
			writer.write(message);
			writer.newLine();
			writer.flush();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public synchronized void createRoom(String roomName)
	{
		this.roomOnwed = roomName;
		this.room = roomName;
	}
	
	public synchronized void changeRoom(String roomName)
	{
		this.room = roomName;
	}
	
}
