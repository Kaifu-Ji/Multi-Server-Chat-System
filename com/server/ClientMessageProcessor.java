package com.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientMessageProcessor extends Thread
{
	volatile boolean stop = false;

	@Override
	public void run()
	{
		while (!stop)
		{
			try
			{
				Message message = ClientMessageQuene.getInstance().takeMessage();
				// System.out.println(message.jsonOperator.job.toJSONString());
				switch (message.jsonOperator.getType())
				{
				case "list":
					list(message);
					break;
				case "quit":
					quit(message);
					break;
				case "who":
					who(message);
					break;
				case "createroom":
					createRoom(message);
					break;
				case "join":
					joinRoom(message);
					break;
				case"":
				default:
					break;
				}
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void joinRoom(Message message)
	{
		String clientName = message.identity;
		String roomName = message.jsonOperator.get("roomid");
		String currentRoom = ClientManager.getInstance().getClient(clientName).room;
		String serverName =  RoomManager.getInstance().hasRoom(roomName);
		if(serverName == null)
		{
			String messageSend = JsonOperator.roomChange(clientName, currentRoom, currentRoom);
			new MessageSender("response", messageSend, roomName, clientName).start();;
			return;
		}
		if(serverName.equals(ServerManager.getInstance().getMyName()))
		{
			String messageSend = JsonOperator.roomChange(clientName, currentRoom, roomName);
			new MessageSender("roomBroadcast", messageSend, currentRoom, clientName).start();
			new MessageSender("response", messageSend, roomName, clientName).start();
			RoomManager.getInstance().leaveRoom(clientName, currentRoom);
			RoomManager.getInstance().joinRoom(clientName, roomName);
			ClientInfo client = ClientManager.getInstance().getClient(clientName);
			client.changeRoom(roomName);
			return;
		}
		String messageSend = JsonOperator.roomChange(clientName, currentRoom, roomName);
		new MessageSender("roomBroadcast", messageSend, currentRoom, clientName).start();
		messageSend = JsonOperator.routeToOtherServer(roomName, serverName);
		new MessageSender("response", messageSend, roomName, clientName).start();
		RoomManager.getInstance().leaveRoom(clientName, currentRoom);
		ClientManager.getInstance().removeClient(clientName);
	}

	private void createRoom(Message message)
	{
		String roomOwner = message.identity;
		String roomName = message.jsonOperator.get("roomid");
		if (RoomManager.getInstance().roomExist(roomName) || (!NameChecker.nameCheck(roomName)))
		{
			System.out.println("unvalue room name");
			String messageSend = JsonOperator.createRoom(roomName, false);
			new MessageSender("response", messageSend, null, roomOwner).start();;
			return;
		}
		ArrayList<ServerInfo> serverList = ServerManager.getInstance().getList();
		String messageSendtoServer = JsonOperator.lockRoom(roomName,
				ServerManager.getInstance().getMyName());
		boolean approved = true;
		ArrayList<Socket> socketList = new ArrayList<>();
		try
		{
			for (ServerInfo serverInfo : serverList)
			{
				Socket socket = new Socket(serverInfo.address, serverInfo.portForServer);
				socketList.add(socket);
				BufferedWriter serverWriter = new BufferedWriter(
						new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
				serverWriter.write(messageSendtoServer);
				serverWriter.newLine();
				serverWriter.flush();
				BufferedReader serverReader = new BufferedReader(
						new InputStreamReader(socket.getInputStream(), "UTF-8"));
				String response = serverReader.readLine();
				approved &= ((String) new JsonOperator(response).get("locked")).equals("true");
			} 
			for (Socket socket : socketList)
			{
				BufferedWriter serverWriter = new BufferedWriter(
						new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
				messageSendtoServer = JsonOperator.releaseRoomID(ServerManager.getInstance().getMyName(), roomName, approved);
				serverWriter.write(messageSendtoServer);
				serverWriter.newLine();
				serverWriter.flush();
				serverWriter.close();
				socket.close();
			}
			String messageSend = JsonOperator.createRoom(roomName, approved);
			new MessageSender("response", messageSend, null, roomOwner).start();
			if (approved)
			{
				ClientInfo client = ClientManager.getInstance().getClient(roomOwner);
				String roomPast = client.room;
				messageSend = JsonOperator.roomChange(roomOwner, roomPast, roomName);
				new MessageSender("roomBroadcast", messageSend, roomPast, roomOwner).start();
				new MessageSender("response", messageSend, roomName, roomOwner).start();
				RoomManager.getInstance().createRoom(roomName, roomOwner);
				RoomManager.getInstance().leaveRoom(roomOwner, roomPast);
				RoomManager.getInstance().joinRoom(roomOwner, roomName);
				client.createRoom(roomName);
				System.out.println("create room successful");
			}
		} catch (Exception e)
		{
			// TODO: handle exception
		}
	}

	private void who(Message m)
	{
		ClientInfo clientInfo = ClientManager.getInstance().getClient(m.identity);
		RoomInfo room = RoomManager.getInstance().getRoom(clientInfo.room);
		String messageSend = JsonOperator.who(room.listClients(), room.roomName, room.roomOwner);
		new MessageSender("response", messageSend, null, m.identity).start();
	}

	private void list(Message m)
	{
		String messageSend = JsonOperator.constructList(RoomManager.getInstance().roomList());
		new MessageSender("response", messageSend, null, m.identity).start();
	}

	private void quit(Message m)
	{
		String messageSend = JsonOperator.roomChange(m.identity,
				ClientManager.getInstance().getClient(m.identity).room, "");
		new MessageSender("response", messageSend, null, m.identity).start();
	}
}
