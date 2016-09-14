package com.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.parser.ParseException;

public class NewClientAcceptor extends Thread
{
	int port;
	Pattern nameRule;
	volatile boolean stop = false;

	public NewClientAcceptor(int port)
	{
		this.port = port;
		nameRule = Pattern.compile("^[a-zA-z]\\w{2,15}$");
	}

	private boolean nameCheck(String name)
	{
		Matcher m = nameRule.matcher(name);
		return m.find();
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
		if (ClientManager.getInstance().nameExist(name) || (!nameCheck(name)))
		{
			clientWriter.write(JsonOperator.responseNewIdentity(name, false));
			clientWriter.newLine();
			clientWriter.flush();
			return;
		}
		ArrayList<ServerInfo> serverList = ServerManager.getInstance().getList();
		String message = JsonOperator.lockMessage(name, ServerManager.getInstance().getMyName());
		boolean approved = true;
		for (ServerInfo serverInfo : serverList)
		{
			Socket socket = new Socket(serverInfo.address, serverInfo.portForServer);
			BufferedWriter serverWriter = new BufferedWriter(
					new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
			serverWriter.write(message);
			serverWriter.newLine();
			serverWriter.flush();
			BufferedReader serverReader = new BufferedReader(
					new InputStreamReader(socket.getInputStream(), "UTF-8"));
			String response = serverReader.readLine();
			approved &= ((String)new JsonOperator(response).get("locked")).equals("true");
		}
		clientWriter.write(JsonOperator.responseNewIdentity(name, approved));
		clientWriter.newLine();
		clientWriter.flush();
		if(approved)
		{
			
		}
		

	}
}
