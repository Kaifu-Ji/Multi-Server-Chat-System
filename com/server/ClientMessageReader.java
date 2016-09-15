package com.server;

import java.io.IOException;
import java.util.ArrayList;

public class ClientMessageReader extends Thread
{
	volatile boolean stop = false;
	@Override
	public void run()
	{
		while (!stop)
		{
			ArrayList<String> clientList = ClientManager.getInstance().getClientsNameList();
			for (String string : clientList)
			{
				try
				{
					String message = null;
					if((message = ClientManager.getInstance().getClient(string).read())!=null)
					{
						System.out.println(message + "from Client"+string);
						ClientMessageQuene.getInstance().addMessage(new Message(message, string));
					}
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
