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
				RoomManager.getInstance().createRoom(roomName, roomOwner);
				RoomManager.getInstance().joinRoom(roomOwner, roomName);
				ClientInfo client = ClientManager.getInstance().getClient(roomOwner);
				String roomPast = client.room;
				client.createRoom(roomName);
				messageSend = JsonOperator.roomChange(roomOwner, roomPast, roomName);
				System.out.println("create room successful");
				new MessageSender("response", messageSend, null, roomOwner).start();
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
		new MessageSender("response", messageSend, null, m.identity).run();
	}

	private void list(Message m)
	{
		String messageSend = JsonOperator.constructList(RoomManager.getInstance().roomList());
		new MessageSender("response", messageSend, null, m.identity).run();
	}

	private void quit(Message m)
	{
		String messageSend = JsonOperator.roomChange(m.identity,
				ClientManager.getInstance().getClient(m.identity).room, "");
		new MessageSender("response", messageSend, null, m.identity).run();
	}
}
