package com.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ServerMain
{
	String serverName;
	int processThreadNumber;

	public ServerMain(String serverName, int threadNumber)
	{
		super();
		this.serverName = serverName;
		this.processThreadNumber = threadNumber;
	}

	public static void main(String[] args) throws IOException
	{
		ServerMain serverMain = new ServerMain("s2", 1);
		serverMain.setup();
		serverMain.work();
	}

	private void work()
	{
		new NewClientAcceptor(4445).start();
		new SeverMessageResponsorMain(5556).start();
		new ClientMessageReader().start();
		new ClientMessageProcessor().start();
	}

	public ServerMain()
	{

	}

	void setup() throws IOException
	{
		File f = new File("servers_conf.txt");
		BufferedReader configReader = new BufferedReader(new FileReader(f));
		String info = configReader.readLine();
		while (info != null)
		{
			String[] temp = info.split("\t");
			if (!temp[0].equals(this.serverName)&&info.charAt(0) != '#')
			{
				ServerInfo serverInfo = new ServerInfo(temp[0], temp[1], Integer.parseInt(temp[2]),
						Integer.parseInt(temp[3]));
				ServerManager.getInstance().addServer(serverInfo);
				RoomManager.getInstance().addRemoteRoom("MainHall-" + temp[0], temp[0]);
			}
			info = configReader.readLine();
		}
		ServerManager.getInstance().setMyname(serverName);
		configReader.close();
		RoomManager.getInstance().createRoom("MainHall-" + serverName,"");
	}

}
