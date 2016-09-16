package com.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SeverMessageResponsorMain extends Thread
{
	int port;
	volatile boolean stop = false;

	public SeverMessageResponsorMain(int port)
	{
		this.port = port;
	}

	@Override
	public void run()
	{
		try
		{
			ServerSocket serverSocket = new ServerSocket(port);
			while (!stop)
			{
				Socket socket = serverSocket.accept();
				new SeverMessageResponsorThread(socket).start();

			}
			serverSocket.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

class SeverMessageResponsorThread extends Thread
{
	Socket socket;

	public SeverMessageResponsorThread(Socket socket)
	{
		this.socket = socket;
		System.out.println("new server message");
	}

	@Override
	public void run()
	{
		boolean stop = false;
		try
		{
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(socket.getInputStream(), "UTF-8"));
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
			while (!stop)
			{
				String message = reader.readLine();
				System.out.println("reciver message from other server" + message);
				JsonOperator jsonOperator = new JsonOperator(message);
				switch (jsonOperator.getType())
				{
				case "lockidentity":
					String clientName = jsonOperator.get("identity");
					boolean approved = !ClientManager.getInstance().nameExist(clientName);
					if (approved)
					{
						ClientManager.getInstance().addLockName(clientName);
						System.out.println("new Identity approved!");
					}
					message = JsonOperator.responseLockIdentity(clientName,
							ServerManager.getInstance().getMyName(), approved);
					writer.write(message);
					writer.newLine();
					writer.flush();
					break;
				case "releaseidentity":
					clientName = jsonOperator.get("identity");
					ClientManager.getInstance().removeLockName(clientName);
					stop = true;
					break;
				case "lockroomid":
					String roomName = jsonOperator.get("roomid");
					approved = !RoomManager.getInstance().roomExist(roomName);
					if (approved)
					{
						RoomManager.getInstance().lockRoom(roomName);
						System.out.println("new roomID approved");
					}
					message = JsonOperator.responseLockRoom(roomName,
							ServerManager.getInstance().getMyName(), approved);
					writer.write(message);
					writer.newLine();
					writer.flush();
					break;
				case "releaseroomid":
					roomName = jsonOperator.get("roomid");
					RoomManager.getInstance().removeLockRoom(roomName);
					if(jsonOperator.get("approved").equals("true"))
					{
						String remoteServer = jsonOperator.get("serverid");
						RoomManager.getInstance().addRemoteRoom(roomName, remoteServer);
					}
					stop = true;
					break;
				default:
					break;
				}
			}
		} catch (Exception e)
		{
			// TODO: handle exception
		}
	}
}
