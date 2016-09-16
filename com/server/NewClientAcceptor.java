package com.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import org.json.simple.parser.ParseException;

public class NewClientAcceptor extends Thread
{
	int port;
	volatile boolean stop = false;

	public NewClientAcceptor(int port)
	{
		this.port = port;
	}

	@Override
	public void run()
	{
		ServerSocket serverSocket = null;
		Socket clientSocket = null;
		try
		{
			serverSocket = new ServerSocket(port);
			while (!stop)
			{
				clientSocket = serverSocket.accept();
				System.out.println(ServerManager.getInstance().myname+"accept a new client");
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
				String message = reader.readLine();
				JsonOperator jsonOperator = new JsonOperator(message);
				String name = jsonOperator.get("identity");
				switch (jsonOperator.getType())
				{
				case "newidentity":
					newIdentity(clientSocket, name);
					break;

				default:
					break;
				}
			}
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally
		{
			if (clientSocket != null)
			{
				try
				{
					clientSocket.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			if (serverSocket != null)
			{
				try
				{
					serverSocket.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	private void newIdentity(Socket clientSocket, String name) throws IOException, ParseException
	{
		BufferedWriter clientWriter = new BufferedWriter(
				new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));
		if (ClientManager.getInstance().nameExist(name) || (!NameChecker.nameCheck(name)))
		{
			System.out.println("unvalue name");
			clientWriter.write(JsonOperator.responseNewIdentity(name, false));
			clientWriter.newLine();
			clientWriter.flush();
			return;
		}
		ArrayList<ServerInfo> serverList = ServerManager.getInstance().getList();
		String message = JsonOperator.lockIdentity(name, ServerManager.getInstance().getMyName());
		boolean approved = true;
		ArrayList<Socket> socketList = new ArrayList<>();
		for (ServerInfo serverInfo : serverList)
		{
			Socket socket = new Socket(serverInfo.address, serverInfo.portForServer);
			socketList.add(socket);
			BufferedWriter serverWriter = new BufferedWriter(
					new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
			serverWriter.write(message);
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
			message = JsonOperator.releaseIdentity(name, ServerManager.getInstance().getMyName());
			serverWriter.write(message);
			serverWriter.newLine();
			serverWriter.flush();
			serverWriter.close();
			socket.close();
		}
		clientWriter.write(JsonOperator.responseNewIdentity(name, approved));
		clientWriter.newLine();
		clientWriter.flush();
		if (approved)
		{
			String mainRoom = "MainHall-" + ServerManager.getInstance().getMyName();
			ClientInfo newClient = new ClientInfo(name,
					mainRoom, null, clientSocket);
			ClientManager.getInstance().addClient(name, newClient);
			RoomManager.getInstance().joinRoom(name, mainRoom);
			clientWriter.write(JsonOperator.roomChange(name,"", mainRoom));
			clientWriter.newLine();
			clientWriter.flush();
		}else {
			clientWriter.close();
			clientSocket.close();
		}

	}
}
